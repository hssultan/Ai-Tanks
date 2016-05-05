package com.bvisiongames.me.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahzji_000 on 11/7/2015.
 */
public class Blast1 extends Effect {

    public Blast1(Vector2 pos, boolean repeating, float angle){

        this.pos = pos;
        this.skin = EffectsVariables.blast1Skin;
        setAnimation(EffectsVariables.blast1Texture, 19);   //speed starting from 17
        this.repeating = repeating;
        this.angle = angle;

    }

}
