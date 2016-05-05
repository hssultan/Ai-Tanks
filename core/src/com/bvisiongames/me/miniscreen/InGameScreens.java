package com.bvisiongames.me.miniscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.bvisiongames.me.JSON.JSONArray;
import com.bvisiongames.me.JSON.JSONObject;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.entity.EntitManager;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.loaders.LoaderState;
import com.bvisiongames.me.screen.MenuScreen;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.screen.ScreenManager;
import com.bvisiongames.me.settings.Assets;
import com.bvisiongames.me.settings.GeneralMethods;
import com.bvisiongames.me.settings.TankMode;

/**
 * Created by Sultan on 8/7/2015.
 */
public class InGameScreens {

    //public ChooseTankScreen chooseTankScreen;
    public AfterGameScreen afterGameScreen;
    public InGameSettings inGameSettings;

    private int inGameScore = 0;
    private Tank.TANKSTYPES inGametankType;

    public InGameScreens(){

        afterGameScreen = new AfterGameScreen();
        inGameSettings = new InGameSettings();

    }

    public void ClientGameOver(){

        inGameScore = EntitManager.tank.tankProperties.kills;
        inGametankType = EntitManager.tank.tankProperties.tankType;
        EntitManager.tank.ActivateDestruction(GeneralMethods.convertFromBodyToPixels(EntitManager.tank.body.getPosition()));
        EntitManager.tank.flagToDestroy();
        EntitManager.tank.dispose();
        EntitManager.tank = null;
        EntitManager.GameState = EntitManager.GAMESTATE.WATCH;
        EntitManager.controllers.hideControllers();

        //show the after game screen
        afterGameScreen.show();

    }

    public class AfterGameScreen{

        Table main;
        Table scrollTable;

        //buttons
        TextButton replay, changeTank;

        //padding between the tanks tables left and right
        float paddingLeft = 40, paddingRight = 40,
                paddingTop = 40, paddingBottom = 40;

        //availability of the buttons in this panel
        private boolean isEnabled = true;

        //position action
        MoveToAction positionAction = new MoveToAction();

        public AfterGameScreen(){

            //main container
            main = new Table();
            Sprite mainBg = new Sprite(new Texture("backgrounds/black.jpg"));
            mainBg.setAlpha(0.7f);
            main.setBackground(new SpriteDrawable(mainBg));
            main.setSize(Tanks.cameraWIDTH, Tanks.cameraHeight / 1.2f);
            main.setPosition(0, Tanks.cameraHeight / 2 - main.getHeight() / 2);
            main.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {}
            });
            //end of main container setup

            //add the title
            Label.LabelStyle titleStyle = new Label.LabelStyle();
            titleStyle.font = MultiGameScreen.titleFont;
            Label title = new Label("Game Over",titleStyle);
            main.add(title).colspan(5).padBottom(paddingBottom);
            main.row();

            //other ui
            addOtherUI();

