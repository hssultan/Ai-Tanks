package com.bvisiongames.me.Tweenanimations;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by Sultan on 9/19/2015.
 */
public class ProgressBarAnimation implements TweenAccessor<ProgressBar> {

    public static final int POSITION_XY = 0;
    public static final int ALLDIMEN = 1;

    public static final int WIDTH = 2;
    public static final int HEIGHT = 3;

    //This type of animation only works on labels
    public static final int NUMBER = 4;

    public static final int ALPHA = 5;
    public static final int RED = 6,
            BLUE = 7,
            GREEN = 8;

    @Override
    public int getValues(ProgressBar target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case WIDTH:
                returnValues[0] = target.getWidth();
                return 1;
            case HEIGHT:
                returnValues[0] = target.getHeight();
                return 1;
            case NUMBER:
                returnValues[0] = target.getValue();
                return 1;
            case ALLDIMEN:
                returnValues[0] = target.getWidth();
                returnValues[1] = target.getHeight();
                return 2;
            case POSITION_XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;
            case RED:
                returnValues[0] = target.getColor().r;
                return 1;
            case GREEN:
                returnValues[0] = target.getColor().g;
                return 1;
            case BLUE:
                returnValues[0] = target.getColor().b;
                return 1;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(ProgressBar target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case WIDTH:
                target.setWidth(newValues[0]);
                break;
            case HEIGHT:
                target.setHeight(newValues[0]);
                break;
            case NUMBER:
                target.setValue(newValues[0]);
                break;
            case ALLDIMEN:
                target.setWidth(newValues[0]);
                target.setHeight(newValues[1]);
                break;
            case POSITION_XY:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                break;
            case ALPHA:
                target.getColor().a = newValues[0];
                break;
            case RED:
                target.getColor().r = newValues[0];
                break;
            case GREEN:
                target.getColor().g = newValues[0];
                break;
            case BLUE:
                target.getColor().b = newValues[0];
                break;
            default: assert false; break;
        }
    }

}
