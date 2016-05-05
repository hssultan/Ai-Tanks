package com.bvisiongames.me.Tweenanimations;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by ahzji_000 on 11/18/2015.
 */
public class CellAnimation implements TweenAccessor<Cell>{

    //WIDTH AND HEIGHT
    public final static int WIDTH_HEIGHT = 3, WIDTH = 4, HEIGHT = 5;

    @Override
    public int getValues(Cell target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case WIDTH_HEIGHT:
                returnValues[0] = target.getMinWidth();
                returnValues[1] = target.getMinHeight();
                return 2;
            case WIDTH:
                returnValues[0] = target.getMinWidth();
                return 1;
            case HEIGHT:
                returnValues[0] = target.getMinHeight();
                return 1;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(Cell target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case WIDTH_HEIGHT:
                target.width(newValues[0]);
                target.height(newValues[1]);
                target.getTable().invalidate();
                break;
            case WIDTH:
                target.width(newValues[0]);
                target.getTable().invalidate();
                break;
            case HEIGHT:
                target.height(newValues[0]);
                target.getTable().invalidate();
                break;
            default: assert false; break;
        }
    }

}
