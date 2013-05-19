package org.projii.client;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.projii.interfaces.*;
import org.projii.commons.utils.RotationAngles;
import org.projii.commons.utils.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.projii.commons.shipLogic.moveLogic;;

public class VisualizerShip implements IVisualizeShip {

	@Override
	public void rotate(float timeForRotation,RotationAngles rotationAngles, AnimatedSprite sprite) {
		 sprite.clearEntityModifiers();
		 RotationModifier entityModifier=new RotationModifier(timeForRotation,rotationAngles.prevAngle,rotationAngles.nextAngle);
		 sprite.registerEntityModifier(entityModifier);
	}

	@Override
	public void setSpeed(Vector2 speedLimit, Vector2 incrementSpeed,
			Body body) {
		Vector2 bodyVelocity=new Vector2(body.getLinearVelocity().x,body.getLinearVelocity().y);
		Vector2 limitedSpeed=moveLogic.getLimitedSpeed(bodyVelocity, speedLimit, incrementSpeed);
		body.setLinearVelocity(limitedSpeed.x,limitedSpeed.y);	
		
	}

	
	



	

}
