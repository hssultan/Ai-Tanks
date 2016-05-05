package com.bvisiongames.me.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ahzji_000 on 11/3/2015.
 */
public class YellowFireBall extends Effect{

    public YellowFireBall(Vector2 pos, boolean repeating, float angle){

            this.pos = pos;
            this.skin = EffectsVariables.yellowFireBallSkin;
            setAnimation(EffectsVariables.yellowFireBallTexture, 17);   //speed starting from 17
            this.repeating = repeating;
            this.angle = angle;

    }

}
