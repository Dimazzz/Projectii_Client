
	package org.projii.client;


import java.util.LinkedList;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.projii.commons.shipLogic.moveLogic;
import org.projii.commons.shipLogic.moveLogic.MovingType;
import org.projii.commons.shipLogic.shootLogic;
import org.projii.commons.utils.RotationAngles;
import org.projii.commons.utils.Size;
import org.projii.commons.utils.Vector2;
import org.projii.interfaces.IVisualizeShip;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
 public class ShipModel {
	
	private static final float SPEED_BULLET = 1000;
	private static final float TIME_BULLET_FLIGHT = 1.5f;
	private final float DEFAULT_TIME_ROTATION=0.3f;
	private final Vector2 SPEED_LIMIT=new Vector2(15, 15);
	
	private AnimatedSprite ship;
	private Body shipBody;
	public SpritePool pPool;
	private float timeCounter = 0, timeRate= 0.25f,betweenRotationsTimeCounter;
	private float delta, prevTime = 0;
	private float UPDATE_TIME=0.075f;
    private float prevAngle=0;
	private boolean isBraking;
    private	IVisualizeShip visualizator;
	public ShipModel(float locationX,float locationY,Size shipSize,ITiledTextureRegion texture,VertexBufferObjectManager objMan,PhysicsWorld physicsWorld)
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
        initializePhysicBody(physicsWorld);
        visualizator=new VisualizerShip();
    }
	public ShipModel(){}
	private void initializePhysicBody(PhysicsWorld physicsWorld)
	{
		final FixtureDef shipFixtureDef = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
		this.shipBody = PhysicsFactory.createBoxBody(physicsWorld, this.ship, BodyType.DynamicBody, shipFixtureDef);
        ship.animate(100);
       
	
	}
    //moving//
	public void movingShip(Vector2 joysticPosition)
	{
		 MovingType currentMovingType=moveLogic.getMovingType(joysticPosition,betweenRotationsTimeCounter,ship.getRotation(),prevAngle, UPDATE_TIME);
		 if(currentMovingType==MovingType.Braking)
		 {
		 	 isBraking=true;
			 return;
		 }
		 isBraking=false;
		 RotationAngles rotationAngles= moveLogic.calculateAngles(joysticPosition,ship.getRotation());
		 float newDeltaAngle=Math.abs(rotationAngles.nextAngle-rotationAngles.prevAngle);
		 Vector2 incrementSpeed=joysticPosition;
		 switch (currentMovingType) 
		 { 
		    //удерживаем джостик в одной позиции
		 	case OneDirection:  {
                                  setSpeed(SPEED_LIMIT, incrementSpeed,shipBody);
		 		                 } 
		 		break;
		 	//медленное вращение-рассортировать на 2 вида с движением и вращением по кругу	
		 	case Smooth  :  {
		 		                setSpeed(SPEED_LIMIT, new Vector2(incrementSpeed.x/1.5f,incrementSpeed.y/1.5f),shipBody);
		 		             	makeRotation(DEFAULT_TIME_ROTATION, rotationAngles,ship,newDeltaAngle);
		 		             } 
		 	      break;
		    case bigAngle:	{
		    	                float timeForRotation=moveLogic.calculateRotationTime(newDeltaAngle, 1);
		    					setSpeed(SPEED_LIMIT, new Vector2(incrementSpeed.x/3,incrementSpeed.y/3),shipBody);
		    					makeRotation(timeForRotation, rotationAngles,ship,newDeltaAngle);
		    					//сделать зависимость между
		                   	}
			     break;
		    default:
			  break;
		 }
		// setshipSpeedWithLimit(SPEED_LIMIT,pValueX,pValueY);
		
		
	}
	/*Realzation*/
	
	private void setSpeed(Vector2 speedLimit,Vector2 incrementSpeed,Body body){
		visualizator.setSpeed(speedLimit, incrementSpeed, body);
		
	}
	private void updateRotateValues(float nextAngle,float newDeltaAngle)
	{
		 betweenRotationsTimeCounter=0;//updating time counter
		 prevAngle=nextAngle;

	}
	
	private void makeRotation(float timeForRotation,RotationAngles rotationAngles,AnimatedSprite sprite,float newDeltaAngle)
	{
		 visualizator.rotate(timeForRotation, rotationAngles, sprite);
		 updateRotateValues(rotationAngles.nextAngle,newDeltaAngle);
		
	}
	
    private void decelerationship(boolean ifSaveMoving){
    	Vector2 shipLinearVelocity=new Vector2(shipBody.getLinearVelocity().x,shipBody.getLinearVelocity().y);
		Vector2 brakingSpeed= moveLogic.getBrakingSpeed(shipLinearVelocity, ifSaveMoving);
    	shipBody.setLinearVelocity(brakingSpeed.x,brakingSpeed.y);//getting previous speed
	    
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
   
    private void shootLinearBullet(Vector2 bulletStartPosition, Vector2 aim,Scene scene,SpritePool bulletPool,LinkedList<Sprite> bulletsToBeAdded) 
	{ 
		Sprite bullet;
		bullet = bulletPool.obtainPoolItem();
		bullet.setPosition(bulletStartPosition.x,bulletStartPosition.y);
		scene.attachChild(bullet);
		MoveModifier  mod = new MoveModifier(TIME_BULLET_FLIGHT,bulletStartPosition.x, aim.x,bulletStartPosition.y, aim.y);
		bullet.registerEntityModifier(mod.deepCopy());
	    bulletsToBeAdded.add(bullet);
	    
	    
	}
	public void shooting(Scene scene,SpritePool bulletPool,LinkedList<Sprite> bulletsToBeAdded,Sound shootingSound) 
	{ 
		if (timeCounter >= timeRate)
		{
			//initialization
			float  deltaAngle=50;float shipRotation=ship.getRotation();float shipLeftX=ship.getX();
			float shipUpY= ship.getY();float shipWidth=ship.getWidth();float shipHeight=ship.getHeight();
		    //endOfInitialization
			Vector2 bullet1StartPosition=shootLogic.getsStartBulletPosition(shipRotation, deltaAngle, shipLeftX, shipUpY, shipWidth, shipHeight);
			Vector2 bullet2StartPosition=shootLogic.getsStartBulletPosition(shipRotation, -deltaAngle, shipLeftX, shipUpY, shipWidth, shipHeight);
			Vector2 AimByLinearDistance=shootLogic.getAimByLinearDistance(SPEED_BULLET, shipRotation, shipLeftX, shipUpY, shipWidth, shipHeight);
			shootLinearBullet(bullet1StartPosition, AimByLinearDistance, scene, bulletPool, bulletsToBeAdded);
			shootLinearBullet(bullet2StartPosition, AimByLinearDistance, scene, bulletPool, bulletsToBeAdded);
			timeCounter=0;
		    shootingSound.play();
			
		}
	    
	}
	
}
 