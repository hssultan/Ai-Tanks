package com.bvisiongames.me.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Sultan on 7/3/2015.
 */
public abstract class Entity {

    private Vector2 pos = new Vector2(0,0),
                    velocity = new Vector2(0,0),
                    direction = new Vector2(0, 0);
    private float angularVelocity = 0,
                    bodyAngle = 0,
                    speed = 0;

    //properties of this class
    public boolean status = true;

    //body objects
    public Body body;

    //gettters
    /**
     * get the position of the body.
     */
    public Vector2 getPosition(){
        return this.pos;
    }
    /**
     * get the body linear velocity in x and y coord.
     */
    public Vector2 getLinearVelocity(){
        return this.velocity;
    }
    /**
     * get the speed of this entity.
     */
    public float getSpeed(){
        return this.speed;
    }
    /**
     * get the angular velocity
     */
    public float getAngularVelocity(){
        return this.angularVelocity;
    }
    /**
     * get body angle that is set in radian.
     */
    public float getBodyAngle(){
        return this.bodyAngle;
    }
    /**
     * gets a vector as components for the direction of the tank.
     */
    public Vector2 getDirection(){
        return this.direction;
    }
    //end of getters

    //setters
    /**
     * set the position of this entity which is in body dimensions.
     * @param x, y
     */
    public void setPosition(float x, float y){
        if(this.pos.x != x || this.pos.y != y){
            this.pos.set(x, y);
        }
    }
    /**
     * set the body angle of this entity.
     * @param angle
     * angle is in pi
     */
    public void setBodyAngle(float angle){
        if(this.bodyAngle != angle){
            this.bodyAngle = (angle >= Math.PI*2)? angle - (float)Math.PI*2 : angle;
            this.bodyAngle = (angle < 0 )? (float)Math.PI*2 + this.bodyAngle : this.bodyAngle;
        }
    }
    /**
     * set the speed of this entity.
     * @param speed
     */
    public void setSpeed(float speed){
        if(this.speed != speed){
            this.speed = speed;
        }
    }
    /**
     * set the angular velocity.
     * @param angularVelocity
     */
    public void setAngularVelocity(float angularVelocity){
        if(this.angularVelocity != angularVelocity){
            this.angularVelocity = angularVelocity;
        }
    }
    /**
     * set the direction of this tank.
     */
    public void setDirection(){
        this.direction.set((float)Math.cos(this.bodyAngle), (float)Math.sin(this.bodyAngle));
    }
    //end of setters


    //main abstracts
    public abstract void update();
    public abstract void render(SpriteBatch spriteBatch);
    public abstract void dispose();
    //end of main abstracts

}
