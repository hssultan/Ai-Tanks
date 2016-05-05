package com.bvisiongames.me.settings;

/**
 * Created by ahzji_000 on 1/10/2016.
 */
public class ConstantVariables {

    //pixels to meters ratio
    public final static float PIXELS_TO_METERS = 20;

    //max energy cost for scanner nearby point
    public final static float MAX_ENERGY_NEARBY_SCANNER_POINT_DISTANCE = 100f,
                                MAX_ENERGY_NEARBY_SCANNER_POINT_ANGLE = 100f;

    //constant math variables
    public final static float FloatPI = (float)Math.PI;

    /**
     * this class contains the box2d filter variables
     */
    public class Box2dVariables{

        public static final char FLASH_LIGHT = 1,
                                 SENSOR = 2;

    }

}
