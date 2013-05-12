package org.projii.commons.utils;


public final  class our_math {

        private static final int FULL_CIRCLE_DEGS = 360;
        public static final float PI = (float) Math.PI;
        public static final float RAD_TO_DEG = 180.0f / PI;
        public static float normalizeAngle(final float inputAngle) {
            float returnAngleValue = inputAngle;
            while (returnAngleValue < 0)
                returnAngleValue += FULL_CIRCLE_DEGS;
            while (returnAngleValue >= FULL_CIRCLE_DEGS)
                returnAngleValue -= FULL_CIRCLE_DEGS;
            return returnAngleValue;
        }
        public static final float radiansToDegrees(final float pRad) {
            return RAD_TO_DEG * pRad;
        }
}
