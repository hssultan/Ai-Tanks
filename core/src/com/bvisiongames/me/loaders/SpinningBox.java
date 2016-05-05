package com.bvisiongames.me.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Sultan on 8/2/2015.
 */
public class SpinningBox {

    private float time;
    public boolean loading = false;

    private Skin loadSkin;

    //animations
    public static Animation searchAnimation;

    private Vector2 pos;

    public SpinningBox(Vector2 position){

        loadSkin = new Skin(new TextureAtlas("loading/joinload.pack"));

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
                new TextureRegion(loadSkin.getRegion("32")),
                new TextureRegion(loadSkin.getRegion("33")),
                new TextureRegion(loadSkin.getRegion("34")),
                new TextureRegion(loadSkin.getRegion("35")),
                new TextureRegion(loadSkin.getRegion("36")),
                new TextureRegion(loadSkin.getRegion("37")),
                new TextureRegion(loadSkin.getRegion("38")),
                new TextureRegion(loadSkin.getRegion("39")),
                new TextureRegion(loadSkin.getRegion("40")),
                new TextureRegion(loadSkin.getRegion("41")),
                new TextureRegion(loadSkin.getRegion("42")),
                new TextureRegion(loadSkin.getRegion("43")),
                new TextureRegion(loadSkin.getRegion("44")),
                new TextureRegion(loadSkin.getRegion("45")),
                new TextureRegion(loadSkin.getRegion("46")),
                new TextureRegion(loadSkin.getRegion("47")),
                new TextureRegion(loadSkin.getRegion("48")),
                new TextureRegion(loadSkin.getRegion("49")),
                new TextureRegion(loadSkin.getRegion("50")),
                new TextureRegion(loadSkin.getRegion("51")),
                new TextureRegion(loadSkin.getRegion("52")),
                new TextureRegion(loadSkin.getRegion("53")),
                new TextureRegion(loadSkin.getRegion("54")),
                new TextureRegion(loadSkin.getRegion("55")),
                new TextureRegion(loadSkin.getRegion("56")),
                new TextureRegion(loadSkin.getRegion("57")),
                new TextureRegion(loadSkin.getRegion("58")),
                new TextureRegion(loadSkin.getRegion("59"))
        );

        searchAnimation.setPlayMode(Animation.PlayMode.LOOP);

        pos = position;

    }

    public void render(SpriteBatch spriteBatch){

        if(loading){
            spriteBatch.begin();
            spriteBatch.draw(searchAnimation.getKeyFrame(time += Gdx.graphics.getDeltaTime()),
                    pos.x - loadSkin.getRegion("0").getRegionWidth()/2, pos.y - loadSkin.getRegion("0").getRegionHeight()/2,
                    loadSkin.getRegion("0").getRegionWidth(), loadSkin.getRegion("0").getRegionHeight() );
            spriteBatch.end();
        }

    }
    public void show(){
        loading = true;
    }
    public void hide(){
        loading = false;
    }
    public void setPosition(float x, float y){
        pos.x = x;
        pos.y = y;
    }
    public void dispose(){
        loading = false;
        loadSkin.dispose();
    }
    public Vector2 GetPos(){
        return pos;
    }
    public float GetWidth(){
        return loadSkin.getRegion("0").getRegionWidth();
    }
    public float GetHeight(){
        return loadSkin.getRegion("0").getRegionHeight();
    }

}
