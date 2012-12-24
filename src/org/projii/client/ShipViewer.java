
	package org.projii.client;


import java.util.LinkedList;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;
import org.projii.client.tools.Size;



import android.util.FloatMath;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

 public class ShipViewer {
	
	private static final float SPEED_BULLET = 1700;
	private static final float TIME_BULLET_FLIGHT = 1.5f;
	private final float DEFAULT_TIME_ROTATION=0.3f;
	private final Vector2 SPEED_LIMIT=new Vector2(15, 15);
	
	private AnimatedSprite ship;
	private Body shipBody;
	public SpritePool pPool;
	private float timeCounter = 0, timeRate= 0.25f,betweenRotationsTimeCounter;
	private float delta, prevTime = 0;
	private float UPDATE_TIME=0.075f;
	private float newDeltaAngle;
    private float oldDeltaAngle=0;//delta between new and old angle is Zero at first time
    private float prevAngle=0;
	private boolean isBraking;
	public enum MovingType { Smooth, OneDirection,Braking,bigAngle}
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
                super.onManagedUpdate(pSecondsElapsed);
                   
                   
           }
        };
        setPhysicBody(physicsWorld);
    }
	public ShipViewer(){}
	private void setPhysicBody(PhysicsWorld physicsWorld)
	{
		final FixtureDef shipFixtureDef = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
		this.shipBody = PhysicsFactory.createBoxBody(physicsWorld, this.ship, BodyType.DynamicBody, shipFixtureDef);
        ship.animate(100);
	
	}
    //moving//
	public void movingShip(Vector2 joysticPosition)
	{
		 MovingType currentMovingType=MoveLogic.getMovingType(joysticPosition,betweenRotationsTimeCounter,ship.getRotation(),prevAngle, UPDATE_TIME);
		 if(currentMovingType==MovingType.Braking)
		 {
		 	 isBraking=true;
			 return;
		 }
		 isBraking=false;
		 RotationAngles rotationAngles= MoveLogic.calculateAngles(joysticPosition,ship.getRotation());
		 float newDeltaAngle=Math.abs(rotationAngles.nextAngle-rotationAngles.prevAngle);
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
		    	                float timeForRotation=MoveLogic.calculateRotationTime(newDeltaAngle, 1);
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
		Vector2 limitedSpeed=MoveLogic.getLimitedSpeed(shipBody.getLinearVelocity(), speedLimit, incrementSpeed);
		shipBody.setLinearVelocity(limitedSpeed);	
		
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
		Vector2 brakingSpeed= MoveLogic.getBrakingSpeed(shipBody.getLinearVelocity(), ifSaveMoving);
    	shipBody.setLinearVelocity(brakingSpeed);//getting previous speed
	    
	}
    public AnimatedSprite getSprite()
	{
		return ship;
	}
	

	public Body getBody()
	{
		return shipBody;
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
    	if(isBraking)
    		decelerationship(true);
		delta = pSecondsElapsed - prevTime;
		prevTime = pSecondsElapsed;
		timeCounter += delta;
		betweenRotationsTimeCounter+=delta;
		
	}
    /*end-time functions*/
	/*shooting*/
   
    private void shootLinearBullet(Vector2 bulletStartPosition, Vector2 aim,Scene scene,SpritePool bulletPool,LinkedList bulletsToBeAdded) 
	{ 
		Sprite bullet;
		bullet = bulletPool.obtainPoolItem();
		bullet.setPosition(bulletStartPosition.x,bulletStartPosition.y);
		scene.attachChild(bullet);
		MoveModifier  mod = new MoveModifier(TIME_BULLET_FLIGHT,bulletStartPosition.x, aim.x,bulletStartPosition.y, aim.y);
		bullet.registerEntityModifier(mod.deepCopy());
	    bulletsToBeAdded.add(bullet);
	    
	    
	}
	public void shooting(Scene scene,SpritePool bulletPool,LinkedList bulletsToBeAdded,Sound shootingSound) 
	{ 
		if (timeCounter >= timeRate)
		{
			//initialization
			float  deltaAngle=50;float shipRotation=ship.getRotation();float shipLeftX=ship.getX();
			float shipUpY= ship.getY();float shipWidth=ship.getWidth();float shipHeight=ship.getHeight();
		    //endOfInitialization
			Vector2 bullet1StartPosition=ShootLogic.getsStartBulletPosition(shipRotation, deltaAngle, shipLeftX, shipUpY, shipWidth, shipHeight);
			Vector2 bullet2StartPosition=ShootLogic.getsStartBulletPosition(shipRotation, -deltaAngle, shipLeftX, shipUpY, shipWidth, shipHeight);
			Vector2 AimByLinearDistance=ShootLogic.getAimByLinearDistance(SPEED_BULLET, shipRotation, shipLeftX, shipUpY, shipWidth, shipHeight);
			shootLinearBullet(bullet1StartPosition, AimByLinearDistance, scene, bulletPool, bulletsToBeAdded);
			shootLinearBullet(bullet2StartPosition, AimByLinearDistance, scene, bulletPool, bulletsToBeAdded);
			timeCounter=0;
		    shootingSound.play();
			
		}
	    
	}
	
}
 