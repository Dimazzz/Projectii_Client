package org.projii.commons.shipLogic;


import org.projii.commons.utils.Vector2;

import static org.projii.commons.utils.our_math.normalizeAngle;

public class shootLogic {
    public static Vector2 getsStartBulletPosition(float shipRotation,float deltaAngle, float shipLeftX,float shipUpY,float shipWidth,float shipHeight)
    {
        float  normAngle=normalizeAngle(shipRotation);
        float angle=(float)Math.toRadians(normAngle+deltaAngle);
        float centerX= (2*shipLeftX+shipWidth)/2+30*(float)Math.sin(angle);
        float centerY= (2*shipUpY+shipHeight)/2-25*(float)Math.cos(angle);
        return  new Vector2(centerX,centerY);
    }
    public static Vector2 getAimByLinearDistance(float speed,float shipRotation,float shipLeftX,float shipUpY,float shipWidth,float shipHeight)
    {
        float  normAngle=normalizeAngle(shipRotation);
        float angle=(float)Math.toRadians(normAngle);
        Vector2 shipCenterPosition=getsStartBulletPosition(shipRotation, 0, shipLeftX, shipUpY, shipWidth, shipHeight);
        float pX= (shipCenterPosition.x+speed*(float)Math.sin(angle));
        float pY= (shipCenterPosition.y-speed*(float)Math.cos(angle));
        return new Vector2(pX,pY);
    }

}