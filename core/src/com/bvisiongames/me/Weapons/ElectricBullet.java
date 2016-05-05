package com.bvisiongames.me.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
 * Created by ahzji_000 on 11/7/2015.
 */
public class ElectricBullet extends Weapon {

    //price tag of this weapon
    public static final float PRICE = 200;    //coins
    public static final String INFO = "Electric Bullet";   //information about this weapon

    //particle effect for lazer bullet
    public ParticleEffect particleEffect = new ParticleEffect();

    public ElectricBullet(Tank tank){

        owner = tank;
        beginAngle = tank.body.getAngle();

        //setup the skin for bullet and position
        bulletstype = BULLETSTYPE.ELECTRICBULLET;
        this.particleEffect.load(MultiGameScreen.particleEffectsManager.particleEffectsFilesManager.laserBulletEffectFile,
                MultiGameScreen.particleEffectsManager.particleEffectsFilesManager.laserBulletImageFileDir);
        this.particleEffect.start();

        pos = new Vector2( (tank.tank.getHeight()/1.4f)* (float)Math.cos( tank.body.getAngle() + Math.PI/2  ) + (tank.getPosition().x*ConstantVariables.PIXELS_TO_METERS),
                (tank.tank.getHeight()/1.4f) *(float)Math.sin(tank.body.getAngle() + Math.PI/2 ) + (tank.getPosition().y*ConstantVariables.PIXELS_TO_METERS) );

        //set the position and angle of the particle effect
        this.particleEffect.setPosition(pos.x, pos.y);
        this.particleEffect.getEmitters().first().getAngle().setHigh(beginAngle*MathUtils.radiansToDegrees);

        //bullet body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.x/ConstantVariables.PIXELS_TO_METERS, pos.y/ConstantVariables.PIXELS_TO_METERS);
        bodyDef.angle = tank.body.getAngle();
        bodyDef.bullet = true;

        body = MultiGameScreen.WORLD.createBody(bodyDef);

        //setup bullet properties
        constantlinearVelocity = 1500/ConstantVariables.PIXELS_TO_METERS;

        //bullet's main fixture creation
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.15f, 0.4f);

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

        //show the muzzle explosion effect
        owner.setAndShowMuzzleEffect();

    }

    @Override
    public void update() {

        //update the position
        this.setPosition(body.getPosition().x, body.getPosition().y);
        //update the body angle
        this.setBodyAngle(body.getAngle());

        //if the the velocity status is true then allow the bullet to move in a linear motion
        if(isVelocityStatus()) {
            body.setLinearVelocity(constantlinearVelocity * (float) Math.cos(beginAngle + Math.PI / 2),
                    constantlinearVelocity * (float) Math.sin(beginAngle + Math.PI / 2));

            this.particleEffect.update(Gdx.graphics.getDeltaTime());
            this.particleEffect.setPosition(getPosition().x * ConstantVariables.PIXELS_TO_METERS,
                                                getPosition().y * ConstantVariables.PIXELS_TO_METERS);
            this.particleEffect.getEmitters().first().getAngle().setHigh(getBodyAngle()*MathUtils.radiansToDegrees - 90);

            if(this.particleEffect.isComplete())
                this.particleEffect.reset();

        }else{

            if(body.isActive()){
                body.setActive(false);
            }

            //else stop the bullet in it's place
            body.setLinearVelocity(0, 0);
            body.setAngularVelocity(0);

        }

    }

    @Override
    public void render(SpriteBatch spriteBatch) {

        //if velocity status is true draw the bullet sprite
        if (isVelocityStatus()) {
            this.particleEffect.draw(spriteBatch);
        }

        //if the velocity status is false and the animation of the blast has finished then change
        //the status of the bullet to false, this will signal to the main thread to dispose it
        if(!isVelocityStatus()){
            status = false;
        }

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

        if(body != null){
            body.setLinearVelocity(0, 0);
            body.setActive(false);
            MultiGameScreen.WORLD.destroyBody(body);
            body.setUserData(null);
            body = null;
        }

    }
}
