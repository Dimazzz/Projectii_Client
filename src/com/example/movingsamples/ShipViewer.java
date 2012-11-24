package com.example.movingsamples;

import java.text.Normalizer;
import java.util.LinkedList;

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

import Tools.Size;

import android.annotation.SuppressLint;
import android.util.FloatMath;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

 public class ShipViewer {

	private AnimatedSprite ship;
	private Body shipBody;
	private boolean isGetToStop;
	private float shotSpeed = 200;
	private float fireCounter = 0, fireRate= 0.33f;

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
	private  float gun1PointX;
	private  float gun1PointY;
	private  float gun2PointX;
	private  float gun2PointY;
	public Body getBody()
	{
		return shipBody;
	}
	///////////////////////////Shooting//////////////////////////////
	public void shoote()
	{
		
	}
	public float getShotSpeed()
	{
		return shotSpeed;
	}
	public float getFireCounter()
	{
		return fireCounter;
	}
	public void setFireCounter(float fireCounter)
	{
		this.fireCounter=fireCounter;
	}
	public void createShot(Bullet bullet,Scene scene,TiledTextureRegion textureReg,VertexBufferObjectManager objMan){
		 if (fireCounter >= fireRate)
		 {
			 float pX=ship.getX();
			 float pY=ship.getY();
			 float  normAngle=Tools.MathUtils.normAngle(ship.getRotation());
			// float  normAngle=ship.getRotation();
		  bullet = new Bullet(pX+14, pY+10, textureReg, objMan);
		  
		  bullet.setAngle(normAngle);
		  bullet.animate(100);
		   bullet.setUserData("shot");
		  scene.attachChild(bullet);
		  bullet= new Bullet(pX+36, pY+10, textureReg, objMan);
		  bullet.setAngle(normAngle);
		  bullet.animate(100);
		  bullet.setUserData("shot");
		  scene.attachChild(bullet);
		  fireCounter = 0;
		}
	}
	public void shootBullet(Scene scene,TiledTextureRegion textureReg,VertexBufferObjectManager objMan,float Width,LinkedList bulletsToBeAdded) 
	{

	  float  deltaAngle=50;
	  float  normAngle=Tools.MathUtils.normAngle(ship.getRotation());
	  float Angle1=(float)Math.toRadians(normAngle-(deltaAngle+10));
	  float Angle2=(float)Math.toRadians(normAngle+deltaAngle);

	  float rad = (float)Math.toRadians(normAngle);
	  
	  float centerX1= (2*ship.getX()+ship.getWidth())/2+30*FloatMath.sin(Angle1);
	  float centerY1= (2*ship.getY()+ship.getHeight())/2-25*FloatMath.cos(Angle1);
	  
	  float centerX2= (2*ship.getX()+ship.getWidth())/2+25*FloatMath.sin(Angle2);
	  float centerY2= (2*ship.getY()+ship.getHeight())/2-25*FloatMath.cos(Angle2);
	  
	  float centerX= (2*ship.getX()+ship.getWidth())/2+FloatMath.sin(rad);
	  float centerY= (2*ship.getY()+ship.getHeight())/2-FloatMath.cos(rad);
	  
	  AnimatedSprite bullet,bullet2;
	  bullet = new AnimatedSprite(centerX1, centerY1,textureReg.deepCopy(), objMan);
	  scene.attachChild(bullet);
	  bullet2=new AnimatedSprite(centerX2, centerY1,textureReg.deepCopy(), objMan);
	  scene.attachChild(bullet2);
	   bullet.animate(100);
	   bullet2.animate(100);
	   

	
	   
	   float pX= (centerX+2000*FloatMath.sin((float)rad));
	   float pY= (centerY-2000*FloatMath.cos((float)rad));
	   
	
	  
	   MoveModifier  mod = new MoveModifier(1.5f,centerX1, pX,centerY1, pY);
	   MoveModifier  mod2 = new MoveModifier(1.5f,centerX2, pX,centerY2, pY);
	
	   bullet2.registerEntityModifier(mod2.deepCopy());
	   bullet.registerEntityModifier(mod.deepCopy());
	    bulletsToBeAdded.add(bullet);
	    bulletsToBeAdded.add(bullet2);
	}
	/*public void shootBullet(Scene scene,TiledTextureRegion textureReg,VertexBufferObjectManager objMan,float Width,LinkedList bulletsToBeAdded) 
	{

	  float  DELTA=ship.getWidth()/4;
	  float  normAngle=Tools.MathUtils.normAngle(ship.getRotation());
	  float rad = (float)Math.toRadians(normAngle);
	  float centerX= (2*ship.getX()+ship.getWidth())/2+FloatMath.sin(rad);
	  float centerY= (2*ship.getY()+ship.getHeight())/2-FloatMath.cos(rad);
	  
	  AnimatedSprite bullet,bullet2;
	  bullet = new AnimatedSprite(centerX-DELTA, centerY,textureReg.deepCopy(), objMan);
	  scene.attachChild(bullet);
	  bullet2=new AnimatedSprite(centerX+DELTA, centerY,textureReg.deepCopy(), objMan);
	  scene.attachChild(bullet2);
	   bullet.animate(100);
	   bullet2.animate(100);
	   

	
	   
	   float pX= (centerX+2000*FloatMath.sin((float)rad));
	   float pY= (centerY-2000*FloatMath.cos((float)rad));
	   MoveModifier mod = null,mod2 = null;
	   boolean marked=false;
	   if((normAngle>80&&normAngle<120)||(normAngle>250&&normAngle<280))
	   {
	     mod = new MoveModifier(1.5f,centerX, pX,centerY-DELTA, pY);
	     mod2 = new MoveModifier(1.5f,centerX, pX,centerY+DELTA, pY);
	     marked=true;
	   }
	   if((normAngle<10&&normAngle>-20)||(normAngle>200&&normAngle<160))
	   {
	     mod = new MoveModifier(1.5f,centerX-DELTA, pX,centerY, pY);
	     mod2 = new MoveModifier(1.5f,centerX+DELTA, pX,centerY, pY);
	     marked=true;
	   }
	   if(!marked)
	   {
		   mod = new MoveModifier(1.5f,centerX-DELTA/2, pX,centerY+DELTA/2, pY);
		   mod2 = new MoveModifier(1.5f,centerX+DELTA/2, pX,centerY-DELTA/2, pY);
	   }
	   bullet2.registerEntityModifier(mod2.deepCopy());
	   bullet.registerEntityModifier(mod.deepCopy());
	    bulletsToBeAdded.add(bullet);
	    bulletsToBeAdded.add(bullet2);
	}*/
	
/////////////////////////////////////Moving////////////////////////////////
	public void setShipRotation(Vector2 joysticPosition)
    {
    	ship.clearEntityModifiers();
		float  nextAngle=Tools.MathUtils.normAngle(MathUtils.radToDeg((float)Math.atan2(joysticPosition.x, -joysticPosition.y)));
	    float normPrevRotation=Tools.MathUtils.normAngle(ship.getRotation());
        RotationModifier entityModifier;
        
        if(Math.abs(nextAngle-normPrevRotation)>181)
        {
        	if(nextAngle>normPrevRotation) nextAngle-=360;
		    else normPrevRotation-=360;
		}
        float angleRotation=Math.abs(nextAngle-normPrevRotation);
       // float timeForRotaion=calculateRotationTime(angleRotation, 1);
		 entityModifier=new RotationModifier(0.5f,normPrevRotation,nextAngle);
		 ship.registerEntityModifier(entityModifier);
    }
	public void decelerationship(boolean ifSaveMoving){
		
		Vector2 velocity = shipBody.getLinearVelocity();//getting previous speed
	    if((velocity.x!=0&&velocity.y!=0))
			if(Math.abs(velocity.x)>0.5&&Math.abs(velocity.y)>0.5)shipBody.setLinearVelocity(velocity.x -(velocity.x/100) , velocity.y-(velocity.y/100)  );
			   else if(ifSaveMoving)
			       {
				      shipBody.setLinearVelocity(velocity.x -((velocity.x/100)-velocity.x%0.01f) , velocity.y-((velocity.y/100)-velocity.y%0.01f));
				   }
			else shipBody.setLinearVelocity(velocity.x -velocity.x , velocity.y-velocity.y  );
		
		
	}
	public void setshipSpeedWithLimit(Vector2 speedLimit,float pValueX,float pValueY){
		Vector2 oldSpeed = shipBody.getLinearVelocity();//getting previous speed of ship
		Vector2 newSpeed=new Vector2(oldSpeed.x + pValueX ,oldSpeed.y + pValueY);//speed of ship with acceleration
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
	private float calculateRotationTime(float angle,float angularSpeed)
	{
		float angleConst=180;float timeConst=1;float speedConst=1;
		float coeff=(timeConst*speedConst)/angleConst;
		float resultRotaionTime=coeff*(angle/angularSpeed);//timeFor180p*(angle/speedRotation);
		
		//float resultRotaionTime=0.5
		return resultRotaionTime;
    }
}
