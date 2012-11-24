package com.example.movingsamples;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Bullet extends AnimatedSprite {

	public Bullet(float pX, float pY, float pWidth, float pHeight,
			ITiledTextureRegion pTiledTextureRegion,
			ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion,
				pTiledSpriteVertexBufferObject);
		// TODO Auto-generated constructor stub
	}
	public Bullet(float pX, float pY, 
			ITiledTextureRegion pTiledTextureRegion,
			ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY,  pTiledTextureRegion,
				pTiledSpriteVertexBufferObject);
		// TODO Auto-generated constructor stub
	}
	public Bullet(float f, float g, TiledTextureRegion textureReg,
			VertexBufferObjectManager objMan) {
		super(f,g,textureReg,objMan);
		// TODO Auto-generated constructor stub
	}
	public float BaseX;
	public float BaseY;
	private float angle;
	public void setAngle(float angle){
		this.angle=angle;
	}
	public float getAngle()
	{
		return angle;
	}
}
