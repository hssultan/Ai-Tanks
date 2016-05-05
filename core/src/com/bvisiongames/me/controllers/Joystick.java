package com.bvisiongames.me.controllers;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.bvisiongames.me.screen.MultiGameScreen;

/**
 * Created by Sultan on 6/23/2015.
 */
public class Joystick{

    private Sprite knob,
                    KnobHolder;

    //Knob Holder properties
    private Vector2 vector = new Vector2(0,0),
    //max Knob Holder width and height
                maxKnobVec = new Vector2(0, 0);
    ;
    Touchpad touchpad;

    public Joystick(){

        knob = new Sprite(MultiGameScreen.controllersSkin.getRegion("controller_knob"));
        KnobHolder = new Sprite(MultiGameScreen.controllersSkin.getRegion("controller_surround"));

        //set knob holder width and height
        this.maxKnobVec.set(KnobHolder.getWidth(), KnobHolder.getHeight());

        Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();
        style.background = new SpriteDrawable(KnobHolder);
        style.knob = new SpriteDrawable(knob);
        touchpad = new Touchpad(10, style);
        touchpad.setPosition(0, 0);

        MultiGameScreen.stage.addActor(touchpad);

    }

    public void hide(){
        touchpad.setVisible(false);
    }
    public void show(){
        touchpad.setVisible(true);
    }

    public void update(){
        vector.set(touchpad.getKnobPercentX()*(KnobHolder.getWidth()/2),
                    touchpad.getKnobPercentY()*(KnobHolder.getHeight()/2));
    }
    //this method will return the direction the vehicle must be moving in
    public Vector2 GetVector(){
        return vector;
    }
    //this method returns the height and width of the knobholder
    public Vector2 GetMaxVectorDimensions(){
        return this.maxKnobVec;
    }

}
