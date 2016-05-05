package com.bvisiongames.me.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Weapons.Weapon;
import com.bvisiongames.me.buildings.BaseBuilding;
import com.bvisiongames.me.protocols.TankIntelligi;
import com.bvisiongames.me.rewards.CoinReward;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.settings.GeneralMethods;
import box2dLight.ConeLight;

/**
 * Created by Sultan on 6/23/2015.
 */
public class Tank extends Entity{

    //sprites
    public Sprite tank; //sprite of the tank body

    //muzzle effect
    public ParticleEffect muzzleEffect,
    //animations for AI controller animation over the tank.
                        controllerGlow;
    //control glow visibility
    public boolean isVisibleControlGlow = false;

    //body objects
    public Vector2 BodyDimensions;

    //reference to the base building
    public BaseBuilding base;

    //Artificial intelli instance
    public TankIntelligi tankIntelligi;

    //properties of the tank instance
    public TankProperties tankProperties = new TankProperties();

    //electric shock instance
    public TankElectricShocked tankElectricShocked = new TankElectricShocked();

    //flashlight
    private ConeLight flashLight;

    //saves the diagonal of this tank in pixels
    private float diagonal = 0;

    /**
     * initiator.
     * @param id
     * @param initialPos
     * @param angle
     * @param tankType
     * @param tankpilot
     */
    public Tank(String id, Vector2 initialPos, float angle, TANKSTYPES tankType, TANKPILOT tankpilot){

        //setup the tank properties
        this.tankProperties.id = id;
        this.tankProperties.tankType = tankType;
        this.tankProperties.tankpilot = tankpilot;

        //setup the tank sprite
        tank = new Sprite(MultiGameScreen.Tanks.getRegion(tankType.toString()));

        //solve for the diagonal
        this.diagonal = (float)Math.sqrt( tank.getWidth()*tank.getWidth() + tank.getHeight()*tank.getHeight() );

        //setup the body properties
        this.setupBody(initialPos, angle);

        //status of this tank
        status = true;

        //setup the tank's speed properties
        setUpSpeedRatio();

        //add the muzzle effect
        this.muzzleEffect = new ParticleEffect();
        this.muzzleEffect.load(MultiGameScreen.particleEffectsManager.particleEffectsFilesManager.muzzleEffectFile,
                MultiGameScreen.particleEffectsManager.particleEffectsFilesManager.muzzleImageFileDir);

        //add the tank explosion effect
        MultiGameScreen.particleEffectsManager.defaultTankExplosionManager.addExplosionEffect();

        //add the lightning effect, if it gets shocked
        MultiGameScreen.particleEffectsManager.lightningManager.addLightnings();

    }

