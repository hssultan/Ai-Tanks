package com.bvisiongames.me.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahzji_000 on 11/8/2015.
 */
public class Fire extends Effect {

    public Fire(Vector2 pos, boolean repeating, float angle){

        this.pos = pos;
        this.skin = EffectsVariables.fireSkin;
        setAnimation(EffectsVariables.fireTexture, 25);   //speed starting from 17
        this.repeating = repeating;
        this.angle = angle;

    }

}
