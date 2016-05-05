package com.bvisiongames.me.Weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.settings.GeneralMethods;

/**
 * Created by Sultan on 6/30/2015.
 */
public class Bullet extends Weapon{

    //price tag of this weapon
    public static final float PRICE = 100;    //coins
    public static final String INFO = "Normal Bullet";   //information about this weapon

    //saves the diagonal of this tank in pixels
    private float diagonal = 0;

    /**
     * initiator.
     */
    public Bullet(Tank tank){

        //set the owner of this tank
        owner = tank;

        //begin angle
        beginAngle = owner.body.getAngle();

        //bullet's position
        pos = new Vector2( (tank.tank.getHeight()/1.4f)* (float)Math.cos( tank.body.getAngle() + Math.PI/2  ) + (tank.getPosition().x*ConstantVariables.PIXELS_TO_METERS),
                (tank.tank.getHeight()/1.4f) *(float)Math.sin(tank.body.getAngle() + Math.PI/2 ) + (tank.getPosition().y*ConstantVariables.PIXELS_TO_METERS) );


        //setup the skin for bullet and position
        bulletstype = BULLETSTYPE.BULLET1;
        bullet = new Sprite(MultiGameScreen.Bullets.getRegion("bullet1"));

        //solve for the diagonal
        this.diagonal = (float)Math.sqrt( bullet.getWidth()*bullet.getWidth() + bullet.getHeight()*bullet.getHeight() );

        //setup bullet properties
        constantlinearVelocity = 2000/ConstantVariables.PIXELS_TO_METERS;

        //bullet body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.x/ConstantVariables.PIXELS_TO_METERS, pos.y/ConstantVariables.PIXELS_TO_METERS);
        bodyDef.angle = tank.body.getAngle();
        bodyDef.bullet = true;

        body = MultiGameScreen.WORLD.createBody(bodyDef);

        //bullet's main fixture creation
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bullet.getWidth()/(2*ConstantVariables.PIXELS_TO_METERS), bullet.getHeight()/(2*ConstantVariables.PIXELS_TO_METERS));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0;

        body.createFixture(fixtureDef);

        body.setUserData(this);

        shape.dispose();
        //end of bullet creation of the main fixture

        //play the shoot sound
        MultiGameScreen.soundeffect.PlayShoot(GeneralMethods.convertFromBodyToPixels(tank.getPosition()));

        //add the muzzle smoke effect to the effect list
        owner.setAndShowMuzzleEffect();

    }

    @Override
    public void update(){

        //update the position and the body angle
        this.setPosition(body.getPosition().x, body.getPosition().y);
        this.setBodyAngle(body.getAngle());

        //if the the velocity status is true then allow the bullet to move in a linear motion
        if(isVelocityStatus()) {
            body.setLinearVelocity(constantlinearVelocity * (float) Math.cos(beginAngle + Math.PI / 2),
                    constantlinearVelocity * (float) Math.sin(beginAngle + Math.PI / 2));

            bullet.setPosition((body.getPosition().x * ConstantVariables.PIXELS_TO_METERS) - bullet.getWidth() / 2,
                    (body.getPosition().y * ConstantVariables.PIXELS_TO_METERS) - bullet.getHeight() / 2);
            bullet.setRotation(beginAngle * MathUtils.radiansToDegrees);
        }else{

            if(body.isActive()){
                body.setActive(false);
            }

            //else stop the bullet in it's place
            body.setLinearVelocity(0, 0);
            body.setAngularVelocity(0);

            bullet.setPosition((body.getPosition().x * ConstantVariables.PIXELS_TO_METERS) - bullet.getWidth() / 2,
                    (body.getPosition().y * ConstantVariables.PIXELS_TO_METERS) - bullet.getHeight() / 2);
            bullet.setRotation(beginAngle * MathUtils.radiansToDegrees);
        }

    }
    @Override
    public void render(SpriteBatch spriteBatch){

        //if velocity status is true draw the bullet sprite
        if (isVelocityStatus()
                && GeneralMethods.isInsideCameraView(GeneralMethods.convertFromBodyToPixels(getPosition()),
                                                        this.diagonal/2,
                                                        MultiGameScreen.camera.frustum)) {
            bullet.draw(spriteBatch);
        }

        //if the velocity status is false and the bullet's status is true draw the blast from the bullet
        if (!isVelocityStatus() && isStatus()){
            //activate the particle
            MultiGameScreen.particleEffectsManager.defaultTankExplosionManager
                    .activateBulletExplosion(GeneralMethods.convertFromBodyToPixels(getPosition()), owner.getDirection());
        }

        //if the velocity status is false and the animation of the blast has finished then change
        //the status of the bullet to false, this will signal to the main thread to dispose it
        if(!isVelocityStatus()){
            status = false;
        }

    }
    @Override
    public void pause(){

    }
    @Override
    public void resume(){

    }
    @Override
    public void dispose(){

        if(body != null){
            body.setLinearVelocity(0, 0);
            bullet.setAlpha(0);

            MultiGameScreen.bodyDisposer.addBodyToDispose(body);
            MultiGameScreen.WORLD.destroyBody(body);
            body.setUserData(null);
            body = null;
        }

    }

    public static float getReloadingTimeSec(BULLETSTYPE bulletstype){

        switch (bulletstype){
            case BULLET1:
                return 5;   //5 sec
            case ELECTRICBULLET:
                return 10;  //10 sec
            default:
                return 5;   //5 seconds for the regular bullet
        }
    }

}
