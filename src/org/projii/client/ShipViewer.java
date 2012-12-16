package org.projii.client;

import java.io.Console;
import java.util.LinkedList;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;
import org.projii.client.tools.Size;


import android.R.string;
import android.annotation.SuppressLint;
import android.util.FloatMath;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

 public class ShipViewer {
	private final int START_JOYSTICK_POSITION=0;
	private static final String TAG = "myLogs";
	private final float DELTA_DIRECTION=2f;
	private final float DEFAULT_TIME_ROTATION=0.3f;
	private final Vector2 SPEED_LIMIT=new Vector2(20, 20);
	private boolean isGetToStop=true;
	
	private AnimatedSprite ship;
	private Body shipBody;
	public SpritePool pPool;
	private float timeCounter = 0, timeRate= 0.25f,betweenRotationsTimeCounter;
	private float delta, prevTime = 0;
	private float UPDATE_TIME=0.075f;
	private RotationAngles rotationAngles;
	private float newDeltaAngle;
    private float oldDeltaAngle=0;//delta between new and old angle is Zero at first time
    private float prevAngle=0;
	enum MovingType { Smooth, OneDirection,Braking,bigAngle}
	public ShipViewer(float locationX,float locationY,Size shipSize,ITiledTextureRegion texture,VertexBufferObjectManager objMan,PhysicsWorld physicsWorld)
	{
		ship = new AnimatedSprite(locationX, locationY,shipSize.getWidth(),shipSize.getHeight(), texture, objMan)
		 {
           @Override
           public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                           float pTouchAreaLocalX, float pTouchAreaLocalY)
           {
				return mFlippedHorizontal;
                  
           }
           @Override
           protected void onManagedUpdate(float pSecondsElapsed) {
                  	if(isGetToStop)
                  	{
                  		
                  		decelerationship(true);
                  	    
                  	}
                
                   super.onManagedUpdate(pSecondsElapsed);
                   
                   
           }
        };
        setPhysicBody(physicsWorld);
}
	private void setPhysicBody(PhysicsWorld physicsWorld)
	{
		final FixtureDef shipFixtureDef = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
		this.shipBody = PhysicsFactory.createBoxBody(physicsWorld, this.ship, BodyType.DynamicBody, shipFixtureDef);
        ship.animate(100);
	
	}
	public AnimatedSprite getSprite()
	{
		return ship;
	}
	public void setShipToStop(){
		isGetToStop=true;
	}
	public void setCancelToStop()
	{
		isGetToStop=false;
	}
	
	public Body getBody()
	{
		return shipBody;
	}
    //moving//
	public void movingShip(Vector2 joysticPosition)
	{
		 MovingType currentMovingType=getMovingType(joysticPosition,betweenRotationsTimeCounter);
		 if(currentMovingType==MovingType.Braking)
			 return;
		 Vector2 incrementSpeed=joysticPosition;
		 switch (currentMovingType) 
		 { 
		    //удерживаем джостик в одной позиции
		 	case OneDirection:  {
                                  setSpeed(SPEED_LIMIT, incrementSpeed);
		 		                 } 
		 		break;
		 	//медленное вращение-рассортировать на 2 вида с движением и вращением по кругу	
		 	case Smooth  :  {
		 		                setSpeed(SPEED_LIMIT, new Vector2(incrementSpeed.x/1.5f,incrementSpeed.y/1.5f));
		 		             	makeRotation(DEFAULT_TIME_ROTATION, rotationAngles);
		 		             } 
		 	      break;
		    case bigAngle:	{
		    	                float timeForRotation=calculateRotationTime(newDeltaAngle, 1,rotationAngles.nextAngle);
		    					setSpeed(SPEED_LIMIT, new Vector2(incrementSpeed.x/3,incrementSpeed.y/3));
		    					makeRotation(timeForRotation, rotationAngles);
		    					//сделать зависимость между
		                   	}
			     break;
		    default:
			  break;
		 }
		// setshipSpeedWithLimit(SPEED_LIMIT,pValueX,pValueY);
		
		
	}
	/*Realzation*/
	private void setSpeed(Vector2 speedLimit,Vector2 incrementSpeed){
		Vector2 oldSpeed = shipBody.getLinearVelocity();//getting previous speed of ship
		Vector2 newSpeed=new Vector2(oldSpeed.x + incrementSpeed.x ,oldSpeed.y + incrementSpeed.y);//speed of ship with acceleration
		Vector2 speedLimitToSet=new Vector2(Math.signum(oldSpeed.x)*speedLimit.x,Math.signum(oldSpeed.y)*speedLimit.y); 
		/////////////////////////////////////////////////////////////
		if(Math.abs(newSpeed.x)<speedLimit.x)
			if(Math.abs(newSpeed.y)<speedLimit.y)
			{
	           shipBody.setLinearVelocity(newSpeed);
			}
			else
			{
				shipBody.setLinearVelocity(newSpeed.x  ,speedLimitToSet.y );
			}
		else if(Math.abs(newSpeed.y)<speedLimit.y)
		     {
	        	shipBody.setLinearVelocity(speedLimitToSet.x  , newSpeed.y );
	       	 }
			 else
			 {
				shipBody.setLinearVelocity(speedLimitToSet);
			 }
	}
	
    private MovingType getMovingType(Vector2 joysticPosition,float wastedTime)
    {		  
    	if(joysticPosition.x ==START_JOYSTICK_POSITION && joysticPosition.y == START_JOYSTICK_POSITION)
    		return MovingType.Braking;
        rotationAngles = calculateAngles(joysticPosition);
    	float deltaDirection=Math.abs(rotationAngles.nextAngle-prevAngle);
        newDeltaAngle=Math.abs(rotationAngles.nextAngle-rotationAngles.prevAngle);	
    	//updateRotateValues(rotationAngles.nextAngle,newDeltaAngle);
    	/*Log.d("Direction","DeltaofDelta->"+String.valueOf(oldDeltaAngle-newDeltaAngle));
		  boolean deltaIsDown= oldDeltaAngle-newDeltaAngle>0;
	      Log.d("Direction", "OneDirection->"+String.valueOf(deltaDirection)+"Dlt->"+String.valueOf(newDeltaAngle));*/
	    boolean isInOneDirection=deltaDirection<=DELTA_DIRECTION;//in one difened direct
	 
		if(wastedTime<=UPDATE_TIME)
		{
			if(isInOneDirection)
			{
				Log.d("inCalcTime","cancelMoviing");
				return MovingType.OneDirection;
			}
			else
			{
				if (newDeltaAngle<=100)
				{
					Log.d("inCalcTime", "Smoothrotation");
					return MovingType.Smooth;
                }//fastturnupdateRotateValues
				else {
					return MovingType.bigAngle;  
			       	}
			}
		}
        return MovingType.Smooth;
    }
 	private RotationAngles calculateAngles(Vector2 joysticPosition)
 	{
 		float  nextAngle=org.projii.client.tools.MathUtils.normAngle(MathUtils.radToDeg((float)Math.atan2(joysticPosition.x, -joysticPosition.y)));
	    float  normPrevRotation=org.projii.client.tools.MathUtils.normAngle(ship.getRotation());
        if(Math.abs(nextAngle-normPrevRotation)>181)
        {
        	if(nextAngle>normPrevRotation) nextAngle-=360;
		    else normPrevRotation-=360;
		}
        return new RotationAngles(normPrevRotation, nextAngle);
 	}
 	private float calculateRotationTime(float newDeltaAngle,float angularSpeed,float nextAngle)
	{
		float angleConst=90;float speedConst=1;
		float resultRotaionTime=0.5f;
		float timeConst=0.3f;
        float coeff=(timeConst*speedConst)/angleConst;
	    resultRotaionTime=coeff*(newDeltaAngle/angularSpeed);//timeFor180p*(angle/speedRotation);
        return resultRotaionTime*1.2f;
	}
	

	private void updateRotateValues(float nextAngle,float newDeltaAngle)
	{
		 betweenRotationsTimeCounter=0;//updating time counter
		 prevAngle=nextAngle;
		 oldDeltaAngle=newDeltaAngle;
	}
	
	private void makeRotation(float timeForRotation,RotationAngles rotationAngles)
	{
		 ship.clearEntityModifiers();
		 RotationModifier entityModifier=new RotationModifier(timeForRotation,rotationAngles.prevAngle,rotationAngles.nextAngle);
		 ship.registerEntityModifier(entityModifier);
		 updateRotateValues(rotationAngles.nextAngle,newDeltaAngle);
		
	}
	
    private void decelerationship(boolean ifSaveMoving){
		
		Vector2 velocity = shipBody.getLinearVelocity();//getting previous speed
	    if((velocity.x!=0&&velocity.y!=0))
			if(Math.abs(velocity.x)>0.5&&Math.abs(velocity.y)>0.5)shipBody.setLinearVelocity(velocity.x -(velocity.x/100) , velocity.y-(velocity.y/100)  );
			   else if(ifSaveMoving)
			       {
				      shipBody.setLinearVelocity(velocity.x -((velocity.x/100)-velocity.x%0.01f) , velocity.y-((velocity.y/100)-velocity.y%0.01f));
				   }
			else shipBody.setLinearVelocity(velocity.x -velocity.x , velocity.y-velocity.y  );
		
		
	}
    ////end-moving realization//////
    /*Time-functions*/
    public float gettimeCounter()
	{
		return timeCounter;
	}
	public void setTimeMovingUpdate(float updateTime)
	{
	   this.UPDATE_TIME=updateTime+0.015f;	
	}
    public void getTimeUpdate(float pSecondsElapsed)
	{
		delta = pSecondsElapsed - prevTime;
		prevTime = pSecondsElapsed;
		timeCounter += delta;
		betweenRotationsTimeCounter+=delta;
	}
    /*end-time functions*/
	/*shooting*/
	public void shootBullet(Scene scene,SpritePool bulletPool,LinkedList bulletsToBeAdded,Sound shootingSound) 
	{ 
		
		if (timeCounter >= timeRate)
		 {
	
	  float  deltaAngle=50;
	  float  normAngle=org.projii.client.tools.MathUtils.normAngle(ship.getRotation());
	  float Angle1=(float)Math.toRadians(normAngle-(deltaAngle+10));
	  float Angle2=(float)Math.toRadians(normAngle+deltaAngle);

	  float rad = (float)Math.toRadians(normAngle);
	  
	  float centerX1= (2*ship.getX()+ship.getWidth())/2+30*FloatMath.sin(Angle1);
	  float centerY1= (2*ship.getY()+ship.getHeight())/2-25*FloatMath.cos(Angle1);
	  
	  float centerX2= (2*ship.getX()+ship.getWidth())/2+25*FloatMath.sin(Angle2);
	  float centerY2= (2*ship.getY()+ship.getHeight())/2-25*FloatMath.cos(Angle2);
	  
	  float centerX= (2*ship.getX()+ship.getWidth())/2+FloatMath.sin(rad);
	  float centerY= (2*ship.getY()+ship.getHeight())/2-FloatMath.cos(rad);
	  
	  Sprite bullet,bullet2;
	  bullet = bulletPool.obtainPoolItem();
	  bullet.setPosition(centerX1, centerY1);
	 
	  scene.attachChild(bullet);
	  //bullet.animate(100);
	  
	  bullet2=bulletPool.obtainPoolItem();
	  bullet2.setPosition(centerX2, centerY2);
	  scene.attachChild(bullet2);

	   float pX= (centerX+2000*FloatMath.sin((float)rad));
	   float pY= (centerY-2000*FloatMath.cos((float)rad));
       MoveModifier  mod = new MoveModifier(1.5f,centerX1, pX,centerY1, pY);
	   MoveModifier  mod2 = new MoveModifier(1.5f,centerX2, pX,centerY2, pY);
	
	   bullet2.registerEntityModifier(mod2.deepCopy());
	   bullet.registerEntityModifier(mod.deepCopy());
	   ///list
	    bulletsToBeAdded.add(bullet);
	    bulletsToBeAdded.add(bullet2);
	    timeCounter=0;
	    shootingSound.play();
		 }
	    
	}
	
}
 
