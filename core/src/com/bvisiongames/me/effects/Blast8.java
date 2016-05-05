package com.bvisiongames.me.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahzji_000 on 11/8/2015.
 */
public class Blast8 extends Effect {

    public Blast8(Vector2 pos, boolean repeating, float angle){

        this.pos = pos;
        this.skin = EffectsVariables.blast8Skin;
        setAnimation(EffectsVariables.blast8Texture, 17);   //speed starting from 17
        this.repeating = repeating;
        this.angle = angle;

    }

}
