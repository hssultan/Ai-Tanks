package com.bvisiongames.me.Tweenanimations;

import com.badlogic.gdx.graphics.g2d.Sprite;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by Sultan on 8/12/2015.
 */
public class SpriteAnimation implements TweenAccessor<Sprite>{

    public static final int POSITION_XY = 0,
                            ALLDIMEN = 1;

    public static final int WIDTH = 2,
                            HEIGHT = 3;

    public static final int ALPHA = 4;
    public static final int RED = 5,
                            BLUE = 6,
                            GREEN = 7;

    public static final int ROTATION = 8;

    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case WIDTH:
                returnValues[0] = target.getWidth();
                return 1;
            case HEIGHT:
                returnValues[0] = target.getHeight();
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
            case ROTATION:
                returnValues[0] = target.getRotation();
                return 1;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case WIDTH:
                target.setRegionWidth((int)newValues[0]);
                break;
            case HEIGHT:
                target.setRegionHeight((int) newValues[0]);
                break;
            case ALLDIMEN:
                target.setRegionWidth((int)newValues[0]);
                target.setRegionHeight((int)newValues[1]);
                break;
            case POSITION_XY:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                break;
            case ALPHA:
                target.setAlpha(newValues[0]);
                break;
            case RED:
                target.setColor(newValues[0], target.getColor().g, target.getColor().b, 1);
                break;
            case GREEN:
                target.setColor(target.getColor().r, newValues[0], target.getColor().b, 1);
                break;
            case BLUE:
                target.setColor(target.getColor().r, target.getColor().g, newValues[0], 1);
                break;
            case ROTATION:
                target.setRotation(newValues[0]);
                break;
            default: assert false; break;
        }
    }

}
