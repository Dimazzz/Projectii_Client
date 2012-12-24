package org.projii.client;

import org.andengine.util.math.MathUtils;
import org.projii.client.ShipViewer.MovingType;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class MoveLogic {
	private final static int START_JOYSTICK_POSITION=0;
	private final static float DELTA_DIRECTION=2f;
	 public static MovingType getMovingType(Vector2 joysticPosition,float wastedTime,float shipRotation,float prevAngle,float upDateTime)
	    {		  
	    	if(joysticPosition.x ==START_JOYSTICK_POSITION && joysticPosition.y == START_JOYSTICK_POSITION)
	    		return MovingType.Braking;
	        RotationAngles  rotationAngles = MoveLogic.calculateAngles(joysticPosition,shipRotation);
	    	float deltaDirection=Math.abs(rotationAngles.nextAngle-prevAngle);
	        float newDeltaAngle=Math.abs(rotationAngles.nextAngle-rotationAngles.prevAngle);	
	    	//updateRotateValues(rotationAngles.nextAngle,newDeltaAngle);
	    	/*Log.d("Direction","DeltaofDelta->"+String.valueOf(oldDeltaAngle-newDeltaAngle));
			  boolean deltaIsDown= oldDeltaAngle-newDeltaAngle>0;
		      Log.d("Direction", "OneDirection->"+String.valueOf(deltaDirection)+"Dlt->"+String.valueOf(newDeltaAngle));*/
		    boolean isInOneDirection=deltaDirection<=DELTA_DIRECTION;//in one difened direct
		    if(wastedTime<=upDateTime)
			{
				if(isInOneDirection)
				{
					//Log.d("inCalcTime","cancelMoviing");
					return MovingType.OneDirection;
				}
				else
				{
					if (newDeltaAngle<=100)
					{
						//Log.d("inCalcTime", "Smoothrotation");
						return MovingType.Smooth;
	                }//fastturnupdateRotateValues
					else {
						return MovingType.bigAngle;  
				       	}
				}
			}
	        return MovingType.Smooth;
	    }
	public static float calculateRotationTime(float newDeltaAngle,float angularSpeed)
	{
		float angleConst=90;float speedConst=1;
		float timeConst=0.3f;
        float coeff=(timeConst*speedConst)/angleConst;
	    float resultRotaionTime=coeff*(newDeltaAngle/angularSpeed);//timeFor180p*(angle/speedRotation);
        return resultRotaionTime*1.2f;
	}
	public static Vector2 getLimitedSpeed(Vector2 oldSpeed,Vector2 speedLimit,Vector2 incrementSpeed){
		Vector2 newSpeed=new Vector2(oldSpeed.x + incrementSpeed.x ,oldSpeed.y + incrementSpeed.y);//speed of ship with acceleration
		Vector2 speedLimitToSet=new Vector2(Math.signum(oldSpeed.x)*speedLimit.x,Math.signum(oldSpeed.y)*speedLimit.y); 
		Vector2 finalSpeed=newSpeed;
		/////////////////////////////////////////////////////////////
		if(Math.abs(newSpeed.x)<speedLimit.x)
			if(Math.abs(newSpeed.y)<speedLimit.y)
				{return finalSpeed;}
			else
				{return finalSpeed=new Vector2(newSpeed.x  ,speedLimitToSet.y);}
		else if(Math.abs(newSpeed.y)<speedLimit.y)
		     	{return finalSpeed=new Vector2(speedLimitToSet.x  , newSpeed.y );}
	         else
				{return finalSpeed= speedLimitToSet;}
	}
	public static RotationAngles calculateAngles(Vector2 joysticPosition,float shipRotation)
 	{
 		float  nextAngle=org.projii.client.tools.MathUtils.normAngle(MathUtils.radToDeg((float)Math.atan2(joysticPosition.x, -joysticPosition.y)));
	    float  normPrevRotation=org.projii.client.tools.MathUtils.normAngle(shipRotation);
        if(Math.abs(nextAngle-normPrevRotation)>181)
        {
        	if(nextAngle>normPrevRotation) nextAngle-=360;
		    else normPrevRotation-=360;
		}
        return new RotationAngles(normPrevRotation, nextAngle);
 	}
    public static Vector2 getBrakingSpeed(Vector2 velocity,boolean ifSaveMoving)
    {
    	Vector2 breakingSpeed=velocity;
    	if((velocity.x!=0&&velocity.y!=0))
		if(Math.abs(velocity.x)>0.5&&Math.abs(velocity.y)>0.5) return breakingSpeed=new Vector2(velocity.x -(velocity.x/100) , velocity.y-(velocity.y/100)  );
		else if(ifSaveMoving)
		     {
				return breakingSpeed=new Vector2(velocity.x -((velocity.x/100)-velocity.x%0.01f) , velocity.y-((velocity.y/100)-velocity.y%0.01f));
			 }
			 else 
				 return breakingSpeed=new Vector2(velocity.x -velocity.x , velocity.y-velocity.y  );
		return breakingSpeed;
    	
    }
}
