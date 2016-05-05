package com.bvisiongames.me.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahzji_000 on 11/8/2015.
 */
public class Blast7 extends Effect {

    public Blast7(Vector2 pos, boolean repeating, float angle){

        this.pos = pos;
        this.skin = EffectsVariables.blast7Skin;
        setAnimation(EffectsVariables.blast7Texture, 17);   //speed starting from 17
        this.repeating = repeating;
        this.angle = angle;

    }

}
