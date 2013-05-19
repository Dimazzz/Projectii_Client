package org.projii.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.projii.commons.utils.Size;
import org.projii.commons.utils.Vector2;

import android.content.res.Resources;
import android.opengl.GLES20;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;


public class GameActivity extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================
	private final float UPDATE_TIME=0.06f;
	private final int BUTTON_SIZE=90;
	private final int FIRE_BUTTON_SIZE=135;

	
	// ===========================================================
	// Fields
	// ===========================================================
	private static  int CAMERA_WIDTH;
	private static  int CAMERA_HEIGHT;
	private boolean mPlaceOnScreenControlsAtDifferentVerticalLocations = false;
	private LinkedList<Sprite> targetList;
	private LinkedList<Sprite> TargetsToBeAdded;
	private LinkedList<Sprite> projectileLL;
	private LinkedList<Sprite> projectilesToBeAdded;
	private Sound shootingSound;
	private Music backgroundMusic;
	private BoundCamera mBoundChaseCamera;
	private TiledTextureRegion mshipTextureRegion;
	private BitmapTextureAtlas mship2TextureAtlas;
	private BitmapTextureAtlas mVehiclesTexture;
	private TextureRegion mVehiclesTextureRegion;
	private BitmapTextureAtlas mBoxTexture;
	private ITextureRegion mBoxTextureRegion;
	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;
	private GameScene mScene;
	
	private SpritePool bulletsPool;
    private SpritePool targetsPool;
	private PhysicsWorld mPhysicsWorld;

    private ShipModel shipViewer;
	
	private ITiledTextureRegion mBadgeTextureRegion;
	
	private Sound explosionSound;
	private BitmapTextureAtlas mHUDTexture;
	private TiledTextureRegion mToggleButtonTextureRegion;

	//==========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		Resources res = getResources();
		CAMERA_HEIGHT = res.getDisplayMetrics().heightPixels;
		CAMERA_WIDTH = res.getDisplayMetrics().widthPixels;
		mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mBoundChaseCamera);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		return engineOptions;
		}
	

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mVehiclesTexture = new BitmapTextureAtlas(this.getTextureManager(), 184, 184, TextureOptions.BILINEAR);
		this.mVehiclesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mVehiclesTexture, this, "pulya.png", 0, 0);
		this.mVehiclesTexture.load();
		
		this.mHUDTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 128,TextureOptions.BILINEAR);
		this.mToggleButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mHUDTexture, this, "toggle_button.png", 0, 0, 2, 1); // 256x128
		this.mBadgeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mHUDTexture, this, "hud_button.png", 256, 0,2,1);
		this.mHUDTexture.load();

		this.mship2TextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 320,80, TextureOptions.BILINEAR);
        this.mshipTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mship2TextureAtlas, this, "face_box_tiled2.png", 0, 0, 4, 1);
    	this.mship2TextureAtlas.load();
       

		this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();

		this.mBoxTexture = new BitmapTextureAtlas(this.getTextureManager(), 30, 52, TextureOptions.BILINEAR);
		this.mBoxTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBoxTexture, this, "box.png", 0, 0);
		this.mBoxTexture.load();
		
		 shootingSound=AudioFactory.getSound("shot.wav",this.mEngine,this);
         explosionSound=AudioFactory.getSound("explosion.wav",this.mEngine,this);
         backgroundMusic = AudioFactory.getMusic("back.wav",this.mEngine,this);
      
         bulletsPool=new SpritePool(mVehiclesTextureRegion, this.getVertexBufferObjectManager(),10,10);
         targetsPool=new SpritePool(mBoxTextureRegion, this.getVertexBufferObjectManager(),30,52);
	}
    private void initializeLists()
    {
    	projectileLL = new LinkedList<Sprite>();
		projectilesToBeAdded = new LinkedList<Sprite>();
		targetList = new LinkedList<Sprite>();
		TargetsToBeAdded = new LinkedList<Sprite>();
    }
	@Override
	public Scene onCreateScene() {
		initializeLists();
		this.mEngine.registerUpdateHandler(new FPSLogger());
        this.mPhysicsWorld = new FixedStepPhysicsWorld(30, new com.badlogic.gdx.math.Vector2(0, 0), false, 8, 1);
		this.mScene = new GameScene();
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);
        mScene.SetBackGroundImage("space.png", new Size(1920, 1200), this); 

		this.mBoundChaseCamera.setBounds(0, 0,mScene.getWorldSize().getWidth() ,mScene.getWorldSize().getHeight());
        this.mBoundChaseCamera.setBoundsEnabled(true);
		
		createBorderBox(mScene, 0, 0, mScene.getWorldSize().getWidth() ,mScene.getWorldSize().getHeight(), 1);
		final float centerX = (CAMERA_WIDTH ) / 2;
		final float centerY = (CAMERA_HEIGHT ) / 2;
		this.shipViewer=new ShipModel(centerX, centerY, new Size(80,80),mshipTextureRegion , this.getVertexBufferObjectManager(),mPhysicsWorld);
	    this.shipViewer.setTimeMovingUpdate(UPDATE_TIME);
        this.mBoundChaseCamera.setChaseEntity(shipViewer.getSprite());
        	this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(shipViewer.getSprite(), shipViewer.getBody(), true, false){
			public void onUpdate(float pSecondsElapsed){
				super.onUpdate(pSecondsElapsed);
				mBoundChaseCamera.updateChaseEntity();
			}
		});
		mScene.attachChild(shipViewer.getSprite());
		this.initializationTatgets(20);
		this.initOnScreenControls();
		mScene.registerUpdateHandler(GameUpdateHandler);
		backgroundMusic.play();
		return this.mScene;
	}

	
	@Override
	public void onGameCreated() {

	}

	private void initOnScreenControls() 
	{
		final float x1 = -20;
		final float y1 = CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight();
	    AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(x1, y1, this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, UPDATE_TIME, this.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				Vector2 joysticPosition=new Vector2(pValueX, pValueY);
				shipViewer.movingShip(joysticPosition);//считываем текущие координаты джостика
            }

			
		public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
			}
		});
		
		final float y2 = (this.mPlaceOnScreenControlsAtDifferentVerticalLocations) ? 0 : y1;
		final float x2 = CAMERA_WIDTH - this.mOnScreenControlBaseTextureRegion.getWidth();
		
		final HUD hud = new HUD();
		final TiledSprite fireSprite=createButtonTiledSprite("fire",(int) x2-40,(int) y2-15, FIRE_BUTTON_SIZE, FIRE_BUTTON_SIZE,  this.mToggleButtonTextureRegion, this.getVertexBufferObjectManager());
        final TiledSprite ultraSkill1Sprite=createButtonTiledSprite("ultButton1",(int) x2-110,(int) y2+40, BUTTON_SIZE, BUTTON_SIZE,  this.mBadgeTextureRegion, this.getVertexBufferObjectManager());
        final TiledSprite ultraSkill2Sprite=createButtonTiledSprite("ultButton2",(int) x2+45,(int) y2-70, BUTTON_SIZE, BUTTON_SIZE,  this.mBadgeTextureRegion, this.getVertexBufferObjectManager());
		TiledSprite[] hudElements={fireSprite,ultraSkill1Sprite,ultraSkill2Sprite};
        for(TiledSprite hudElement:hudElements)
        {
        	hud.attachChild(hudElement);
        	hud.registerTouchArea(hudElement);
        }
		analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);   
		analogOnScreenControl.getControlBase().setScaleCenter(-20, 128);  
		analogOnScreenControl.getControlBase().setScale(1.75f);   
		analogOnScreenControl.getControlKnob().setScale(1.75f);  
		analogOnScreenControl.setOnControlClickEnabled(true);
		analogOnScreenControl.refreshControlKnobPosition();
		
		this.mBoundChaseCamera.setHUD(hud);
		mScene.setChildScene(analogOnScreenControl);
	}
	private IUpdateHandler GameUpdateHandler = new IUpdateHandler() {
	

		@Override
		public void reset() {
		}

		@Override
		public void onUpdate(float pSecondsElapsed) {

			
			shipViewer.getTimeUpdate(GameActivity.this.mEngine.getSecondsElapsedTotal());
			Iterator<Sprite> targets = targetList.iterator();
			Sprite _target;
			boolean hit = false;

			// iterating over the targets
			while (targets.hasNext()) {
				_target = targets.next();

				// if target passed the left edge of the screen, then remove it
				// and call a fail
				if (_target.getX() <= -_target.getWidth()) {
					// removeSprite(_target, targets);
					targetsPool.recyclePoolItem(_target);
					targets.remove();
					// fail();
					break;
				}
				Iterator<Sprite> bullets = projectileLL.iterator();
				Sprite _projectile;
				// iterating over all the projectiles (bullets)
				while (bullets.hasNext()) {
					_projectile = bullets.next();

					// in case the projectile left the screen
					boolean isBulletLeftScreen=(_projectile.getX() >= (mBoundChaseCamera.getCenterX()+mBoundChaseCamera.getWidth()/2)
							|| _projectile.getX() <= (mBoundChaseCamera.getCenterX()- mBoundChaseCamera.getWidth()/2)
		                    || _projectile.getY() >=(mBoundChaseCamera.getCenterY() +mBoundChaseCamera.getHeight()/2)
						    || _projectile.getY() <=(mBoundChaseCamera.getCenterY() - mBoundChaseCamera.getHeight()/2));
					if (isBulletLeftScreen)
					{
						bulletsPool.recyclePoolItem(_projectile);
						bullets.remove();
						continue;
					}

					// if the targets collides with a projectile, remove the
					// projectile and set the hit flag to true
					if (_target.collidesWith(_projectile)) {
						explosionSound.play();
						bulletsPool.recyclePoolItem(_projectile);
						bullets.remove();
						hit = true;
						break;
					}
				}

				// if a bullet hit the target, remove the target, increment
				// the hit count, and update the score
				if (hit) {
					
					targetsPool.recyclePoolItem(_target);
					targets.remove();
					hit = false;
				}
			}

			// a work around to avoid ConcurrentAccessException
			projectileLL.addAll(projectilesToBeAdded);
			projectilesToBeAdded.clear();

			targetList.addAll(TargetsToBeAdded);
			TargetsToBeAdded.clear();

		}
	};

	private void initializationTatgets(int count) {
		Random rand = new Random();	   
		for(int i=0; i<count;i++)
		{  float pX=rand.nextInt((int) mBoundChaseCamera.getBoundsWidth()-100);
		   float pY=rand.nextInt((int) mBoundChaseCamera.getBoundsHeight()-100);
			addTarget(pX, pY, mPhysicsWorld,mScene);
		}
	
	}

	private void addTarget(float pX,float pY,PhysicsWorld physicWorld,Scene scene) {
		 
		 Sprite box = targetsPool.obtainPoolItem();//
         box.setPosition(pX,pY);
		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0.1f, 0.5f, 0.5f);
		final Body boxBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, box, BodyType.DynamicBody, boxFixtureDef);
		boxBody.setLinearDamping(10);
		boxBody.setAngularDamping(10);
        physicWorld.registerPhysicsConnector(new PhysicsConnector(box, boxBody, true, true));
		scene.attachChild(box);
		TargetsToBeAdded.add(box);

	}
private TiledSprite createButtonTiledSprite(final String function,int positionX,int positionY,int width,int height, ITiledTextureRegion BadgeTextureRegion, VertexBufferObjectManager objectManager)
{
	final TiledSprite sprite = new TiledSprite(positionX, positionY,width,height, BadgeTextureRegion, objectManager) {
		public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
    		switch(pAreaTouchEvent.getAction()){
    		case TouchEvent.ACTION_DOWN:
    			this.setCurrentTileIndex(0);
    			 executeSpriteTouchEvent(function);//shipViewer.shootBullet(mScene,bulletsPool,projectilesToBeAdded,shootingSound);
               break;
    			
			case TouchEvent.ACTION_UP:
				this.setCurrentTileIndex(1);
				//shootingSound.setLooping(false);
				break;
			case TouchEvent.ACTION_MOVE:
				break;
			
    		}
    		return true;
		}	
	};
	return sprite;
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
private void executeSpriteTouchEvent(String buttonName)
{
	if(buttonName=="fire"){ shipViewer.shooting(mScene,bulletsPool,projectilesToBeAdded,shootingSound);}
	if(buttonName=="ultButton1"){GameActivity.this.initializationTatgets(20);}
	if(buttonName=="ultButton2"){}
}



}