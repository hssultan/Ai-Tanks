package com.bvisiongames.me.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Tweenanimations.Box2dActorAnimation;
import com.bvisiongames.me.camera.OrthoCamera;
import com.bvisiongames.me.settings.Assets;
import com.bvisiongames.me.vidAnimations.IntroAnimation;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Sultan on 7/16/2015.
 */
public class SplashScreen extends Screen {

    public static OrthoCamera camera;
    public static Stage stage;

    private IntroAnimation introAnimation;

    public static TweenManager tweenManager;

    public static BitmapFont titleFont;

    @Override
    public void create() {

        //initialize the camera
        camera = new OrthoCamera();
        //set and reset the viewport width and height
        camera.update();
        camera.resize();

        Assets.loadMenuScreenAssets();

        stage = new Stage(new FitViewport(Tanks.cameraWIDTH, Tanks.cameraHeight));
        tweenManager = new TweenManager();

        setupFonts();

        introAnimation = new IntroAnimation();

        Tween.registerAccessor(Actor.class, new Box2dActorAnimation());

        introAnimation.initiateBegin();

    }
    private void setupFonts(){

        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/chunk.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 100;
        fontParameter.color = new Color(0.43f, 0.5f, 0.61f, 1);
        fontParameter.borderColor = Color.WHITE;
        fontParameter.borderWidth = 5;
        fontParameter.shadowColor = new Color(0.2f, 0.2f, 0.2f, 1);
        fontParameter.shadowOffsetX = 5;
        fontParameter.shadowOffsetY = 5;

        titleFont = freeTypeFontGenerator.generateFont(fontParameter);

    }

    @Override
    public void update() {
        camera.update();
        tweenManager.update(Gdx.graphics.getDeltaTime());

        introAnimation.update();

        stage.act();

    }

    @Override
    public void render(SpriteBatch sb) {

        Gdx.gl.glClearColor(0.34f, 0.03f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(camera.combined);

        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        camera.resize();
    }

    @Override
    public void dispose() {

        stage.dispose();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

}
