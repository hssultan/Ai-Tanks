package com.bvisiongames.me.Tweenanimations;

import com.bvisiongames.me.animations.RadialProgressBar;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by ahzji_000 on 11/25/2015.
 */
public class RadialProgressBarAnimation implements TweenAccessor<RadialProgressBar> {

    public static final int PERCENT = 0;
    /**
     * opacity at both ends
     */
    public static final int OPACITY_1_2 = 1;
    public static final int OPACITY_1 = 2;
    public static final int OPACITY_2 = 3;

    @Override
    public int getValues(RadialProgressBar target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case PERCENT:
                returnValues[0] = target.getPercentage();
                return 1;
            case OPACITY_1:
                returnValues[0] = target.getRadialOpacity().x;
                return 1;
            case OPACITY_2:
                returnValues[0] = target.getRadialOpacity().y;
                return 1;
            case OPACITY_1_2:
                returnValues[0] = target.getRadialOpacity().x;
                returnValues[1] = target.getRadialOpacity().y;
                return 2;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(RadialProgressBar target, int tweenType, float[] newValues) {
        switch (tweenType){
            case PERCENT:
                target.setPercentage(newValues[0]);
                break;
            case OPACITY_1_2:
                target.setRadialOpacity(newValues[0], newValues[1]);
                break;
            case OPACITY_1:
                target.setRadialOpacity(newValues[0], target.getRadialOpacity().y);
                break;
            case OPACITY_2:
                target.setRadialOpacity(target.getRadialOpacity().x, newValues[0]);
                break;
        }
    }
}
