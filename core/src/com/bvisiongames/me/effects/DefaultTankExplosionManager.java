package com.bvisiongames.me.effects;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.settings.GeneralMethods;
import java.util.ArrayList;
import java.util.List;
import box2dLight.PointLight;

/**
 * Created by ahzji_000 on 2/7/2016.
 */
public class DefaultTankExplosionManager {

    //file handlers
    private FileHandle tankExplosionEffect, tankExplosionImg,
                        bulletExplosionEffect, bulletExplosionImg;

    //list of tank explosions
    public List<TankExplosionEntity> tankExplosionEntities = new ArrayList<TankExplosionEntity>();
    //list of bullet explosions
    public List<TankBulletExplosionEntity> bulletExplosionEntities = new ArrayList<TankBulletExplosionEntity>();

    /**
     * initiator.
     * @param tankExplosionEffect
     * @param tankExplosionImg
     */
    public DefaultTankExplosionManager(FileHandle tankExplosionEffect, FileHandle tankExplosionImg,
                                       FileHandle bulletExplosionEffect, FileHandle bulletExplosionImg){

        this.tankExplosionEffect = tankExplosionEffect;
        this.tankExplosionImg = tankExplosionImg;
        this.bulletExplosionEffect = bulletExplosionEffect;
        this.bulletExplosionImg = bulletExplosionImg;

    }


    /**
     * update method
     * @param delta
     */
    public void update(float delta){

        //update the tank explosion destroyed entities
        int length = this.tankExplosionEntities.size();
        for(int i = 0; i < length; i++){
            if(this.tankExplosionEntities.get(i).isVisible){
                this.tankExplosionEntities.get(i).update(delta);
            }
        }

        //update the bullets explosion entities
        length = this.bulletExplosionEntities.size();
        for(int i = 0; i < length; i++){
            if(this.bulletExplosionEntities.get(i).isVisible){
                this.bulletExplosionEntities.get(i).update(delta);
            }
        }

    }

    /**
     * render method
     * @param batch
     */
    public void render(SpriteBatch batch){

        int length = this.tankExplosionEntities.size();
        for(int i = 0; i < length; i++){
            if(this.tankExplosionEntities.get(i).isVisible){
                this.tankExplosionEntities.get(i).render(batch);
            }
        }

        //render the bullets explosion entities
        length = this.bulletExplosionEntities.size();
        for(int i = 0; i < length; i++){
            if(this.bulletExplosionEntities.get(i).isVisible){
                this.bulletExplosionEntities.get(i).render(batch);
            }
        }

    }

    /**
     * dispose
     */
    public void dispose(){

        int length = this.tankExplosionEntities.size();
        for(int i = 0; i < length; i++){
            this.tankExplosionEntities.get(i).dispose();
        }

        //render the bullets explosion entities
        length = this.bulletExplosionEntities.size();
        for(int i = 0; i < length; i++){
            this.bulletExplosionEntities.get(i).dispose();
        }

        //clear all the lists
        this.tankExplosionEntities.clear();
        this.bulletExplosionEntities.clear();

    }


    //setters
    /**
     * add more tank explosion entities to the list.
     */
    public void addExplosionEffect(){
        //add the explosion destroyed
        this.tankExplosionEntities.add(new TankExplosionEntity());
        //add two explosions per tank entity for bullets
        this.bulletExplosionEntities.add(new TankBulletExplosionEntity());
        this.bulletExplosionEntities.add(new TankBulletExplosionEntity());
    }
    /**
     * activate an invisible tank explosion entity (destroyed).
     * @param position
     */
    public void activateExplosion(Vector2 position){
        int length = this.tankExplosionEntities.size();
        for(int i = 0; i < length; i++){
            if(!this.tankExplosionEntities.get(i).isVisible){
                this.tankExplosionEntities.get(i).activate(position);
                break;
            }
        }
    }
    /**
     * activate an invisible bullet explosion.
     * @param position
     * the position where the explosion happened.
     * @param direction
     * the direction from where the bullet came from.
     */
    public void activateBulletExplosion(Vector2 position, Vector2 direction){
        int length = this.bulletExplosionEntities.size();
        for(int i = 0; i < length; i++){
            if(!this.bulletExplosionEntities.get(i).isVisible){
                this.bulletExplosionEntities.get(i).activate(position, direction);
                break;
            }
        }
    }
    //end of setters


