package com.bvisiongames.me.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.bvisiongames.me.screen.MultiGameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahzji_000 on 2/11/2016.
 */
public class BodyDisposer {

    //list of bodies to be disposed
    public List<Body> bodies = new ArrayList<Body>();


    /**
     * add a body to the list to be disposed.
     * @param body
     */
    public void addBodyToDispose(Body body){
        this.bodies.add(body);
    }
    /**
     * dispose the bodies.
     */
    public void dispose(){

        int length = bodies.size();
        for(int i = 0; i < length; i++){
            MultiGameScreen.WORLD.destroyBody(bodies.get(i));
            bodies.get(i).setUserData(null);
        }

        //clear the list
        bodies.clear();

    }

}
