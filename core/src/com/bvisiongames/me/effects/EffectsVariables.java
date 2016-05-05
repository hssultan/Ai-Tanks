package com.bvisiongames.me.effects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bvisiongames.me.settings.Assets;

/**
 * Created by ahzji_000 on 11/3/2015.
 *
 *                          NOTICE
 * <-----------SKINS MUST BE INITIATED BEFORE USE-------------------></-----------SKINS>
 *
 */
public class EffectsVariables {


    //effects skins
    public static Skin blackXskin, energyBallSkin, fireBallSkin,
                        fireEacterSkin, fireScissorsSkin, fireSignSkin,
                        thunderBallSkin, thunderTopDownSkin, yellowFireBallSkin,
                        oasisSkin, blueXskin, fireSkin, whirlSkin,
                        yellowFireBallWindSkin, rainSkin, muzzleSmokeSkin,
                        blast1Skin, blast2Skin, blast3Skin, blast4Skin,
                        blast5Skin, blast6Skin, blast7Skin, blast8Skin,
                        blast9Skin, blast10Skin, blast11Skin, blast12Skin,
                        hitSkin, rocketFlameSkin, coinSkin;

    public static TextureRegion[] blackXTexture, energyBallTexture, fireBallTexture,
                                    fireEaterTexture, fireScissorsTexture, fireSignTexture,
                                    thunderBallTexture, thunderTopDownTexture, yellowFireBallTexture,
                                    oasisTexture, blueXTexture, fireTexture, whirlTexture,
                                    yellowFireBallWindTexture, rainTexture, muzzleSmokeTexture,
                                    blast1Texture, blast2Texture, blast3Texture, blast4Texture,
                                    blast5Texture, blast6Texture, blast7Texture, blast8Texture,
                                    blast9Texture, blast10Texture, blast11Texture, blast12Texture,
                                    hitTexture, rocketFlameTexture, coinTexture;


