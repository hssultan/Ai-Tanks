package com.bvisiongames.me.Tweenanimations;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by Sultan on 8/8/2015.
 */
public class CameraAnimation implements TweenAccessor<Camera>{

    public static final int POSITION_XY = 0;

    @Override
    public int getValues(Camera target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POSITION_XY:
                returnValues[0] = target.position.x;
                returnValues[1] = target.position.y;
                return 2;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(Camera target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION_XY:
                target.position.set(newValues[0], newValues[1], target.position.z);
                break;
            default: assert false; break;
        }
    }

}
