package com.bvisiongames.me.Tweenanimations;

import com.bvisiongames.me.loaders.ConnectionLoader;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by ahzji_000 on 11/11/2015.
 */
public class PercentDisplayAnimation implements TweenAccessor<ConnectionLoader.PercentDisplay> {

    public static final int PERCENT = 0;

    @Override
    public int getValues(ConnectionLoader.PercentDisplay target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case PERCENT:
                returnValues[0] = target.getPercent();
                return 1;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(ConnectionLoader.PercentDisplay target, int tweenType, float[] newValues) {

        switch (tweenType) {
            case PERCENT:
                target.setPercent((int)newValues[0]);
                break;
            default: assert false; break;
        }

    }
}