    public static void initiate(){

        //initiate the skins
        blackXskin = Assets.GameFileNames.getBlackXSkin();
        energyBallSkin = Assets.GameFileNames.getEnergyBallSkin();
        fireBallSkin = Assets.GameFileNames.getFireBallSkin();
        fireEacterSkin = Assets.GameFileNames.getFireEaterSkin();
        fireScissorsSkin = Assets.GameFileNames.getFireScissorSkin();
        fireSignSkin = Assets.GameFileNames.getFireSignSkin();
        thunderBallSkin = Assets.GameFileNames.getThunderBallSkin();
        thunderTopDownSkin = Assets.GameFileNames.getThunderTopDownSkin();
        yellowFireBallSkin = Assets.GameFileNames.getYellowFireBallSkin();
        yellowFireBallWindSkin = Assets.GameFileNames.getYellowFireBallWindSkin();
        rainSkin = Assets.GameFileNames.getRainSkin();
        muzzleSmokeSkin = Assets.GameFileNames.getMuzzleSmokeSkin();
        blast1Skin = Assets.GameFileNames.getBlast1Skin();
        oasisSkin = Assets.GameFileNames.getOasisSkin();
        blueXskin = Assets.GameFileNames.getBlueXSkin();
        fireSkin = Assets.GameFileNames.getFireSkin();
        whirlSkin = Assets.GameFileNames.getWhirlSkin();
        blast2Skin = Assets.GameFileNames.getBlast2Skin();
        blast3Skin = Assets.GameFileNames.getBlast3Skin();
        blast4Skin = Assets.GameFileNames.getBlast4Skin();
        blast5Skin = Assets.GameFileNames.getBlast5Skin();
        blast6Skin = Assets.GameFileNames.getBlast6Skin();
        blast7Skin = Assets.GameFileNames.getBlast7Skin();
        blast8Skin = Assets.GameFileNames.getBlast8Skin();
        blast9Skin = Assets.GameFileNames.getBlast9Skin();
        blast10Skin = Assets.GameFileNames.getBlast10Skin();
        blast11Skin = Assets.GameFileNames.getBlast11Skin();
        blast12Skin = Assets.GameFileNames.getBlast12Skin();
        hitSkin = Assets.GameFileNames.getHitSkin();
        rocketFlameSkin = Assets.GameFileNames.getRocketFlameSkin();
        coinSkin = Assets.GameFileNames.getCoinSkin();

        //initiate the textures arrays

        //black explosion texture
        blackXTexture = new TextureRegion[blackXskin.getAtlas().getRegions().size];
        for(int i = 0; i < blackXskin.getAtlas().getRegions().size; i++){
            blackXTexture[i] = blackXskin.getRegion(Integer.toString(i));
        }

        //energy ball texture
        energyBallTexture = new TextureRegion[energyBallSkin.getAtlas().getRegions().size];
        for(int i = 0; i < energyBallSkin.getAtlas().getRegions().size; i++){
            energyBallTexture[i] = energyBallSkin.getRegion(Integer.toString(i));
        }

        //energy ball texture
        fireBallTexture = new TextureRegion[fireBallSkin.getAtlas().getRegions().size];
        for(int i = 0; i < fireBallSkin.getAtlas().getRegions().size; i++){
            fireBallTexture[i] = fireBallSkin.getRegion(Integer.toString(i));
        }

        //fire Eater texture
        fireEaterTexture = new TextureRegion[fireEacterSkin.getAtlas().getRegions().size];
        for(int i = 0; i < fireEacterSkin.getAtlas().getRegions().size; i++){
            fireEaterTexture[i] = fireEacterSkin.getRegion(Integer.toString(i));
        }

        //fire Scissors texture
        fireScissorsTexture = new TextureRegion[fireScissorsSkin.getAtlas().getRegions().size];
        for(int i = 0; i < fireScissorsSkin.getAtlas().getRegions().size; i++){
            fireScissorsTexture[i] = fireScissorsSkin.getRegion(Integer.toString(i));
        }

        //fire Sign texture
        fireSignTexture = new TextureRegion[fireSignSkin.getAtlas().getRegions().size];
        for(int i = 0; i < fireSignSkin.getAtlas().getRegions().size; i++){
            fireSignTexture[i] = fireSignSkin.getRegion(Integer.toString(i));
        }

        //Thunder Ball texture
        thunderBallTexture = new TextureRegion[thunderBallSkin.getAtlas().getRegions().size];
        for(int i = 0; i < thunderBallSkin.getAtlas().getRegions().size; i++){
            thunderBallTexture[i] = thunderBallSkin.getRegion(Integer.toString(i));
        }

        //Thunder Top Down texture
        thunderTopDownTexture = new TextureRegion[thunderTopDownSkin.getAtlas().getRegions().size];
        for(int i = 0; i < thunderTopDownSkin.getAtlas().getRegions().size; i++){
            thunderTopDownTexture[i] = thunderTopDownSkin.getRegion(Integer.toString(i));
        }

        //Yellow Fire Ball texture
        yellowFireBallTexture = new TextureRegion[yellowFireBallSkin.getAtlas().getRegions().size];
        for(int i = 0; i < yellowFireBallSkin.getAtlas().getRegions().size; i++){
            yellowFireBallTexture[i] = yellowFireBallSkin.getRegion(Integer.toString(i));
        }

        //Yellow Fire Ball Wind texture
        yellowFireBallWindTexture = new TextureRegion[yellowFireBallWindSkin.getAtlas().getRegions().size];
        for(int i = 0; i < yellowFireBallWindSkin.getAtlas().getRegions().size; i++){
            yellowFireBallWindTexture[i] = yellowFireBallWindSkin.getRegion(Integer.toString(i));
        }

        //Rain texture
        rainTexture = new TextureRegion[rainSkin.getAtlas().getRegions().size];
        for(int i = 0; i < rainSkin.getAtlas().getRegions().size; i++){
            rainTexture[i] = rainSkin.getRegion(Integer.toString(i));
        }

        //Muzzle Smoke texture
        muzzleSmokeTexture = new TextureRegion[muzzleSmokeSkin.getAtlas().getRegions().size];
        for(int i = 0; i < muzzleSmokeSkin.getAtlas().getRegions().size; i++){
            muzzleSmokeTexture[i] = muzzleSmokeSkin.getRegion(Integer.toString(i));
        }

        //Blast1 texture
        blast1Texture = new TextureRegion[blast1Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast1Skin.getAtlas().getRegions().size; i++){
            blast1Texture[i] = blast1Skin.getRegion(Integer.toString(i));
        }

        //Blast2 texture
        blast2Texture = new TextureRegion[blast2Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast2Skin.getAtlas().getRegions().size; i++){
            blast2Texture[i] = blast2Skin.getRegion(Integer.toString(i));
        }

        //Blast3 texture
        blast3Texture = new TextureRegion[blast3Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast3Skin.getAtlas().getRegions().size; i++){
            blast3Texture[i] = blast3Skin.getRegion(Integer.toString(i));
        }

        //Blast4 texture
        blast4Texture = new TextureRegion[blast4Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast4Skin.getAtlas().getRegions().size; i++){
            blast4Texture[i] = blast4Skin.getRegion(Integer.toString(i));
        }

        //Blast5 texture
        blast5Texture = new TextureRegion[blast5Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast5Skin.getAtlas().getRegions().size; i++){
            blast5Texture[i] = blast5Skin.getRegion(Integer.toString(i));
        }

        //Blast6 texture
        blast6Texture = new TextureRegion[blast6Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast6Skin.getAtlas().getRegions().size; i++){
            blast6Texture[i] = blast6Skin.getRegion(Integer.toString(i));
        }

        //Blast7 texture
        blast7Texture = new TextureRegion[blast7Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast7Skin.getAtlas().getRegions().size; i++){
            blast7Texture[i] = blast7Skin.getRegion(Integer.toString(i));
        }

        //Blast8 texture
        blast8Texture = new TextureRegion[blast8Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast8Skin.getAtlas().getRegions().size; i++){
            blast8Texture[i] = blast8Skin.getRegion(Integer.toString(i));
        }

        //Blast9 texture
        blast9Texture = new TextureRegion[blast9Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast9Skin.getAtlas().getRegions().size; i++){
            blast9Texture[i] = blast9Skin.getRegion(Integer.toString(i));
        }

        //Blast10 texture
        blast10Texture = new TextureRegion[blast10Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast10Skin.getAtlas().getRegions().size; i++){
            blast10Texture[i] = blast10Skin.getRegion(Integer.toString(i));
        }

        //Blast11 texture
        blast11Texture = new TextureRegion[blast11Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast11Skin.getAtlas().getRegions().size; i++){
            blast11Texture[i] = blast11Skin.getRegion(Integer.toString(i));
        }

        //Blast12 texture
        blast12Texture = new TextureRegion[blast12Skin.getAtlas().getRegions().size];
        for(int i = 0; i < blast12Skin.getAtlas().getRegions().size; i++){
            blast12Texture[i] = blast12Skin.getRegion(Integer.toString(i));
        }

        //Hit texture
        hitTexture = new TextureRegion[hitSkin.getAtlas().getRegions().size];
        for(int i = 0; i < hitSkin.getAtlas().getRegions().size; i++){
            hitTexture[i] = hitSkin.getRegion(Integer.toString(i));
        }

        //Rocket Flame texture
        rocketFlameTexture = new TextureRegion[rocketFlameSkin.getAtlas().getRegions().size];
        for(int i = 0; i < rocketFlameSkin.getAtlas().getRegions().size; i++){
            rocketFlameTexture[i] = rocketFlameSkin.getRegion(Integer.toString(i));
        }

        //Oasis texture
        oasisTexture = new TextureRegion[oasisSkin.getAtlas().getRegions().size];
        for(int i = 0; i < oasisSkin.getAtlas().getRegions().size; i++){
            oasisTexture[i] = oasisSkin.getRegion(Integer.toString(i));
        }

        //Blue Explosion texture
        blueXTexture = new TextureRegion[blueXskin.getAtlas().getRegions().size];
        for(int i = 0; i < blueXskin.getAtlas().getRegions().size; i++){
            blueXTexture[i] = blueXskin.getRegion(Integer.toString(i));
        }

        //Fire texture
        fireTexture = new TextureRegion[fireSkin.getAtlas().getRegions().size];
        for(int i = 0; i < fireSkin.getAtlas().getRegions().size; i++){
            fireTexture[i] = fireSkin.getRegion(Integer.toString(i));
        }

        //Whirl texture
        whirlTexture = new TextureRegion[whirlSkin.getAtlas().getRegions().size];
        for(int i = 0; i < whirlSkin.getAtlas().getRegions().size; i++){
            whirlTexture[i] = whirlSkin.getRegion(Integer.toString(i));
        }

        //coin texture
        coinTexture = new TextureRegion[coinSkin.getAtlas().getRegions().size];
        for(int i = 0; i < coinSkin.getAtlas().getRegions().size; i++){
            coinTexture[i] = coinSkin.getRegion(Integer.toString(i));
        }

    }

    public static void dispose(){

        blackXskin.dispose();
        energyBallSkin.dispose();
        fireBallSkin.dispose();
        fireEacterSkin.dispose();
        fireScissorsSkin.dispose();
        fireSignSkin.dispose();
        thunderBallSkin.dispose();
        thunderTopDownSkin.dispose();
        yellowFireBallSkin.dispose();
        yellowFireBallWindSkin.dispose();
        muzzleSmokeSkin.dispose();
        blast1Skin.dispose();
        oasisSkin.dispose();
        blueXskin.dispose();
        fireSkin.dispose();
        whirlSkin.dispose();
        blast2Skin.dispose();
        blast3Skin.dispose();
        blast4Skin.dispose();
        blast5Skin.dispose();
        blast6Skin.dispose();
        blast7Skin.dispose();
        blast8Skin.dispose();
        blast9Skin.dispose();
        blast10Skin.dispose();
        blast11Skin.dispose();
        blast12Skin.dispose();
        hitSkin.dispose();
        rocketFlameSkin.dispose();
        coinSkin.dispose();

    }

}
