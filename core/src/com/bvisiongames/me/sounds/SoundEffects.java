package com.bvisiongames.me.sounds;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.Assets;

/**
 * Created by Sultan on 8/20/2015.
 */
public class SoundEffects {

    public float Volume = 0;

    private Sound shoot;

    public SoundEffects(){

        //setup the volume
        if(Tanks.gameState.getSoundState())
            {Volume = 0;}
        else
            {Volume = ((float)Tanks.gameState.getSoundLevel()/10f);}

        shoot = Assets.assetManager.get(Assets.GameFileNames.shootSoundString, Sound.class);

    }
    public void refresh(){

        //setup the volume
        if(Tanks.gameState.getSoundState())
        {Volume = 0;}
        else
        {Volume = ((float)Tanks.gameState.getSoundLevel()/10f);}

        shoot = Assets.assetManager.get(Assets.GameFileNames.shootSoundString, Sound.class);

    }

    public long PlayShoot(Vector2 pos){
        return shoot.play(Volume*
                (30/(float)(  Math.sqrt(
                        Math.pow(MultiGameScreen.camera.position.x - pos.x, 2)    +    Math.pow(MultiGameScreen.camera.position.y - pos.y, 2)
                )  ))
        );
    }
    public long PlayShoot(float x, float y){
        return shoot.play(Volume*
                        (30/(float)(  Math.sqrt(
                                Math.pow(MultiGameScreen.camera.position.x - x, 2)    +    Math.pow(MultiGameScreen.camera.position.y - y, 2)
                        )  ))
        );
    }

}
