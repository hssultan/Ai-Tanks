package com.bvisiongames.me.rewards;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.effects.Coins;
import com.bvisiongames.me.entity.EntitManager;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.settings.GeneralMethods;

/**
 * Created by ahzji_000 on 12/17/2015.
 */
public class CoinReward {

    private boolean status = true,      //status of this coin
                    collecting = false, //status of collecting this coin
                    collected = false;  //status whether this coin has been collected
    private int value = 0;
    private Vector2 position = new Vector2(0, 0);
    private Body body;

    private Coins coinsAnimation;

    /**
     * initiator.
     * @param value
     * value of this coin.
     * @param position
     * position of this body.
     */
    public CoinReward(int value, Vector2 position){

        this.position = position.cpy();
        this.value = value;

        //setup animation for this coin
        this.coinsAnimation = new Coins(this.position.cpy(), true, 0);
        this.coinsAnimation.setWidth(this.coinsAnimation.getWidth()/2);
        this.coinsAnimation.setHeight(this.coinsAnimation.getHeight()/2);
        EntitManager.effectsList.add(this.coinsAnimation);

        //setup a body for this coin
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((position.x + GeneralMethods.GenerateRandom(-2, 2) ) / ConstantVariables.PIXELS_TO_METERS,
                                (position.y + GeneralMethods.GenerateRandom(-2, 2)) / ConstantVariables.PIXELS_TO_METERS);

        body = MultiGameScreen.WORLD.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(this.coinsAnimation.getWidth()/(2* ConstantVariables.PIXELS_TO_METERS));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 1f;
        fixtureDef.density = 0.7f;

        this.body.createFixture(fixtureDef);

        shape.dispose();

        //set the userdata to this class
        this.body.setUserData(this);
        //set the velocity damping
        this.body.setAngularDamping(2);
        this.body.setLinearDamping(2);
        //end of body setup

        //make the center of rotation the center of the animation
        coinsAnimation.setCenterOfRotation(new Vector2(coinsAnimation.getWidth()/2, coinsAnimation.getHeight()/2));

    }

    //setters
    /**
     * sets the position of this coin.
     * @param position
     * position of this coin.
     */
    public void setPosition(Vector2 position){
        this.position.set(position.x, position.y);
    }
    /**
     * dispose.
     */
    public void dispose(){

        this.coinsAnimation.flagDispose();
        this.body.setUserData(null);
        MultiGameScreen.WORLD.destroyBody(this.body);
        this.body = null;

    }
    /**
     * flag this coin for disposal
     */
    public void flagToDispose(){
        this.status = false;
    }
    /**
     * start animation toward the coin displayer when there is collision detection between main tank and this coin.
     */
    public void initiateCollect(){
        this.collecting = true;
    }
    //end of setters


    //getters
    /**
     * get the position in pixels
     */
    public Vector2 getPosition(){
        return this.position;
    }
    /**
     * is this coin still valid to be rendered and updated.
     */
    public boolean isAlive(){
        return this.status;
    }
    /**
     * get the value of this coin.
     */
    public int getValue(){
        return this.value;
    }
    /**
     * returns whether the coin is being collected.
     */
    public boolean isCollecting(){
        return this.collecting;
    }
    /**
     * returns whether this coin has been collected.
     */
    public boolean isCollected(){
        return this.collected;
    }
    /**
     * this checks whether the coin is near the coins displayer.
     */
    //small variables for the near coin display method
    private Circle nearCoinCircle = new Circle(0, 0, 0);
    public boolean isNearCoinDisplayer(){
        nearCoinCircle.setPosition(EntitManager.controllers.clientScore.getRPosition());
        nearCoinCircle.setRadius(20);
        if(nearCoinCircle.contains(getPosition())){
            return true;
        }

        return false;
    }
    //end of getters


    //update and render
    public void update(){

        //update the position
        this.position = GeneralMethods.convertFromBodyToPixels(this.body.getPosition());

        //animate the collecting animation
        if(isCollecting()){

            //deactivate the body from sensing
            this.body.setActive(false);

            //move the body toward the coin displayer
            this.position.add(
                    (EntitManager.controllers.clientScore.getRPosition().x - this.position.x)/6,
                    (EntitManager.controllers.clientScore.getRPosition().y - this.position.y)/6
            );
            this.body.setTransform(
                    GeneralMethods.convertFromPixelsToBody(this.position.x, this.position.y),
                    this.body.getAngle()
            );

            if(isNearCoinDisplayer() && !isCollected()){
                //add coins to the player
                EntitManager.tank.tankProperties.coins += getValue();
                Tanks.jarCoins.addCoins(getValue());
                EntitManager.controllers.clientScore.AnimateCoinsChange(Tanks.jarCoins.getCoins());
                //hide the coin animation
                this.coinsAnimation.hide();
                //flag this coin to be disposed
                flagToDispose();
                //set the collected to true
                collected = true;
            }

        }

        //update the position of the animation
        this.coinsAnimation.setPos(this.position.x - this.coinsAnimation.getWidth()/2,
                                    this.position.y - this.coinsAnimation.getHeight()/2);
        this.coinsAnimation.setAngle(this.body.getAngle());

    }

}
