package com.bvisiongames.me.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Tweenanimations.Box2dActorAnimation;
import com.bvisiongames.me.camera.OrthoCamera;
import com.bvisiongames.me.miniscreen.CreditScreen;
import com.bvisiongames.me.miniscreen.SettingScreen;
import com.bvisiongames.me.settings.Assets;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Sultan on 6/26/2015.
 */
public class MenuScreen extends Screen implements InputProcessor {

    OrthoCamera camera;
    public static Stage stage, miniscreenStage;
    public static BitmapFont fntTitle, playerFnt,
                                defaultFonts;

    //ui of stage
    private Label title;
    public static TextButton play;
    private Button settings, info;
    public static TextField inputName;
    public static Label playerData;

    //miniscreens
    private CreditScreen creditScreens;
    private SettingScreen settingScreen;

    //tween Manager
    public static TweenManager tweenManager;

    @Override
    public void create() {

            //initialize the camera
            camera = new OrthoCamera();
            //set and reset the viewport width and height
            camera.update();
            camera.resize();

            //initiate the stage
            stage = new Stage(new FitViewport(Tanks.cameraWIDTH, Tanks.cameraHeight));
            miniscreenStage = new Stage(new FitViewport(Tanks.cameraWIDTH, Tanks.cameraHeight));

            //check if the assets has been loaded for the menu screens
            if(Assets.isMenuLoaded){
                setupScreen();
            }else{
                //else tell the update and render methods to not loop through objects
                propertiesLoaded = false;
            }

    }
    private boolean propertiesLoaded = true, assetLoaderTriggered = true;

