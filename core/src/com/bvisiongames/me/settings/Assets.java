package com.bvisiongames.me.settings;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bvisiongames.me.others.Text;
import com.bvisiongames.me.others.TextLoader;

/**
 * Created by ahzji_000 on 11/10/2015.
 */
public class Assets {

    public static final AssetManager assetManager = new AssetManager();

    public static boolean isMenuLoaded = false, isGameLoaded = false;

    public static void loadMenuScreenAssets(){

        assetManager.load(MenuFileNames.MenuString, TextureAtlas.class);
        assetManager.load(MenuFileNames.TanksString, TextureAtlas.class);
        assetManager.load(MenuFileNames.bulletString, TextureAtlas.class);
        assetManager.load(MenuFileNames.Land_Icons, TextureAtlas.class);
        assetManager.load(MenuFileNames.blackBgString, Texture.class);

        //load fonts for the main menu
        MenuFileNames.MainMenuFonts.loadFonts();

        //load fonts for the lobby menu
        MenuFileNames.LobbyFonts.loadFonts();

    }

    public static void loadGameScreenAssets(){

        //load the game skins
        assetManager.load(GameFileNames.boxString, TextureAtlas.class);
        assetManager.load(GameFileNames.tankString, TextureAtlas.class);
        assetManager.load(GameFileNames.landString, TextureAtlas.class);
        assetManager.load(GameFileNames.bulletString, TextureAtlas.class);
        assetManager.load(GameFileNames.controllerString, TextureAtlas.class);
        assetManager.load(GameFileNames.gameString, TextureAtlas.class);
        assetManager.load(GameFileNames.bunkersString, TextureAtlas.class);
        assetManager.load(GameFileNames.plainBlackString, Texture.class);
        assetManager.load(GameFileNames.plainChocolateString, Texture.class);
        assetManager.load(GameFileNames.plainGreenString, Texture.class);

        //load the effects
        assetManager.load(GameFileNames.blackXString, TextureAtlas.class);
        assetManager.load(GameFileNames.energyBallString, TextureAtlas.class);
        assetManager.load(GameFileNames.fireBallString, TextureAtlas.class);
        assetManager.load(GameFileNames.fireEaterString, TextureAtlas.class);
        assetManager.load(GameFileNames.fireScissorString, TextureAtlas.class);
        assetManager.load(GameFileNames.fireSignString, TextureAtlas.class);
        assetManager.load(GameFileNames.thunderBallString, TextureAtlas.class);
        assetManager.load(GameFileNames.thunderTopDownString, TextureAtlas.class);
        assetManager.load(GameFileNames.yellowFireBallString, TextureAtlas.class);
        assetManager.load(GameFileNames.yellowFireBallWindString, TextureAtlas.class);
        assetManager.load(GameFileNames.rainString, TextureAtlas.class);
        assetManager.load(GameFileNames.muzzleSmokeString, TextureAtlas.class);
        assetManager.load(GameFileNames.blast1String, TextureAtlas.class);
        assetManager.load(GameFileNames.oasisString, TextureAtlas.class);
        assetManager.load(GameFileNames.blueXString, TextureAtlas.class);
        assetManager.load(GameFileNames.fireString, TextureAtlas.class);
        assetManager.load(GameFileNames.whirlString, TextureAtlas.class);
        assetManager.load(GameFileNames.blast2String, TextureAtlas.class);
        assetManager.load(GameFileNames.blast3String, TextureAtlas.class);
        assetManager.load(GameFileNames.blast4String, TextureAtlas.class);
        assetManager.load(GameFileNames.blast5String, TextureAtlas.class);
        assetManager.load(GameFileNames.blast6String, TextureAtlas.class);
        assetManager.load(GameFileNames.blast7String, TextureAtlas.class);
        assetManager.load(GameFileNames.blast8String, TextureAtlas.class);
        assetManager.load(GameFileNames.blast9String, TextureAtlas.class);
        assetManager.load(GameFileNames.blast10String, TextureAtlas.class);
        assetManager.load(GameFileNames.blast11String, TextureAtlas.class);
        assetManager.load(GameFileNames.blast12String, TextureAtlas.class);
        assetManager.load(GameFileNames.hitString, TextureAtlas.class);
        assetManager.load(GameFileNames.rocketFlameString, TextureAtlas.class);
        assetManager.load(GameFileNames.coinString, TextureAtlas.class);

        //load the fonts
        GameFileNames.GameFonts.loadFonts();

        //load the text files
        GameFileNames.GameTextFiles.loadTexts();

        //load sounds
        assetManager.load(GameFileNames.shootSoundString, Sound.class);

    }

