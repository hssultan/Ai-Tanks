package com.bvisiongames.me.Tweenanimations;

import com.bvisiongames.me.displays.ClientScore;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by ahzji_000 on 12/18/2015.
 */
public class ClientScoreAnimation implements TweenAccessor<ClientScore> {

    public static final int KILLS = 0,
                            COINS = 1;

    @Override
    public int getValues(ClientScore target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case KILLS:
                returnValues[0] = target.getTotalKills();
                return 1;
            case COINS:
                returnValues[0] = target.getTotalCoins();
                return 1;
            default: assert false; return -1;
        }
    }

    @Override
    public void setValues(ClientScore target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case KILLS:
                target.setTotalKills((int) newValues[0]);
                break;
            case COINS:
                target.setTotalCoins((int)newValues[0]);
                break;
            default: assert false; break;
        }
    }
}
