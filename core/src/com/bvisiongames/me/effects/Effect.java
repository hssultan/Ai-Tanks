package com.bvisiongames.me.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.GeneralMethods;

/**
 * Created by ahzji_000 on 11/3/2015.
 */
public abstract class Effect {

    Vector2 pos, centerOfRotation = new Vector2(0, 0);
    Animation animation;
    float time;
    boolean visibility = true;
    boolean repeating = false;
    float angle = 0, width = 0, height = 0;

    Skin skin;

    //set the diagonal
    private float diagonal;

    public void setAnimation(TextureRegion[] textureRegions, float speed){

        this.animation = new Animation(1/speed, textureRegions);

        //set the width and height
        width = skin.getRegion("0").getRegionWidth();
        height = skin.getRegion("0").getRegionHeight();

        //set the diagonal
        this.diagonal = (float)Math.sqrt( width*width + height*height );

    }

    public void render(SpriteBatch spriteBatch) {

        if(visibility && GeneralMethods.isInsideCameraView(pos, diagonal/2, MultiGameScreen.camera.frustum)){
            spriteBatch.draw(animation.getKeyFrame(time += Gdx.graphics.getDeltaTime()),
                    pos.x,
                    pos.y,
                    centerOfRotation.x, centerOfRotation.y,
                    getWidth(),
                    getHeight(),
                    1, 1,
                    angle * MathUtils.radiansToDegrees);
            if(repeating && isFinished()){
                restart();
            }
        }

    }

    /**
     * sets the angle in degree
     * @param angle
     * sets the angle in degree.
     */
    public void setAngle(float angle){
        this.angle = angle;
    }
    public void setPos(Vector2 pos){
        this.pos = pos;
    }
    public void setPos(float x, float y){
        this.pos.set(x, y);
    }
    public Vector2 getPos(){
        return pos;
    }
    public void setWidth(float width){
        this.width = width;
    }
    public void setHeight(float height){ this.height = height; }
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
    public void setCenterOfRotation(Vector2 centerOfRotation){
        this.centerOfRotation.set(centerOfRotation.x, centerOfRotation.y);
    }
    public Vector2 getCenterOfRotation(){
        return this.centerOfRotation;
    }

    public boolean isFinished(){
        return animation.isAnimationFinished(time);
    }
    public boolean isVisibile() {
        return visibility;
    }
    public void flagDispose(){
        repeating = false;
    }
    public void hide(){
        visibility = false;
    }
    public void show(){
        visibility = true;
    }
    public void restart(){
        time = 0;
    }

}
