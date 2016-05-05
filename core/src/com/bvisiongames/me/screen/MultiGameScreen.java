package com.bvisiongames.me.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bvisiongames.me.Tweenanimations.ActorAnimation;
import com.bvisiongames.me.Tweenanimations.BodyAnimation;
import com.bvisiongames.me.Tweenanimations.CameraAnimation;
import com.bvisiongames.me.Tweenanimations.CellAnimation;
import com.bvisiongames.me.Tweenanimations.ClientScoreAnimation;
import com.bvisiongames.me.Tweenanimations.CustomStackAnimation;
import com.bvisiongames.me.Tweenanimations.DayStateAnimation;
import com.bvisiongames.me.Tweenanimations.ProgressBarAnimation;
import com.bvisiongames.me.Tweenanimations.SpriteAnimation;
import com.bvisiongames.me.camera.OrthoCamera;
import com.bvisiongames.me.displays.ClientScore;
import com.bvisiongames.me.effects.EffectsVariables;
import com.bvisiongames.me.effects.ParticleEffectsManager;
import com.bvisiongames.me.entity.BodyDisposer;
import com.bvisiongames.me.entity.EntitManager;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.Weapons.EntitysWeaponManger;
import com.bvisiongames.me.loaders.SmallLoading;
import com.bvisiongames.me.miniscreen.InGameScreens;
import com.bvisiongames.me.protocols.ObjectUpdate;
import com.bvisiongames.me.protocols.Timmer;
import com.bvisiongames.me.settings.Assets;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.sounds.SoundEffects;
import com.bvisiongames.me.weather.DayState;
import com.bvisiongames.me.weather.Weather;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import box2dLight.RayHandler;

/**
 * Created by Sultan on 6/26/2015.
 */
