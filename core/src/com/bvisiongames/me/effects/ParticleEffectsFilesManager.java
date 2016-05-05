package com.bvisiongames.me.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by ahzji_000 on 2/6/2016.
 */
public class ParticleEffectsFilesManager {

    /**
     * muzzle file handlers
     */
    public FileHandle muzzleEffectFile,
                        muzzleImageFileDir;

    /**
     * control glow handlers
     */
    public FileHandle controlGlowEffectFile,
                        controlGlowImageFileDir;

    /**
     * tank explosion handlers
     */
    public FileHandle tankExplosionEffectFile,
                        tankExplosionImageFileDir;

    /**
     * bullet1 explosion handlers
     */
    public FileHandle bullet1ExplosionEffectFile,
            bullet1ExplosionImageFileDir;

    /**
     * laser bullet handlers
     */
    public FileHandle laserBulletEffectFile,
                        laserBulletImageFileDir;

    //setters
    /**
     * load the muzzle effect file handlers.
     */
    public void loadMuzzleFiles(){
        this.muzzleEffectFile = Gdx.files.internal("particleeffects/muzzleFire");
        this.muzzleImageFileDir = Gdx.files.internal("");
    }
    /**
     * load the control glow effect handlers.
     */
    public void loadControlGlowFiles(){
        this.controlGlowEffectFile = Gdx.files.internal("particleeffects/controlGlow");
        this.controlGlowImageFileDir = Gdx.files.internal("");
    }
    /**
     * load the tank explosion effect handlers.
     */
    public void loadtankExplosionFiles(){
        this.tankExplosionEffectFile = Gdx.files.internal("particleeffects/tankExplosion");
        this.tankExplosionImageFileDir = Gdx.files.internal("");
    }
    /**
     * load bullet explosion effect handlers.
     */
    public void loadBullet1ExplosionFiles(){
        this.bullet1ExplosionEffectFile = Gdx.files.internal("particleeffects/bullet1Explosion");
        this.bullet1ExplosionImageFileDir = Gdx.files.internal("");
    }
    /**
     * load laser bullet effect handlers.
     */
    public void loadLaserBulletFiles(){
        this.laserBulletEffectFile = Gdx.files.internal("particleeffects/laserBullet");
        this.laserBulletImageFileDir = Gdx.files.internal("");
    }
    //end of setters


    //getters

    //end of getters

}
