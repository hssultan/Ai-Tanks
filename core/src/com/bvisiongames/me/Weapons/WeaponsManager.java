package com.bvisiongames.me.Weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.screen.MultiGameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahzji_000 on 2/12/2016.
 */
public class WeaponsManager {

    //list holding all the weapons
    public List<Weapon> weaponList = new ArrayList<Weapon>();


    //setters
    /**
     * add a bullet
     * @param bulletType
     * the bullet type to be added
     * @param owner
     * the owner of this bullet
     */
    public synchronized void addBullet(Weapon.BULLETSTYPE bulletType, Tank owner){

        //reference to the bullet to be used
        Weapon bullet = null;

        int length = this.weaponList.size();
        for(int i = 0; i < length; i++){

            if(!this.weaponList.get(i).isStatus()
                    && this.weaponList.get(i).bulletstype == bulletType){
                bullet = this.weaponList.get(i);
            }

        }

        //if the bullet is not null
        if(bullet != null){
            bullet.reset(owner);
        }else{
            switch (bulletType){
                case ELECTRICBULLET:
                    this.weaponList.add(new ElectricBullet(owner));
                    break;
                case BULLET1:
                    this.weaponList.add(new Bullet(owner));
                    break;
            }
        }

    }
    //end of setters


    /**
     * update
     */
    public void update(){

        int length = this.weaponList.size();
        for(int i = 0; i < length; i++){

            if(this.weaponList.get(i).isStatus()){
                this.weaponList.get(i).update();
            }

        }

    }

    /**
     * render
     * @param batch
     */
    public void render(SpriteBatch batch){

        int length = this.weaponList.size();
        for(int i = 0; i < length; i++){

            if(this.weaponList.get(i).isStatus()){
                this.weaponList.get(i).render(batch);
            }

        }

    }

    /**
     * dispose
     */
    public void dispose(){

        this.weaponList.clear();

    }

}
