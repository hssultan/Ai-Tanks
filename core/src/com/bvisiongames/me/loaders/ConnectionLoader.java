package com.bvisiongames.me.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Tweenanimations.PercentDisplayAnimation;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Sultan on 7/25/2015.
 */
public class ConnectionLoader {

    private Animation animation;
    private Skin loadSkin;
    private boolean loading = false;
    private float time;
    private Sprite bgCover;
    private Vector2 pos;

    //percent properties
    public PercentDisplay percentDisplay;

    //tween manager
    private TweenManager tweenManager;

    public ConnectionLoader(){

        tweenManager = new TweenManager();
        Tween.registerAccessor(PercentDisplay.class, new PercentDisplayAnimation());

        //set up the background properties
        bgCover = new Sprite(new Texture("backgrounds/black.jpg"));
        bgCover.setSize(Tanks.cameraWIDTH, Tanks.cameraHeight);
        bgCover.setPosition(0, 0);
        bgCover.setAlpha(0.6f);

        //setup animating cube
        loadSkin = new Skin(new TextureAtlas("loading/loading.pack"));
        setupAnimation();
        animation.setPlayMode(Animation.PlayMode.LOOP);

        //add the percent display
        percentDisplay = new PercentDisplay();

    }
    private void setupAnimation(){
        animation = new Animation(1/40f,
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
                new TextureRegion(loadSkin.getRegion("51"))
        );
    }

    public void render(SpriteBatch spriteBatch){

        tweenManager.update(Gdx.graphics.getDeltaTime());

        if(loading){

            //first check if the pos does not have zero coordinates and the size of the bg is not zero
            if(pos == null){
                pos = new Vector2(Tanks.cameraWIDTH/2 - loadSkin.getRegion("0").getRegionWidth()/2,
                        Tanks.cameraHeight/2 - loadSkin.getRegion("0").getRegionHeight()/2);
            }
            if(bgCover.getWidth() == 0 || bgCover.getHeight() == 0){
                bgCover.setSize(Tanks.cameraWIDTH, Tanks.cameraHeight);
            }

            spriteBatch.begin();
            bgCover.draw(spriteBatch);
            spriteBatch.draw(animation.getKeyFrame(time += Gdx.graphics.getDeltaTime()),
                            pos.x, pos.y,
                            loadSkin.getRegion("0").getRegionWidth(), loadSkin.getRegion("0").getRegionHeight());
            percentDisplay.render(spriteBatch);
            spriteBatch.end();

        }


    }
    public void hide(){
        loading = false;
        percentDisplay.hide();
    }
    public void show(){
        loading = true;
    }
    public void dispose(){
        loadSkin.dispose();
        bgCover.getTexture().dispose();
    }

    //percent display class
    public class PercentDisplay{

        private int percent = 0, targetPercent = 0;
        private BitmapFont bitmapFont;

        private boolean visibility = false;

        public PercentDisplay(){

            FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/chunk.otf"));
            FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            fontParameter.size = 40;
            fontParameter.color = Color.WHITE;
            fontParameter.borderWidth = 5;

            bitmapFont = freeTypeFontGenerator.generateFont(fontParameter);

        }

        //setters
        public void setPercent(int percent){
            this.percent = percent;
        }
        //end of setters
        //getters
        public int getPercent(){
            return percent;
        }
        //end of getters

        public void render(SpriteBatch spriteBatch){

            if(visibility){
                bitmapFont.draw(spriteBatch, percent+" %", pos.x + 60, pos.y - 50, 10, 1, false);
            }

        }

        public void hide(){
            visibility = false;
            this.percent = 0;
        }
        public void show(){
            visibility = true;
            this.percent = 0;
        }

        public void updatePercent(int percent){
            if(percent != targetPercent){
                targetPercent = percent;
                Tween.to(percentDisplay, PercentDisplayAnimation.PERCENT, 0.7f)
                        .target(percent)
                        .start(tweenManager);
            }
        }

    }

}
