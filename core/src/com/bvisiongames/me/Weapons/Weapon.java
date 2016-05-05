package com.bvisiongames.me.Weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.bvisiongames.me.entity.*;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.ConstantVariables;

/**
 * Created by ahzji_000 on 11/6/2015.
 */
public abstract class Weapon{

    //bullet weapon property
    public Body body;
    public Sprite bullet;
    public Vector2 pos = new Vector2(0, 0);
    public float bodyAngle = 0;

    //class properties
    public boolean status = true; //the status of this bullet
    public boolean velocityStatus = true; //velocity status is for if the bullet is moving
    public float beginAngle = 0;

    public float constantlinearVelocity = 100;

    //owner of the bullet tank reference
    public Tank owner;

    //bullet type
    public BULLETSTYPE bulletstype;

    //blast affecting area dimensions
    private Vector2 blastDimBullet1 = new Vector2(10, 10),
                    blastDimElectric = new Vector2(10, 10);

    public abstract void update();
    public abstract void render(SpriteBatch spriteBatch);
    public abstract void pause();
    public abstract void resume();
    public abstract void dispose();

    //getters
    /**
     * get the position of the body.
     */
    public Vector2 getPosition(){
        return this.pos;
    }
    /**
     * get body angle that is set in radian.
     */
    public float getBodyAngle(){
        return this.bodyAngle;
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
    //end of setters


    public boolean isVelocityStatus(){
        return velocityStatus;
    }
    public boolean isStatus(){
        return status;
    }

    /**
     * get the blast affecting area
     * @return
     */
    public Vector2 getBlastDimension(){
        switch (bulletstype){
            case BULLET1:
                return this.blastDimBullet1;
            case ELECTRICBULLET:
                return this.blastDimElectric;
            default:
                return this.blastDimBullet1;
        }
    }

    //below is the querycallback for the impulse or effect
    public QueryCallback queryCallback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {

            switch (bulletstype){
                case BULLET1:
                    bulletInfluence(fixture);
                    break;
                case ELECTRICBULLET:
                    electricInfluence(fixture);
                    break;
            }

            return true;
        }
    };
    //method below are for each bullet and it's influence on another object
    private void bulletInfluence(Fixture fixture){

        if( fixture.getBody().getUserData() != null &&
                fixture.getBody().getUserData() instanceof SmallBox){
            ((SmallBox)fixture.getBody().getUserData()).hitByBullet((Bullet)body.getUserData());
        }

    }
    private void electricInfluence(Fixture fixture){

        if( fixture.getBody().getUserData() != null &&
                fixture.getBody().getUserData() instanceof Tank ){
            ((Tank)fixture.getBody().getUserData()).tankElectricShocked.hitByElectricShock(getPosition());
        }

    }

    public void initiateExplosion(){
        //stop the bullet's body
        velocityStatus = false;

        //below is the queryaabb for initiating the impulse
        MultiGameScreen.WORLD.QueryAABB(queryCallback,
                getPosition().x - getBlastDimension().x,
                getPosition().y - getBlastDimension().y,
                getPosition().x + getBlastDimension().x,
                getPosition().y + getBlastDimension().y );

    }
    public Tank getOwner(){
        return owner;
    }

    /**
     * reset the weapon.
     */
    public void reset(Tank tank){

        //set the owner of this bullet
        this.owner = tank;

        //begin angle
        beginAngle = owner.body.getAngle();

        //bullet's position
        pos = new Vector2( (tank.tank.getHeight()/1.4f)* (float)Math.cos( tank.body.getAngle() + Math.PI/2  ) + (tank.getPosition().x* ConstantVariables.PIXELS_TO_METERS),
                (tank.tank.getHeight()/1.4f) *(float)Math.sin(tank.body.getAngle() + Math.PI/2 ) + (tank.getPosition().y*ConstantVariables.PIXELS_TO_METERS) );

        this.body.setTransform(pos.x/ConstantVariables.PIXELS_TO_METERS, pos.y/ConstantVariables.PIXELS_TO_METERS,
                                beginAngle);

        //set the user data
        body.setUserData(this);

        //reset the body's active to true
        body.setActive(true);

        //set status and velocity
        this.velocityStatus = true;
        this.status = true;

        //add the muzzle smoke effect to the effect list
        owner.setAndShowMuzzleEffect();

    }

    //this is an enum for the types of bullets used
    public enum BULLETSTYPE{
        COIN,BULLET1, ELECTRICBULLET
    }

}
