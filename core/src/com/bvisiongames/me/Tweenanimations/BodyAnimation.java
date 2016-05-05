package com.bvisiongames.me.Tweenanimations;

import com.badlogic.gdx.physics.box2d.Body;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by Sultan on 8/23/2015.
 */
public class BodyAnimation implements TweenAccessor<Body> {

    public static final int POSITION_XY = 0;

    public static final int ANGLE = 1;

    public static final int ARC = 2;

    @Override
    public int getValues(Body target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POSITION_XY:
                returnValues[0] = target.getPosition().x;
                returnValues[1] = target.getPosition().y;
                return 2;
            case ANGLE:
                returnValues[0] = target.getAngle();
                return 1;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(Body target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION_XY:
                target.setTransform(newValues[0], newValues[1], target.getAngle());
                break;
            case ANGLE:
                target.setTransform(target.getPosition().x, target.getPosition().y, newValues[0]);
                break;
            default: assert false; break;
        }
    }
}
