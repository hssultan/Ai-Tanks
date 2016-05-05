package com.bvisiongames.me.weather;

import com.bvisiongames.me.screen.MultiGameScreen;

/**
 * Created by ahzji_000 on 1/10/2016.
 */
public class Weather {

    public DayState dayState;

    public Weather(){
        dayState = new DayState();
    }


    /**
     * turn it to day.
     */
    public void turnLightsOn(){
        dayState.daystate = MultiGameScreen.DAYSTATE.DAY;
        dayState.setAmbientLight(0);
    }
    /**
     * turn it to night.
     */
    public void turnLightsOff(){
        dayState.daystate = MultiGameScreen.DAYSTATE.NIGHT;
        dayState.setAmbientLight(1);
    }

}
