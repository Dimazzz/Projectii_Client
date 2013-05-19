package org.projii.client;

import java.util.Random;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Mob extends Sprite {
    private final PhysicsHandler mPhysicsHandler;
            Random randomGenerator = new Random();
            private float RandomX;
            private float RandomY;
            private int CAMERA_WIDTH=1920;
            private int CAMERA_HEIGHT=1080; 
    public Mob(final float pX, final float pY, final ITextureRegion mTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, mTextureRegion, pVertexBufferObjectManager);
        this.mPhysicsHandler = new PhysicsHandler(this);
        this.registerUpdateHandler(this.mPhysicsHandler);
                    RandomX =randomGenerator.nextInt(3);
                    RandomY =randomGenerator.nextInt(3);
                    RandomX=RandomX*100;  
                    RandomY=RandomY*100;
        this.mPhysicsHandler.setVelocity(RandomX, RandomY);
    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if(this.mX < 0) {
            this.mPhysicsHandler.setVelocityX(RandomX);
        } else if(this.mX + this.getWidth() > CAMERA_WIDTH) {
            this.mPhysicsHandler.setVelocityX(-RandomX);
        }

        if(this.mY < 0) {
            this.mPhysicsHandler.setVelocityY(RandomY);
        } else if(this.mY + this.getHeight() > CAMERA_HEIGHT) {
            this.mPhysicsHandler.setVelocityY(-RandomY);
        }

        super.onManagedUpdate(pSecondsElapsed);
    }
}
