package org.projii.interfaces;

import org.andengine.entity.sprite.AnimatedSprite;

import org.projii.commons.utils.RotationAngles;
import org.projii.commons.utils.Vector2;

import com.badlogic.gdx.physics.box2d.Body;

public interface IVisualizeShip extends IVisualizeObject {

	public void rotateShip(float timeForRotation,RotationAngles rotationAngles,AnimatedSprite sprite,float newDeltaAngle);
	public void defineSpeed(Vector2 speedLimit,Vector2 incrementSpeed,Body body); 
}
