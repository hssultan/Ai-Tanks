package com.bvisiongames.me;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bvisiongames.me.loaders.ConnectionLoader;
import com.bvisiongames.me.screen.ScreenManager;
import com.bvisiongames.me.screen.SplashScreen;
import com.bvisiongames.me.settings.GameState;
import com.bvisiongames.me.settings.JarCoins;

public class Tanks extends ApplicationAdapter{
    SpriteBatch batch;
    public static float WIDTH = 1024,      //1024  (/3)
                        HEIGHT = 812;   //812   (/3)
    public static float cameraWIDTH = 0,
            cameraHeight = 0;

    public static String PREF_FILE_NAME = "MINI_DB";
    //---------------->

    //loading
    public static ConnectionLoader SocketStandBy;

    //this class keeps the game state
    public static GameState gameState;

    //this class keeps track of the coins
    public static JarCoins jarCoins;

    @Override
    public void create () {

        //initiate the preference and game state
        gameState = new GameState(Gdx.app.getPreferences(PREF_FILE_NAME));
        jarCoins = new JarCoins(gameState.getTotalCoins());

        batch = new SpriteBatch();

        //initiate the connection loading object
        SocketStandBy = new ConnectionLoader();

        //set the screen below
        ScreenManager.setScreen(new SplashScreen());

        Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(ScreenManager.GetCurrentScreen() != null ){
            ScreenManager.GetCurrentScreen().update();
        }
        if(ScreenManager.GetCurrentScreen() != null ){
            ScreenManager.GetCurrentScreen().render(batch);
        }

        //render the loading during socket connection
        SocketStandBy.render(batch);

    }

    @Override
    public void resume() {
        super.resume();
        if(ScreenManager.GetCurrentScreen() != null ){
            ScreenManager.GetCurrentScreen().resume();
        }

    }

    @Override
    public void pause() {
        super.pause();
        if(ScreenManager.GetCurrentScreen() != null ){
            ScreenManager.GetCurrentScreen().pause();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        SocketStandBy.dispose(); //dispose the socket connection standby

        if(ScreenManager.GetCurrentScreen() != null ){
            ScreenManager.GetCurrentScreen().dispose();
        }

        batch.dispose();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if(ScreenManager.GetCurrentScreen() != null ){
            ScreenManager.GetCurrentScreen().resize(width, height);
        }
    }
}
