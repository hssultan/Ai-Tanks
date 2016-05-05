package com.bvisiongames.me.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bvisiongames.me.Weapons.Weapon;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.settings.GeneralMethods;

/**
 * Created by Sultan on 7/2/2015.
 */
public class SmallBox extends Entity{

    public Sprite box;

    public BOXTYPE boxType = BOXTYPE.box_small;
    public boolean Center = false;

    //saves the diagonal of this tank in pixels
    private float diagonal = 0;

    public SmallBox(Vector2 initialPosition, float angle, BOXTYPE boxtype, boolean center){

        Center = center;
        box = new Sprite(MultiGameScreen.Boxes.getRegion(boxtype.toString()));
        boxType = boxtype;

        //solve for the diagonal
        this.diagonal = (float)Math.sqrt( box.getWidth()*box.getWidth() + box.getHeight()*box.getHeight() );

        //bullet body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        if(center){
            bodyDef.position.set(initialPosition.x/ConstantVariables.PIXELS_TO_METERS, initialPosition.y/ConstantVariables.PIXELS_TO_METERS);
        }else{
            bodyDef.position.set((initialPosition.x + box.getWidth()/2)/ConstantVariables.PIXELS_TO_METERS,
                                    (initialPosition.y + box.getHeight()/2)/ConstantVariables.PIXELS_TO_METERS);
        }
        bodyDef.angle = angle;
        bodyDef.bullet = true;
        bodyDef.angularDamping = 5;
        bodyDef.linearDamping = 5;

        body = MultiGameScreen.WORLD.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(box.getWidth()/(2*ConstantVariables.PIXELS_TO_METERS), box.getHeight()/(2*ConstantVariables.PIXELS_TO_METERS));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 8f;
        fixtureDef.friction = 1f;

        body.createFixture(fixtureDef);
        body.setUserData(this);

        shape.dispose();

    }

    @Override
    public void update(){

        //update entity properties
        this.setPosition(body.getPosition().x, body.getPosition().y);
        this.setBodyAngle(body.getAngle());

        //update the sprite properties
        box.setPosition((body.getPosition().x*ConstantVariables.PIXELS_TO_METERS) - box.getWidth() / 2,
                        (body.getPosition().y* ConstantVariables.PIXELS_TO_METERS) - box.getHeight() / 2);
        box.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

    }
    @Override
    public void render(SpriteBatch batch){

        if(GeneralMethods.isInsideCameraView(GeneralMethods.convertFromBodyToPixels(getPosition()),
                                                this.diagonal/2,
                                                MultiGameScreen.camera.frustum)){
            box.draw(batch);
        }

    }
    @Override
    public void dispose(){



    }

    //small variables for the hitbybullet method
    private Vector2 linearImpulseHitByBullet = new Vector2(0, 0);
    public void hitByBullet(Weapon weapon){

        int hitBulletIntensity;

        //intensity of the blast based on the bullet type
        switch (weapon.bulletstype){
            case BULLET1:
                hitBulletIntensity = 200;
                break;
            default:
                hitBulletIntensity = 200;
                break;
        }

        //apply the impulse on the box
        linearImpulseHitByBullet.set(body.getPosition().x - weapon.body.getPosition().x,
                                        body.getPosition().y - weapon.body.getPosition().y);
        body.applyLinearImpulse(
                linearImpulseHitByBullet.scl(hitBulletIntensity),
                body.getPosition(), true);

    }

    public enum BOXTYPE{
        box_2x1, box_2x2, box, box_small
    }

}
