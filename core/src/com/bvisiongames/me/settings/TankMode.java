package com.bvisiongames.me.settings;

/**
 * Created by ahzji_000 on 10/31/2015.
 */
public class TankMode {

    /**
     * below are the decrementing powers for bullets.
     */
    public static final float EASY_BULLET1_DECREMENT = 0.2f,
                                MEDIUM_BULLET1_DECREMENT = 0.4f,
                                HARD_BULLET1_DECREMENT = 0.6f,
                                //electric difficulty
                                EASY_ELECTRIC_DECREMENT = 0.2f,
                                MEDIUM_ELECTRIC_DECREMENT = 0.4f,
                                HARD_ELECTRIC_DECREMENT = 0.6f;
    /**
     * below are the incrementing powers for bullets.
     */
    public static final float EASY_BULLET1_INCREMENT = 0.2f,
                                MEDIUM_BULLET1_INCREMENT = 0.4f,
                                HARD_BULLET1_INCREMENT = 0.6f,
                                //electric difficulty
                                EASY_ELECTRIC_INCREMENT = 0.2f,
                                MEDIUM_ELECTRIC_INCREMENT = 0.4f,
                                HARD_ELECTRIC_INCREMENT = 0.6f;

    //below are the ratios for the movement of the tank
    //EASY MODE has the smallest decimal
    //HARD MODE has the largest decimal which is no greater than 0.5f
    private static final float EASY_RATIO_X_DIRECTION = 0.2f, EASY_RATIO_Y_DIRECTION = 0.1f,
                              MEDIUM_RATIO_X_DIRECTION = 0.35f, MEDIUM_RATIO_Y_DIRECTION = 0.35f,
                              HARD_RATIO_X_DIRECTION = 0.5f, HARD_RATIO_Y_DIRECTION = 0.5f;

    //below are the radius scopes for scoping nearby tanks
    private static final float EASY_SCOPE_TANKS = 800/ConstantVariables.PIXELS_TO_METERS,
                                MEDIUM_SCOPE_TANKS = 1000/ConstantVariables.PIXELS_TO_METERS,
                                HARD_SCOPE_TANKS = 1200/ConstantVariables.PIXELS_TO_METERS;

    //below is the radius scope for cpu tank
    private static final float NEARBY_SCOPE_RADIUS = 150/ConstantVariables.PIXELS_TO_METERS;

    /**
     * gets the nearby scope for tanks.
     */
    public static float getNearbyScope(){
        return NEARBY_SCOPE_RADIUS;
    }
    /**
     * gets the scope of detection for nearby tanks
     * @param tanklevel
     */
    public static float getTankScope(TankModeType tanklevel){

        switch (tanklevel){
            case EASY_MODE:
                return EASY_SCOPE_TANKS;
            case MEDIUM_MODE:
                return MEDIUM_SCOPE_TANKS;
            case HARD_MODE:
                return HARD_SCOPE_TANKS;
            default:
                return EASY_SCOPE_TANKS;
        }

    }
    /**
     * gets the scope of attack for the targeted tank entity
     * @param tanklevel
     */
    public static float getAttackTankScope(TankModeType tanklevel){

        switch (tanklevel){
            case EASY_MODE:
                return EASY_SCOPE_TANKS/2.5f;
            case MEDIUM_MODE:
                return MEDIUM_SCOPE_TANKS/2.5f;
            case HARD_MODE:
                return HARD_SCOPE_TANKS/2.5f;
            default:
                return EASY_SCOPE_TANKS/2.5f;
        }

    }

    /**
     * gets the ratio of movement of the tank speed in x direction.
     * @param tanklevel
     */
    public static float getTankRatioMovementX(TankModeType tanklevel){

        switch (tanklevel){
            case EASY_MODE:
                return EASY_RATIO_X_DIRECTION;
            case MEDIUM_MODE:
                return MEDIUM_RATIO_X_DIRECTION;
            case HARD_MODE:
                return HARD_RATIO_X_DIRECTION;
            default:
                return EASY_RATIO_X_DIRECTION;
        }

    }
    /**
     * gets the ratio of movement of the tank speed in y direction.
     * @param tanklevel
     */
    public static float getTankRatioMovementY(TankModeType tanklevel){

        switch (tanklevel){
            case EASY_MODE:
                return EASY_RATIO_Y_DIRECTION;
            case MEDIUM_MODE:
                return MEDIUM_RATIO_Y_DIRECTION;
            case HARD_MODE:
                return HARD_RATIO_Y_DIRECTION;
            default:
                return EASY_RATIO_Y_DIRECTION;
        }

    }

    /**
     * level of tank entity.
     */
    public enum TankModeType{
        EASY_MODE, MEDIUM_MODE, HARD_MODE
    }

}
