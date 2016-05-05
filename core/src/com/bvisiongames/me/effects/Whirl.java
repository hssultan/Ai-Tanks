package com.bvisiongames.me.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahzji_000 on 11/8/2015.
 */
public class Whirl extends Effect {

    public Whirl(Vector2 pos, boolean repeating, float angle){

        this.pos = pos;
        this.skin = EffectsVariables.whirlSkin;
        setAnimation(EffectsVariables.whirlTexture, 20);   //speed starting from 17
        this.repeating = repeating;
        this.angle = angle;

    }

}
