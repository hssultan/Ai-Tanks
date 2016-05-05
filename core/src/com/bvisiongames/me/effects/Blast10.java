package com.bvisiongames.me.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahzji_000 on 11/8/2015.
 */
public class Blast10 extends Effect {

    /**
     *
     * @param pos
     * @param repeating
     * @param angle
     * @param frequency
     * speed of animation 1->infinite, the higher the faster.
     */
    public Blast10(Vector2 pos, boolean repeating, float angle, float frequency){

        this.pos = pos;
        this.skin = EffectsVariables.blast10Skin;
        setAnimation(EffectsVariables.blast10Texture, frequency);   //speed starting from 17
        this.repeating = repeating;
        this.angle = angle;

    }

}
