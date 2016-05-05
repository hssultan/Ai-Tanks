package com.bvisiongames.me.Tweenanimations;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by Sultan on 7/28/2015.
 */
public class Box2dActorAnimation implements TweenAccessor<Actor> {

    public static final int POSITION_XY = 0,
                            ALLDIMEN = 1;

    public static final int WIDTH = 2,
                            HEIGHT = 3;

    //This type of animation only works on labels
    public static final int NUMBER = 4;
    //-------------------------------->

    public static final int ALPHA = 5,
                            RED = 6,
                            BLUE = 7,
                            GREEN = 8;

    public static final int SCALE = 11;

    @Override
    public int getValues(Actor target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case WIDTH:
                returnValues[0] = target.getWidth();
                return 1;
            case HEIGHT:
                returnValues[0] = target.getHeight();
                return 1;
            case NUMBER:
                returnValues[0] = Integer.parseInt(((Label) target).getText().toString());
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
            case SCALE:
                returnValues[0] = target.getScaleX();
                returnValues[1] = target.getScaleY();
                return 1;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(Actor target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case WIDTH:
                target.setWidth(newValues[0]);
                break;
            case HEIGHT:
                target.setHeight(newValues[0]);
                break;
            case NUMBER:
                ((Label)target).setText(Integer.toString((int) newValues[0]));
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
                target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
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
            case SCALE:
                target.setScale(newValues[0], newValues[1]);
                break;
            default: assert false; break;
        }
    }
}
