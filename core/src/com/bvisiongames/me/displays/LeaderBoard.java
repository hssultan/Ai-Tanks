package com.bvisiongames.me.displays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.bvisiongames.me.JSON.JSONArray;
import com.bvisiongames.me.JSON.JSONObject;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Tweenanimations.Box2dActorAnimation;
import com.bvisiongames.me.screen.MultiGameScreen;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

/**
 * Created by Sultan on 9/10/2015.
 */
public class LeaderBoard {

    //top variables
    private Image top, topIcon;
    private Label topText;

    //list variables
    private Table listTable, listHolder;
    private ScrollPane listScroll;

    //properties of the leaderboard
    private boolean isDisplayed = false,
                    running = false;

    public LeaderBoard(){

        createTop();
        createList();

        //add the ui to stage
        MultiGameScreen.stage.addActor(listTable);

        MultiGameScreen.stage.addActor(top);
        MultiGameScreen.stage.addActor(topIcon);
        MultiGameScreen.stage.addActor(topText);

    }
    private void createTop(){

        top = new Image(new NinePatch(MultiGameScreen.gameSkin.getRegion("grayup"), 10,10,10,10));
        top.setColor(top.getColor().r, top.getColor().g, top.getColor().b, 0.8f);
        topIcon = new Image(new TextureRegion(MultiGameScreen.gameSkin.getRegion("leaderboard")));
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = MultiGameScreen.defaultFonts;
        style.fontColor = Color.GRAY;
        topText = new Label("LeaderBoard", style);

        //set positions and dimensions
        top.setSize(topIcon.getWidth() + 20, topIcon.getHeight() + 20);
        top.setPosition(Tanks.cameraWIDTH - top.getWidth() - 10, Tanks.cameraHeight - top.getHeight() - 10);

        topIcon.setPosition(top.getX() + 10, top.getY() + 10);

        topText.setPosition(top.getX() + top.getWidth() - topText.getWidth() - 10, top.getY() + 10);
        topText.setVisible(false);

        //set listeners
        topIcon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {if(!running){if (isDisplayed) {hide();} else {display();} running = true;}}
        });
        topText.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {if(!running){if (isDisplayed) {hide();} else {display();} running = true;}}
        });

    }
    private void createList(){

        listTable = new Table();
        Sprite img = new Sprite(new Texture("backgrounds/black.jpg"));
        img.setAlpha(0.7f);
        listTable.setBackground(new SpriteDrawable(img));
        listTable.setSize(top.getWidth() + topText.getWidth(), 0);
        listTable.setPosition(top.getX() - topText.getWidth() - 5, top.getY());

        listHolder = new Table();
        listHolder.setSize(top.getWidth() + topText.getWidth(), 150);

        listScroll = new ScrollPane(listHolder);
        listScroll.setSize(top.getWidth() + topText.getWidth(), 150);

        listTable.add(listScroll);

    }
    private void display(){
        isDisplayed = true;
        //top background
        Tween.to(top, Box2dActorAnimation.POSITION_XY, 1f)
                .target(top.getX() - topText.getWidth() - 5, top.getY())
                .setCallbackTriggers(TweenCallback.END)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if(type == TweenCallback.END){
                            topText.setVisible(true);
                            //animate the listTable
                            showList();
                        }
                    }
                })
                .start(MultiGameScreen.tweenManager);
        Tween.to(top, Box2dActorAnimation.WIDTH, 1f)
                .target(top.getWidth() + topText.getWidth())
                .start(MultiGameScreen.tweenManager);

        //top icon
        Tween.to(topIcon, Box2dActorAnimation.POSITION_XY, 1f)
                .target(top.getX() - topText.getWidth() - 5 + 10, topIcon.getY())
                .start(MultiGameScreen.tweenManager);

    }
    private void hide(){
        isDisplayed = false;
        hideList();
    }

    private void showList(){

        Tween.to(listTable, Box2dActorAnimation.HEIGHT, 1f)
                .target(150)
                .setCallbackTriggers(TweenCallback.END)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if(type == TweenCallback.END){
                            running = false;
                        }
                    }
                })
                .start(MultiGameScreen.tweenManager);
        Tween.to(listTable, Box2dActorAnimation.POSITION_XY, 1f)
                .target(listTable.getX(), listTable.getY() - 150)
                .start(MultiGameScreen.tweenManager);

    }
    private void hideList(){

        Tween.to(listTable, Box2dActorAnimation.HEIGHT, 1f)
                .target(0)
                .setCallbackTriggers(TweenCallback.END)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == TweenCallback.END) {
                            //top background
                            Tween.to(top, Box2dActorAnimation.POSITION_XY, 1f)
                                    .target(top.getX() + topText.getWidth() + 5, top.getY())
                                    .setCallbackTriggers(TweenCallback.BEGIN)
                                    .setCallback(new TweenCallback() {
                                        @Override
                                        public void onEvent(int type, BaseTween<?> source) {
                                            if (type == TweenCallback.BEGIN) {
                                                topText.setVisible(false);
                                                running = false;
                                            }
                                        }
                                    })
                                    .start(MultiGameScreen.tweenManager);
                            Tween.to(top, Box2dActorAnimation.WIDTH, 1f)
                                    .target(top.getWidth() - topText.getWidth())
                                    .start(MultiGameScreen.tweenManager);

                            //top icon
                            Tween.to(topIcon, Box2dActorAnimation.POSITION_XY, 1f)
                                    .target(top.getX() + topText.getWidth() + 5 + 10, topIcon.getY())
                                    .start(MultiGameScreen.tweenManager);
                        }
                    }
                })
                .start(MultiGameScreen.tweenManager);

        Tween.to(listTable, Box2dActorAnimation.POSITION_XY, 1f)
                .target(listTable.getX(), listTable.getY() + 150)
                .start(MultiGameScreen.tweenManager);

    }

    JSONObject msg;
    public void onMessage(String message){

        //parse the message
        this.msg = new JSONObject(message);
        if(!this.msg.isNull("LScores")){

            //reset the table contents
            listHolder.reset();

            JSONArray scores = new JSONArray(this.msg.get("LScores").toString());

            for(int i = 0; i < scores.length(); i++ ){

                JSONObject obj = new JSONObject(scores.get(i).toString());

                if(obj.getInt("id") == Tanks.gameState.getUserID()){
                    Label.LabelStyle style = new Label.LabelStyle();
                    style.font = MultiGameScreen.smallSizeFont;
                    style.fontColor = Color.ORANGE;
                    Label name = new Label(obj.getString("name"), style);
                    name.setWrap(true);
                    name.setEllipsis(true);
                    Label score = new Label(Integer.toString(obj.getInt("score")), style);
                    listHolder.add(name).width(100).padRight(20).align(Align.center);
                    listHolder.add(score);
                    listHolder.row();
                }else{
                    Label.LabelStyle style = new Label.LabelStyle();
                    style.font = MultiGameScreen.smallSizeFont;
                    style.fontColor = Color.GRAY;
                    Label name = new Label(obj.getString("name"), style);
                    name.setWrap(true);
                    name.setEllipsis(true);
                    Label score = new Label(Integer.toString(obj.getInt("score")), style);
                    listHolder.add(name).width(100).padRight(20).align(Align.center);
                    listHolder.add(score);
                    listHolder.row();
                }

                obj = null;

            }

            scores = null;

        }

    }

}
