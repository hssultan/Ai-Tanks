package com.bvisiongames.me.vidAnimations;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Tweenanimations.Box2dActorAnimation;
import com.bvisiongames.me.screen.MenuScreen;
import com.bvisiongames.me.screen.ScreenManager;
import com.bvisiongames.me.screen.SplashScreen;
import com.bvisiongames.me.settings.Assets;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

/**
 * Created by Sultan on 8/15/2015.
 */
public class IntroAnimation {

    private Label title;

    boolean loading = false;

    public IntroAnimation(){

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = SplashScreen.titleFont;
        title = new Label("Tanks", labelStyle);
        title.setPosition(Tanks.cameraWIDTH/2 - title.getWidth()/2, Tanks.cameraHeight/2 - title.getHeight()/2);
        title.setColor(title.getColor().r, title.getColor().g, title.getColor().b, 0);

        SplashScreen.stage.addActor(title);

    }
    public void initiateBegin(){

        Tween.to(title, Box2dActorAnimation.ALPHA, 2)
                .target(1)
                .setCallbackTriggers(TweenCallback.END)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == TweenCallback.END) {
                            loading = true;
                        }
                    }
                })
                .start(SplashScreen.tweenManager);

    }
    public void initiateEnd(){

        Tween.to(title, Box2dActorAnimation.ALPHA, 2.5f)
                .target(0)
                .setCallbackTriggers(TweenCallback.END)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == TweenCallback.END) {
                            ScreenManager.setScreen(new MenuScreen());
                        }
                    }
                })
                .start(SplashScreen.tweenManager);

    }

    boolean isAssetLoaded = false,  //this tells if the asset manager has done loading
            triggered = true;       //this tells if the next method that will fade away the title to procceed

    public void update(){

        if(Assets.assetManager.update()){
            Assets.isMenuLoaded = true;
            isAssetLoaded = true;
        }

        if(isAssetLoaded && loading && triggered){
            triggered  = false;
            initiateEnd();
        }

    }

}
