package org.projii.client;


import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

public class SpritePool extends GenericPool<Sprite> {
    private ITextureRegion mTextureRegion;
    private VertexBufferObjectManager objectManager;
    private int width;
    private int height;
    public SpritePool(ITextureRegion pTextureRegion,VertexBufferObjectManager objectManager,int width,int height) {
        if (pTextureRegion == null) {
            // Need to be able to create a Sprite so the Pool needs to have a TextureRegion
                throw new IllegalArgumentException("The texture region must not be NULL");
        }
        mTextureRegion = pTextureRegion;
        this.objectManager=objectManager;
        this.width=width;
        this.height=height;
    }
   
    /** Called when a projectile is required but there isn't one in the pool */
    
    @Override
    protected Sprite onAllocatePoolItem() {
        return new Sprite(0,0,width,height, mTextureRegion, objectManager);
    }

    /** Called when a projectile is sent to the pool */
    protected void onHandleRecycleItem(final Sprite projectile) {
        projectile.clearEntityModifiers();
        projectile.clearUpdateHandlers();
        projectile.setVisible(false);
        projectile.detachSelf();
        projectile.reset();
    }
}
