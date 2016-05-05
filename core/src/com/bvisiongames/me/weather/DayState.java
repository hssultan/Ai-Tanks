package com.bvisiongames.me.weather;

import com.bvisiongames.me.screen.MultiGameScreen;
import box2dLight.RayHandler;

/**
 * Created by ahzji_000 on 1/10/2016.
 */
public class DayState {

    public float ambientLight = 0;

    public RayHandler rayHandler;

    public MultiGameScreen.DAYSTATE daystate;

    //setter methods
    /**
     * set the ray handler.
     * @param rayHandler
     */
    public void setRayHandler(RayHandler rayHandler){
        this.rayHandler = rayHandler;
    }
    /**
     * set ambient light.
     * @param ambientLight
     */
    public void setAmbientLight(float ambientLight){
        this.ambientLight = ambientLight;
        this.rayHandler.setAmbientLight(this.ambientLight);
    }
    //end of setter methods


    //getter methods

    //end of getter methods

}