    private void setupScreen(){
        //creating fonts
        setupFonts();

        //setup the gui
        setupMainGui();

        //creditScreen
        creditScreens = new CreditScreen();
        settingScreen = new SettingScreen();

        //add the listeners below
        //set the input listener
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(this);
        multi.addProcessor(miniscreenStage);
        multi.addProcessor(stage);
        Gdx.input.setInputProcessor(multi);

        //this disable textfield input focus
        stage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField)) stage.setKeyboardFocus(null);
                return false;
            }
        });

        //tween manager register
        //And tween manager
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new Box2dActorAnimation());

        propertiesLoaded = true;
    }

    private void setupFonts(){

        fntTitle = Assets.assetManager.get(Assets.MenuFileNames.MainMenuFonts.fntTitleString, BitmapFont.class);

        playerFnt = Assets.assetManager.get(Assets.MenuFileNames.MainMenuFonts.playerFntString, BitmapFont.class);

        defaultFonts = Assets.assetManager.get(Assets.MenuFileNames.MainMenuFonts.defaultFontsString, BitmapFont.class);

    }
    private void setupMainGui(){

        //paddings
        float paddingTop = 50, paddingBottom = 50,
                paddingLeft = 50, paddingRight = 50;

        //setup title
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = fntTitle;
        title = new Label("Tanks", titleStyle);
        title.setPosition(Tanks.cameraWIDTH/2 - title.getWidth()/2, Tanks.cameraHeight - title.getHeight() - paddingTop*3);

        //setup the input name field
        TextField.TextFieldStyle inputNameStyle = new TextField.TextFieldStyle();
        inputNameStyle.background =
                new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.MenuFileNames.getMenuSkin().getRegion("input")), 10,10,10,10));
        inputNameStyle.cursor = Assets.MenuFileNames.getMenuSkin().getDrawable("cursor");
        inputNameStyle.cursor.setMinWidth(3);
        inputNameStyle.font = defaultFonts;
        inputNameStyle.fontColor = Color.GRAY;
        inputName = new TextField(Tanks.gameState.getUserName(), inputNameStyle);
        inputName.setMaxLength(40);
        inputName.setMessageText("Enter Name");
        inputName.setSize(Tanks.cameraWIDTH / 3, inputName.getHeight());
        inputName.setPosition(Tanks.cameraWIDTH / 2 - inputName.getWidth() / 2,
                                title.getY() - inputName.getHeight() - paddingTop);

        //play button
        TextButton.TextButtonStyle txtBtnStyle = new TextButton.TextButtonStyle();
        txtBtnStyle.up = new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.MenuFileNames.getMenuSkin().getRegion("blueup")), 5,5,5,5));
        txtBtnStyle.up.setLeftWidth(40);
        txtBtnStyle.up.setRightWidth(40);
        txtBtnStyle.down = new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown")), 5,5,5,5));
        txtBtnStyle.down.setLeftWidth(40);
        txtBtnStyle.down.setRightWidth(40);
        txtBtnStyle.font = defaultFonts;
        txtBtnStyle.fontColor = Color.WHITE;
        txtBtnStyle.pressedOffsetX = -1;
        txtBtnStyle.pressedOffsetY = -1;
        txtBtnStyle.downFontColor = new Color(0.4f, 0.4f, 0.4f, 1);
        play = new TextButton("PLAY", txtBtnStyle);
        play.setPosition(Tanks.cameraWIDTH / 2 - play.getWidth() / 2, paddingBottom * 2.5f);
        play.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!play.isDisabled()){
                    Tanks.gameState.setUserName(inputName.getText().toString());
                    play.setDisabled(true);

                    MenuScreen.play.setDisabled(false);

                    //change the screen
                    ScreenManager.setScreen(new Lobby());
                }
            }
        });

        //score gui
        Label.LabelStyle playerDataStyle = new Label.LabelStyle();
        playerDataStyle.font = playerFnt;
        playerData = new Label("Kills: "+Tanks.gameState.getKills()+" Deaths: "+Tanks.gameState.getDeaths(),
                playerDataStyle);
        playerData.setPosition(Tanks.cameraWIDTH / 2 - playerData.getWidth() / 2,
                play.getY() + play.getHeight() + 100);

        //setup the settings button
        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        btnStyle.up = new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.MenuFileNames.getMenuSkin().getRegion("setting")), 5,5,5,5));
        btnStyle.down = new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.MenuFileNames.getMenuSkin().getRegion("settingdown")), 5,5,5,5));
        settings = new Button(btnStyle);
        settings.setPosition(paddingLeft, paddingBottom);
        settings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingScreen.Triggered();
            }
        });

        //setup the info button
        Button.ButtonStyle infoStyle = new Button.ButtonStyle();
        infoStyle.up = new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.MenuFileNames.getMenuSkin().getRegion("infoup")), 10,10,10,10));
        infoStyle.down = new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.MenuFileNames.getMenuSkin().getRegion("infodown")), 10,10,10,10));
        info = new Button(infoStyle);
        info.setPosition(Tanks.cameraWIDTH - info.getWidth() - paddingRight, paddingBottom);
        info.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                creditScreens.Triggered();
            }
        });

        //add the gui to the stage
        stage.addActor(title);
        stage.addActor(inputName);
        stage.addActor(play);
        stage.addActor(playerData);
        stage.addActor(settings);
        stage.addActor(info);

    }

    @Override
    public void update() {

        if(propertiesLoaded){
            camera.update();
            tweenManager.update(Gdx.graphics.getDeltaTime());

            stage.act();
            miniscreenStage.act();

        }else{

            if(assetLoaderTriggered){
                Tanks.SocketStandBy.show();
                Tanks.SocketStandBy.percentDisplay.show();
                assetLoaderTriggered = false;
                Assets.loadMenuScreenAssets();
            }else if(Assets.assetManager.update()){
                setupScreen();
                Tanks.SocketStandBy.hide();
            }else if(!Assets.assetManager.update()){
                Tanks.SocketStandBy.percentDisplay.updatePercent((int)(Assets.assetManager.getProgress()*100));
            }

        }

    }

    @Override
    public void render(SpriteBatch sb) {

        Gdx.gl.glClearColor(0.34f, 0.03f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(camera.combined);

        if(propertiesLoaded){

            stage.draw();
            miniscreenStage.draw();

            //miniscreen render
            settingScreen.render(sb);
            creditScreens.render(sb);
            //----------------

        }

    }

    @Override
    public void resize(int width, int height) {
        camera.resize();
    }

    @Override
    public void dispose() {

        stage.dispose();

        //dispose the miniscreens
        if(creditScreens != null)
            creditScreens.dispose();
        if(settingScreen != null)
            settingScreen.dispose();

    }

    @Override
    public void pause() {



    }

    @Override
    public void resume() {



    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean result = false;

        /*
        //disable the on touch of all other objects
        if(Tanks.SocketStandBy.onTouch(screenX, screenY)){
            return true;
        }else{
            result = Tanks.SocketStandBy.onTouch(screenX, screenY);
        }
        */
        //end of disabling everything else--------

        return result;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
