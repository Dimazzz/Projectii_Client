package org.projii.client;

import java.util.Iterator;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.projii.client.tools.Size;


public class GameScene extends Scene {

	public GameScene(){
	       super();
	}
	
	private Size worldSize;
	
	public void SetBackGroundImage(String pathToFile,final Size imageSize,SimpleBaseGameActivity game){
		worldSize=imageSize;
		/////////////////////////////////////////////////////////////////
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BitmapTextureAtlas mBackTextureAtlas = new BitmapTextureAtlas(game.getTextureManager(), (int) imageSize.getWidth(), (int) imageSize.getHeight(), TextureOptions.BILINEAR);
	    ITextureRegion mBackTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackTextureAtlas,game,"space.png", 0, 0);
        mBackTextureAtlas.load();
		Sprite sprite  = new Sprite(0, 0, imageSize.getWidth(), imageSize.getHeight(), mBackTextureRegion, game.getVertexBufferObjectManager());
	    attachChild(sprite);
	}
	public Size getWorldSize()
	{
		return worldSize;
	}
	public void setWorldSize(Size newWorldSize)
	{
		worldSize=newWorldSize;
	}
	public void setWorldSize(Float width,float height)
	{
		worldSize= new Size(width,height);
	}
	public void removeSprite(final Sprite _sprite,SimpleBaseGameActivity game, Iterator it) {
	    game.runOnUpdateThread(new Runnable() {
	        @Override
	        public void run() {
	            detachChild(_sprite);
	        }
	    });
	    it.remove();
	}
}
