package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.projii.client.MoveLogic;
import org.projii.client.RotationAngles;
import com.badlogic.gdx.math.Vector2;
import org.projii.client.ShipViewer.MovingType;

public class testMove_Logic {
	Vector2 joysticPosition=null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testGetTypeMoving()
	{
		joysticPosition=new Vector2(0,0);
		float prevAngle=181;
		float currentAngle=0;
		MovingType type=MoveLogic.getMovingType(joysticPosition, 0.015f, currentAngle, prevAngle, 0.2f);
		assertEquals(MovingType.Braking, type);
		////////////////////////////////////////
		joysticPosition=new Vector2(0,1);
		type=MoveLogic.getMovingType(joysticPosition, 0.015f, currentAngle, prevAngle, 0.2f);
		assertEquals(MovingType.OneDirection, type);
		/////////////////////////////////////////
		prevAngle=70;
		type=MoveLogic.getMovingType(joysticPosition, 0.015f, currentAngle, prevAngle, 0.2f);
		assertEquals(MovingType.bigAngle, type);
		////////////////////////////////////////////////////
		currentAngle=120;
		type=MoveLogic.getMovingType(joysticPosition, 0.015f, currentAngle, prevAngle, 0.2f);
		assertEquals(MovingType.Smooth, type);
		
	}
	@Test
	public void testCalculateRotationAngles()
	{
		joysticPosition=new Vector2(0, -1);
		RotationAngles angles=MoveLogic.calculateAngles(joysticPosition,0);
		assertEquals((int)angles.nextAngle, 0);
		joysticPosition=new Vector2(0, 1);
		angles=MoveLogic.calculateAngles(joysticPosition,0);
		assertEquals((int)angles.nextAngle,180);
		joysticPosition=new Vector2(1, 0);
		angles=MoveLogic.calculateAngles(joysticPosition,0);
		assertEquals((int)angles.nextAngle,90);
		joysticPosition=new Vector2(-1, 0);
		angles=MoveLogic.calculateAngles(joysticPosition,0);
		assertEquals((int)angles.nextAngle,-90);
		//it means than angle is always lower 180
	}
	@Test
	public void testCalculateRotationTime()
	{
		float time=MoveLogic.calculateRotationTime(180, 1);
		assertTrue(0.6f- time<0.00001f);
	    time=MoveLogic.calculateRotationTime(45, 1);
		assertTrue(0.15f- time<0.00001f);
		time=MoveLogic.calculateRotationTime(360, 1);
		assertTrue(1.2f- time<0.00001f);
		
	}
	@Test
	public void testGetLimitedSpeed() {
		Vector2 oldSpeed=new Vector2(10, 10);
		Vector2 incrementSpeed=new Vector2(5, 5);
		Vector2 speedLimit=new Vector2(16,16);
		Vector2 finalSpeed=MoveLogic.getLimitedSpeed(oldSpeed, speedLimit, incrementSpeed);
		assertNotNull(finalSpeed);
		assertTrue(Math.abs(finalSpeed.x-15)<0.001f&&Math.abs(finalSpeed.y-15)<0.001f);
		speedLimit.x=13;
		finalSpeed=MoveLogic.getLimitedSpeed(oldSpeed, speedLimit, incrementSpeed);
		assertNotNull(finalSpeed);
		assertTrue(Math.abs(finalSpeed.x-13)<0.001f&&Math.abs(finalSpeed.y-15)<0.001f);
		speedLimit.y=13;
		finalSpeed=MoveLogic.getLimitedSpeed(oldSpeed, speedLimit, incrementSpeed);
		assertEquals(13, (int)finalSpeed.x);
		assertEquals(13, (int)finalSpeed.y);
		speedLimit.x=15;
		finalSpeed=MoveLogic.getLimitedSpeed(oldSpeed, speedLimit, incrementSpeed);
		assertTrue((int)finalSpeed.x==15&&(int)finalSpeed.y==13);
	}

}
