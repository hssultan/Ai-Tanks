package com.bvisiongames.me.buildings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;

/**
 * Created by Sultan on 9/19/2015.
 */
public abstract class Buildings {

    //borders of the base
    public float[] corners;
    public Vector2[] Vcorners;
    public ChainShape shape;
    public Vector2 pos;

    public abstract void update();
    public abstract void render(SpriteBatch batch);
    public abstract void renderTop(SpriteBatch batch);

    /**
     * returns the position in body world dimensions.
     * @return
     */
    public Vector2 getPosition(){
        return pos;
    }

}
