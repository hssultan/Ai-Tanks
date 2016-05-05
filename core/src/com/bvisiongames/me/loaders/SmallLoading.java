package com.bvisiongames.me.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Sultan on 7/25/2015.
 */
public class SmallLoading {

    private float time;
    //visibility of this animation
    private boolean isVisible = false;

    private Skin loadSkin;

    //animations
    public static Animation searchAnimation;

    //position of this animation
    private Vector2 pos;

    //state of this loader animation
    public LoaderState loaderState = LoaderState.REPEATING;

    //run time out in millisecond
    public int runTimeOut = 1000;
    //frames count
    private int framesCount = 0;

    public SmallLoading(Vector2 position){

        loadSkin = new Skin(new TextureAtlas("loading/searchload.pack"));

        //initiate the search loading
        searchAnimation = new Animation(1/40f,
                new TextureRegion(loadSkin.getRegion("0")),
                new TextureRegion(loadSkin.getRegion("1")),
                new TextureRegion(loadSkin.getRegion("2")),
                new TextureRegion(loadSkin.getRegion("3")),
                new TextureRegion(loadSkin.getRegion("4")),
                new TextureRegion(loadSkin.getRegion("5")),
                new TextureRegion(loadSkin.getRegion("6")),
                new TextureRegion(loadSkin.getRegion("7")),
                new TextureRegion(loadSkin.getRegion("8")),
                new TextureRegion(loadSkin.getRegion("9")),
                new TextureRegion(loadSkin.getRegion("10")),
                new TextureRegion(loadSkin.getRegion("11")),
                new TextureRegion(loadSkin.getRegion("12")),
                new TextureRegion(loadSkin.getRegion("13")),
                new TextureRegion(loadSkin.getRegion("14")),
                new TextureRegion(loadSkin.getRegion("15")),
                new TextureRegion(loadSkin.getRegion("16")),
                new TextureRegion(loadSkin.getRegion("17")),
                new TextureRegion(loadSkin.getRegion("18")),
                new TextureRegion(loadSkin.getRegion("19")),
                new TextureRegion(loadSkin.getRegion("20")),
                new TextureRegion(loadSkin.getRegion("21")),
                new TextureRegion(loadSkin.getRegion("22")),
                new TextureRegion(loadSkin.getRegion("23")),
                new TextureRegion(loadSkin.getRegion("24")),
                new TextureRegion(loadSkin.getRegion("25")),
                new TextureRegion(loadSkin.getRegion("26")),
                new TextureRegion(loadSkin.getRegion("27")),
                new TextureRegion(loadSkin.getRegion("28")),
                new TextureRegion(loadSkin.getRegion("29")),
                new TextureRegion(loadSkin.getRegion("30")),
                new TextureRegion(loadSkin.getRegion("31")),
                new TextureRegion(loadSkin.getRegion("32"))
        );

        searchAnimation.setPlayMode(Animation.PlayMode.LOOP);

        pos = position;

    }

    public void update(float delta){

        if(isVisible){

            if(loaderState == LoaderState.RUN_ONCE){

                if(framesCount <= ((float)runTimeOut/delta)/1000){
                    framesCount++;
                }else{
                    //hide this animation and reset the frame count
                    framesCount = 0;
                    isVisible = false;
                }

            }

        }

    }
    public void render(Batch spriteBatch){
        if(isVisible){
            spriteBatch.begin();
            spriteBatch.draw(searchAnimation.getKeyFrame(time += Gdx.graphics.getDeltaTime()),
                    pos.x - loadSkin.getRegion("0").getRegionWidth() / 2, pos.y - loadSkin.getRegion("0").getRegionHeight()/2);
            spriteBatch.end();
        }
    }
    public void show(){
        isVisible = true;
    }
    public void hide(){
        isVisible = false;
    }
    public void setPosition(float x, float y){
        pos.x = x;
        pos.y = y;
    }
    public void dispose(){
        isVisible = false;
        loadSkin.dispose();
    }

    //setters
    /**
     * change the
     */
    //end of setters

    //getters
    public Vector2 GetPos(){
        return pos;
    }
    public float GetWidth(){
        return loadSkin.getRegion("0").getRegionWidth();
    }
    public float GetHeight(){
        return loadSkin.getRegion("0").getRegionHeight();
    }
    //end of getters

}
