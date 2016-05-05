package com.bvisiongames.me.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bvisiongames.me.Maps.MapManager;

/**
 * Created by ahzji_000 on 2/7/2016.
 */
public class ParticleEffectsManager {

    //particle effect file manager
    public ParticleEffectsFilesManager particleEffectsFilesManager = new ParticleEffectsFilesManager();

    //default tank explosion manager
    public DefaultTankExplosionManager defaultTankExplosionManager;

    //lightning manager
    public LightningManager lightningManager;

    /**
     * initiator.
     */
    public ParticleEffectsManager(MapManager.MAPTYPE level){

        switch (level){
            case LEVEL1:
                particleEffectsFilesManager.loadMuzzleFiles();
                particleEffectsFilesManager.loadtankExplosionFiles();
                particleEffectsFilesManager.loadControlGlowFiles();
                particleEffectsFilesManager.loadBullet1ExplosionFiles();
                particleEffectsFilesManager.loadLaserBulletFiles();
                break;
        }

        //default tank manager
        this.defaultTankExplosionManager = new DefaultTankExplosionManager(
                //tank explosion destroyed files
                this.particleEffectsFilesManager.tankExplosionEffectFile,
                this.particleEffectsFilesManager.tankExplosionImageFileDir,
                //tank bullet explosion files
                this.particleEffectsFilesManager.bullet1ExplosionEffectFile,
                this.particleEffectsFilesManager.bullet1ExplosionImageFileDir
                );

        //lightning manager
        this.lightningManager = new LightningManager();

    }

    /**
     * update method.
     * @param delta
     */
    public void update(float delta){

        //update the default tank explosions
        this.defaultTankExplosionManager.update(delta);

        //update the lightning manager
        this.lightningManager.update(delta);

    }

    /**
     * render method.
     * @param batch
     */
    public void render(SpriteBatch batch){

        batch.begin();
        //render the default tank stuff
        this.defaultTankExplosionManager.render(batch);
        //render the lightning stuff here
        this.lightningManager.render(batch);
        batch.end();

    }

    /**
     * dispose
     */
    public void dispose(){

        this.defaultTankExplosionManager.dispose();
        this.lightningManager.dispose();

    }

}
