package com.bvisiongames.me.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahzji_000 on 11/8/2015.
 */
public class Oasis extends Effect {

    public Oasis(Vector2 pos, boolean repeating, float angle){

        this.pos = pos;
        this.skin = EffectsVariables.oasisSkin;
        setAnimation(EffectsVariables.oasisTexture, 3);   //speed starting from 1
        this.repeating = repeating;
        this.angle = angle;

    }

}