    //body methods
    /**
     * setup the body for this tank.
     * @param initialPos
     * @param angle
     */
    private void setupBody(Vector2 initialPos, float angle){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((initialPos.x + tank.getWidth()/2)/ConstantVariables.PIXELS_TO_METERS,
                (initialPos.y + tank.getHeight()/2)/ConstantVariables.PIXELS_TO_METERS);
        bodyDef.angle = angle;

        body = MultiGameScreen.WORLD.createBody(bodyDef);

        //calculate the dimensions of body (width and height)
        BodyDimensions = new Vector2(tank.getWidth() / (2 * ConstantVariables.PIXELS_TO_METERS),
                tank.getHeight() / (2 * ConstantVariables.PIXELS_TO_METERS));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(BodyDimensions.x, BodyDimensions.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        setTankDensity(fixtureDef);
        fixtureDef.friction = 0.5f;

        //set the damping of the body
        body.setLinearDamping(3f);
        body.setAngularDamping(3f);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        shape.dispose();
    }
    /**
     * setup the max linear speed and angular speed of the tank's body.
     */
    private void setUpSpeedRatio(){

        float forewardRatio = 1.2f, rotationRatio = 1.5f;

        switch (this.tankProperties.tankType){

            case E100:
                this.tankProperties.MaxVelocity = GetMaxTankSpeed(TANKSTYPES.E100)/forewardRatio;
                this.tankProperties.MaxRotationVelocity = 3/rotationRatio;
                break;
            case KV2:
                this.tankProperties.MaxVelocity = GetMaxTankSpeed(TANKSTYPES.KV2)/forewardRatio;
                this.tankProperties.MaxRotationVelocity = 2.5f/rotationRatio;
                break;
            case M6:
                this.tankProperties.MaxVelocity = GetMaxTankSpeed(TANKSTYPES.M6)/forewardRatio;
                this.tankProperties.MaxRotationVelocity = 2.4f/rotationRatio;
                break;
            case PZKPFWIV:
                this.tankProperties.MaxVelocity = GetMaxTankSpeed(TANKSTYPES.PZKPFWIV)/forewardRatio;
                this.tankProperties.MaxRotationVelocity = 2.5f/rotationRatio;
                break;
            case PZKPFWIVG:
                this.tankProperties.MaxVelocity = GetMaxTankSpeed(TANKSTYPES.PZKPFWIVG)/forewardRatio;
                this.tankProperties.MaxRotationVelocity = 2.4f/rotationRatio;
                break;
            case T34:
                this.tankProperties.MaxVelocity = GetMaxTankSpeed(TANKSTYPES.T34)/forewardRatio;
                this.tankProperties.MaxRotationVelocity = 2f/rotationRatio;
                break;
            case VK3601H:
                this.tankProperties.MaxVelocity = GetMaxTankSpeed(TANKSTYPES.VK3601H)/forewardRatio;
                this.tankProperties.MaxRotationVelocity = 2f/rotationRatio;
                break;

        }

    }

    /**
     * setup the tank density based on type.
     * @param def
     * the fixture Def of the body to adjust it's density.
     */
    private void setTankDensity(FixtureDef def){

        switch (this.tankProperties.tankType){
            case KV2:
                def.density = 8f;
                this.tankProperties.density = 8f;
                break;
            case VK3601H:
                def.density = 5f;
                this.tankProperties.density = 5f;
                break;
            case T34:
                def.density = 4f;
                this.tankProperties.density = 4f;
                break;
            case PZKPFWIVG:
                def.density = 7f;
                this.tankProperties.density = 7f;
                break;
            case PZKPFWIV:
                def.density = 6f;
                this.tankProperties.density = 6f;
                break;
            case E100:
                def.density = 4f;
                this.tankProperties.density = 4f;
                break;
            case M6:
                def.density = 7f;
                this.tankProperties.density = 7f;
                break;
        }

    }
    //end of body methods

    //setters
    /**
     * add front light.
     */
    public void addFrontLight(){
        this.flashLight = new ConeLight(MultiGameScreen.rayHandler,
                120, new Color(1, 1, 1, 0.8f), 50,
                this.body.getPosition().x,
                this.body.getPosition().y,
                body.getAngle()*MathUtils.radiansToDegrees + 90,
                30
        );
        this.flashLight.attachToBody(body, getBodyDimensions().x / (2 * ConstantVariables.PIXELS_TO_METERS), 1, 90);
        this.flashLight.setContactFilter(
                (short) ConstantVariables.Box2dVariables.FLASH_LIGHT,
                (short) ConstantVariables.Box2dVariables.SENSOR,
                (short) 1);
        this.flashLight.setSoftnessLength(0.5f);
    }
    /**
     * switch light on.
     */
    public void switchFlashOn(){
        if(this.flashLight != null){
            this.flashLight.setActive(true);
        }
    }
    /**
     * switch light off.
     */
    public void switchFlashOff(){
        if(this.flashLight != null){
            this.flashLight.setActive(false);
        }
    }
    /**
     * this add the artificial intelli and the glow animation to the tank.
     * @param artificialInt
     */
    public void addAI(TankIntelligi artificialInt){
        this.tankIntelligi = artificialInt;
        this.tankIntelligi.start();

        //add the control glow effect
        this.controllerGlow = new ParticleEffect();
        this.controllerGlow.load(MultiGameScreen.particleEffectsManager.particleEffectsFilesManager.controlGlowEffectFile,
                                MultiGameScreen.particleEffectsManager.particleEffectsFilesManager.controlGlowImageFileDir);
        this.controllerGlow.start();

    }
    /**
     * set a base building for this tank.
     * @param base
     * the base building to be set up for this tank.
     */
    public void setTankBase(BaseBuilding base){
        this.base = base;
    }
    //end of setters

    //updaters and renderers methods
    /**
     * main update method of the tank
     */
    //small variables for the update mthod
    private float updateD = 0;
    private Vector2 updateLinearVelocity = new Vector2(0, 0);
    public void update(Vector2 vectorControll, Vector2 MaxVectorDimensions) {

        //update lives of the base (if there is a base)
        if(base != null)
            base.updateLives(this.tankProperties.health);

        //update the position, velocity,etc of the tank
        this.setPosition(body.getPosition().x, body.getPosition().y);
        this.setBodyAngle(body.getAngle() + (float)Math.PI/2);
        this.setSpeed(body.getLinearVelocity().len());
        this.setAngularVelocity(body.getAngularVelocity());
        this.setDirection();

        //update and do the math for the motion of the tank if it is not electrically shocked
        if(this.tankIntelligi == null){
            if(!this.tankElectricShocked.IsElectricallyShocked){
                body.setAngularVelocity(-vectorControll.x * (this.tankProperties.MaxRotationVelocity / (MaxVectorDimensions.x / 2)));
                updateD = vectorControll.y * ( this.tankProperties.MaxVelocity/ (MaxVectorDimensions.y/2) );
                this.updateLinearVelocity.set(
                        updateD * (float) Math.cos(this.getBodyAngle()),
                        updateD * (float) Math.sin(this.getBodyAngle()));
                body.setLinearVelocity(this.updateLinearVelocity.x, this.updateLinearVelocity.y);
            }else{

                //update the electrically shocked instance
                this.tankElectricShocked.update(Gdx.graphics.getDeltaTime());

                body.setAngularVelocity(0);
                body.setLinearVelocity(0, 0);
            }
        }else{

            if(!this.tankElectricShocked.IsElectricallyShocked){
                body.setAngularVelocity(-vectorControll.x * (this.tankProperties.MaxRotationVelocity / (MaxVectorDimensions.x / 2)));
                updateD = vectorControll.y * ( this.tankProperties.MaxVelocity/ (MaxVectorDimensions.y/2) );
                this.updateLinearVelocity.set(
                        updateD * (float) Math.cos(body.getAngle() + Math.PI / 2),
                        updateD * (float) Math.sin(body.getAngle() + Math.PI / 2));
                body.setLinearVelocity(this.updateLinearVelocity.x, this.updateLinearVelocity.y);
            }else{

                this.tankElectricShocked.update(Gdx.graphics.getDeltaTime());

                body.setAngularVelocity(0);
                body.setLinearVelocity(0, 0);

            }

        }

        //update the muzzle fire effect
        this.muzzleEffect.update(Gdx.graphics.getDeltaTime());

        //update the sprites position and angle rotation
        tank.setPosition(((body.getPosition().x*ConstantVariables.PIXELS_TO_METERS) - tank.getWidth() / 2),
                            ((body.getPosition().y*ConstantVariables.PIXELS_TO_METERS) - tank.getHeight() / 2) );
        tank.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

    }
    /**
     * overriden method of entity class.
     */
    @Override
    public void update(){

        //update the position, velocity,etc of the tank
        this.setPosition(body.getPosition().x, body.getPosition().y);
        this.setBodyAngle(body.getAngle() + (float)Math.PI/2);
        this.setSpeed(body.getLinearVelocity().len());
        this.setAngularVelocity(body.getAngularVelocity());
        this.setDirection();

        //update the sprites position and angle rotation
        tank.setPosition(((body.getPosition().x * ConstantVariables.PIXELS_TO_METERS) - tank.getWidth() / 2),
                ((body.getPosition().y * ConstantVariables.PIXELS_TO_METERS) - tank.getHeight() / 2));
        tank.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

    }
    /**
     * updates the sprite's position and sync some AI methods with this main thread.
     * this method is used for other cpu tanks.
     */
    //small variables for ai update
    private Vector2 controlGlowV = new Vector2(0, 0);
    public void ai_update(){

        if(tankIntelligi != null){
            //calls the sync method on the AI
            this.tankIntelligi.sync();

            //set the x and y of control glow v
            this.controlGlowV.set(this.getPosition().x * ConstantVariables.PIXELS_TO_METERS,
                    this.getPosition().y * ConstantVariables.PIXELS_TO_METERS);

            //update the control glow effect
            if(this.isVisibleControlGlow){
                this.controllerGlow.update(Gdx.graphics.getDeltaTime());
            }
            //position the controller glow animation at the tank
            this.controllerGlow.setPosition(this.controlGlowV.x, this.controlGlowV.y);
        }

    }
    /**
     * updates the tank based on joystick controller for this tank.
     */
    public void UpdateTank_Joystick(){

        this.update(EntitManager.controllers.joystick.GetVector(),
                EntitManager.controllers.joystick.GetMaxVectorDimensions()); //the main player tank updates when only game starts

        //update the camera position if the linear velocity when is different than zero
        if (this.body.getLinearVelocity().x != 0 || this.body.getLinearVelocity().y != 0) {
            MultiGameScreen.camera.setPosition(this.body.getPosition().x * ConstantVariables.PIXELS_TO_METERS,
                    this.body.getPosition().y * ConstantVariables.PIXELS_TO_METERS);
        }

    }
    /**
     * render the tank sprites.
     * @param batch
     */
    @Override
    public void render(SpriteBatch batch) {

        //draw the sprite of the tank
        //first check whether it is in camera view
        if(GeneralMethods.isInsideCameraView(GeneralMethods.convertFromBodyToPixels(getPosition()),
                                                this.diagonal/2, MultiGameScreen.camera.frustum)){
            tank.draw(batch);
            //draw the muzzle effect
            this.muzzleEffect.draw(batch);
            //render the control glow effect on top
            if(isVisibleControlGlow){
                this.controllerGlow.draw(batch);
                if(this.controllerGlow.isComplete())
                    this.controllerGlow.reset();
            }
        }

    }
    //end of updaters and renderers methods

    @Override
    public void dispose(){

        if(!status){

            if(this.tankIntelligi == null){
                //update the total kills
                Tanks.gameState.setKills(Tanks.gameState.getKills() + this.tankProperties.kills);
                //update the total coins
                Tanks.jarCoins.setCoins(Tanks.gameState.getTotalCoins());
                Tanks.gameState.setCoins(Tanks.jarCoins.getCoins() + this.tankProperties.coins);
                Tanks.jarCoins.setCoins(Tanks.gameState.getTotalCoins());
                //update the total deaths
                Tanks.gameState.setDeaths(Tanks.gameState.getDeaths() + 1);
                //remove the flash light
                switchFlashOff();
            }

            //release the base
            if(base != null){
                base.release();
            }

            //add the body to the body disposer to be disposed
            MultiGameScreen.bodyDisposer.addBodyToDispose(this.body);

            //null the body
            body = null;

        }

    }
    public void flagToDestroy(){
        status = false;
    }

    public void pause(){

        //pause the ai
        if(this.tankIntelligi != null){
            this.tankIntelligi.Pause();
        }

    }
    public void resume(){
        //resume the ai
        if(this.tankIntelligi != null) {
            this.tankIntelligi.Resume();
        }
    }

    //getters
    /**
     * get the body dimensions in body2d dimensions.
     * this returns the width and height in half.
     */
    public Vector2 getBodyDimensions(){
        return this.BodyDimensions;
    }
    public boolean getTankState(){
        return status;
    }
    /**
     * gets teh artificial intellegence.
     */
    public TankIntelligi getArtificialInt(){
        return this.tankIntelligi;
    }
    /**
     * returns the diagonal of this tank in pixels.
     */
    public float getDiagonal(){
        return (float)Math.sqrt( this.tank.getWidth()*this.tank.getWidth() + this.tank.getHeight()*this.tank.getHeight() );
    }
    //end of getters

    //decrementors
    public void decrementHealth(com.bvisiongames.me.Weapons.Bullet.BULLETSTYPE bulletstype){
        if(tankProperties.enabledHealth){
            switch (bulletstype){
                case BULLET1:
                    this.tankProperties.health -= (20 - this.tankProperties.density);
                    break;
            }
        }
    }
    //end of decrementors

    //setters
    /**
     * show and set the muzzle effect.
     */
    //tmp variables
    private Vector2 tmpMuzzleVec2 = new Vector2(0, 0);
    public void setAndShowMuzzleEffect(){

        //calculate the position of the muzzle
        this.tmpMuzzleVec2.set(this.getDirection());
        this.tmpMuzzleVec2.setLength(tank.getHeight() / 2);
        this.tmpMuzzleVec2.set(
                this.getPosition().x*ConstantVariables.PIXELS_TO_METERS + this.tmpMuzzleVec2.x,
                this.getPosition().y*ConstantVariables.PIXELS_TO_METERS + this.tmpMuzzleVec2.y
        );

        this.muzzleEffect.reset();

        //set the position
        this.muzzleEffect.setPosition(tmpMuzzleVec2.x, tmpMuzzleVec2.y);

        //set the high angle
        this.muzzleEffect.getEmitters().first().getAngle().setHigh(
                this.getBodyAngle()*MathUtils.radiansToDegrees - 45,
                this.getBodyAngle()*MathUtils.radiansToDegrees + 45
        );
        this.muzzleEffect.getEmitters().get(1).getAngle().setHigh(
                this.getBodyAngle()*MathUtils.radiansToDegrees - 45,
                this.getBodyAngle()*MathUtils.radiansToDegrees + 45
        );
        //set the low angle
        this.muzzleEffect.getEmitters().first().getAngle().setLow(this.getBodyAngle() * MathUtils.radiansToDegrees);
        this.muzzleEffect.getEmitters().get(1).getAngle().setLow(this.getBodyAngle() * MathUtils.radiansToDegrees);

        //start the muzzle effect
        this.muzzleEffect.start();

    }
    /**
     * disable the body (no collision detection on this body tank).
     */
    public void disableBodyCollision(){
        this.body.setActive(false);
    }
    /**
     * enables the body (enables collision detection on this body tank).
     */
    public void enableBodyCollision(){
        this.body.setActive(true);
    }
    /**
     * resurect this tank.
     */
    //small variables
    private Vector2 lastPosResurect = new Vector2(0, 0);
    public void resurect(){

        //notify of death
        this.getArtificialInt().notifyOfDeath();

        //reset the properties of the tank
        this.status = true;
        this.tankProperties.health = 100;
        this.tankProperties.kills = 0;

        //last position of this tank
        lastPosResurect.set(this.body.getPosition().x * ConstantVariables.PIXELS_TO_METERS,
                this.body.getPosition().y * ConstantVariables.PIXELS_TO_METERS);

        //add the reward
        addReward(lastPosResurect);

        //activate the destruction animation
        ActivateDestruction(lastPosResurect);

        //set the position of the tank to the assembly buildings
        //move the tank to an assembly building
        EntitManager.mapManager.getLevel().getAssemblyBuildings()[0].addTankToAssemble(this);

        //freeze the tank tank intelli
        this.tankIntelligi.freeze();

        //reset the weapon manager of this tank
        this.tankIntelligi.resetWeaponManager();

        //disable the tank body so it can go through assembly building
        disableBodyCollision();

        //hide the controller glow
        this.isVisibleControlGlow = false;

    }
    /**
     * activate the tank.
     * this method is only called when it has TankIntelli.
     */
    public void activateTank(){

        //enable the body
        enableBodyCollision();

        //defreeze the intelli of this tank
        this.tankIntelligi.defreeze();

        //show the controller glow animation
        this.isVisibleControlGlow = true;

    }
    /**
     * add the rewarding coins after destruction.
     * @param position
     */
    public void addReward(Vector2 position){

        for(int i = 0; i < 5; i++){
            EntitManager.coinRewardManager.addCoin(new CoinReward(2, position));
        }

    }
    /**
     * add an explosion for destruction of this tank.
     * @param position
     */
    public void ActivateDestruction(Vector2 position){
        //activate a tank explosion
        MultiGameScreen.particleEffectsManager.defaultTankExplosionManager.activateExplosion(position);
    }
    //end of setters

    //begin of enums
    /**
     * enums for the tank type.
     */
    public enum TANKSTYPES{
        E100, KV2, M6, PZKPFWIV,
        PZKPFWIVG, T34, VK3601H
    }
    /**
     * enums for the tank pilot.
     */
    public enum TANKPILOT{
        MAIN_PLAYER, CPU
    }
    //end of enums

    /**
     * this class stores the tank's basic properties like health...etc
     */
    public class TankProperties{

        public String id;
        //density of the tank
        public float density = 5;
        //precentage health
        public int health = 100;
        //kills and coins
        public int kills = 0, coins = 0;
        //Max properties of the tank
        public float MaxVelocity = 30.0f,
                MaxRotationVelocity = 1.0f;
        //tank type
        public TANKSTYPES tankType = TANKSTYPES.E100;

        //state of health
        public boolean enabledHealth = true;

        //tank pilot
        public TANKPILOT tankpilot;

        //setters
        /**
         * enable health
         */
        public void enableHealth(){
            this.enabledHealth = true;
        }
        /**
         * disable health
         */
        public void disableHealth(){
            this.enabledHealth = false;
        }
        //end of setters

        /**
         * notify ai of the bullet shot by it of it scoring on a target.
         */
        public void notifyAIofScore(Weapon weapon){

            if(tankIntelligi != null){
                tankIntelligi.notifyOfScore(weapon.bulletstype);
            }

        }

        /**
         * notify ai of being damaged.
         */
        public void notifyAIofDamage(Entity entity){

            if(tankIntelligi != null){
                tankIntelligi.notifyOfDamage(entity);
            }

        }

    }

    /**
     * this class holds the method for electrically shocked state.
     */
    public class TankElectricShocked{

        //time out of electric shock
        public int timeMillisecond = 4000;
        //frames count
        public int framesCount = 0;

        //state of the electric shock
        public boolean IsElectricallyShocked = false;

        //updaters and renderers methods
        /**
         * updates
         */
        public void update(float delta){

            if( this.framesCount <= ((float)timeMillisecond/delta)/1000 ){
                //increment the frames count
                this.framesCount++;
            }else{
                this.framesCount = 0;
                this.IsElectricallyShocked = false;
            }

        }
        //end of updaters and renderers methods

        //electric shock methods
        public void hitByElectricShock(Vector2 sourcePos){

                if(sourcePos.dst(Tank.this.getPosition()) < 10){
                    if(!this.IsElectricallyShocked){
                        this.IsElectricallyShocked = true;
                        MultiGameScreen.particleEffectsManager.lightningManager
                                .activate(Tank.this.getPosition().x * ConstantVariables.PIXELS_TO_METERS,
                                            Tank.this.getPosition().y * ConstantVariables.PIXELS_TO_METERS,
                                            sourcePos.x * ConstantVariables.PIXELS_TO_METERS,
                                            sourcePos.y * ConstantVariables.PIXELS_TO_METERS,
                                            4000);
                    }
                }

        }
        //end of electric shock methods


    }


    /**
     * properties of tanks.
     */
    //small variables for the getmaxTank Speed method
    private static float getMaxSpeedspeed = 0;
    public static float GetMaxTankSpeed(TANKSTYPES type){
        getMaxSpeedspeed = 0;
        switch (type){
            case E100:
                getMaxSpeedspeed = 400/ConstantVariables.PIXELS_TO_METERS;
                break;
            case KV2:
                getMaxSpeedspeed = 200/ConstantVariables.PIXELS_TO_METERS;
                break;
            case M6:
                getMaxSpeedspeed = 300/ConstantVariables.PIXELS_TO_METERS;
                break;
            case VK3601H:
                getMaxSpeedspeed = 100/ConstantVariables.PIXELS_TO_METERS;
                break;
            case PZKPFWIV:
                getMaxSpeedspeed = 100/ConstantVariables.PIXELS_TO_METERS;
                break;
            case PZKPFWIVG:
                getMaxSpeedspeed = 150/ConstantVariables.PIXELS_TO_METERS;
                break;
        }
        return getMaxSpeedspeed;
    }
    //small variables for the getmaxTank armor method
    private static float getMaxArmorarmor = 0;
    public static float GetMaxTankArmor(TANKSTYPES type){
        getMaxArmorarmor = 0;
        switch (type){
            case E100:
                getMaxArmorarmor = 20;
                break;
            case KV2:
                getMaxArmorarmor = 40;
                break;
            case M6:
                getMaxArmorarmor = 30;
                break;
            case VK3601H:
                getMaxArmorarmor = 75;
                break;
            case PZKPFWIV:
                getMaxArmorarmor = 50;
                break;
            case PZKPFWIVG:
                getMaxArmorarmor = 55;
                break;
        }
        return getMaxArmorarmor;
    }

}
