package com.bvisiongames.me.Tweenanimations;

import com.badlogic.gdx.scenes.scene2d.Actor;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by ahzji_000 on 11/14/2015.
 */
public class ActorAnimation implements TweenAccessor<Actor> {

    //POSITIONS
    public final static int POSITION_XY = 0,
                            POSITION_X = 1, POSITION_Y = 2;
    //WIDTH AND HEIGHT
    public final static int WIDTH_HEIGHT = 3, WIDTH = 4, HEIGHT = 5;
    //COLORS
    public final static int COLOR_R = 6, COLOR_B = 7, COLOR_G = 8;
    public final static int ALPHA = 9;
    public final static int COLOR_RB = 10, COLOR_GA = 11;

    @Override
    public int getValues(Actor target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POSITION_XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            case POSITION_X:
                returnValues[0] = target.getX();
                return 1;
            case POSITION_Y:
                returnValues[0] = target.getY();
                return 1;
            case WIDTH_HEIGHT:
                returnValues[0] = target.getWidth();
                returnValues[1] = target.getHeight();
                return 2;
            case WIDTH:
                returnValues[0] = target.getWidth();
                return 1;
            case HEIGHT:
                returnValues[0] = target.getHeight();
                return 1;
            case COLOR_R:
                returnValues[0] = target.getColor().r;
                return 1;
            case COLOR_B:
                returnValues[0] = target.getColor().b;
                return 1;
            case COLOR_G:
                returnValues[0] = target.getColor().g;
                return 1;
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;
            case COLOR_RB:
                returnValues[0] = target.getColor().r;
                returnValues[1] = target.getColor().b;
                return 2;
            case COLOR_GA:
                returnValues[0] = target.getColor().g;
                returnValues[1] = target.getColor().a;
                return 2;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(Actor target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION_XY:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                break;
            case POSITION_X:
                target.setX(newValues[0]);
                break;
            case POSITION_Y:
                target.setY(newValues[0]);
                break;
            case WIDTH_HEIGHT:
                target.setWidth(newValues[0]);
                target.setHeight(newValues[1]);
                break;
            case WIDTH:
                target.setWidth(newValues[0]);
                break;
            case HEIGHT:
                target.setHeight(newValues[0]);
                break;
            case COLOR_R:
                target.getColor().r = newValues[0];
                break;
            case COLOR_B:
                target.getColor().b = newValues[0];
                break;
            case COLOR_G:
                target.getColor().g = newValues[0];
                break;
            case ALPHA:
                target.getColor().a = newValues[0];
                break;
            case COLOR_RB:
                target.getColor().r = newValues[0];
                target.getColor().b = newValues[1];
                break;
            case COLOR_GA:
                target.getColor().g = newValues[0];
                target.getColor().a = newValues[1];
                break;
            default: assert false; break;
        }
    }

}
