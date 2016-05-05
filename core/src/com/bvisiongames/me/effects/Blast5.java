package com.bvisiongames.me.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahzji_000 on 11/8/2015.
 */
public class Blast5 extends Effect {

    public Blast5(Vector2 pos, boolean repeating, float angle){

        this.pos = pos;
        this.skin = EffectsVariables.blast5Skin;
        setAnimation(EffectsVariables.blast5Texture, 17);   //speed starting from 17
        this.repeating = repeating;
        this.angle = angle;

    }

}
