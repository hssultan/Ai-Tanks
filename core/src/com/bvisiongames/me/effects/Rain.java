package com.bvisiongames.me.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahzji_000 on 11/7/2015.
 */
public class Rain extends Effect {

    public Rain(Vector2 pos, boolean repeating, float angle){

        this.pos = pos;
        this.skin = EffectsVariables.rainSkin;
        setAnimation(EffectsVariables.rainTexture, 17);   //speed starting from 17
        this.repeating = repeating;
        this.angle = angle;

    }

}