public class MultiGameScreen extends Screen
        implements GestureDetector.GestureListener{

        //class properties
        public static OrthoCamera camera;
        public static World WORLD;

        //scene 2d objects
        public static Stage stage, ResultPanel;

        //skins for all objects
        public static Skin gameSkin,
                            Boxes, Tanks, Lands, Bullets,
                            controllersSkin;

        //fonts
        public static BitmapFont defaultFonts, titleFont, smallSizeFont,    //regular font
                                    scrollFonts,            //after and before game fonts
                                    topBarGameFont;

        //create the game play manager
        public static EntitManager entityManager;

        //create the game screen option manager
        public static InGameScreens inGameScreens;

        //box2d debugger
        private Box2DDebugRenderer debugRenderer;
        private OrthographicCamera box2dCamera;

        //tween manager
        //tween manager for animation
        public static TweenManager tweenManager;

        //sound effect player
        public static SoundEffects soundeffect;

        private ShapeRenderer shapeRenderer;

        //lifecycle of box2d
        public static boolean box2dLifeCycle = true; //pause: false;
                                                     // resume: true;

        //weather state
        public static Weather weather = new Weather();

        //particle effect manager
        public static ParticleEffectsManager particleEffectsManager;

        //object updater
        public static ObjectUpdate objectUpdater = new ObjectUpdate();

        //interface that can be controlled from different threads or classes
        public static GameInterface gameInterface = new GameInterface() {
            @Override
            public void pause() {
                if(entityManager != null){
                    //pause the object updater
                    objectUpdater.Pause();
                    //pause the entity manager
                    entityManager.pause();
                    box2dLifeCycle = false;
                }
            }

            @Override
            public void resume() {
                if(entityManager != null){
                    //resume the object updater
                    objectUpdater.Resume();
                    //resume the entity manager
                    entityManager.resume();
                    box2dLifeCycle = true;
                }
            }
        };

        //ray handler
        public static RayHandler rayHandler;

        //small loading animation
        public static SmallLoading smallLoading;

        //body disposer
        public static BodyDisposer bodyDisposer = new BodyDisposer();

        //FPS logger
        FPSLogger fpsLogger = new FPSLogger();

        //timer
        private Timmer timmer = new Timmer(1000);

        @Override
        public void create() {

            //initialize the camera
            camera = new OrthoCamera();
            //set and reset the viewport width and height
            camera.update();
            camera.resize();

            //check if the assets has been loaded for the menu screens
            if(Assets.isGameLoaded){
                setupScreen();
            }else{
                //else tell the update and render methods to not loop through objects
                propertiesLoaded = false;
            }

        }

        private boolean propertiesLoaded = true, assetLoaderTriggered = true;

        private void setupScreen(){

            //adjust the camera
            camera.zoom -= 0.2;

            //setup the position of the small loading animation
            this.smallLoading = new SmallLoading(new Vector2(com.bvisiongames.me.Tanks.cameraWIDTH/2,
                                                                com.bvisiongames.me.Tanks.cameraHeight/2));

            //create the world
            WORLD = new World( new Vector2( 0, 0), true );
            debugRenderer = new Box2DDebugRenderer();

            //setup the box2d light
            this.rayHandler = new RayHandler(WORLD);
            this.rayHandler.setShadows(true);
            box2dCamera = new OrthographicCamera(camera.viewportWidth, camera.viewportHeight);

            //initiate the particle effect manager
            this.particleEffectsManager = new ParticleEffectsManager(com.bvisiongames.me.Tanks.gameState.maps.getLevel());

            //create the stage and their skins
            setupStages();

            //initiate the skins
            setupSkins();

            //setup fonts
            setUpFonts();

            //register the tween event and initiate the tween manager
            setTweenListeners();

            //initiate the entity manager
            entityManager = new EntitManager();
            entityManager.GameState = EntitManager.GAMESTATE.WATCH;

            //set the input listener
            setupListeners();

            //initiate the In Game screen setter
            inGameScreens = new InGameScreens();

            //initiate the sound effects
            soundeffect = new SoundEffects();

            //shape render
            shapeRenderer = new ShapeRenderer();

            //set the main player properties
            setPlayerProperties();

            //setup the weather properties
            //setup the day state
            this.weather.dayState.setRayHandler(this.rayHandler);
            this.weather.turnLightsOn();
            //animate the ambient light
            setupAmbientLight();

            //run the object updater
            this.objectUpdater.start();

            propertiesLoaded = true;

        }

        private void setupAmbientLight(){

            switch (weather.dayState.daystate){
                case DAY:
                    Tween.to(weather.dayState, DayStateAnimation.AMBIENT_NUM, 3)
                            .target(1)
                            .setCallbackTriggers(TweenCallback.END)
                            .setCallback(new TweenCallback() {
                                @Override
                                public void onEvent(int type, BaseTween<?> source) {
                                    if(type == TweenCallback.END){
                                        entityManager.tank.switchFlashOff();
                                    }
                                }
                            })
                            .start(tweenManager);
                    break;
                case NIGHT:
                    Tween.to(weather.dayState, DayStateAnimation.AMBIENT_NUM, 3)
                            .target(0.2f)
                            .setCallbackTriggers(TweenCallback.END)
                            .setCallback(new TweenCallback() {
                                @Override
                                public void onEvent(int type, BaseTween<?> source) {
                                    if (type == TweenCallback.END) {
                                        entityManager.tank.switchFlashOn();
                                    }
                                }
                            })
                            .start(tweenManager);
                    break;
                default:
                    Tween.to(weather.dayState, DayStateAnimation.AMBIENT_NUM, 3)
                            .target(1)
                            .setCallbackTriggers(TweenCallback.END)
                            .setCallback(new TweenCallback() {
                                @Override
                                public void onEvent(int type, BaseTween<?> source) {
                                    if (type == TweenCallback.END) {
                                        entityManager.tank.switchFlashOff();
                                    }
                                }
                            })
                            .start(tweenManager);
            }

        }
        private void setTweenListeners(){
            tweenManager = new TweenManager();
            Tween.registerAccessor(Body.class, new BodyAnimation());
            Tween.registerAccessor(Sprite.class, new SpriteAnimation());
            Tween.registerAccessor(Actor.class, new ActorAnimation());
            Tween.registerAccessor(EntitysWeaponManger.CustomStack.class, new CustomStackAnimation());
            Tween.registerAccessor(Cell.class, new CellAnimation());
            Tween.registerAccessor(Camera.class, new CameraAnimation());
            Tween.registerAccessor(ProgressBar.class, new ProgressBarAnimation());
            Tween.registerAccessor(ClientScore.class, new ClientScoreAnimation());
            Tween.registerAccessor(DayState.class, new DayStateAnimation());
        }
        private void setupListeners(){
            InputMultiplexer multi = new InputMultiplexer();
            multi.addProcessor(ResultPanel);
            multi.addProcessor(stage);
            multi.addProcessor(new GestureDetector(this));
            Gdx.input.setInputProcessor(multi);
        }
        private void setupStages(){
            stage = new Stage(new FitViewport(com.bvisiongames.me.Tanks.cameraWIDTH, com.bvisiongames.me.Tanks.cameraHeight));
            ResultPanel = new Stage(new FitViewport(com.bvisiongames.me.Tanks.cameraWIDTH, com.bvisiongames.me.Tanks.cameraHeight));
        }
        private void setupSkins(){

            Boxes = Assets.GameFileNames.getBoxSkin();
            Tanks = Assets.GameFileNames.getTankSkin();
            Lands = Assets.GameFileNames.getLandSkin();
            Bullets = Assets.GameFileNames.getBulletSkin();
            controllersSkin = Assets.GameFileNames.getControllerSkin();
            gameSkin = Assets.GameFileNames.getGameSkin();

            //initiate the skins for the effects skins and their textures
            EffectsVariables.initiate();

        }
        private void setUpFonts(){

            defaultFonts = Assets.assetManager.get(Assets.GameFileNames.GameFonts.defaultFontsString, BitmapFont.class);

            titleFont = Assets.assetManager.get(Assets.GameFileNames.GameFonts.titleFontString, BitmapFont.class);

            smallSizeFont = Assets.assetManager.get(Assets.GameFileNames.GameFonts.smallSizeFontString, BitmapFont.class);

            scrollFonts = Assets.assetManager.get(Assets.GameFileNames.GameFonts.scrollFontsString, BitmapFont.class);

            topBarGameFont = Assets.assetManager.get(Assets.GameFileNames.GameFonts.topBarGameFontString, BitmapFont.class);

        }

        /**
         * add the player properties
         */
        private void setPlayerProperties(){
        //proceed to the game play and set a position for the tank and define everything required to play
        // and set the base

        EntitManager.tank = new Tank("-1",
                    new Vector2(com.bvisiongames.me.Tanks.cameraWIDTH/2, com.bvisiongames.me.Tanks.cameraHeight/2),
                    180* MathUtils.degreesToRadians,
                    Tank.TANKSTYPES.valueOf(com.bvisiongames.me.Tanks.gameState.getUserTankType()),
                    Tank.TANKPILOT.MAIN_PLAYER);
        entityManager.tank.addFrontLight();
        entityManager.tank.switchFlashOff();

        //start the game
        entityManager.startGame();

        //animate camera to the place of the tank
        Tween.to(MultiGameScreen.camera, CameraAnimation.POSITION_XY, 1)
                .target(EntitManager.tank.body.getPosition().x* ConstantVariables.PIXELS_TO_METERS,
                        EntitManager.tank.body.getPosition().y* ConstantVariables.PIXELS_TO_METERS)
                .start(MultiGameScreen.tweenManager);

        }

        @Override
        public void update() {

            if(propertiesLoaded){

                //update the ingame panels
                this.inGameScreens.update(Gdx.graphics.getDeltaTime());

                //update the small loading animation
                this.smallLoading.update(Gdx.graphics.getDeltaTime());

                //update the particle effect manager
                this.particleEffectsManager.update(Gdx.graphics.getDeltaTime());

                //update the shape renderer
                shapeRenderer.setProjectionMatrix(camera.combined);

                //update tween manager
                if(box2dLifeCycle)
                    tweenManager.update(Gdx.graphics.getDeltaTime());

                //update stages
                stage.act();
                ResultPanel.act();

                //update main camera and the box2d camera
                camera.update();
                box2dCamera.update();
                //update the position of box2d camera to the main camera
                box2dCamera.position.set(camera.position);
                box2dCamera.zoom = camera.zoom;
                box2dCamera.combined.scl(ConstantVariables.PIXELS_TO_METERS);

                //update world
                if(box2dLifeCycle) {
                    WORLD.step(Gdx.graphics.getDeltaTime(), 6, 2);
                    //rayHandler.update();
                }

                //update entity manager 1
                entityManager.update1();

                //update entity manger 2
                entityManager.update2();

                //dispose the bodies to be disposed
                this.bodyDisposer.dispose();

                //sync the object updater
                this.objectUpdater.sync();

            }else{

                if(assetLoaderTriggered){
                    com.bvisiongames.me.Tanks.SocketStandBy.show();
                    com.bvisiongames.me.Tanks.SocketStandBy.percentDisplay.show();
                    assetLoaderTriggered = false;
                    Assets.loadGameScreenAssets();
                }else if(Assets.assetManager.update()){
                    setupScreen();
                    com.bvisiongames.me.Tanks.SocketStandBy.hide();
                }else if(!Assets.assetManager.update()){
                    com.bvisiongames.me.Tanks.SocketStandBy.percentDisplay.updatePercent((int)(Assets.assetManager.getProgress()*100));
                }

            }

        }

        @Override
        public void render(SpriteBatch batch) {

            //fps logger
            //fpsLogger.log();

            //batch projection matrix adjustment
            batch.setProjectionMatrix(camera.combined);

            if(propertiesLoaded){

                //rendering sprites in objects
                batch.begin();
                entityManager.render(batch);
                batch.end();

                //update and render the handler
                /*
                rayHandler.setCombinedMatrix(box2dCamera.combined,
                        camera.position.x, camera.position.y,
                        camera.viewportWidth * ConstantVariables.PIXELS_TO_METERS,
                        camera.viewportHeight * ConstantVariables.PIXELS_TO_METERS);
                rayHandler.render();
                */

                //render the particle effect manager objects
                this.particleEffectsManager.render(batch);

                //render stages
                stage.draw();
                ResultPanel.draw();

                //render box2d debugger
                //this.debugRenderer.render(WORLD, box2dCamera.combined);

                /*
                //render shapes
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.RED);

                //render target destination
                TankIntelligi intllegi = (entityManager.otherTanks.getTank(0.0f) != null)?
                                            entityManager.otherTanks.getTank(0.0f).getArtificialInt()
                                            : null;

                if(intllegi != null){
                    shapeRenderer.circle(
                            intllegi.getAction().getDestinationPoint().x * ConstantVariables.PIXELS_TO_METERS,
                            intllegi.getAction().getDestinationPoint().y * ConstantVariables.PIXELS_TO_METERS,
                            5);

                    shapeRenderer.setColor(Color.BLUE);
                    //render all the patrol points
                    RoutePlanner routePlanner = intllegi.artificialMemoryManager.tankIntelliDataAnalyzer.intelliDataPointsManager.routePlanner;
                    int length = routePlanner.patrolPoints.size();
                    for (int i = 0; i < length; i++){
                        shapeRenderer.circle(routePlanner.patrolPoints.get(i).coordinates.x * ConstantVariables.PIXELS_TO_METERS,
                                routePlanner.patrolPoints.get(i).coordinates.y * ConstantVariables.PIXELS_TO_METERS,
                                5);
                    }

                    //render the scanner and route patrol point chosen
                    if(routePlanner.tmpChosenPoint != null){
                        shapeRenderer.setColor(Color.BLUE);
                        shapeRenderer.rectLine(routePlanner.tmpChosenPoint.coordinates.x * ConstantVariables.PIXELS_TO_METERS,
                                routePlanner.tmpChosenPoint.coordinates.y * ConstantVariables.PIXELS_TO_METERS,
                                intllegi.tank.getPosition().x * ConstantVariables.PIXELS_TO_METERS,
                                intllegi.tank.getPosition().y * ConstantVariables.PIXELS_TO_METERS,
                                2);
                    }

                    shapeRenderer.setColor(Color.RED);
                    if(intllegi.nearbyAreaScanner.lowestEnergyPoint != null){
                        shapeRenderer.rectLine(
                                intllegi.nearbyAreaScanner.lowestEnergyPoint.coordinates.x * ConstantVariables.PIXELS_TO_METERS,
                                intllegi.nearbyAreaScanner.lowestEnergyPoint.coordinates.y * ConstantVariables.PIXELS_TO_METERS,
                                intllegi.tank.getPosition().x * ConstantVariables.PIXELS_TO_METERS,
                                intllegi.tank.getPosition().y * ConstantVariables.PIXELS_TO_METERS,
                                2);
                    }
                }

                shapeRenderer.end();
                */

                //render the small loading animation
                this.smallLoading.render(stage.getBatch());

            }

        }

        @Override
        public void resize(int width, int height) {

            camera.resize();

        }

        @Override
        public void dispose() {

            //set the box2dlife to false
            box2dLifeCycle = false;

            if(propertiesLoaded){
                //dispose the object updater
                objectUpdater.dispose();
                //objects dispose
                entityManager.dispose();
            }

            //dispose skins
            Assets.disposeGameAssets();

            if(propertiesLoaded){
                //dispose stages
                stage.dispose();
                ResultPanel.dispose();
                //dispose the ray handler
                rayHandler.dispose();
                //dispose the world
                WORLD.dispose();
                //dispose the loading animation
                this.smallLoading.dispose();

                //dispose skins
                gameSkin.dispose();
                Boxes.dispose();
                Tanks.dispose();
                Lands.dispose();
                Bullets.dispose();
                controllersSkin.dispose();

                //dispose the particle effects
                this.particleEffectsManager.dispose();

            }

        }

        @Override
        public void pause() {

            if(propertiesLoaded){
                inGameScreens.inGameSettings.AutoperformClickPause();
            }

        }

        @Override
        public void resume() {

            if(propertiesLoaded){

            }

        }

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {

            entityManager.pan(x,y, deltaX,deltaY);

            return true;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {

            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {

            if(initialDistance>distance){

                if(camera.zoom<1){
                    camera.zoom += 0.005f;
                }

            }else if(initialDistance<distance){

                if(camera.zoom > 0.5f){
                    camera.zoom -= 0.005f;
                }

            }

            return true;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

    /**
     * game interface for pause or resume
     */
    public interface GameInterface{
        void pause();
        void resume();
    }

    /**
     * game day state.
     */
    public enum DAYSTATE{
        DAY, NIGHT
    }

}