            //add the change tank button
            TextButton.TextButtonStyle selectStyle = new TextButton.TextButtonStyle();
            selectStyle.font = MultiGameScreen.defaultFonts;
            selectStyle.up = new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("blueup"), 10,10,10,10));
            selectStyle.down = new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("bluedown"), 10,10,10,10));
            selectStyle.disabled = new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("bluedown"), 10,10,10,10));
            selectStyle.pressedOffsetX = -1;
            selectStyle.pressedOffsetY = -1;
            changeTank = new TextButton("Main Menu", selectStyle);
            changeTank.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (isEnabled && !changeTank.isDisabled()) {

                        //go back to main menu
                        ScreenManager.setScreen(new MenuScreen());

                    }
                }
            });
            main.add(changeTank).align(Align.left);

            //add the replay button
            replay = new TextButton("Replay", selectStyle);
            replay.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if ( isEnabled && !replay.isDisabled()) {

                        //reset the tank live status
                        JSONArray hold = new JSONArray(Tanks.gameState.getPlayers()),
                                    newHolder = new JSONArray();
                        for(int i = 0; i < hold.length(); i++){
                            JSONObject ob = new JSONObject(hold.get(i).toString());
                            ob.put("status", true);
                            ob.put("score", 0);
                            newHolder.put(ob);
                        }

                        ScreenManager.setScreen(new MultiGameScreen());

                    }
                }
            });
            main.add(replay).align(Align.right).padLeft(main.getWidth()
                    - replay.getWidth() - changeTank.getWidth()
                    - paddingLeft - paddingRight);

            //setup actions
            this.positionAction.setPosition(0, Tanks.cameraHeight / 2 - main.getHeight() / 2);
            this.positionAction.setInterpolation(new Interpolation.SwingOut(1));
            this.positionAction.setDuration(1.1f);

            //hide the main table
            hide();

            MultiGameScreen.ResultPanel.addActor(main);

        }

        public void hide(){

            main.setVisible(false);

            main.setPosition(0, -Tanks.cameraHeight / 2);

        }

        //tmp variable for show
        private Timer.Task tmpShowTask = new Timer.Task() {
            @Override
            public void run() {
                enableMenuBtns();
            }
        };
        public void show(){

            //hide the in game settings display
            inGameSettings.hide();
            inGameSettings.deactivate();

            //disable buttons
            disableMenuBtns();
            //enable them after 1500 milli second
            MultiGameScreen.smallLoading.loaderState = LoaderState.RUN_ONCE;
            MultiGameScreen.smallLoading.runTimeOut = 1100;
            MultiGameScreen.smallLoading.show();
            //enable the menu buttons after 1100 millisecond
            Timer.schedule(tmpShowTask, 1.1f);

            main.setVisible(true);
            //add the position action
            positionAction.restart();
            main.addAction(positionAction);

            //clear the scroll table first
            scrollTable.clear();

            //define styles for list content
            Label.LabelStyle mainTxtStyle = new Label.LabelStyle();
            mainTxtStyle.font = MultiGameScreen.scrollFonts;
            mainTxtStyle.fontColor = Color.ORANGE;

            //add the main player score
            scrollTable.add(new Image(new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("dead")))))
                    .padLeft(paddingLeft).padBottom(paddingBottom);
            scrollTable.add(new Image(MultiGameScreen.Tanks.getRegion(inGametankType.toString())))
                    .padLeft(paddingLeft).padBottom(paddingBottom);
            scrollTable.add(new Label(
                    (Tanks.gameState.getUserName().equals(""))?
                            "Main Player" : Tanks.gameState.getUserName()
                    , mainTxtStyle))
                    .padLeft(paddingLeft).padBottom(paddingBottom);
            scrollTable.add(new Label(inGameScore+" points", mainTxtStyle))
                    .padLeft(paddingLeft).padBottom(paddingBottom);

            scrollTable.row();

            Label.LabelStyle txtStyle = new Label.LabelStyle();
            txtStyle.font = MultiGameScreen.scrollFonts;

            //add the tanks score by ranking them from the highest to lowest
            JSONArray hold = new JSONArray(Tanks.gameState.getPlayers());
            for(int i = 0; i < hold.length(); i++){
                JSONObject ob = new JSONObject(hold.get(i).toString());

                //add the image state
                if(ob.getBoolean("status")){
                    scrollTable.add(new Image(new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("alive")))))
                            .padLeft(paddingLeft).padBottom(paddingBottom);
                }else{
                    scrollTable.add(new Image(new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("dead")))))
                            .padLeft(paddingLeft).padBottom(paddingBottom);
                }

                scrollTable.add(new Image(MultiGameScreen.Tanks.getRegion(ob.getString("type"))))
                        .padLeft(paddingLeft).padBottom(paddingBottom);

                //add the name of the player
                scrollTable.add(new Label("Player "+ob.getInt("id"), txtStyle))
                        .padLeft(paddingLeft).padBottom(paddingBottom);

                //add the score
                scrollTable.add(new Label(ob.getInt("score")+" points", txtStyle))
                        .padLeft(paddingLeft).padBottom(paddingBottom);

                //add the difficulty
                if(ob.get("difficulty").equals(TankMode.TankModeType.EASY_MODE.toString())){
                    scrollTable.add(new Label("Difficulty: easy", txtStyle))
                            .padLeft(paddingLeft).padBottom(paddingBottom);
                }else if(ob.get("difficulty").equals(TankMode.TankModeType.MEDIUM_MODE.toString())){
                    scrollTable.add(new Label("Difficulty: medium", txtStyle))
                            .padLeft(paddingLeft).padBottom(paddingBottom);
                }else{
                    scrollTable.add(new Label("Difficulty: Hard", txtStyle))
                            .padLeft(paddingLeft).padBottom(paddingBottom);
                }

                //add a row
                scrollTable.row();
            }

        }

        private void addOtherUI(){

            scrollTable = new Table();
            ScrollPane scrollPane = new ScrollPane(scrollTable);
            scrollPane.setSize(main.getWidth() - paddingRight - paddingLeft, main.getHeight() / 2);

            main.add(scrollPane)
                    .width(main.getWidth() - paddingRight - paddingLeft)
                    .height(main.getHeight() / 2)
                    .colspan(5);
            main.row();

        }

        /**
         * disable the menu panel buttons
         */
        public void disableMenuBtns(){
            this.isEnabled = false;
        }
        /**
         * enable the menu panel buttons
         */
        public void enableMenuBtns(){
            this.isEnabled = true;
        }

    }

    public class InGameSettings{

        Button menuBtn;
        Table menuTable;
        Stack menuStack;

        Image bgCover;

        //padding between the tanks tables left and right
        float paddingLeft = 15, paddingRight = 15,
                paddingTop = 15, paddingBottom = 15;

        //position action
        MoveToAction positionAction = new MoveToAction();

        //clicking availability
        private boolean isEnabled = true;

        public InGameSettings(){

            //setup the actions
            positionAction.setPosition(Tanks.cameraWIDTH / 2 - (Tanks.cameraWIDTH / 2) / 2,
                    Tanks.cameraHeight / 2 - Tanks.cameraHeight / 4);
            positionAction.setInterpolation(new Interpolation.SwingOut(1));
            positionAction.setDuration(1.1f);

            //add the click menu button that triggers the display of the in game menu
            addMenuBtn();

            //add the in Game menu display
            addInGameDisplay();

            //hide the menu settings screen
            hide();

        }

        private void addMenuBtn(){

            Button.ButtonStyle btnStyle = new Button.ButtonStyle();
            Sprite btnSpriteup = new Sprite(MultiGameScreen.gameSkin.getRegion("pause")),
                    btnSpritedown = new Sprite(MultiGameScreen.gameSkin.getRegion("pause"));
            btnSpritedown.setAlpha(0.6f);
            btnStyle.up = new SpriteDrawable(btnSpriteup);
            btnStyle.down = new SpriteDrawable(btnSpritedown);
            btnStyle.checked = new SpriteDrawable(btnSpritedown);
            menuBtn = new Button(btnStyle);
            menuBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!menuBtn.isDisabled()) {
                        show();
                    }
                }
            });

            Image bgImage = new Image(new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("grayup"), 5, 5, 5, 5)));
            bgImage.setPosition(0, Tanks.cameraHeight - menuBtn.getHeight() - paddingTop - paddingBottom);
            bgImage.setSize(paddingLeft + paddingRight + menuBtn.getWidth(),
                    paddingBottom + paddingTop + menuBtn.getHeight());
            bgImage.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    if(isEnabled){
                        performClick(menuBtn);

                        //animate the click by switching to the checked state
                        menuBtn.setChecked(true);
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                menuBtn.setChecked(false);
                            }
                        }, 0.1f);
                    }

                }
            });

            menuBtn.setPosition(paddingLeft, bgImage.getY() + paddingBottom);

            MultiGameScreen.stage.addActor(bgImage);
            MultiGameScreen.stage.addActor(menuBtn);
        }

        private void addInGameDisplay(){

            //add a black background
            bgCover = new Image();
            bgCover.setDrawable(new SpriteDrawable(new Sprite(Assets.assetManager.get(Assets.GameFileNames.plainBlackString,
                    Texture.class))));
            bgCover.setSize(Tanks.cameraWIDTH, Tanks.cameraHeight);
            bgCover.setColor(0, 0, 0, 0.7f);
            bgCover.setPosition(0, 0);
            bgCover.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (isEnabled) {
                        hide();
                    }
                }
            });
            bgCover.setVisible(false);
            MultiGameScreen.stage.addActor(bgCover);

            //initiate the stack
            this.menuStack = new Stack();
            this.menuStack.setSize(Tanks.cameraWIDTH / 2, Tanks.cameraHeight / 2);
            this.menuStack.setPosition(Tanks.cameraWIDTH / 2 - (Tanks.cameraWIDTH / 2) / 2,
                                        Tanks.cameraHeight / 2 - Tanks.cameraHeight/4);

            //initiate the table
            menuTable = new Table();
            menuTable.setBackground(new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("paneloption"), 5, 5, 5, 5)));

            Label.LabelStyle titleStyle = new Label.LabelStyle();
            titleStyle.font = MultiGameScreen.titleFont;
            titleStyle.fontColor = Color.GRAY;
            Label title = new Label("Pause", titleStyle);
            menuTable.add(title).padBottom(paddingBottom).colspan(2);
            menuTable.row();

            //add buttons
            TextButton.TextButtonStyle selectStyle = new TextButton.TextButtonStyle();
            selectStyle.font = MultiGameScreen.defaultFonts;
            selectStyle.up = new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("blueup"), 10,10,10,10));
            selectStyle.down = new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("bluedown"), 10,10,10,10));
            selectStyle.disabled = new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("bluedown"), 10,10,10,10));
            selectStyle.pressedOffsetX = -1;
            selectStyle.pressedOffsetY = -1;
            TextButton resume = new TextButton("Resume", selectStyle);
            resume.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(isEnabled){
                        hide();
                    }
                }
            });
            TextButton exit = new TextButton("Main Menu", selectStyle);
            exit.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(isEnabled){
                        ScreenManager.setScreen(new MenuScreen());
                    }
                }
            });

            //add sound mute option
            Label.LabelStyle soundStyle = new Label.LabelStyle();
            soundStyle.font = MultiGameScreen.defaultFonts;
            soundStyle.fontColor = Color.GRAY;
            Label sound = new Label("Sound", soundStyle);
            CheckBox.CheckBoxStyle soundCheckStyle = new CheckBox.CheckBoxStyle();
            soundCheckStyle.font = MultiGameScreen.defaultFonts;
            soundCheckStyle.up = new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("on"), 10,10,10,10));
            soundCheckStyle.checked = new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("off"), 10,10,10,10));
            final CheckBox soundCheck = new CheckBox("", soundCheckStyle);
            if(Tanks.gameState.getSoundState()){
                soundCheck.setChecked(true);
            }else{
                soundCheck.setChecked(false);
            }
            soundCheck.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(isEnabled){
                        if (soundCheck.isChecked()) {
                            Tanks.gameState.setSound(true);
                        } else {
                            Tanks.gameState.setSound(false);
                        }
                        MultiGameScreen.soundeffect.refresh();
                    }
                }
            });

            //add the table buttons
            menuTable.add(resume).padBottom(paddingBottom*1.2f).colspan(2);
            menuTable.row();
            menuTable.add(sound).padRight(paddingRight).padBottom(paddingBottom);
            menuTable.add(soundCheck).padBottom(paddingBottom).width(84).height(48);
            menuTable.row();
            menuTable.add(exit).colspan(2).padTop(paddingTop);

            this.menuStack.add(this.menuTable);
            MultiGameScreen.stage.addActor(this.menuStack);

        }

        private void performClick(Actor actor) {
            Array<EventListener> listeners = actor.getListeners();
            for(int i=0;i<listeners.size;i++)
            {
                if(listeners.get(i) instanceof ClickListener){
                    ((ClickListener)listeners.get(i)).clicked(null, 0, 0);
                }
            }
        }

        public void AutoperformClickPause() {
            Array<EventListener> listeners = menuBtn.getListeners();
            for(int i=0;i<listeners.size;i++)
            {
                if(listeners.get(i) instanceof ClickListener){
                    ((ClickListener)listeners.get(i)).clicked(null, 0, 0);
                }
            }
        }

        public void hide(){
            //menu button availability
            activate();

            //menu panel visibility
            this.menuStack.setVisible(false);
            bgCover.setVisible(false);

            //set the position and size of the menu stack
            this.menuStack.setPosition(Tanks.cameraWIDTH / 2 - (Tanks.cameraWIDTH / 2) / 2,
                                        - Tanks.cameraHeight / 2);

            MultiGameScreen.gameInterface.resume();

        }

        //show timer
        private Timer.Task tmpShowTimer = new Timer.Task() {
            @Override
            public void run() {
                enableMenuBtns();
            }
        };
        public void show(){
            //menu button availability
            deactivate();

            //menu panel visibility
            this.menuStack.setVisible(true);
            bgCover.setVisible(true);

            //animate the menu stack
            //animate the position
            positionAction.restart();
            this.menuStack.addAction(positionAction);

            //disable buttons
            disableMenuBtns();
            //enable them after 1500 milli second
            MultiGameScreen.smallLoading.loaderState = LoaderState.RUN_ONCE;
            MultiGameScreen.smallLoading.runTimeOut = 1100;
            MultiGameScreen.smallLoading.show();
            //enable the menu buttons after 1100 millisecond
            Timer.schedule(tmpShowTimer, 1.1f);

            MultiGameScreen.gameInterface.pause();

        }

        /**
         * disable the menu panel buttons
         */
        public void disableMenuBtns(){
            this.isEnabled = false;
        }
        /**
         * enable the menu panel buttons
         */
        public void enableMenuBtns(){
            this.isEnabled = true;
        }

        public void deactivate(){
            menuBtn.setDisabled(true);
        }
        public void activate(){
            menuBtn.setDisabled(false);
        }

    }

    /**
     * update the in game screen panels
     */
    public void update(float delta){}

}
