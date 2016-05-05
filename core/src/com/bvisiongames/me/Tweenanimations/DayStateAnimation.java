package com.bvisiongames.me.Tweenanimations;

import com.bvisiongames.me.weather.DayState;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by ahzji_000 on 1/10/2016.
 */
public class DayStateAnimation implements TweenAccessor<DayState> {

    public final static int AMBIENT_NUM = 0;

    @Override
    public int getValues(DayState target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case AMBIENT_NUM:
                returnValues[0] = target.ambientLight;
                return 1;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(DayState target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case AMBIENT_NUM:
                target.setAmbientLight(newValues[0]);
                break;
            default: assert false; break;
        }
    }
}
