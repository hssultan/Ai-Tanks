package com.bvisiongames.me.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahzji_000 on 12/17/2015.
 */
public class Coins extends Effect {

    public Coins(Vector2 pos, boolean repeating, float angle){

        this.pos = pos;
        this.skin = EffectsVariables.coinSkin;
        setAnimation(EffectsVariables.coinTexture, 19);   //speed starting from 17
        this.repeating = repeating;
        this.angle = angle;

    }

}
