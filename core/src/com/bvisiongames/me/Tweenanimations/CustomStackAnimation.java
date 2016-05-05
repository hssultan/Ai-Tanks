package com.bvisiongames.me.Tweenanimations;

import com.bvisiongames.me.Weapons.EntitysWeaponManger;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by ahzji_000 on 11/25/2015.
 */
public class CustomStackAnimation implements TweenAccessor<EntitysWeaponManger.CustomStack> {

    //brightness and contrast
    public static final int BRIGHTNESS = 0,
                        CONTRAST = 1,
                        GLOW_RATIO = 2,
                        BRIGHTNESS_GLOW_RATIO = 3;

    @Override
    public int getValues(EntitysWeaponManger.CustomStack target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case BRIGHTNESS:
                returnValues[0] = target.getBrightness();
                return 1;
            case CONTRAST:
                returnValues[0] = target.getContrast();
                return 1;
            case GLOW_RATIO:
                returnValues[0] = target.getGlowRatio();
                return 1;
            case BRIGHTNESS_GLOW_RATIO:
                returnValues[0] = target.getBrightness();
                returnValues[1] = target.getGlowRatio();
                return 2;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(EntitysWeaponManger.CustomStack target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case BRIGHTNESS:
                target.setBrightness(newValues[0]);
                break;
            case CONTRAST:
                target.setContrast(newValues[0]);
                break;
            case GLOW_RATIO:
                target.setGlowRatio(newValues[0]);
                break;
            case BRIGHTNESS_GLOW_RATIO:
                target.setBrightness(newValues[0]);
                target.setGlowRatio(newValues[1]);
                break;
            default: assert false; break;
        }
    }
}
