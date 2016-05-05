package com.bvisiongames.me.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Json;
import com.bvisiongames.me.Maps.MapManager;
import com.bvisiongames.me.Weapons.Weapon;
import com.bvisiongames.me.Weapons.WeaponsManager;
import com.bvisiongames.me.buildings.BaseBuilding;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.controllers.Fire;
import com.bvisiongames.me.controllers.Joystick;
import com.bvisiongames.me.displays.ClientScore;
import com.bvisiongames.me.displays.DamageDisplay;
import com.bvisiongames.me.effects.Effect;
import com.bvisiongames.me.protocols.TankIntelligi;
import com.bvisiongames.me.rewards.CoinReward;
import com.bvisiongames.me.rewards.CoinRewardManager;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.Assets;
import com.bvisiongames.me.settings.TankBulletsProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sultan on 7/6/2015.
 */
public class EntitManager {

    //GAME PROPERTIES
    public static GAMESTATE GameState = GAMESTATE.WATCH;
    //Game LifeCyle
    private GAMELIVE gamelive = GAMELIVE.PAUSED;

    //create map
    public static MapManager mapManager;
    //create objects
    public static Tank tank;

    //other players tanks
    public static otherPlayers otherTanks;

    //create displays controllers
    public static Controllers controllers;
    //-------

    //tank interface
    public static TankIntelligi.TankInterface tankInterface;

    //bullets variables
    public static WeaponsManager weaponsManager = new WeaponsManager();

    //list of effects
    public static List<Effect> effectsList = new ArrayList<Effect>();

    //Reward coins manager
    public static CoinRewardManager coinRewardManager;

    //camera manager
    private CameraManager cameraManager = new CameraManager();

    //level database information
    public static TankBulletsProperty tankBulletsProperty;

    //world contact listener
    private ContactListener worldContactListener = new ContactListener() {
        @Override
        public void beginContact(final Contact contact) {

            //check bullets first
            LooperForBullets(contact);
            //check coins second
            LooperForCoins(contact);
            //check for ai entity collisions
            LooperForAIEntities(contact);

        }

        @Override
        public void endContact(final Contact contact) {

            //check for ai entity collision on end.
            LooperForAIEntitiesEnd(contact);

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

            //looper for ai pre solver
            LooperForAIEntitiesPreSolver(contact);

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }

    };

