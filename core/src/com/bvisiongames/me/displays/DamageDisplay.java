package com.bvisiongames.me.displays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Tweenanimations.ProgressBarAnimation;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.Assets;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

/**
 * Created by Sultan on 8/11/2015.
 */
public class DamageDisplay {

    private ProgressBar progressBar;
    private Label progressLabel;

    public DamageDisplay(){

        //create the progress bar
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        Sprite tmpImg = new Sprite(Assets.assetManager.get(Assets.GameFileNames.plainBlackString, Texture.class));
        tmpImg.setAlpha(0.7f);
        progressBarStyle.background = new SpriteDrawable(tmpImg);
        progressBarStyle.background.setLeftWidth(10);
        progressBarStyle.background.setRightWidth(10);
        progressBarStyle.knob = new SpriteDrawable(new Sprite(Assets.assetManager.get(Assets.GameFileNames.plainGreenString, Texture.class)));
        progressBarStyle.knobBefore = new SpriteDrawable(new Sprite(Assets.assetManager.get(Assets.GameFileNames.plainGreenString, Texture.class)));
        progressBar = new ProgressBar(0, 100, 1, false, progressBarStyle);
        progressBar.setValue(1);
        progressBar.setSize(300, 30);
        progressBar.setPosition(Tanks.cameraWIDTH / 2 - progressBar.getWidth() / 2, Tanks.cameraHeight - progressBar.getHeight() - 50);

        //create the health label
        Label.LabelStyle progressLabelStyle = new Label.LabelStyle();
        progressLabelStyle.font = MultiGameScreen.smallSizeFont;
        progressLabelStyle.fontColor = Color.WHITE;
        progressLabelStyle.background = new SpriteDrawable(tmpImg);
        progressLabelStyle.background.setTopHeight(9);
        progressLabelStyle.background.setBottomHeight(10);
        progressLabelStyle.background.setLeftWidth(10);
        progressLabel = new Label("Health", progressLabelStyle);
        progressLabel.setPosition(progressBar.getX() - progressLabel.getWidth(), progressBar.getY() - 11);

        MultiGameScreen.stage.addActor(progressBar);
        MultiGameScreen.stage.addActor(progressLabel);

        tmpImg.getTexture().dispose();

    }
    public void hide(){

        progressBar.setVisible(false);
        progressLabel.setVisible(false);

    }
    public void show(){

        progressBar.setVisible(true);
        progressLabel.setVisible(true);

    }
    //small variables
    private TweenCallback updateHealthCallBack = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            if (type == TweenCallback.END) {
                MultiGameScreen.inGameScreens.ClientGameOver();
            }
        }
    };
    public void updateHealth(int lives){

        if(lives > 0){
            Tween.to(progressBar, ProgressBarAnimation.NUMBER, 0.5f)
                    .target(lives)
                    .start(MultiGameScreen.tweenManager);
        }else{
            Tween.to(progressBar, ProgressBarAnimation.NUMBER, 0.5f)
                    .target(lives)
                    .setCallbackTriggers(TweenCallback.END)
                    .setCallback(updateHealthCallBack)
                    .start(MultiGameScreen.tweenManager);
        }

    }
    public void resetDamage(){
        Tween.to(progressBar, ProgressBarAnimation.NUMBER, 0.7f)
                .target(100)
                .start(MultiGameScreen.tweenManager);
    }

}
