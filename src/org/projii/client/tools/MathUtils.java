package org.projii.client.tools;

public final class MathUtils {
	private static final int FULL_CIRCLE_DEGS = 360;
	public static float normAngle(final float inputAngle) {
		float retval = inputAngle;
		while (retval < 0)
			retval += FULL_CIRCLE_DEGS;
		while (retval >= FULL_CIRCLE_DEGS)
			retval -= FULL_CIRCLE_DEGS;
		return retval;
	}
}
