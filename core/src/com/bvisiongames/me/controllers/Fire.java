package com.bvisiongames.me.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.entity.EntitManager;
import com.bvisiongames.me.Weapons.Weapon;
import com.bvisiongames.me.Weapons.EntitysWeaponManger;
import com.bvisiongames.me.screen.MultiGameScreen;

/**
 * Created by Sultan on 6/30/2015.
 */
public class Fire {

    private TextButton fire;
    private EntitysWeaponManger weaponManger;

    private Vector2 pos;

    public Fire(){

        //add fire button
        addFireButton();

        //add the Weapon Manager
        addWeaponManager();

    }

    /**
     * adds the weapon manager that displays the weapon button during the game.
     */
    private void addWeaponManager(){
        weaponManger = new EntitysWeaponManger(fire, true);

        //add the normal bullet if checked
        if(Tanks.gameState.weapons.getNormalBulletState()){
            weaponManger.setBulletsBtn(true, false);
            weaponManger.addBullet(Weapon.BULLETSTYPE.BULLET1);
            weaponManger.bullet1.performClick();
        }

        //add the electric bullet if checked
        if(Tanks.gameState.weapons.getElectricBulletState()){
            weaponManger.setBulletsBtn(false, true);
            for(int i = 0; i < Tanks.gameState.weapons.getElectricBulletTotal(); i++){
                weaponManger.addBullet(Weapon.BULLETSTYPE.ELECTRICBULLET);
            }
            weaponManger.electricBullet.performClick();
        }

    }

    /**
     * add the fire button with the animating reload circle
     */
    private void addFireButton(){
        //initialize the button
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = new NinePatchDrawable(new NinePatch(MultiGameScreen.controllersSkin.getRegion("fireup"), 5,5,5,5));
        textButtonStyle.down = new NinePatchDrawable(new NinePatch(MultiGameScreen.controllersSkin.getRegion("firedown"),5,5,5,5));
        textButtonStyle.disabled = new NinePatchDrawable(new NinePatch(MultiGameScreen.controllersSkin.getRegion("fireDisabled"),5,5,5,5));
        textButtonStyle.font = MultiGameScreen.defaultFonts;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.disabledFontColor = Color.GRAY;
        textButtonStyle.up.setLeftWidth(20);
        textButtonStyle.up.setRightWidth(20);
        textButtonStyle.up.setTopHeight(10);
        textButtonStyle.up.setBottomHeight(10);
        fire = new TextButton("Fire", textButtonStyle);
        pos = new Vector2(Tanks.cameraWIDTH - fire.getWidth(), fire.getHeight());
        fire.setPosition(pos.x - fire.getWidth() / 2, pos.y);

        fire.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!fire.isDisabled() && EntitManager.tank != null
                        && !EntitManager.tank.tankElectricShocked.IsElectricallyShocked
                        && weaponManger.isReadyToShoot()) {
                    weaponManger.shoot(EntitManager.tank);
                }

            }
        });

        MultiGameScreen.stage.addActor(fire);
    }

    /**
     * update and render methods.
     */
    public void update(){

        weaponManger.update();

    }
    public void render(SpriteBatch spriteBatch){
        weaponManger.render(spriteBatch);
    }

    /**
     * hide nad show methods.
     */
    public void hide(){
        fire.setVisible(false);
        weaponManger.hide();
    }
    public void show(){
        fire.setVisible(true);
        weaponManger.show();
    }

}
