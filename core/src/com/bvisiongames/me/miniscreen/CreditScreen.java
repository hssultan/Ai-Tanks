package com.bvisiongames.me.miniscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Tweenanimations.Box2dActorAnimation;
import com.bvisiongames.me.screen.MenuScreen;
import com.bvisiongames.me.settings.Assets;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

/**
 * Created by Sultan on 9/16/2015.
 */
public class CreditScreen {

    private Image bg, panel;
    private Label title;
    private ScrollPane container;
    private Table containerBox;

    //properties of panel box
    private float paddingTop = 10, paddingBottom = 20,
            paddingLeft = 10, paddingRight = 10;

    //buttons
    private TextButton cancel;

    private boolean isVisible = false;

    public CreditScreen(){

        //background image
        bg = new Image(new TextureRegion(new Texture("backgrounds/black.jpg")));
        bg.setColor(bg.getColor().r, bg.getColor().g, bg.getColor().b, 0.7f);
        bg.setSize(Tanks.cameraWIDTH, Tanks.cameraHeight);
        bg.setPosition(0, 0);
        bg.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Triggered();
            }
        });

        //background panel
        panel = new Image(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("white_panel"), 5,5,5,5));
        panel.setSize(0, 0);
        panel.setPosition(Tanks.cameraWIDTH/2, Tanks.cameraHeight/2);

        //title
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = MenuScreen.playerFnt;
        title = new Label("Credits", titleStyle);
        title.setPosition(Tanks.cameraWIDTH / 6 + (Tanks.cameraWIDTH/1.4f)/2 - title.getWidth()/2,
                            Tanks.cameraHeight / 5 + Tanks.cameraHeight/1.7f - title.getHeight() - paddingTop*3 );

        //add the apply and cancel buttons
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = MenuScreen.defaultFonts;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("blueup"), 5,5,5,5));
        buttonStyle.up.setLeftWidth(15);
        buttonStyle.up.setRightWidth(15);
        buttonStyle.up.setBottomHeight(10);
        buttonStyle.up.setTopHeight(10);
        buttonStyle.down = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown"), 5,5,5,5));
        buttonStyle.down.setLeftWidth(15);
        buttonStyle.down.setRightWidth(15);
        buttonStyle.down.setBottomHeight(10);
        buttonStyle.down.setTopHeight(10);
        buttonStyle.pressedOffsetX = -1;
        buttonStyle.pressedOffsetY = -1;
        buttonStyle.downFontColor = new Color(0.4f, 0.4f, 0.4f, 1);
        cancel = new TextButton("cancel", buttonStyle);
        cancel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Triggered();
            }
        });
        cancel.setPosition(Tanks.cameraWIDTH/6 + paddingLeft*3, Tanks.cameraHeight/5 + paddingBottom*2);

        //the scroll container
        containerBox = new Table();
        container = new ScrollPane(containerBox);
        container.setPosition(Tanks.cameraWIDTH/6, Tanks.cameraHeight/5 + cancel.getHeight() + paddingBottom*2);
        container.setSize(Tanks.cameraWIDTH/1.4f,
                Tanks.cameraHeight/1.7f - cancel.getHeight() - title.getHeight() - paddingBottom - paddingTop*4);

        //add to containerBox
        addToContainerBox();

        //hide everything
        hide();
        hideContent();

        //add actors
        MenuScreen.miniscreenStage.addActor(bg);
        MenuScreen.miniscreenStage.addActor(panel);
        MenuScreen.miniscreenStage.addActor(title);
        MenuScreen.miniscreenStage.addActor(container);
        MenuScreen.miniscreenStage.addActor(cancel);

    }

    public void render(SpriteBatch batch){}

    private void addToContainerBox(){

        //add to containerBox
        Label.LabelStyle linkStyle = new Label.LabelStyle();
        linkStyle.font = MenuScreen.defaultFonts;
        linkStyle.fontColor = Color.BLUE;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = MenuScreen.defaultFonts;
        labelStyle.fontColor = Color.GRAY;

        containerBox.align(Align.left)
                .padLeft(paddingLeft*3).padTop(paddingTop).padBottom(paddingBottom*2);
        containerBox.add(new Label("source 1", labelStyle))
                .padBottom(paddingBottom * 20);
        containerBox.row();
        containerBox.add(new Label("source 2", labelStyle));

    }

    public void Triggered(){

        if(isVisible){

            animateHide();

        }else{

            animateShow();

        }

    }

    public void dispose(){

    }

    private void hide(){

        isVisible = false;
        bg.setVisible(false);
        panel.setVisible(false);

    }
    private void show(){

        isVisible = true;
        bg.setVisible(true);
        panel.setVisible(true);

    }

    private void hideContent(){

        title.setVisible(false);
        container.setVisible(false);
        cancel.setVisible(false);

    }
    private void showContent() {

        title.setVisible(true);
        container.setVisible(true);
        cancel.setVisible(true);

    }

    //tmp variable for animate hide
    private TweenCallback animateHideCallback1 = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            if (type == TweenCallback.BEGIN) {
                hideContent();
            }
        }
    },
        animateHideCallback2 = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                if (type == TweenCallback.END) {
                    hide();
                }
            }
    };
    private void animateHide(){

        Tween.to(panel, Box2dActorAnimation.ALLDIMEN, 0.5f)
                .target(0, 0)
                .setCallbackTriggers(TweenCallback.BEGIN)
                .setCallback(animateHideCallback1)
                .start(MenuScreen.tweenManager);
        Tween.to(panel, Box2dActorAnimation.POSITION_XY, 0.5f)
                .target(Tanks.cameraWIDTH / 2, Tanks.cameraHeight / 2)
                .setCallbackTriggers(TweenCallback.END)
                .setCallback(animateHideCallback2)
                .start(MenuScreen.tweenManager);

    }

    //tmp variables for animate show
    private TweenCallback animateShowCallback1 = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            if (type == TweenCallback.BEGIN) {
                show();
            }
        }
    }, animateShowCallback2 = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            if (type == TweenCallback.END) {
                showContent();
            }
        }
    };
    private void animateShow(){

        Tween.to(panel, Box2dActorAnimation.POSITION_XY, 0.5f)
                .target(Tanks.cameraWIDTH / 6, Tanks.cameraHeight / 5)
                .setCallbackTriggers(TweenCallback.BEGIN)
                .setCallback(animateShowCallback1)
                .start(MenuScreen.tweenManager);
        Tween.to(panel, Box2dActorAnimation.ALLDIMEN, 0.5f)
                .target(Tanks.cameraWIDTH / 1.4f, Tanks.cameraHeight / 1.7f)
                .setCallbackTriggers(TweenCallback.END)
                .setCallback(animateShowCallback2)
                .start(MenuScreen.tweenManager);

    }

}