    public static void disposeGameAssets(){

        assetManager.clear();
        isGameLoaded = false;

    }
    public static void disposeMenuAssets(){

        assetManager.clear();
        isMenuLoaded = false;

    }

    public static class MenuFileNames{

        public static final String blackBgString = "backgrounds/black.jpg",
                MenuString = "AllMenus/AllMenus.pack", TanksString = "tanks/tanks.pack",
                bulletString = "bullets/bullets.pack", Land_Icons = "land/land_icon.pack";

        public static Skin getMenuSkin(){
            return new Skin(assetManager.get(MenuString, TextureAtlas.class));
        }
        public static Skin getTanksSkin(){
            return new Skin(assetManager.get(TanksString, TextureAtlas.class));
        }
        public static Skin getBulletSkin(){
            return new Skin(assetManager.get(bulletString, TextureAtlas.class));
        }
        public static Skin getLandIcons(){
            return new Skin(assetManager.get(Land_Icons, TextureAtlas.class));
        }

        public static class MainMenuFonts{

            public static String fntTitleString = "fntTitle.ttf", playerFntString = "playerFnt.ttf",
                    defaultFontsString = "defaultFonts.ttf";

            public static void loadFonts(){

                //file handler one for ttf files
                FileHandleResolver resolver1 = new InternalFileHandleResolver();
                assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver1));
                assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver1));
                //file handler two for otf files
                FileHandleResolver resolver2 = new InternalFileHandleResolver();
                assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver2));
                assetManager.setLoader(BitmapFont.class, ".otf", new FreetypeFontLoader(resolver2));

                //create title font
                FreetypeFontLoader.FreeTypeFontLoaderParameter titleFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                titleFontParameter.fontFileName = "fonts/normal.ttf";
                titleFontParameter.fontParameters.color = new Color(0.43f, 0.5f, 0.61f, 1);
                titleFontParameter.fontParameters.borderWidth = 5;
                titleFontParameter.fontParameters.borderColor = Color.WHITE;
                titleFontParameter.fontParameters.size = 120;
                assetManager.load(fntTitleString, BitmapFont.class, titleFontParameter);

                //create player data
                FreetypeFontLoader.FreeTypeFontLoaderParameter playerDataParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                playerDataParameter.fontFileName = "fonts/normal.ttf";
                playerDataParameter.fontParameters.color = Color.GRAY;
                playerDataParameter.fontParameters.borderColor = Color.WHITE;
                playerDataParameter.fontParameters.borderWidth = 3;
                playerDataParameter.fontParameters.size = 60;
                assetManager.load(playerFntString, BitmapFont.class, playerDataParameter);

                //create default font
                FreetypeFontLoader.FreeTypeFontLoaderParameter defaultParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                defaultParameter.fontFileName = "fonts/alpha.ttf";
                defaultParameter.fontParameters.size = 30;
                defaultParameter.fontParameters.color = Color.WHITE;
                assetManager.load(defaultFontsString, BitmapFont.class, defaultParameter);

            }

        }

        public static class LobbyFonts{

            public static String pageTitleFontString = "pageTitleFont.otf", roomTitleFontString = "roomTitleFont.otf",
                    panelOptionFontBtnString = "panelOptionFontBtn.otf", roomTotalFontString = "roomTotalFont.otf";

            public static void loadFonts(){

                //setup the room page title
                FreetypeFontLoader.FreeTypeFontLoaderParameter titleFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                titleFontParameter.fontFileName = "fonts/chunk.otf";
                titleFontParameter.fontParameters.color = new Color(0.43f, 0.5f, 0.61f, 1);
                titleFontParameter.fontParameters.borderWidth = 3;
                titleFontParameter.fontParameters.borderColor = Color.WHITE;
                titleFontParameter.fontParameters.size = 40;
                assetManager.load(pageTitleFontString, BitmapFont.class, titleFontParameter);

                //setup room title font
                FreetypeFontLoader.FreeTypeFontLoaderParameter roomTitleParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                roomTitleParameter.fontFileName = "fonts/chunk.otf";
                roomTitleParameter.fontParameters.color = new Color(0.43f, 0.5f, 0.61f, 1);
                roomTitleParameter.fontParameters.borderColor = Color.WHITE;
                roomTitleParameter.fontParameters.size = 25;
                assetManager.load(roomTitleFontString, BitmapFont.class, roomTitleParameter);

                //setup room total font
                FreetypeFontLoader.FreeTypeFontLoaderParameter roomTotalParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                roomTotalParameter.fontFileName = "fonts/chunk.otf";
                roomTotalParameter.fontParameters.color = new Color(0.43f, 0.5f, 0.61f, 1);
                roomTotalParameter.fontParameters.borderColor = Color.WHITE;
                roomTotalParameter.fontParameters.size = 25;
                assetManager.load(roomTotalFontString, BitmapFont.class, roomTotalParameter);

                //setup panel option font button
                FreetypeFontLoader.FreeTypeFontLoaderParameter panelOptionParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                panelOptionParameter.fontFileName = "fonts/chunk.otf";
                panelOptionParameter.fontParameters.color = Color.WHITE;
                panelOptionParameter.fontParameters.size = 25;
                assetManager.load(panelOptionFontBtnString, BitmapFont.class, panelOptionParameter);

            }

        }

    }
    public static class GameFileNames{

        //game variables
        public static String boxString = "boxes/boxes.pack", tankString = "tanks/tanks.pack",
                        landString = "land/land.pack", bulletString = "bullets/bullets.pack",
                        controllerString = "controllers/controller.pack", gameString = "game/game.pack",
                        bunkersString = "bunkers/bunkers.pack",
                        plainGreenString = "backgrounds/green.jpg", plainBlackString = "backgrounds/black.jpg",
                        plainChocolateString = "backgrounds/chocolate.jpg",

                        //below are directories for sound files
                        shootSoundString = "game/sounds/shoot.mp3";

        //effects string variables
        public static String blackXString = "effects/black_explosion.pack", energyBallString = "effects/energy_ball.pack",
                            fireBallString = "effects/fire_ball.pack", fireEaterString = "effects/fire_eater.pack",
                            fireScissorString = "effects/fire_scissors.pack", fireSignString = "effects/fire_sign.pack",
                            thunderBallString = "effects/thunder_ball.pack", thunderTopDownString = "effects/thunder_top_down.pack",
        yellowFireBallString = "effects/yellow_fire_ball.pack", yellowFireBallWindString = "effects/yellow_fire_ball_wind.pack",
        rainString = "effects/rain.pack", muzzleSmokeString = "blast/muzzle.pack", blast1String = "blast/6.pack", oasisString = "effects/oasis.pack",
        blueXString = "effects/blue_explosion.pack", fireString = "effects/fire.pack", whirlString = "effects/whirl.pack", blast2String = "blast/circle.pack",
        blast3String = "blast/blast3.pack", blast4String = "blast/blast4.pack", blast5String = "blast/blast5.pack", blast6String = "blast/blast6.pack",
        blast7String = "blast/blast7.pack", blast8String = "blast/blast8.pack", blast9String = "blast/blast9.pack", blast10String = "blast/blast10.pack",
        blast11String = "blast/blast11.pack", blast12String = "blast/blast12.pack", hitString = "blast/hit.pack", rocketFlameString = "blast/rocket_flame.pack",
        coinString = "effects/coin.pack";

        //effects skins
        public static Skin getBlast4Skin(){
            return new Skin(assetManager.get(blast4String, TextureAtlas.class));
        }
        public static Skin getBlast5Skin(){
            return new Skin(assetManager.get(blast5String, TextureAtlas.class));
        }
        public static Skin getBlast6Skin(){
            return new Skin(assetManager.get(blast6String, TextureAtlas.class));
        }
        public static Skin getBlast7Skin(){
            return new Skin(assetManager.get(blast7String, TextureAtlas.class));
        }
        public static Skin getBlast8Skin(){
            return new Skin(assetManager.get(blast8String, TextureAtlas.class));
        }
        public static Skin getBlast9Skin(){
            return new Skin(assetManager.get(blast9String, TextureAtlas.class));
        }
        public static Skin getBlast10Skin(){
            return new Skin(assetManager.get(blast10String, TextureAtlas.class));
        }
        public static Skin getBlast11Skin(){
            return new Skin(assetManager.get(blast11String, TextureAtlas.class));
        }
        public static Skin getBlast12Skin(){
            return new Skin(assetManager.get(blast12String, TextureAtlas.class));
        }
        public static Skin getHitSkin(){
            return new Skin(assetManager.get(hitString, TextureAtlas.class));
        }
        public static Skin getRocketFlameSkin(){
            return new Skin(assetManager.get(rocketFlameString, TextureAtlas.class));
        }
        public static Skin getBlackXSkin(){
            return new Skin(assetManager.get(blackXString, TextureAtlas.class));
        }
        public static Skin getEnergyBallSkin(){
            return new Skin(assetManager.get(energyBallString, TextureAtlas.class));
        }
        public static Skin getFireBallSkin(){
            return new Skin(assetManager.get(fireBallString, TextureAtlas.class));
        }
        public static Skin getFireEaterSkin(){
            return new Skin(assetManager.get(fireEaterString, TextureAtlas.class));
        }
        public static Skin getFireScissorSkin(){
            return new Skin(assetManager.get(fireScissorString, TextureAtlas.class));
        }
        public static Skin getFireSignSkin(){
            return new Skin(assetManager.get(fireSignString, TextureAtlas.class));
        }
        public static Skin getThunderBallSkin(){
            return new Skin(assetManager.get(thunderBallString, TextureAtlas.class));
        }
        public static Skin getThunderTopDownSkin(){
            return new Skin(assetManager.get(thunderTopDownString, TextureAtlas.class));
        }
        public static Skin getYellowFireBallSkin(){
            return new Skin(assetManager.get(yellowFireBallString, TextureAtlas.class));
        }
        public static Skin getYellowFireBallWindSkin(){
            return new Skin(assetManager.get(yellowFireBallWindString, TextureAtlas.class));
        }
        public static Skin getRainSkin(){
            return new Skin(assetManager.get(rainString, TextureAtlas.class));
        }
        public static Skin getMuzzleSmokeSkin(){
            return new Skin(assetManager.get(muzzleSmokeString, TextureAtlas.class));
        }
        public static Skin getBlast1Skin(){
            return new Skin(assetManager.get(blast1String, TextureAtlas.class));
        }
        public static Skin getOasisSkin(){
            return new Skin(assetManager.get(oasisString, TextureAtlas.class));
        }
        public static Skin getBlueXSkin(){
            return new Skin(assetManager.get(blueXString, TextureAtlas.class));
        }
        public static Skin getFireSkin(){
            return new Skin(assetManager.get(fireString, TextureAtlas.class));
        }
        public static Skin getWhirlSkin(){
            return new Skin(assetManager.get(whirlString, TextureAtlas.class));
        }
        public static Skin getBlast2Skin(){
            return new Skin(assetManager.get(blast2String, TextureAtlas.class));
        }
        public static Skin getBlast3Skin(){
            return new Skin(assetManager.get(blast3String, TextureAtlas.class));
        }
        public static Skin getCoinSkin(){
            return new Skin(assetManager.get(coinString, TextureAtlas.class));
        }

        //main game skins
        public static Skin getBunkersSkin(){
            return new Skin(assetManager.get(bunkersString, TextureAtlas.class));
        }
        public static Skin getBoxSkin(){
            return new Skin(assetManager.get(boxString, TextureAtlas.class));
        }
        public static Skin getTankSkin(){
            return new Skin(assetManager.get(tankString, TextureAtlas.class));
        }
        public static Skin getLandSkin(){
            return new Skin(assetManager.get(landString, TextureAtlas.class));
        }
        public static Skin getBulletSkin(){
            return new Skin(assetManager.get(bulletString, TextureAtlas.class));
        }
        public static Skin getControllerSkin(){
            return new Skin(assetManager.get(controllerString, TextureAtlas.class));
        }
        public static Skin getGameSkin(){
            return new Skin(assetManager.get(gameString, TextureAtlas.class));
        }

        public static class GameTextFiles{

            public static String levelString = "levels/levelone.txt";

            public static void loadTexts(){

                //set the loading of the levels information
                assetManager.setLoader(
                        Text.class,
                        new TextLoader(new InternalFileHandleResolver())
                        );
                assetManager.load( new AssetDescriptor( levelString, Text.class, new TextLoader.TextParameter() ));

            }

            public static String getLevelString(){
                return assetManager.get(levelString, Text.class).getString();
            }

        }

        public static class GameFonts{

            public static String defaultFontsString = "defaultFonts.ttf", titleFontString = "titleFont.ttf",
                    smallSizeFontString = "smallSizeFont.ttf", scrollFontsString = "scrollFonts.ttf",
                    topBarGameFontString = "topBarGameFont.ttf";

            public static void loadFonts(){

                //file handler one for ttf files
                FileHandleResolver resolver1 = new InternalFileHandleResolver();
                assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver1));
                assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver1));

                //default font
                FreetypeFontLoader.FreeTypeFontLoaderParameter defaultFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                defaultFontParameter.fontFileName = "fonts/alpha.ttf";
                defaultFontParameter.fontParameters.color = Color.WHITE;
                defaultFontParameter.fontParameters.size = 30;
                assetManager.load(defaultFontsString, BitmapFont.class, defaultFontParameter);

                //title font
                FreetypeFontLoader.FreeTypeFontLoaderParameter titleFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                titleFontParameter.fontFileName = "fonts/alpha.ttf";
                titleFontParameter.fontParameters.color = Color.WHITE;
                titleFontParameter.fontParameters.size = 50;
                assetManager.load(titleFontString, BitmapFont.class, titleFontParameter);

                //small sized font
                FreetypeFontLoader.FreeTypeFontLoaderParameter smallSizeFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                smallSizeFontParameter.fontFileName = "fonts/alpha.ttf";
                smallSizeFontParameter.fontParameters.color = Color.WHITE;
                smallSizeFontParameter.fontParameters.size = 20;
                assetManager.load(smallSizeFontString, BitmapFont.class, smallSizeFontParameter);

                //scroll font
                FreetypeFontLoader.FreeTypeFontLoaderParameter scrollFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                scrollFontParameter.fontFileName = "fonts/normal.ttf";
                scrollFontParameter.fontParameters.color = Color.WHITE;
                scrollFontParameter.fontParameters.size = 30;
                assetManager.load(scrollFontsString, BitmapFont.class, scrollFontParameter);

                //top bar game font
                FreetypeFontLoader.FreeTypeFontLoaderParameter topBarGameFontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                topBarGameFontParameter.fontFileName = "fonts/antonio-regular.ttf";
                topBarGameFontParameter.fontParameters.color = Color.WHITE;
                topBarGameFontParameter.fontParameters.size = 40;
                assetManager.load(topBarGameFontString, BitmapFont.class, topBarGameFontParameter);

            }

        }

    }

}
