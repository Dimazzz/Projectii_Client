package com.example.joysticandmovingsamples;





import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;

import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.content.res.Resources;
import android.graphics.Point;
import android.opengl.GLES20;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Transform;

public class MainActivity extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================



	private static  int CAMERA_WIDTH;
	private static  int CAMERA_HEIGHT;
	private boolean isGetToStop=false;
	private final int START_JOYSTICK_POSITION=0;
	// ===========================================================
	// Fields
	// ===========================================================

	private BoundCamera mBoundChaseCamera;

	private BitmapTextureAtlas mVehiclesTexture;
	private TextureRegion mVehiclesTextureRegion;

	private BitmapTextureAtlas mBoxTexture;
	private ITextureRegion mBoxTextureRegion;
	private TiledTextureRegion mPlayerTextureRegion;
	private TMXTiledMap mTMXTiledMap;
	protected int mCactusCount;

    private Point worldSize;


	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;

	private Scene mScene;

	private PhysicsWorld mPhysicsWorld;

	private Body ShipBody;
	private Sprite Ship;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		Resources res = getResources();
		CAMERA_HEIGHT = res.getDisplayMetrics().heightPixels;
		CAMERA_WIDTH = res.getDisplayMetrics().widthPixels;
		worldSize=new Point(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
  
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mBoundChaseCamera);

		}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mVehiclesTexture = new BitmapTextureAtlas(this.getTextureManager(), 30, 52, TextureOptions.BILINEAR);
		this.mVehiclesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mVehiclesTexture, this, "box.png", 0, 0);
		this.mVehiclesTexture.load();

		


		this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();

		this.mBoxTexture = new BitmapTextureAtlas(this.getTextureManager(), 30, 52, TextureOptions.BILINEAR);
		this.mBoxTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBoxTexture, this, "box.png", 0, 0);
		this.mBoxTexture.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mPhysicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);
		this.mScene = new Scene();
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);
		try {
			final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
					/* We are going to count the tiles that have the property "cactus=true" set. */
					if(pTMXTileProperties.containsTMXProperty("cactus", "true")) {
						MainActivity.this.mCactusCount++;
					}
				}
			});
			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/desert.tmx");

			
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}

		final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
		mScene.attachChild(tmxLayer);

		/* Make the camera not exceed the bounds of the TMXEntity. */
		
		this.mBoundChaseCamera.setBounds(0, 0, tmxLayer.getWidth(),tmxLayer.getHeight());
		worldSize.x=tmxLayer.getWidth();
		worldSize.y=tmxLayer.getHeight();
		
		//gameToast(" height-> "+String.valueOf(tmxLayer.getHeight())+"width-> "+String.valueOf(tmxLayer.getWidth()));
	//	gameToast(" height-> "+String.valueOf(CAMERA_HEIGHT)+"width-> "+String.valueOf(CAMERA_HEIGHT));
		
		this.mBoundChaseCamera.setBoundsEnabled(true);
		//this.initBorders();
		createBorderBox(mScene, 0, 0, tmxLayer.getWidth(), tmxLayer.getHeight(), 2);
		final float centerX = (CAMERA_WIDTH - this.mVehiclesTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mVehiclesTextureRegion.getHeight()) / 2;
	    this.Ship = new Sprite(centerX, centerY, 30, 52, this.mVehiclesTextureRegion, this.getVertexBufferObjectManager())
        {
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                                float pTouchAreaLocalX, float pTouchAreaLocalY)
                {
					return mFlippedHorizontal;
                       
                }
                @Override
                protected void onManagedUpdate(float pSecondsElapsed) {
                       	//if(isGetToStop)
                	     //  decelerationShip(MainActivity.this.ShipBody ,true);
                        super.onManagedUpdate(pSecondsElapsed);
                        
                        
                }
        };  
        this.mBoundChaseCamera.setChaseEntity(Ship);
        final FixtureDef carFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		this.ShipBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, this.Ship, BodyType.DynamicBody, carFixtureDef);

		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(Ship, ShipBody, true, false){
			public void onUpdate(float pSecondsElapsed){
				super.onUpdate(pSecondsElapsed);
				mBoundChaseCamera.updateChaseEntity();
			}
		});
		mScene.attachChild(Ship);
		
		this.initObstacles();
		this.initOnScreenControls();

	

		return this.mScene;
	}

	@Override
	public void onGameCreated() {

	}
    private void SmoothRotation(Body body,float prevAngle,float nextAngle,int steps){
    	
    }
	// ===========================================================
	// Methods
	// ===========================================================

	private void initOnScreenControls() {
		
		final float x1 = 0;
		final float y1 = CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight();
		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(x1, y1, this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				// MainActivity.this.mBoundChaseCamera.updateChaseEntity();
				setShipSpeedWithLimit(ShipBody,new Vector2(10,10),pValueX,pValueY);
				if(!(pValueX ==START_JOYSTICK_POSITION && pValueY == START_JOYSTICK_POSITION)) {
				   float prevAngle= ShipBody.getAngle();
				  
				   float nextAngle=(float)Math.atan2(pValueX, -pValueY) ;
				   
				   //gameToast("PrevAngle->"+String.valueOf(MathUtils.radToDeg(prevAngle))+"NextAngle->"+String.valueOf(MathUtils.radToDeg(nextAngle)));
				   
					//ShipBody.setTransform(ShipBody.getWorldCenter(),nextAngle);
				   // ParallelEntityModifier entityModifier=new ParallelEntityModifier(new RotationModifier(1f, 180, 0));
				//   MainActivity.this.Ship.registerEntityModifier(entityModifier);
					//gameToast("PValueX->"+ String.valueOf(pValueX)+"PValueY->"+ String.valueOf(pValueY));
                   MainActivity.this.Ship.setRotation(MathUtils.radToDeg((float)Math.atan2(pValueX, -pValueY)));
                   isGetToStop=false;
				}
				else {
					isGetToStop=true;
					Vector2 speedBeforeTouchUp=ShipBody.getLinearVelocity();
				   	ShipBody.setLinearVelocity(speedBeforeTouchUp.x -(speedBeforeTouchUp.x/50), speedBeforeTouchUp.y-(speedBeforeTouchUp.y/50)  );//первоначальное торможение скорости 
					}
				
			}

		
		public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
			}
		});
		analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);   //������������
		analogOnScreenControl.getControlBase().setScaleCenter(-20, 128);   // ���������� ����� �������� 
		analogOnScreenControl.getControlBase().setScale(1.75f);   // ������ ������� ��������
		analogOnScreenControl.getControlKnob().setScale(1.75f);  // ������ ���������� � ������ 
		analogOnScreenControl.setOnControlClickEnabled(true);
		analogOnScreenControl.refreshControlKnobPosition();
		
		this.mScene.setChildScene(analogOnScreenControl);
	}

	private void initShip() {
		

		
	}

	private void initObstacles() {
		this.addObstacle(CAMERA_WIDTH / 2,  50);
		this.addObstacle((CAMERA_WIDTH / 2)-200, 100);
		this.addObstacle(CAMERA_WIDTH / 3+100, CAMERA_HEIGHT /2);
		this.addObstacle(CAMERA_WIDTH -100, CAMERA_HEIGHT -200);
		this.addObstacle(worldSize.x / 2,  worldSize.y/2);
		this.addObstacle(worldSize.x /2, 500);
		this.addObstacle(CAMERA_WIDTH / 3+100, 7*worldSize.x / 8);
		this.addObstacle(100, 500);
	
	}

	private void addObstacle(final float pX, final float pY) {
		final Sprite box = new Sprite(pX, pY, 30, 52, this.mBoxTextureRegion, this.getVertexBufferObjectManager());

		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0.1f, 0.5f, 0.5f);
		final Body boxBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, box, BodyType.DynamicBody, boxFixtureDef);
		boxBody.setLinearDamping(10);
		boxBody.setAngularDamping(10);

		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(box, boxBody, true, true));

		this.mScene.attachChild(box);
	}
	public void gameToast(final String msg) {
	    this.runOnUiThread(new Runnable() {
	        @Override
	       public void run() {
	           Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
private void setShipSpeedWithLimit(Body Ship,Vector2 speedLimit,float pValueX,float pValueY){
	Vector2 speed = Ship.getLinearVelocity();//getting previous speed of ship
	if(speed.x+pValueX<speedLimit.x)
		if(speed.y+pValueY<speedLimit.y)
           Ship.setLinearVelocity(speed.x + pValueX  , speed.y + pValueY );
		else  Ship.setLinearVelocity(speed.x + pValueX  , speedLimit.y );
	else if(speed.y+pValueY<speedLimit.y)
        Ship.setLinearVelocity(speedLimit.x  , speed.y + pValueY );
		else  Ship.setLinearVelocity( speedLimit.x  , speedLimit.y );
	
}
private void decelerationShip(Body ship,boolean ifSaveMoving){
	
	Vector2 velocity = ship.getLinearVelocity();//getting previous speed
    if((velocity.x!=0&&velocity.y!=0))
		if(Math.abs(velocity.x)>0.5&&Math.abs(velocity.y)>0.5)ship.setLinearVelocity(velocity.x -(velocity.x/100) , velocity.y-(velocity.y/100)  );
		   else if(ifSaveMoving)
		       {
			      ship.setLinearVelocity(velocity.x -((velocity.x/100)-velocity.x%0.01f) , velocity.y-((velocity.y/100)-velocity.y%0.01f));
			   }
		else ship.setLinearVelocity(velocity.x -velocity.x , velocity.y-velocity.y  );
	
	
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