    //getter

    //end of getter


    /**
     * class of tank explosion entities (destroyed)
     */
    private class TankExplosionEntity{

        //particle effect
        private ParticleEffect particleEffect = new ParticleEffect();

        //add the light from explosion if at night
        private PointLight pointLight;

        //visibility
        public boolean isVisible = false;

        /**
         * initiator.
         */
        public TankExplosionEntity(){

            //add the particle effect
            this.particleEffect.load(DefaultTankExplosionManager.this.tankExplosionEffect,
                    DefaultTankExplosionManager.this.tankExplosionImg);
            this.particleEffect.start();

            //add the point light explosion
            this.pointLight = new PointLight(MultiGameScreen.rayHandler,
                                            300, Color.WHITE, 30, 0, 0);
            this.pointLight.setContactFilter(
                    (short) ConstantVariables.Box2dVariables.FLASH_LIGHT,
                    (short) ConstantVariables.Box2dVariables.SENSOR,
                    (short) 1);
            this.pointLight.setSoftnessLength(0.5f);
            this.pointLight.setActive(false);

        }

        /**
         * activate this effect at a location.
         * @param position
         */
        public void activate(Vector2 position){
            isVisible = true;
            this.particleEffect.reset();
            this.particleEffect.setPosition(position.x, position.y);
            if(MultiGameScreen.weather.dayState.daystate == MultiGameScreen.DAYSTATE.NIGHT){
                this.pointLight.setActive(true);
                this.pointLight.setPosition(GeneralMethods.convertFromPixelsToBody(position));
            }

        }

        /**
         * update method
         * @param delta
         */
        public void update(float delta){
            if(isVisible){
                //update the particle effect
                this.particleEffect.update(delta);
                //when particle effect is done then hide it
                if(this.particleEffect.isComplete()){
                    //deactivate the point light
                    this.pointLight.setActive(false);
                    isVisible = false;
                }
            }
        }

        /**
         * render method
         * @param batch
         */
        public void render(SpriteBatch batch){
            if(isVisible){
                this.particleEffect.draw(batch);
            }
        }

        /**
         * dispose
         */
        public void dispose(){
            this.particleEffect.dispose();
        }

    }

    /**
     * class of tank explosion bullets
     */
    private class TankBulletExplosionEntity{

        //particle effect
        private ParticleEffect particleEffect = new ParticleEffect();

        //visibility
        public boolean isVisible = false;

        //position
        private Vector2 position = new Vector2(0, 0);

        //direction
        private Vector2 direction = new Vector2(0, 0);

        /**
         * initiator.
         */
        public TankBulletExplosionEntity(){

            this.particleEffect.load(DefaultTankExplosionManager.this.bulletExplosionEffect,
                    DefaultTankExplosionManager.this.bulletExplosionImg);
            this.particleEffect.start();

        }

        /**
         * activate this effect at a location.
         * @param position
         * @param direction
         */
        public void activate(Vector2 position, Vector2 direction){

            this.position.set(position);
            isVisible = true;
            this.particleEffect.reset();
            this.particleEffect.setPosition(position.x, position.y);

        }

        /**
         * update method
         * @param delta
         */
        public void update(float delta){
                this.particleEffect.update(delta);
                //when particle effect is done then hide it
                if(this.particleEffect.isComplete()){
                    isVisible = false;
                }
        }

        /**
         * render method
         * @param batch
         */
        public void render(SpriteBatch batch){
            if(isVisible){
                this.particleEffect.draw(batch);
            }
        }

        /**
         * dispose
         */
        public void dispose(){
            this.particleEffect.dispose();
        }

    }

}
