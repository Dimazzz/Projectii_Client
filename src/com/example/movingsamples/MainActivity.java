package com.example.movingsamples;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import Tools.Size;
import android.content.res.Resources;
import android.graphics.Point;
import android.opengl.GLES20;
import android.util.FloatMath;
import android.widget.Toast;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.util.FPSLogger;

import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.math.MathUtils;
import com.example.*;

public class MainActivity extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================



	private static  int CAMERA_WIDTH;
	private static  int CAMERA_HEIGHT;
	private final int START_JOYSTICK_POSITION=0;
	private boolean mPlaceOnScreenControlsAtDifferentVerticalLocations = false;
	private LinkedList targetLL;
	private LinkedList TargetsToBeAdded;
	private LinkedList projectileLL;
	private LinkedList projectilesToBeAdded;
	private Sound shootingSound;
	private Music backgroundMusic;
	private float delta, prevTime = 0;
	
	// ===========================================================
	// Fields
	// ===========================================================

	private BoundCamera mBoundChaseCamera;
	private TiledTextureRegion mshipTextureRegion;
	private BitmapTextureAtlas mship2TextureAtlas;
	private BitmapTextureAtlas mVehiclesTexture;
	private TextureRegion mVehiclesTextureRegion;
	private BitmapTextureAtlas mBoxTexture;
	private ITextureRegion mBoxTextureRegion;
    private Point worldSize;
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;
	private MyScene mScene;

	private PhysicsWorld mPhysicsWorld;

    private ShipViewer shipViewer;
	private BitmapTextureAtlas mBackTextureAtlas;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mBadgeTextureRegion;
	private BitmapTextureAtlas mAnimatedShots;
	private TiledTextureRegion mAnimatedSpriteShotTextureRegion;
	private Sound explosionSound;
	private BitmapTextureAtlas mHUDTexture;
	private TiledTextureRegion mToggleButtonTextureRegion;
	private Sound quueSound;

	//==========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		Resources res = getResources();
		CAMERA_HEIGHT = res.getDisplayMetrics().heightPixels;
		CAMERA_WIDTH = res.getDisplayMetrics().widthPixels;
		worldSize=new Point(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mBoundChaseCamera);
		
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);

		return engineOptions;
		}
	

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 350, 350, TextureOptions.BILINEAR);
		BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "button.png", 0, 0);
		this.mBadgeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "badge.png", 150, 0);

		this.mBitmapTextureAtlas.load();

		this.mVehiclesTexture = new BitmapTextureAtlas(this.getTextureManager(), 184, 184, TextureOptions.BILINEAR);
		this.mVehiclesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mVehiclesTexture, this, "ship2.png", 0, 0);
		this.mVehiclesTexture.load();
		this.mHUDTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128,TextureOptions.BILINEAR);
		this.mToggleButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mHUDTexture, this, "toggle_button.png", 0, 0, 2, 1); // 256x128
		this.mHUDTexture.load();

		this.mBackTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 1920, 1200, TextureOptions.BILINEAR);
	    BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBackTextureAtlas, this,"space.png", 0, 0);
	    	
        this.mBackTextureAtlas.load();
 
        this.mship2TextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 320,80, TextureOptions.BILINEAR);
        this.mshipTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mship2TextureAtlas, this, "face_box_tiled.png", 0, 0, 4, 1);
    	
        this.mship2TextureAtlas.load();
       

		this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();

		this.mBoxTexture = new BitmapTextureAtlas(this.getTextureManager(), 30, 52, TextureOptions.BILINEAR);
		this.mBoxTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBoxTexture, this, "box.png", 0, 0);
		this.mBoxTexture.load();
		
		this.mAnimatedShots = new BitmapTextureAtlas(this.getTextureManager(),64, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mAnimatedSpriteShotTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mAnimatedShots, this, "shots5.png", 0, 0, 4, 2);
         this.mAnimatedShots.load();
         SoundFactory.setAssetBasePath("mfx/");
         try {
             shootingSound = SoundFactory.createSoundFromAsset(mEngine
                 .getSoundManager(), this, "shot.wav");
         } catch (IllegalStateException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         
         try {
             explosionSound = SoundFactory.createSoundFromAsset(mEngine
                 .getSoundManager(), this, "explosion.wav");
         } catch (IllegalStateException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         try {
             quueSound = SoundFactory.createSoundFromAsset(mEngine
                 .getSoundManager(), this, "queue.wav");
         } catch (IllegalStateException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         MusicFactory.setAssetBasePath("mfx/");

         try {
             backgroundMusic = MusicFactory.createMusicFromAsset(mEngine
                 .getMusicManager(), this, "back.wav");
             backgroundMusic.setLooping(true);
         } catch (IllegalStateException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
		
	}

	@Override
	public Scene onCreateScene() {
		projectileLL = new LinkedList();
		projectilesToBeAdded = new LinkedList();
		targetLL = new LinkedList();
		TargetsToBeAdded = new LinkedList();
		this.mEngine.registerUpdateHandler(new FPSLogger());
        this.mPhysicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);
		this.mScene = new MyScene();
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);
        mScene.SetBackGroundImage("space.png", new Size(1920, 1200), this); 


		worldSize.x=1920;
		worldSize.y=1200;
		this.mBoundChaseCamera.setBounds(0, 0,worldSize.x ,worldSize.y);
        this.mBoundChaseCamera.setBoundsEnabled(true);
		
		createBorderBox(mScene, 0, 0, worldSize.x, worldSize.y, 1);
		final float centerX = (CAMERA_WIDTH - this.mVehiclesTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mVehiclesTextureRegion.getHeight()) / 2;
		this.shipViewer=new ShipViewer(centerX, centerY, new Size(80,80),mshipTextureRegion , this.getVertexBufferObjectManager(),mPhysicsWorld);
	  
        this.mBoundChaseCamera.setChaseEntity(shipViewer.getSprite());
        	this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(shipViewer.getSprite(), shipViewer.getBody(), true, false){
			public void onUpdate(float pSecondsElapsed){
				super.onUpdate(pSecondsElapsed);
				mBoundChaseCamera.updateChaseEntity();
			}
		});
        	
        	/*mScene.registerUpdateHandler(new IUpdateHandler() {

        	    @Override
        	    public void reset() {
        	        // TODO Auto-generated method stub
        	    }

        	    @Override
        	    public void onUpdate(float pSecondsElapsed) {
        	    	delta = mScene.getSecondsElapsedTotal() - prevTime;
        	    	prevTime = mScene.getSecondsElapsedTotal();
        	    	shipViewer.setFireCounter(shipViewer.getFireCounter() + delta);
        	    	updateShots(pSecondsElapsed);
        	    }
        	});*/
		mScene.attachChild(shipViewer.getSprite());
		
		this.initObstacles(20);
		this.initOnScreenControls();

		
		mScene.registerUpdateHandler(detect);
		backgroundMusic.play();
		return this.mScene;
	}

	
	@Override
	public void onGameCreated() {

	}
 
	private void updateShots(float pSecondsElapsed) {
		 for (int i = 0; i < this.mEngine.getScene().getChildCount(); i++)
		 {
		  if(mScene.getChildByIndex(i).getUserData() == "shot")
		  {  Bullet bullet=(Bullet) mScene.getChildByIndex(i);
            double rad = Math.toRadians(90-bullet.getAngle());
            

		  bullet.setPosition(bullet.getX()+10*FloatMath.cos((float)rad), bullet.getY() - 10*FloatMath.sin((float)rad));
		  } 
		  if (mScene.getChildByIndex(i).getY() < -100.0f)
		   this.mEngine.getScene().detachChild(mScene.getChildByIndex(i));
		 };  
		}
	private boolean _BtnTouched = false;
	private void initOnScreenControls() 
	{
		final HUD hud = new HUD();
		final float x1 = -20;
		final float y1 = CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight();
	    AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(x1, y1, this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
			   
				shipViewer.setshipSpeedWithLimit(new Vector2(30,30),pValueX,pValueY);
			        
			if(!(pValueX ==START_JOYSTICK_POSITION && pValueY == START_JOYSTICK_POSITION)) {
       		   
				   shipViewer.setShipRotation(new Vector2(pValueX,pValueY));
				   shipViewer.setCancelToStop();
                  // backgroundMusic
				}
				else {
					shipViewer.setShipToStop();
						}
			
			}

			// Флаг для определения того, что коснулись кнопки.
			
		public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
			//gameToast("It's clicked")
			}
		});
		
		final float y2 = (this.mPlaceOnScreenControlsAtDifferentVerticalLocations) ? 0 : y1;
		final float x2 = CAMERA_WIDTH - this.mOnScreenControlBaseTextureRegion.getWidth();
	
			final TiledSprite fireSprite = new TiledSprite(x2-40, y2-15,135,135, this.mToggleButtonTextureRegion, this.getVertexBufferObjectManager()) {
		public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
    		switch(pAreaTouchEvent.getAction()){
    		case TouchEvent.ACTION_DOWN:
    			this.setCurrentTileIndex(1);
    			shipViewer.shootBullet(mScene, mAnimatedSpriteShotTextureRegion,this.getVertexBufferObjectManager(), worldSize.x, projectilesToBeAdded);
				
    			shootingSound.play();
				 MainActivity.this._BtnTouched = true;
		         break;
    			
			case TouchEvent.ACTION_UP:
				this.setCurrentTileIndex(0);
				shootingSound.setLooping(false);
				break;
			case TouchEvent.ACTION_MOVE:
				//shipViewer.shootBullet(mScene, mAnimatedSpriteShotTextureRegion,this.getVertexBufferObjectManager(), worldSize.x, projectilesToBeAdded);
				//quueSound.play();
			//	shootingSound.play();
			    //shootingSound.setLooping(true);
				break;
			
    		}
    		return true;
		}
	};
	/*final Sprite fireSprite = new Sprite(x2-40, y2-15,140,140, this.mNextTextureRegion, this.getVertexBufferObjectManager()) {
		@Override
		public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			if(pSceneTouchEvent.isActionDown()) {
				///shipViewer.createShot(mAnimatedShotSprite, mScene, mAnimatedSpriteShotTextureRegion, this.getVertexBufferObjectManager());
				shipViewer.shootBullet(mScene, mAnimatedSpriteShotTextureRegion,this.getVertexBufferObjectManager(), worldSize.x, projectilesToBeAdded);
				shootingSound.play();
				
			}
			return true;
		};
	};*/
	hud.attachChild(fireSprite);

	hud.registerTouchArea(fireSprite);
		
		final Sprite ult1Sprite = new Sprite(x2-110, y2+40,90,90, this.mBadgeTextureRegion, this.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					MainActivity.this.initObstacles(20);
				}
				return true;
			};
		};
		hud.attachChild(ult1Sprite);

		hud.registerTouchArea(ult1Sprite);
		final Sprite ult2Sprite = new Sprite(x2+45, y2-70,90,90, this.mBadgeTextureRegion, this.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					gameToast("Rotation->"+String.valueOf(Tools.MathUtils.normAngle(shipViewer.getSprite().getRotation())));
					//float angleRad=Math.
					gameToast("Sin->"+String.valueOf(Math.sin(Math.toRadians(Tools.MathUtils.normAngle(shipViewer.getSprite().getRotation())))));
					gameToast("Cos->"+String.valueOf(Math.cos(Math.toRadians(Tools.MathUtils.normAngle(shipViewer.getSprite().getRotation())))));
				}
				return true;
			};
		};
		hud.attachChild(ult2Sprite);

		hud.registerTouchArea(ult2Sprite);
		
		

		
		analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);   //������������
		analogOnScreenControl.getControlBase().setScaleCenter(-20, 128);   // ���������� ����� �������� 
		analogOnScreenControl.getControlBase().setScale(1.75f);   // ������ ������� ��������
		analogOnScreenControl.getControlKnob().setScale(1.75f);  // ������ ���������� � ������ 
		analogOnScreenControl.setOnControlClickEnabled(true);
		
		analogOnScreenControl.refreshControlKnobPosition();
		this.mBoundChaseCamera.setHUD(hud);
		mScene.setChildScene(analogOnScreenControl);
	}
    
	public void gameToast(final String msg) {
	    this.runOnUiThread(new Runnable() {
	        @Override
	       public void run() {
	           Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	IUpdateHandler detect = new IUpdateHandler() {
	    @Override
	    public void reset() {
	    }

	    @Override
	    public void onUpdate(float pSecondsElapsed) {

	        Iterator<Sprite> targets = targetLL.iterator();
	        Sprite _target;
	        boolean hit = false;

	        while (targets.hasNext()) {
	            _target = targets.next();

	            boolean iSOutWorldDown=_target.getX() <= -_target.getWidth()||_target.getY() <= -_target.getHeight();
	            boolean isOutWorldUp=_target.getX()+_target.getWidth()>=worldSize.x||_target.getY()+_target.getHeight()>=worldSize.y;
	            
	            if (iSOutWorldDown||isOutWorldUp) {
	                removeSprite(_target, targets);
	                break;
	            }
	            Iterator<Sprite> projectiles = projectileLL.iterator();
	            Sprite _projectile;
	            while (projectiles.hasNext()) {
	                _projectile = projectiles.next();

	               /* if (_projectile.getX() >= mBoundChaseCamera.getWidth()
	                    || _projectile.getY() >= mBoundChaseCamera.getHeight()
	                    + _projectile.getHeight()
	                    || _projectile.getY() <= -_projectile.getHeight()) {
	                        removeSprite(_projectile, projectiles);
	                        continue;
	                }*/

	                if (_target.collidesWith(_projectile)) {
	                	explosionSound.play();
	                    removeSprite(_projectile, projectiles);
	                    hit = true;
	                    break;
	                }
	            }

	            if (hit) {
	            	
	                removeSprite(_target, targets);
	                hit = false;
	            }

	        }
	        projectileLL.addAll(projectilesToBeAdded);
	        projectilesToBeAdded.clear();

	        targetLL.addAll(TargetsToBeAdded);
	        TargetsToBeAdded.clear();
	    }
	};

	
//That way, we won’t interrupt the work of the engine drawing when we remove the sprites
	public void removeSprite(final Sprite _sprite, Iterator it) {
	    runOnUpdateThread(new Runnable() {

	        @Override
	        public void run() {
	            mScene.detachChild(_sprite);
	        }
	    });
	    it.remove();
	}
	private void initObstacles(int count) {
		Random rand = new Random();	   
		for(int i=0; i<count;i++)
		{  float pX=rand.nextInt((int) mBoundChaseCamera.getBoundsWidth()-100);
		   float pY=rand.nextInt((int) mBoundChaseCamera.getBoundsHeight()-100);
			addObstacle(pX, pY, mPhysicsWorld,mScene);
		}
	
	}

	private void addObstacle(float pX,float pY,PhysicsWorld physicWorld,Scene scene) {
		 Sprite box = new Sprite(pX, pY, 30, 52, this.mBoxTextureRegion, this.getVertexBufferObjectManager());

		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0.1f, 0.5f, 0.5f);
		final Body boxBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, box, BodyType.DynamicBody, boxFixtureDef);
		boxBody.setLinearDamping(10);
		boxBody.setAngularDamping(10);
        physicWorld.registerPhysicsConnector(new PhysicsConnector(box, boxBody, true, true));
		scene.attachChild(box);
		TargetsToBeAdded.add(box);

	}

private void createBorderBox(Scene scene,float pointFromX,float pointFromY,float width,float height,int fatness)
{
	final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
	final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);

    Rectangle[] boxLines=new Rectangle[]{
    		/*bottomOuter*/	new Rectangle(pointFromX,      height - fatness, width,  fatness, vertexBufferObjectManager),
    		/*topOuter*/    new Rectangle(pointFromX,      pointFromY,       width,  fatness, vertexBufferObjectManager),
    		/*leftOuter*/   new Rectangle(pointFromX,      pointFromY,       fatness,height, vertexBufferObjectManager),
    		/*rightOuter*/  new Rectangle(width - fatness, pointFromY,       2,      height, vertexBufferObjectManager)
    		};
    int increment=0;
	while(increment<4){
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, boxLines[increment], BodyType.StaticBody, wallFixtureDef);
		scene.attachChild(boxLines[increment]);
		increment++;
	}
}

}