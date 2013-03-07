package org.projii.client;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.projii.interfaces.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class VisualizerShip implements IVisualizeShip {

	
	@Override
	public void rotateShip(float timeForRotation,RotationAngles rotationAngles, AnimatedSprite sprite,
			float newDeltaAngle) {
		
		 sprite.clearEntityModifiers();
		 RotationModifier entityModifier=new RotationModifier(timeForRotation,rotationAngles.prevAngle,rotationAngles.nextAngle);
		 sprite.registerEntityModifier(entityModifier);
		
		
	}

	@Override
	public void defineSpeed(Vector2 speedLimit, Vector2 incrementSpeed,
			Body body) {
		Vector2 limitedSpeed=MoveLogic.getLimitedSpeed(body.getLinearVelocity(), speedLimit, incrementSpeed);
		body.setLinearVelocity(limitedSpeed);	
		
	}



	

}