    /**
     * initiator.
     */
    public EntitManager(){

        //initiate other level's entities
        otherTanks = new otherPlayers();

        //initialize controllers
        controllers = new Controllers();
        controllers.createControllers();
        //hide controllers
        controllers.hideControllers();

        //this listens for collisions
        MultiGameScreen.WORLD.setContactListener(this.worldContactListener);

        //load level tank properties
        String data = Assets.GameFileNames.GameTextFiles.getLevelString();
        this.tankBulletsProperty = new Json().fromJson(TankBulletsProperty.class, data);

        //intiate the map
        mapManager = new MapManager();
        mapManager.setMaptype(Tanks.gameState.maps.getLevel());
        mapManager.initiateMap();

        //initiate the coinreward manager
        coinRewardManager = new CoinRewardManager();

        //tank interface
        tankInterface = new TankIntelligi.TankInterface() {
            @Override
            public void shoot(final Tank tank) {

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        weaponsManager.addBullet(Weapon.BULLETSTYPE.BULLET1, tank);
                    }
                });

            }

            @Override
            public void shootElectricShock(final Tank tank) {

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        weaponsManager.addBullet(Weapon.BULLETSTYPE.ELECTRICBULLET, tank);
                    }
                });

            }
        };

    }

    //for ai entity collision detection on pre solve collision.
    private synchronized void LooperForAIEntitiesPreSolver(Contact contact){
        //validation for the nearby sensors ai
        if( contact.getFixtureA().getBody().getUserData() != null
                && contact.getFixtureA().getBody().getUserData() instanceof Tank
                && ((Tank)contact.getFixtureA().getBody().getUserData()).getArtificialInt() != null ){

            ((Tank)contact.getFixtureA().getBody().getUserData()).getArtificialInt()
                    .getSensorsManager().collisionFixtureAnalyzerPreSolver(contact.getFixtureA(), contact.getFixtureB(), contact);

        }else
        if( contact.getFixtureB().getBody().getUserData() != null
                && contact.getFixtureB().getBody().getUserData() instanceof Tank
                && ((Tank)contact.getFixtureB().getBody().getUserData()).getArtificialInt() != null){

            ((Tank)contact.getFixtureB().getBody().getUserData()).getArtificialInt()
                    .getSensorsManager().collisionFixtureAnalyzerPreSolver(contact.getFixtureB(), contact.getFixtureA(), contact);

        }

    }
    //for ai entity collision detection on begin collision.
    private synchronized void LooperForAIEntities(Contact contact){
        //validation for the nearby sensors ai
        if( contact.getFixtureA().getBody().getUserData() != null
                && contact.getFixtureA().getBody().getUserData() instanceof Tank
                && ((Tank)contact.getFixtureA().getBody().getUserData()).getArtificialInt() != null ){

            ((Tank)contact.getFixtureA().getBody().getUserData()).getArtificialInt()
                    .getSensorsManager().collisionFixtureAnalyzer(contact.getFixtureA(), contact.getFixtureB(),
                    contact.getWorldManifold());

        }else
        if( contact.getFixtureB().getBody().getUserData() != null
                && contact.getFixtureB().getBody().getUserData() instanceof Tank
                && ((Tank)contact.getFixtureB().getBody().getUserData()).getArtificialInt() != null){

            ((Tank)contact.getFixtureB().getBody().getUserData()).getArtificialInt()
                    .getSensorsManager().collisionFixtureAnalyzer(contact.getFixtureB(), contact.getFixtureA(),
                    contact.getWorldManifold());

        }

        /*
        //validation for the external sensors ai
        if(contact.getFixtureA().getBody().getUserData() != null
                && contact.getFixtureA().getBody().getUserData() instanceof TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity
                && ( ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureA().getBody().getUserData())
                        .externalsensor == TankIntelligi.EXTERNALSENSOR.SEARCH_EMPTY_NEARBY_AREA_SENSOR
                        ||
                    ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureA().getBody().getUserData())
                            .externalsensor == TankIntelligi.EXTERNALSENSOR.SEARCH_EMPTY_NEARBY_ROUTE_SENSOR
                    )
                ){

            ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureA().getBody().getUserData())
                    .emptyCircleAreaSensor.reportCollisionBegin(contact.getFixtureA(), contact.getFixtureB());

        }else
        if( contact.getFixtureB().getBody().getUserData() != null
                && contact.getFixtureB().getBody().getUserData() instanceof TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity
                && (
                    ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureB().getBody().getUserData())
                            .externalsensor == TankIntelligi.EXTERNALSENSOR.SEARCH_EMPTY_NEARBY_AREA_SENSOR
                    ||
                    ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureB().getBody().getUserData())
                            .externalsensor == TankIntelligi.EXTERNALSENSOR.SEARCH_EMPTY_NEARBY_ROUTE_SENSOR
                    )
                ){

            ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureB().getBody().getUserData())
                    .emptyCircleAreaSensor.reportCollisionBegin(contact.getFixtureB(), contact.getFixtureA());

        }
        */

    }
    //for ai entity collision detection on end collision.
    private synchronized void LooperForAIEntitiesEnd(Contact contact){

        //validation for nearby ai sensors.
        if( contact.getFixtureA().getBody().getUserData() != null
                && contact.getFixtureA().getBody().getUserData() instanceof Tank
                && ((Tank)contact.getFixtureA().getBody().getUserData()).getArtificialInt() != null ){

            ((Tank)contact.getFixtureA().getBody().getUserData()).getArtificialInt()
                    .getSensorsManager().collisionFixtureAnalyzerEnd(contact.getFixtureA(), contact.getFixtureB());

        }else
        if( contact.getFixtureB().getBody().getUserData() != null
                && contact.getFixtureB().getBody().getUserData() instanceof Tank
                && ((Tank)contact.getFixtureB().getBody().getUserData()).getArtificialInt() != null ){

            ((Tank)contact.getFixtureB().getBody().getUserData()).getArtificialInt()
                    .getSensorsManager().collisionFixtureAnalyzerEnd(contact.getFixtureB(), contact.getFixtureA());

        }

        /*
        //validation for the external sensors ai
        if( contact.getFixtureA().getBody().getUserData() != null
                && contact.getFixtureA().getBody().getUserData() instanceof TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity
                && (
                    ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureA().getBody().getUserData())
                            .externalsensor == TankIntelligi.EXTERNALSENSOR.SEARCH_EMPTY_NEARBY_AREA_SENSOR
                    ||
                    ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureA().getBody().getUserData())
                            .externalsensor == TankIntelligi.EXTERNALSENSOR.SEARCH_EMPTY_NEARBY_ROUTE_SENSOR
                    )
                ){

            ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureA().getBody().getUserData())
                    .emptyCircleAreaSensor.reportCollisionEnd(contact.getFixtureA(), contact.getFixtureB());

        }else
        if( contact.getFixtureB().getBody().getUserData() != null
                && contact.getFixtureB().getBody().getUserData() instanceof TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity
                && (
                    ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureB().getBody().getUserData())
                            .externalsensor == TankIntelligi.EXTERNALSENSOR.SEARCH_EMPTY_NEARBY_AREA_SENSOR
                    ||
                    ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureB().getBody().getUserData())
                            .externalsensor == TankIntelligi.EXTERNALSENSOR.SEARCH_EMPTY_NEARBY_ROUTE_SENSOR
                    )
                ){

            ((TankIntelligi.ExternalSensors.EmptyCircleAreaSensor.BodyIdentity)contact.getFixtureB().getBody().getUserData())
                    .emptyCircleAreaSensor.reportCollisionEnd(contact.getFixtureB(), contact.getFixtureA());

        }
        */

    }
    //for bullets collision detection
    private synchronized void LooperForBullets(Contact contact){

           if(contact.getFixtureA() != null && contact.getFixtureA().getBody().getUserData() != null
                   && contact.getFixtureA().getBody().isBullet() //check if it is a bullet
               && contact.getFixtureA().getBody().getUserData() instanceof com.bvisiongames.me.Weapons.Weapon &&
                   ((com.bvisiongames.me.Weapons.Weapon)contact.getFixtureA().getBody().getUserData()).isStatus() && ((com.bvisiongames.me.Weapons.Weapon)contact.getFixtureA().getBody().getUserData()).isVelocityStatus()
                       && !contact.getFixtureA().isSensor() && !contact.getFixtureB().isSensor()
                    //and make sure that fixture B is not a scoping sensor
                   && ( (contact.getFixtureB().getUserData() != null && !(contact.getFixtureB().getUserData() instanceof TankIntelligi.NEARBYSENSOR) )
                            || contact.getFixtureB().getUserData() == null  )
                   ){
                        //if fixture B is a tank
                        checkTankCollisionBullet(contact.getFixtureB(), contact.getFixtureA());
                        //if fixture A is the bullet
                        if (contact.getFixtureA() != null)
                            ((com.bvisiongames.me.Weapons.Weapon)contact.getFixtureA().getBody().getUserData()).initiateExplosion();
           }

            if(contact.getFixtureB() != null && contact.getFixtureB().getBody().getUserData() != null
                    && contact.getFixtureB().getBody().isBullet() //check if it is a bullet
                    && contact.getFixtureB().getBody().getUserData() instanceof com.bvisiongames.me.Weapons.Weapon &&
                    ((com.bvisiongames.me.Weapons.Weapon)contact.getFixtureB().getBody().getUserData()).isStatus() && ((com.bvisiongames.me.Weapons.Weapon)contact.getFixtureB().getBody().getUserData()).isVelocityStatus()
                    && !contact.getFixtureA().isSensor() && !contact.getFixtureB().isSensor()
                    //and make sure that fixture A is not a scoping sensor
                    && ( (contact.getFixtureA().getUserData() != null && !(contact.getFixtureA().getUserData() instanceof TankIntelligi.NEARBYSENSOR) )
                            || contact.getFixtureA().getUserData() == null  )
                    ){
                        //if fixture A is a tank
                        checkTankCollisionBullet(contact.getFixtureA(), contact.getFixtureB());
                        //if fixture B is the bullet
                        if(contact.getFixtureB() != null)
                            ((com.bvisiongames.me.Weapons.Weapon)contact.getFixtureB().getBody().getUserData()).initiateExplosion();
            }

    }
    //hitFixture: is the other body that got hit
    //below we check whether it got hit by a tank
    //enemysFixture: is the bullet's fixture, it contains the owner's tank instance
    private void checkTankCollisionBullet(Fixture hitFixture, Fixture enemysFixture){

        if(hitFixture.getBody().getUserData() != null
                && hitFixture.getBody().getUserData() instanceof Tank
                && ((Tank)hitFixture.getBody().getUserData()).getTankState()){
            //check who got hit
            if( EntitManager.tank != null && EntitManager.tank.tankProperties.id
                    == ((Tank)hitFixture.getBody().getUserData()).tankProperties.id){
                //main player got hit
                //the disposing of the tank instance will be done by the damageDisplayer instance
                EntitManager.tank.decrementHealth(((com.bvisiongames.me.Weapons.Weapon) enemysFixture.getBody().getUserData()).bulletstype);
                //update the total number of kills for the owner of this bullet
                if(EntitManager.tank.tankProperties.health <= 0){
                    ((Weapon)enemysFixture.getBody().getUserData()).getOwner().tankProperties.kills++;
                }
                //notify the ai of the owner that it has a bullet that hit a target
                ((Weapon)enemysFixture.getBody().getUserData()).getOwner().tankProperties
                        .notifyAIofScore((Weapon)enemysFixture.getBody().getUserData());
                //update the health bar
                Controllers.damageDisplay.updateHealth(EntitManager.tank.tankProperties.health);
            }else{
                //computer got hit
                //check if computer has health greater than zero
                if(((Tank)hitFixture.getBody().getUserData()).tankProperties.health > 0){
                    //decrement health of the cpu tank
                    ((Tank)hitFixture.getBody().getUserData())
                            .decrementHealth(((com.bvisiongames.me.Weapons.Weapon)enemysFixture.getBody().getUserData()).bulletstype);
                    //notify the hit cpu tank of damage
                    ((Tank)hitFixture.getBody().getUserData()).tankProperties.notifyAIofDamage(((com.bvisiongames.me.Weapons.Weapon)enemysFixture.getBody().getUserData()).getOwner());
                }else{
                    //if not greater than zero then dispose it
                    //who shot the bullet get rewarded with score if the enemy dies
                    if( EntitManager.tank != null &&
                            ((Weapon)enemysFixture.getBody().getUserData()).getOwner().tankProperties.id
                                    == EntitManager.tank.tankProperties.id){
                        //main player shot the bullet
                        EntitManager.tank.tankProperties.kills++;
                        Controllers.clientScore.AnimateKillsChange(EntitManager.tank.tankProperties.kills);
                        //notify the ai of damage
                        ((Weapon)enemysFixture.getBody().getUserData()).getOwner().tankProperties.notifyAIofDamage((Entity) hitFixture.getBody().getUserData());
                    }else{
                        //other player shot the bullet
                        ((Weapon)enemysFixture.getBody().getUserData()).getOwner().tankProperties.kills++;
                        //notify the ai of score
                        ((Weapon)enemysFixture.getBody().getUserData()).getOwner().tankProperties
                                .notifyAIofScore((Weapon) enemysFixture.getBody().getUserData());
                    }
                    //first shutown the ai and then dispose the tank
                    ((Tank)hitFixture.getBody().getUserData()).flagToDestroy();
                }
            }
        }

    }
    //end of bullet collision detection
    //for coins collision detection
    private synchronized void LooperForCoins(Contact contact){

        if(
                //if fixture A is a coin
                (contact.getFixtureA() != null && contact.getFixtureA().getBody().getUserData() != null
                        && contact.getFixtureA().getBody().getUserData() instanceof CoinReward &&
                        ((CoinReward)contact.getFixtureA().getBody().getUserData()).isAlive()
                    && contact.getFixtureB() != null && contact.getFixtureB().getBody().getUserData() != null
                        && contact.getFixtureB().getBody().getUserData() instanceof Tank
                )

                ){

                applyCollisionResponses(
                        (Tank)contact.getFixtureB().getBody().getUserData(),
                        (CoinReward)contact.getFixtureA().getBody().getUserData()
                );

        }else if(
                //else if fixture B is a coin
                (contact.getFixtureB() != null && contact.getFixtureB().getBody().getUserData() != null
                        && contact.getFixtureB().getBody().getUserData() instanceof CoinReward &&
                        ((CoinReward)contact.getFixtureB().getBody().getUserData()).isAlive()
                        && contact.getFixtureA() != null && contact.getFixtureA().getBody().getUserData() != null
                        && contact.getFixtureA().getBody().getUserData() instanceof Tank
                )
                ){

            applyCollisionResponses(
                    (Tank)contact.getFixtureA().getBody().getUserData(),
                    (CoinReward)contact.getFixtureB().getBody().getUserData()
            );

        }

    }
    /**
     * takes in the instances of tank and the coin to apply the responses
     * @param tank
     * @param coin
     */
    private void applyCollisionResponses(Tank tank, CoinReward coin){

        if(tank != null && coin != null
                && EntitManager.tank != null
                && tank.tankProperties.id == EntitManager.tank.tankProperties.id
                && tank.tankProperties.id == EntitManager.tank.tankProperties.id){
            coin.initiateCollect();
        }

    }
    //end of coins collision detection

    //update methods
    /**
     * update number 1 for the entity manager.
     */
    public void update1(){

        //update the camera position
        cameraManager.update();

        //update the coin manager
        coinRewardManager.update();

        //update the controllers
        this.controllers.update();

        //update the game play settings
        if(GameState == GAMESTATE.WATCH){
            controllers.fire.hide();
        }else{
            if(gamelive != GAMELIVE.PAUSED)
                controllers.fire.update();

            controllers.fire.show();
        }

        //update objects in the world
        if(GameState == GAMESTATE.WATCH){
            mapManager.update();        //ground updates in watch and start
        }else if(GameState == GAMESTATE.PLAY){

            //when start update ground and main tank
            mapManager.update();

            if(tank != null){
                //only update the main player tank when he has chosen what tank he will use
                tank.UpdateTank_Joystick();
            }
        }

    }
    /**
     * update number 2 for the entity manager.
     */
    public void update2(){

        //object that update when watching the game or playing
        //they don't update when the game is stopped
        if(GameState == GAMESTATE.WATCH || GameState == GAMESTATE.PLAY) {

            //update other tanks
            this.otherTanks.update();

            //update all the entities on map
            //this.updateEntities();

            //update the bullets
            //this.updateBullets();
            this.weaponsManager.update();

        }

    }
    //end of updates

    //render methods
    /**
     * updates and render effects on the map
     */
    //small variables
    private List<Integer> effectsIndexDisposer = new ArrayList<Integer>();
    private void renderAndUpdateEffects(SpriteBatch spriteBatch){

        //update and render all effects
        int len = this.effectsList.size();
        for(int i = 0; i < len; i++){
            Effect effect = this.effectsList.get(i);
            if(!effect.isFinished()){
                effect.render(spriteBatch);
            }else{
                effectsIndexDisposer.add(i);
            }
        }

        //remove the effect if finished
        len = this.effectsIndexDisposer.size();
        for(int i = 0; i < len; i++){
            this.effectsList.remove(this.effectsIndexDisposer.get(i));
        }
        //clear the effect index disposer
        this.effectsIndexDisposer.clear();

    }
    /**
     * render method for the entity manager
     * @param batch
     */
    public void render(SpriteBatch batch){

        if(GameState == GAMESTATE.PLAY){

            //all objects render when game is started
            this.mapManager.render(batch);
            //render the boxes sprites
            //this.renderEntities(batch);
            //render the main tank with controllers if it has been set
            if(this.tank != null){
                this.tank.render(batch);
            }
            //render other tanks when game is started
            this.otherTanks.render(batch);

            //render bullets
            //this.renderBullets(batch);
            this.weaponsManager.render(batch);

            //render top sprites of the map
            mapManager.renderTopSprites(batch);

            //render effects
            this.renderAndUpdateEffects(batch);

            //render entities from the object updater
            MultiGameScreen.objectUpdater.drawEntities(batch);

            //render fire objects
            if(gamelive != GAMELIVE.PAUSED)
                controllers.fire.render(batch);

        }else if(GameState == GAMESTATE.WATCH){
            //only ground and other game objects render when watch is on
            mapManager.render(batch);
            //render the boxes sprites
            //this.renderEntities(batch);
            //render other tanks when game is being watched
            otherTanks.render(batch);

            //render bullets
            //renderBullets(batch);
            this.weaponsManager.render(batch);

            //render top sprites of the map
            mapManager.renderTopSprites(batch);

            //render effects
            renderAndUpdateEffects(batch);

            //render entities from the object updater
            MultiGameScreen.objectUpdater.drawEntities(batch);

        }

    }
    //end of render methods

    public void dispose(){

            //dispose other tanks
            otherTanks.dispose();

            //clear the list
            this.weaponsManager.dispose();
        /*
            //dispose the entities
            int len = this.entities.size();
            for(int i = 0; i < len; i++){
                Entity entity = this.entities.get(i);
                entity.dispose();
            }
        */

            //dispose my tank
            if(tank != null){
                tank.dispose();
                tank = null;
            }

            //dispose effects
            effectsList.clear();

            //dispose the coin manager
            coinRewardManager.dispose();

    }
    public void pause(){

        gamelive = GAMELIVE.PAUSED;

        //pause other tanks
        otherTanks.pause();

    }
    public void resume(){

        gamelive = GAMELIVE.RESUMED;

        //resume other tanks
        otherTanks.resume();

    }

    //getters
    /***
     * get the bases list from the map manager.
     */
    public BaseBuilding[] getBaseBuildingList(){
        return mapManager.getLevel().getBaseBuildings();
    }
    //end of getters


    //setters
    /**
     * start game. (this method reset the client score,
     * change the state of the game to play, reset damage Display, and shows the controllers).
     */
    public void startGame(){
        controllers.clientScore.reset();
        GameState = GAMESTATE.PLAY;
        controllers.showControllers();
        controllers.damageDisplay.resetDamage();
    }
    //end of setters

    public void pan(float x, float y, float deltaX, float deltaY){

        cameraManager.setCameraPan(x, y, deltaX, deltaY);

    }

    /**
     * this class has the methods for the controllers, damageDisplay, and client score.
     */
    public static class Controllers{

        public static Joystick joystick;
        private Fire fire;
        public static DamageDisplay damageDisplay;
        public static ClientScore clientScore;

        /**
         * initiate the instances of all the controllers.
         */
        public void createControllers(){

            damageDisplay = new DamageDisplay();
            joystick = new Joystick();
            fire = new Fire();
            clientScore = new ClientScore();

        }


        /**
         * show the controllers and displays
         */
        public void hideControllers(){
            joystick.hide();
            fire.hide();
            damageDisplay.hide();
            clientScore.hide();
        }

        /**
         * show the controllers and displays
         */
        public void showControllers(){
            joystick.show();
            fire.show();
            damageDisplay.show();
            clientScore.show();
        }

        /**
         * update the controllers.
         */
        public void update(){
            this.joystick.update();
        }

    }

    /**
     * this class controlls the camera movement.
     */
    public class CameraManager{

        private Vector2 cameraPanSpeed  = new Vector2(0, 0);
        private float PanRatio = 0.5f;

        //setters
        /**
         * this method sets the pan variables.
         * @param posX
         * @param posY
         * @param deltaX
         * @param deltaY
         */
        public void setCameraPan(float posX, float posY, float deltaX, float deltaY){
            this.cameraPanSpeed.set(-deltaX, deltaY);
        }
        //end of setters


        //getters

        //end of getters


        //update method
        public void update(){
            if(Math.abs(cameraPanSpeed.x) > PanRatio || Math.abs(cameraPanSpeed.y) > PanRatio ){
                MultiGameScreen.camera.translate(cameraPanSpeed);
                cameraPanSpeed.x = (cameraPanSpeed.x > 0)? cameraPanSpeed.x - PanRatio: cameraPanSpeed.x + PanRatio;
                cameraPanSpeed.y = (cameraPanSpeed.y > 0)? cameraPanSpeed.y - PanRatio: cameraPanSpeed.y +PanRatio;
            }
        }
        //end of update method

    }

    /**
     * this is the enum of the game state e.g play or watch.
     */
    public enum GAMESTATE{
        WATCH, PLAY
    }
    /**
     * this is the enum of the game pause or resume state.
     */
    public enum GAMELIVE{
        PAUSED, RESUMED
    }

}
