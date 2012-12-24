package org.projii.client;

import android.util.FloatMath;

import com.badlogic.gdx.math.Vector2;

public class ShootLogic {
	public static Vector2 getsStartBulletPosition(float shipRotation,float deltaAngle, float shipLeftX,float shipUpY,float shipWidth,float shipHeight)
    {
    	float  normAngle=org.projii.client.tools.MathUtils.normAngle(shipRotation);
		float angle=(float)Math.toRadians(normAngle+deltaAngle);
		float centerX= (2*shipLeftX+shipWidth)/2+30*FloatMath.sin(angle);
		float centerY= (2*shipUpY+shipHeight)/2-25*FloatMath.cos(angle);
		return  new Vector2(centerX,centerY);
    }
    public static Vector2 getAimByLinearDistance(float speed,float shipRotation,float shipLeftX,float shipUpY,float shipWidth,float shipHeight)
    {
    	float  normAngle=org.projii.client.tools.MathUtils.normAngle(shipRotation);
    	float angle=(float)Math.toRadians(normAngle);
    	Vector2 shipCenterPosition=getsStartBulletPosition(shipRotation, 0, shipLeftX, shipUpY, shipWidth, shipHeight);
    	float pX= (shipCenterPosition.x+speed*FloatMath.sin(angle));
		float pY= (shipCenterPosition.y-speed*FloatMath.cos(angle));
    	return new Vector2(pX,pY);
    }

}
