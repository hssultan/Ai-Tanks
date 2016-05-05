package com.bvisiongames.me.protocols;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.bvisiongames.me.Weapons.Weapon;
import com.bvisiongames.me.Weapons.EntitysWeaponManger;
import com.bvisiongames.me.entity.EntitManager;
import com.bvisiongames.me.entity.Entity;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.intelligence.NearbyAreaScanner;
import com.bvisiongames.me.intelligence.PointProperties;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.intelligence.ArtificialMemoryManager;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.settings.GeneralMethods;
import com.bvisiongames.me.settings.TankBulletsProperty;
import com.bvisiongames.me.settings.TankMode;

/**
 * Created by ahzji_000 on 12/19/2015.
 */
public class TankIntelligi extends Thread implements Runnable {

    //lifecycle variables
    public boolean status = false; //the status of this thread
    private boolean freeze = false; //reponsible for the freezing of some methods
    private boolean paused = false; //status of pause of this thread
    private boolean synced = false; //syncing variable with other threads
    public boolean isDoneShuttingDown = false; //save the state of when this thread is done shutting down
    //end of lifecycle variables

    //the instance of the tank being controlled
    public Tank tank;

    //tank level
    private TankMode.TankModeType tankModeType = TankMode.TankModeType.EASY_MODE;

    //instance of the radar
    private Radar radar = new Radar();

    //instance of the action
    private Action action = new Action();

    //instance of the sensors manager
    private SensorsManager sensorsManager;

    //weapon manager
    public EntitysWeaponManger weaponManger;

    //memory ai
    public ArtificialMemoryManager artificialMemoryManager;

    //nearby scanner
    public NearbyAreaScanner nearbyAreaScanner;

    /**
     * initiator.
     * @param tank
     * takes in the tank that needs to be controlled.
     */
    public TankIntelligi(Tank tank, TankMode.TankModeType tankModeType){

        //set the tank to be controlled
        this.tank = tank;

        //set the level of tank mode
        this.tankModeType = tankModeType;

        //set some internal variables
        this.status = true; //the state of this thread

        //add sensors for nearby objects to the tank
        this.sensorsManager = new SensorsManager();

        //initiate the nearby area scanner
        this.nearbyAreaScanner = new NearbyAreaScanner(this.tank);

        //initiate the ai memory and load the data
        artificialMemoryManager = new ArtificialMemoryManager();
        //Tanks.gameState.intelligence.setInitAttckProb(MapManager.MAPTYPE.LEVEL1, Tank.TANKSTYPES.E100,
        //                TankMode.TankModeType.EASY_MODE, null);
        //Tanks.gameState.intelligence.saveData();
        artificialMemoryManager.loadData(EntitManager.mapManager.getMaptype(),
                this.tank.tankProperties.tankType, this.tankModeType, tank, nearbyAreaScanner);

        //initiate the weapon manager
        this.weaponManger = new EntitysWeaponManger();
        //reset the weapon manager
        this.resetWeaponManager();

    }

    //getters
    /**
     * get the sensors manager instance.
     */
    public SensorsManager getSensorsManager(){
        return this.sensorsManager;
    }
    /**
     * get the radar instance.
     */
    public Radar getRadar(){
        return this.radar;
    }
    /**
     * get the action instance.
     */
    public Action getAction(){
        return this.action;
    }
    //end of getters


    //setters
    /**
     * reset weapon manager based on tank mode, type and level.
     */
    public void resetWeaponManager(){

        //first reset all the total bullets to zero
        this.weaponManger.electricBullet.totalBullets = 0;
        this.weaponManger.bullet1.totalBullets = 0;

        //set to the normal bullet
        this.weaponManger.setBulletstype(Weapon.BULLETSTYPE.BULLET1);

        //add the normal bullets
        for(int i = 0; i < TankBulletsProperty.getTotalNormalBulletsLevel(this.tankModeType, this.tank.tankProperties.tankType); i++){
            this.weaponManger.addBullet(Weapon.BULLETSTYPE.BULLET1);
        }

        //add the electric bullets
        for(int i = 0; i < TankBulletsProperty.getTotalElectricBulletsLevel(this.tankModeType, this.tank.tankProperties.tankType); i++){
            this.weaponManger.addBullet(Weapon.BULLETSTYPE.ELECTRICBULLET);
        }

    }
    /**
     * gets the state if the thread is frozen.
     */
    public boolean isFrozen(){
        return this.freeze;
    }
    /**
     * check whether this thread's methods are synced with some methods in another thread.
     */
    public boolean isSynced(){
        return this.synced;
    }
    /**
     * checks whether this thread is paused.
     */
    public boolean isPaused(){
        return this.paused;
    }
    //end of setters


    //interface methods
    /**
     * freeze the firing of some methods.
     */
    public void freeze(){
        this.freeze = true;
    }
    /**
     * defreeze the firing of some methods.
     */
    public void defreeze(){
        this.freeze = false;
    }
    /**
     * pause the methods of this thread.
     */
    public void Pause(){
        this.paused = true;
    }
    /**
     * resumes the methods of this thread.
     */
    public void Resume(){
        this.paused = false;
    }
    /**
     * shutdown this thread.
     */
    public void shutdown(){
        this.status = false;
        this.isDoneShuttingDown = false;
    }
    /**
     * sync that is called in the other threads to sync some methods here.
     */
    public void sync(){
        this.synced = true;
    }
    /**
     * reset the synced after some methods has been called and are synced with outside thread.
     * it is called after the methods that are being synced inside this thread.
     */
    public void desync(){
        this.synced = false;
    }
    /**
     * notify of getting damaged.
     */
    public void notifyOfDamage(Entity entity){
        //notify the attackdetected tank instance
        action.attackDetectedTank.notifyOfDamage(entity);
        //decrement once for the chosen bullet type if there is an attack point
        if(action.attackDetectedTank.attackPoint != null){
            switch (weaponManger.getBulletstype()){
                case BULLET1:
                    action.attackDetectedTank.attackPoint.decrementNormalBulletFitness(tankModeType);
                    break;
                case ELECTRICBULLET:
                    action.attackDetectedTank.attackPoint.decrementElectricBulletFitness(tankModeType);
                    break;
            }
        }
    }
    /**
     * notify of scoring.
     * @param bulletType
     * the type of bullet shot. this will help determine which bullet was more effective
     * and best fitted.
     */
    public void notifyOfScore(Weapon.BULLETSTYPE bulletType){
        //notify the attackdetected tank instance
        action.attackDetectedTank.notifyOfScore();
        if(action.attackDetectedTank.attackPoint != null){
            switch (bulletType){
                case BULLET1:           //increment the fitness for bullet1 twice
                    action.attackDetectedTank.attackPoint.incrementNormalBulletFitness(tankModeType);
                    break;
                case ELECTRICBULLET:    //increment the fitness for electric once
                    action.attackDetectedTank.attackPoint.incrementElectricBulletFitness(tankModeType);
                    break;
            }
        }
    }
    /**
     * notify of death.
     */
    public void notifyOfDeath(){
        //twice because it died
        if(action.attackDetectedTank.attackPoint != null){
            switch (weaponManger.getBulletstype()){
                case BULLET1:
                    action.attackDetectedTank.attackPoint.decrementNormalBulletFitness(tankModeType);
                    action.attackDetectedTank.attackPoint.decrementNormalBulletFitness(tankModeType);
                    break;
                case ELECTRICBULLET:
                    action.attackDetectedTank.attackPoint.decrementElectricBulletFitness(tankModeType);
                    action.attackDetectedTank.attackPoint.decrementElectricBulletFitness(tankModeType);
                    break;
            }
        }
    }
    //end of interface methods

    @Override
    public void run() {

        //initiate the loop
        while (this.status){

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //disable the firing of these method from firing when paused or frozen
            if(!this.isFrozen() && !this.isPaused()){

                //call methods that does not need syncing below---->

                //look for objects nearby using the radar lookAround method
                radar.lookAround();

                //checks what to do based on the radar
                //first check if we are stuck
                if(radar.getStuckListener().isStuck())
                {

                    //before action
                    this.action.beforeAction(AIACTIVITYSTATE.IS_STUCK);

                    //apply action
                    action.getMoveToFreeZone().MoveToAFreeZone();

                    //after action
                    this.action.afterAction(AIACTIVITYSTATE.IS_STUCK);

                }else
                //check whether objects are nearby
                if(radar.getNearbyObjects().isThereNearbyObject())
                {

                    this.action.beforeAction(AIACTIVITYSTATE.AVOIDING_OBSTACLE);

                    //react to the nearby objects
                    this.action.getNearbyEvadingTech().react();

                    //we don't need to lock patrol to find a nearby patrol point
                    //since the nearby will check if it is near the destination point
                    //and it will setup the next patrol point.

                    this.action.afterAction(AIACTIVITYSTATE.AVOIDING_OBSTACLE);

                }else
                //chase a tank if it is nearby
                if(radar.getTankChaser().isThereTankToChase())
                {

                    //before action
                    this.action.beforeAction(AIACTIVITYSTATE.CHASING_ENTITY);

                    //react to the detected tank
                    this.action.getAttackDetectedTank().findTechToAttack();

                    //after action
                    this.action.afterAction(AIACTIVITYSTATE.CHASING_ENTITY);


                }else
                //lastly if nothing is nearby then patrol around
                {

                    //before action
                    this.action.beforeAction(AIACTIVITYSTATE.PATROLLING);

                    //call the patrol method below
                    this.action.getPatrolMove().patrolAround();

                    //after action
                    this.action.afterAction(AIACTIVITYSTATE.PATROLLING);

                }

                //<---end of methods that does not need syncing---->

                //method that are synced with the other thread
                if(isSynced()){
                    //call methods here-------->

                    //update the weapon manager
                    this.weaponManger.update();

                    //<----end of methods that needs syncing---->
                    //desyncing does not need to be called since it is called down below after tank update
                }
                //end of syncing of this thread with the other thread

            }
            //end of disabling firing of these methods
            else{

                //reset instances like radar if the ai is paused
                radar.reset();
                action.reset();

            }

            //method that are synced with the other thread
            if(isSynced()){
                //call methods here-------->

                //update the tank
                if(this.status){
                    this.tank.update(this.action.getJoyStickDirection(), this.action.getMaxJoyStickDirection());
                }

                //<----end of methods that needs syncing---->
                //reset the syncing method
                desync();
            }


        }
        //end of loop

        //change the state of the isDoneShuttingDown to true
        this.isDoneShuttingDown = true;

    }

    /**
     * this class holds near tank instances, map bounderies, and other positions.
     * it is like a radar that senses objects nearby.
     */
    public class Radar{

        //tank chaser
        private TankChaser tankChaser = new TankChaser();
        //stuck listener
        private StuckListener stuckListener = new StuckListener();
        //nearby objects
        private NearbyObjects nearbyObjects = new NearbyObjects();

        //radar listener
        /**
         * looks around by calling different method in here.
         */
        public void lookAround(){

            //this checks some listeners for the action
            TankIntelligi.this.action.checkListeners();

            //check if this tank is stuck
            stuckListener.checkIfStuck();

        }
        //end of radar listeners


        //getters
        /**
         * gets the tank Chaser class that holds the close tank to be chased.
         */
        public TankChaser getTankChaser(){
            return this.tankChaser;
        }
        /**
         * gets the stuck listener instance that holds if this tank is stuck.
         */
        public StuckListener getStuckListener(){
            return this.stuckListener;
        }
        /**
         * gets the nearby objects.
         */
        public NearbyObjects getNearbyObjects(){
            return this.nearbyObjects;
        }
        //end of getters


        //setters
        /**
         * reset when paused.
         */
        public void reset(){

            //reset and update coordinates to the stuck
            this.getStuckListener().reset();

        }
        //end of setters

        /**
         * class that looks and set a tank to be chased and taken down.
         */
        public class TankChaser{

            //Entity to be chased
            public Entity targetEntity;

            //setters
            /**
             * sets the tank position to be chased.
             *
             */
            public void setTankToChase(Entity targetEntity){

                //get the priority of this entity and set the target entity to the result
                this.targetEntity = artificialMemoryManager.tankIntelliDataAnalyzer.intelliDataPointsManager.
                                    getHighPriorityEntity(this.targetEntity, targetEntity);

                //notify the route planer of finding an entity
                artificialMemoryManager.tankIntelliDataAnalyzer.intelliDataPointsManager
                        .routePlanner.notifyOfEntityNearby(this.targetEntity);

            }
            /**
             * reset the tank to chase.
             */
            public void removeTankToChase(){
                this.targetEntity = null;
            }
            //end of setter


            //getters
            /**
             * is there a tank to chase.
             */
            public boolean isThereTankToChase(){
                //return (this.tankPositionToChase != null)? true: false;
                return (this.targetEntity != null)? true : false;
            }
            //end of getters

        }

        /**
         * class the checks if this tank is stuck against a wall or been in place.
         */
        public class StuckListener{

            //this var holds whether we are stuck
            private boolean stuck = false;
            //this var tells whether it has been disabled
            private boolean status = true;
            //this var is for locking this method in a certain state
            private boolean locked = false;

            //timer for frames
            private int timerCounter = 0;

            //last position of the tank
            private Vector2 lastPos = new Vector2(0, 0);

            //last direction of the tank
            private float lastAngle = 0;

            //setters
            /**
             * disable
             */
            public void disable(){
                this.status = false;
            }
            /**
             * enable
             */
            public void enable(){
                this.status = true;
            }
            /**
             * update the position of the lastpos of the tank
             */
            public void updateLastPos(){
                this.lastPos.set(TankIntelligi.this.tank.getPosition().x,
                                    TankIntelligi.this.tank.getPosition().y);
            }
            /**
             * update the angle direction of the tank
             */
            public void updateLastDirection(){
                this.lastAngle = TankIntelligi.this.tank.getBodyAngle();
            }
            /**
             * reset the properties of stuck class to initial.
             */
            public void reset(){
                this.stuck = false;
                this.timerCounter = 0;
                this.enable();
                this.lastPos.set(0, 0);
                this.lastAngle = 0;
            }
            /**
             * lock in a certain state.
             */
            public void lock(){
                this.locked = true;
            }
            /**
             * unlock from certain state
             */
            public void unlock(){
                this.locked = false;
            }
            //end of setters


            //getters
            /**
             * is locked in a certain state.
             */
            public boolean isLocked(){
                return locked;
            }
            /**
             * checks whether the last position is still the same as the current tank position.
             */
            public boolean isStillInSamePosition(){
                return (
                        this.lastPos.dst(TankIntelligi.this.tank.getPosition()) < 0.1f
                        )? true : false;
            }
            /**
             * checks whether it is still in the same direction.
             */
            public boolean isStillInSameDirection(){
                return ( Math
                        .abs( this.lastAngle
                                - TankIntelligi.this.tank.getBodyAngle() ) < 0.1f )?

                        true : false;
            }
            /**
             * tells whether this tank is stuck.
             */
            public boolean isStuck(){
                return (this.status)? ((this.stuck)? true: false): false ;
            }
            /**
             * tells whether it has been disabled.
             */
            public boolean isDisabled(){
                return !this.status;
            }
            //end of getters


            //listener
            /**
             * checks whether this tank is stuck.
             */
            public void checkIfStuck(){

                //start of locking this if statement
                if(!isLocked()){

                    if(!isDisabled()){

                        //check if still at the same position
                        if(isStillInSamePosition() && isStillInSameDirection() && this.timerCounter > 1000){

                            //enable stuck
                            this.stuck = true;

                            //lock this state so we can unstuck this tank
                            this.lock();

                        }else{

                            //increment timer if still in same place and if not then reset the timer to zero
                            if(isStillInSamePosition() && isStillInSameDirection()){
                                this.timerCounter++;
                            }else{
                                this.timerCounter = 0;
                                this.updateLastDirection();
                                this.updateLastPos();
                            }

                            //reset stuck to unstuck
                            this.stuck = false;

                        }

                    }else{

                        //reset everything in this class
                        this.reset();

                        //update the last position of the tank
                        this.updateLastPos();

                        //update last direction
                        this.updateLastDirection();

                    }

                //end of locking if statement
                }

            }
            //end of listener

        }

        /**
         * class that checks whether there are any objects nearby
         */
        public class NearbyObjects{

            //tells whether there is a nearby object
            private boolean hasNearbyObject = false;

            //setters
            /**
             * set nearby object detection on.
             */
            public void setHasNearbyObject(){
                this.hasNearbyObject = true;
            }
            /**
             * disable the nearby detection on.
             */
            public void desetHasNearbyObject(){
                this.hasNearbyObject = false;
            }
            //end of setters


            //getter
            /**
             * tells whether there is a nearby object.
             */
            public boolean isThereNearbyObject(){
                return this.hasNearbyObject;
            }
            //end of getters

        }

    }

    /**
     * this class holds the classes that makes this tank react.
     */
    public class Action{

        //vector of the joystick
        private Vector2 joyStickDirection = new Vector2(0, 0),
                        maxJoyStickDirection = new Vector2(
                                MultiGameScreen.controllersSkin.getRegion("controller_surround").getRegionWidth(),
                                MultiGameScreen.controllersSkin.getRegion("controller_surround").getRegionWidth()
                        );

        //destination point to move to
        private Vector2 destinationPoint = new Vector2(0, 0);

        //move to a free zone instance
        private MoveToFreeZone moveToFreeZone = new MoveToFreeZone();

        //apply moves of tank joystick
        private ApplyMove applyMove = new ApplyMove();

        //patrol around instance
        private PatrolMove patrolMove = new PatrolMove();

        //attack detected instance
        private AttackDetectedTank attackDetectedTank = new AttackDetectedTank();

        //ai activity state instance
        private AIActivityState aiActivityState = new AIActivityState();

        //evading nearby objects instance
        private NearbyEvadingTech nearbyEvadingTech = new NearbyEvadingTech();


        /**
         * initiator of action.
         */
        public Action(){

            //reset the maxjoystick so the speed of the tank is adjusted to the level mode
            this.maxJoyStickDirection.scl(TankMode.getTankRatioMovementX(TankIntelligi.this.tankModeType),
                    TankMode.getTankRatioMovementY(TankIntelligi.this.tankModeType));

        }

        //listeners
        /**
         * this just updates some instance in the radar lookaround method.
         */
        public void checkListeners(){


        }
        //end of listeners


        //getters
        /**
         * gets the ai activity state instance.
         */
        public AIActivityState getAiActivityState(){
            return this.aiActivityState;
        }
        /**
         * gets the nearby evading tech instance.
         */
        public NearbyEvadingTech getNearbyEvadingTech(){
            return this.nearbyEvadingTech;
        }
        /**
         * gets the move to free zone instance.
         */
        public MoveToFreeZone getMoveToFreeZone(){
            return this.moveToFreeZone;
        }
        /**
         * gets the patrol around instance.
         */
        public PatrolMove getPatrolMove(){
            return this.patrolMove;
        }
        /**
         * gets the attack technique instance.
         */
        public AttackDetectedTank getAttackDetectedTank(){
            return this.attackDetectedTank;
        }
        /**
         * gets the joystick Directions.
         */
        public Vector2 getJoyStickDirection(){
            return this.joyStickDirection;
        }
        /**
         * gets the max joystick Directions.
         */
        public Vector2 getMaxJoyStickDirection(){
            return this.maxJoyStickDirection;
        }
        /**
         * get the destination point.
         */
        public Vector2 getDestinationPoint(){
            return this.destinationPoint;
        }
        /**
         * gets the apply move class
         */
        public ApplyMove getApplyMove(){
            return this.applyMove;
        }
        /**
         * this returns whether the tank is at destination or not.
         * @param accuracy
         * accuracy to the destination measured in box2d dimensions.
         */
        public boolean isAtPoint(float accuracy){
            return (TankIntelligi.this.tank.getPosition().dst(Action.this.getDestinationPoint()) <= accuracy)? true: false;
        }
        //end of getters


        //setters
        /**
         * set destination point
         * @param destination
         */
        public void setDestinationPoint(Vector2 destination){
            this.destinationPoint.set(destination);
        }
        /**
         * apply actions or adjustments before action.
         * @param activityState
         */
        public void beforeAction(AIACTIVITYSTATE activityState){

            switch (activityState){
                case AVOIDING_OBSTACLE:
                    //update the joystick when transitioning to different ai state
                    action.applyMove.updateJoystick(activityState);
                    break;
                case IS_STUCK:
                    //update the joystick when transitioning to different ai state
                    action.applyMove.updateJoystick(activityState);
                    break;
                case CHASING_ENTITY:
                    //update the joystick when transitioning to different ai state
                    action.applyMove.updateJoystick(activityState);
                    break;
                case PATROLLING:
                    //update the joystick when transitioning to different ai state
                    action.applyMove.updateJoystick(activityState);
                    break;
            }

        }
        /**
         * apply actions or adjustements after action.
         * @param activityState
         */
        public void afterAction(AIACTIVITYSTATE activityState){

            switch (activityState){
                case AVOIDING_OBSTACLE:
                    //update the ai activity state
                    action.getAiActivityState().setAiactivitystateToAvoidObstacle();
                    break;
                case IS_STUCK:
                    //lock the patrol around if there is no patrol
                    action.getPatrolMove().lock();
                    //update the ai activity state
                    action.getAiActivityState().setAiactivitystateToIsStuck();
                    break;
                case CHASING_ENTITY:
                    //lock the patrol around if there is not patrol
                    action.getPatrolMove().lock();
                    //update the ai activity state
                    action.getAiActivityState().setAiactivitystateToChasingEntity();
                    break;
                case PATROLLING:
                    //update the ai activity state
                    action.getAiActivityState().setAiactivitystateToPatrolling();
                    break;
            }

        }
        /**
         * reset the action instances.
         */
        public void reset(){

            //lock the patrol around instance
            getPatrolMove().lock();

            //reset the joystick to stop moving
            this.applyMove.seizeCompleteMoveing();

        }
        //end of setter

        /**
         * class that frees the tank and moves it to a more free spot.
         */
        public class MoveToFreeZone{

            //this locks the destination point once it has been set and is reset once is at point
            private boolean locked = false;

            //setters
            /**
             * locks the calculation of a destination point.
             */
            public void lock(){
                this.locked = true;
            }

            /**
             * unlocks the calculation of a destination point
             */
            public void unlock(){
                this.locked = false;
            }
            /**
             * reset.
             */
            public void reset(){
                this.unlock();
            }
            //end of setters


            //getters
            /**
             * tells whether the calculation of a destination point has been locked
             */
            public boolean isLocked(){
                return this.locked;
            }
            //end of getter


            //actor
            public void MoveToAFreeZone(){

                if(!this.isLocked()){

                    nearbyAreaScanner.resetPoints();
                    nearbyAreaScanner.scopeFreeAreaPoints(null);

                    this.lock();

                }

                if(this.isLocked()){

                    //steer the tank to point with 0.5f accuracy
                    if (!Action.this.isAtPoint(0.4f)) {
                        //update the destination point to the lowest energy point
                        TankIntelligi.this.action.destinationPoint.set(nearbyAreaScanner.lowestEnergyPoint.coordinates);
                        Action.this.applyMove.SteerThenMove();
                    } else {
                        //when point is reached then unlock the stuck listener and reset it
                        TankIntelligi.this.getRadar().getStuckListener().unlock();
                        TankIntelligi.this.getRadar().getStuckListener().reset();

                        //unlock the calculation of a destination point and reset it
                        this.unlock();
                        this.reset();

                        //reset the joystick to not moving
                        Action.this.joyStickDirection.set(0, 0);

                        //resetting the destination point to zero
                        Action.this.destinationPoint.set(0, 0);

                    }
                }

            }
            //end of actor

        }

        /**
         * class that moves the tank around over the patrol points.
         */
        public class PatrolMove{

            //locking state of setting up the patrol point
            private boolean locked = false;

            //setters
            /**
             * lock setting the currentPatrol point.
             */
            public void lock(){
                this.locked = true;
            }
            /**
             * unlock setting the currentPatrol point
             */
            public void unlock(){
                this.locked = false;
            }
            /**
             * set the destination point to the current patrol point.
             */
            public void setDestToPatrolPoint(Vector2 destination){
                Action.this.destinationPoint.set(destination);

            }
            //end of setters


            //getters
            /**
             * check whether setting up the currentPatrol point has been locked or not.
             */
            public boolean isLocked(){
                return this.locked;
            }
            //end of getters


            //move methods
            /**
             * apply patroling state.
             */
            public void patrolAround(){

                //if this instance has been locked then set up a new point to patrol to
                if(isLocked()){

                    //set the current patrol point to the destination point
                    this.setDestToPatrolPoint(artificialMemoryManager.tankIntelliDataAnalyzer.intelliDataPointsManager
                            .routePlanner.findaPatrolPoint());

                    //unlock it so the code inside this if statement can't run again until it is locked again
                    this.unlock();

                }

                if(!Action.this.isAtPoint(5f)){

                    Action.this.applyMove.SteerThenMove();

                }else{

                    //unlock this from setting a close patrol point
                    this.lock();

                }

            }
            //end of move methods

        }

        /**
         * class that uses strategies to attack the detected tank.
         */
        public class AttackDetectedTank{

            //current attack point
            PointProperties attackPoint;
            //state of the attack point, it has been set or not.
            private boolean atkPointHasBeenSet = false;
            //vector from own tank to attack point
            public Vector2 vecFromTankToTarget = new Vector2(0, 0);

            //setters
            /**
             * notify of damage.
             */
            public void notifyOfDamage(Entity entity){

                //enable lethality based on the entity
                artificialMemoryManager.tankIntelliDataAnalyzer.intelliDataPointsManager
                        .enableLethality(entity);

                //change the lethality of this tank entity
                radar.tankChaser.setTankToChase(entity);

                //find a new point of attack
                atkPointHasBeenSet = false;

            }
            /**
             * notify of score.
             */
            public void notifyOfScore(){

            }
            //end of setters


            //getters
            /**
             * checks whether the attack point the tank is going after is not far behind the targeted tank.
             */
            private boolean isAttkBehindTarget(){
                if(TankIntelligi.this.radar.tankChaser.targetEntity != null &&
                        attackPoint.coordinates.dst(TankIntelligi.this.tank.getPosition())
                        > TankIntelligi.this.tank.getPosition().dst(TankIntelligi.this.radar.tankChaser.targetEntity.getPosition()) ){
                    return true;
                }else{
                    return false;
                }
            }
            /**
             * is the target in scope to shoot at.
             */
            public boolean isTargetInScope(){

                if(Math.abs(vecFromTankToTarget.angle() - TankIntelligi.this.tank.getBodyAngle()*MathUtils.radiansToDegrees)
                        < 5){
                    return true;
                }

                return false;
            }
            //end of getters


            //technique methods
            /**
             * finds a technique to attack the detected tank.
             */
            public void findTechToAttack(){

                //first disable the stuck method
                TankIntelligi.this.radar.stuckListener.disable();

                if(artificialMemoryManager.tankIntelliDataAnalyzer.intelliDataPointsManager
                        .chosenEntityFitnessManager.lethalityOfEntity){
                    //find a point near the targeted tank entity that has the highest fitness.
                    if(!atkPointHasBeenSet){
                        attackPoint = artificialMemoryManager.tankIntelliDataAnalyzer.intelliDataPointsManager
                                .findHighFitness(
                                        TankIntelligi.this.radar.tankChaser.targetEntity.getPosition(),
                                        EntitManager.tank.getBodyAngle(), TankIntelligi.this.tank);
                        atkPointHasBeenSet = true;
                    }

                    //call the weapon selector so it can choose the best suited weapon for this point
                    artificialMemoryManager.tankIntelliDataAnalyzer.intelliDataPointsManager
                            .findHighFitnessWeaponType(weaponManger, attackPoint);

                    //readjust the point if the targeted tank is moving
                    attackPoint.calculateCoordinates(
                            TankIntelligi.this.radar.tankChaser.targetEntity.getPosition(),
                            TankIntelligi.this.radar.tankChaser.targetEntity.getBodyAngle());

                    //if the attack point is colliding with any fixture then choose a different one
                    if(GeneralMethods.isThereCollisionTank(attackPoint.coordinates,
                            TankIntelligi.this.radar.tankChaser.targetEntity.getPosition())){
                        atkPointHasBeenSet = false;
                    }

                    //if the attack point is far behind the target tank then find a different point
                    if(isAttkBehindTarget()){
                        atkPointHasBeenSet = false;
                    }

                    //set the destination point ot the attackPoint coordinates
                    destinationPoint.set(attackPoint.coordinates);
                    vecFromTankToTarget.set(radar.tankChaser.targetEntity.getPosition().x - TankIntelligi.this.tank.getPosition().x,
                            radar.tankChaser.targetEntity.getPosition().y - TankIntelligi.this.tank.getPosition().y);

                    //move to the point
                    if(isAtPoint(1f)){
                        applyMove.seizeCompleteMoveing();
                        //applyMove.aimToPoint(radar.tankChaser.tankPositionToChase);
                        applyMove.aimToPointRapidly(radar.tankChaser.targetEntity.getPosition());
                    }else{
                        applyMove.SteerThenMove();
                    }

                    //check if the tank weapon manager is ready to fire and the target is in scope
                    if(weaponManger.isReadyToShoot() && isTargetInScope() && !tank.tankElectricShocked.IsElectricallyShocked){
                        weaponManger.shoot(TankIntelligi.this.tank);
                        //notify the weapon selector of bullet shot
                        artificialMemoryManager.tankIntelliDataAnalyzer.intelliDataPointsManager
                                .weaponSelecter.notifyShot(weaponManger.getBulletstype());
                    }
                }else{

                    radar.tankChaser.removeTankToChase();

                }

            }
            //end of technique methods

        }

        /**
         * contains the evading methods of a nearby objects.
         */
        public class NearbyEvadingTech{

            //setters
            /**
             * calibrate the joystick y based on the inner back and front sensors.
             */
            private void calibrateJoyYFrontAndBackSensors(){

                //front inner
                if(TankIntelligi.this.getSensorsManager().isInner_front_sensor()){
                    Action.this.applyMove.seizeMovingToHaltY();
                }

                //back inner
                if(TankIntelligi.this.getSensorsManager().isInner_back_sensor()){
                    Action.this.applyMove.SlowDownToFixedRateYForeword(0.1f);
                }

            }
            /**
             * calibrate right and left middle sensors
             */
            private void calibrateJoyXLeftAndRightSensors(){

                //left sensor
                if(TankIntelligi.this.getSensorsManager().isLeft_middle_sensor()){
                    Action.this.applyMove.SteerAcceleratedRightToRatio(0.5f);
                }

                //right sensor
                if(TankIntelligi.this.getSensorsManager().isRight_middle_sensor()){
                    Action.this.applyMove.SteerAcceleratedLeftToRatio(0.5f);
                }

            }
            /**
             * calibrate the joystick based on outer front sensors.
             */
            private void calibrateJoyOuterFrontSensors(){

                //left outer front sensors
                //left sensor 1
                if(TankIntelligi.this.getSensorsManager().isOuter_left_front_sensor1()){
                    Action.this.applyMove.SteerAcceleratedRightToRatio(0.2f);
                }
                //left sensor 2
                if(TankIntelligi.this.getSensorsManager().isOuter_left_front_sensor2()){
                    Action.this.applyMove.SteerAcceleratedRightToRatio(0.3f);
                }
                //left sensor 3
                if(TankIntelligi.this.getSensorsManager().isOuter_left_front_sensor3()){
                    Action.this.applyMove.SteerAcceleratedRightToRatio(0.4f);
                }
                //left sensor 4
                if(TankIntelligi.this.getSensorsManager().isOuter_left_front_sensor4()){
                    Action.this.applyMove.SteerAcceleratedRightToRatio(0.5f);
                }

                //right outer front sensors
                //right sensor 1
                if(TankIntelligi.this.getSensorsManager().isOuter_right_front_sensor1()){
                    Action.this.applyMove.SteerAcceleratedLeftToRatio(0.2f);
                }
                //right sensor 2
                if(TankIntelligi.this.getSensorsManager().isOuter_right_front_sensor2()){
                    Action.this.applyMove.SteerAcceleratedLeftToRatio(0.3f);
                }
                //right sensor 3
                if(TankIntelligi.this.getSensorsManager().isOuter_right_front_sensor3()){
                    Action.this.applyMove.SteerAcceleratedLeftToRatio(0.4f);
                }
                //right sensor 4
                if(TankIntelligi.this.getSensorsManager().isOuter_right_front_sensor4()){
                    Action.this.applyMove.SteerAcceleratedLeftToRatio(0.5f);
                }

                //slow down foreword if any of the outer front right or left sensors are triggered
                if(     //left sensors
                        TankIntelligi.this.getSensorsManager().isOuter_left_front_sensor1()
                        || TankIntelligi.this.getSensorsManager().isOuter_left_front_sensor2()
                        || TankIntelligi.this.getSensorsManager().isOuter_left_front_sensor3()
                        || TankIntelligi.this.getSensorsManager().isOuter_left_front_sensor4()
                        //right sensors
                        || TankIntelligi.this.getSensorsManager().isOuter_right_front_sensor1()
                        || TankIntelligi.this.getSensorsManager().isOuter_right_front_sensor2()
                        || TankIntelligi.this.getSensorsManager().isOuter_right_front_sensor3()
                        || TankIntelligi.this.getSensorsManager().isOuter_right_front_sensor4()
                        ){
                    Action.this.applyMove.SlowDownToFixedRateYForeword(0.2f);
                }

                //outer front middle sensor
                if(TankIntelligi.this.getSensorsManager().isOuter_front_middle_sensor()){
                    Action.this.applyMove.SlowDownToFixedRateYForeword(0.3f);
                    Action.this.applyMove.SlowDownToFixedRateYForeword(0.3f);
                    //then increase or decrease joy x according to prev joy x
                    if(Action.this.joyStickDirection.x > 0){
                        Action.this.applyMove.SteerAcceleratedRightToRatio(0.5f);
                    }else{
                        Action.this.applyMove.SteerAcceleratedLeftToRatio(0.5f);
                    }
                }

            }
            /**
             * calibrate the joystick based on outer back sensors.
             */
            private void calibrateJoyOuterBackSensors(){

                //outer left back sensors
                //left sensor 1
                if(TankIntelligi.this.getSensorsManager().isOuter_left_back_sensor1()){
                    Action.this.applyMove.SteerAcceleratedRightToRatio(0.2f);
                }
                //left sensor 2
                if(TankIntelligi.this.getSensorsManager().isOuter_left_back_sensor2()){
                    Action.this.applyMove.SteerAcceleratedRightToRatio(0.3f);
                }
                //left sensor 3
                if(TankIntelligi.this.getSensorsManager().isOuter_left_back_sensor3()){
                    Action.this.applyMove.SteerAcceleratedRightToRatio(0.4f);
                }
                //left sensor 4
                if(TankIntelligi.this.getSensorsManager().isOuter_left_back_sensor4()){
                    Action.this.applyMove.SteerAcceleratedRightToRatio(0.5f);
                }

                //outer right back sensors
                //right sensor 1
                if(TankIntelligi.this.getSensorsManager().isOuter_right_back_sensor1()){
                    Action.this.applyMove.SteerAcceleratedLeftToRatio(0.2f);
                }
                //right sensor 2
                if(TankIntelligi.this.getSensorsManager().isOuter_right_back_sensor2()){
                    Action.this.applyMove.SteerAcceleratedLeftToRatio(0.3f);
                }
                //right sensor 3
                if(TankIntelligi.this.getSensorsManager().isOuter_right_back_sensor3()){
                    Action.this.applyMove.SteerAcceleratedLeftToRatio(0.4f);
                }
                //right sensor 4
                if(TankIntelligi.this.getSensorsManager().isOuter_right_back_sensor4()){
                    Action.this.applyMove.SteerAcceleratedLeftToRatio(0.5f);
                }

                //slow down foreword if any of the outer back right or left sensors are triggered
                if(     //left sensors
                        TankIntelligi.this.getSensorsManager().isOuter_left_back_sensor1()
                        || TankIntelligi.this.getSensorsManager().isOuter_left_back_sensor1()
                        || TankIntelligi.this.getSensorsManager().isOuter_left_back_sensor2()
                        || TankIntelligi.this.getSensorsManager().isOuter_left_back_sensor3()
                        || TankIntelligi.this.getSensorsManager().isOuter_left_back_sensor4()
                        //right sensors
                        || TankIntelligi.this.getSensorsManager().isOuter_right_back_sensor1()
                        || TankIntelligi.this.getSensorsManager().isOuter_right_back_sensor2()
                        || TankIntelligi.this.getSensorsManager().isOuter_right_back_sensor3()
                        || TankIntelligi.this.getSensorsManager().isOuter_right_back_sensor4()
                        ){
                        Action.this.applyMove.SlowDownToFixedRateYForeword(0.2f);
                }

                //outer back middle sensor
                if(TankIntelligi.this.getSensorsManager().isOuter_back_middle_sensor()){
                    Action.this.applyMove.SlowDownToFixedRateYForeword(0.3f);
                    Action.this.applyMove.SlowDownToFixedRateYForeword(0.3f);
                    //then increase or decrease joy x according to prev joy x
                    if(Action.this.joyStickDirection.x > 0){
                        Action.this.applyMove.SteerAcceleratedRightToRatio(0.5f);
                    }else{
                        Action.this.applyMove.SteerAcceleratedLeftToRatio(0.5f);
                    }
                }

            }
            //end of setters


            //getters

            //end of getters


            //checkers
            /**
             * if the entity is at the point destination and act accordingly.
             */
            private void ifAtPointAct(){

                //if there is a destination point nearby and the entity is on it then move to another one
                if(Action.this.isAtPoint(7f)){

                    if(getAiActivityState().aiactivitystate == AIACTIVITYSTATE.PATROLLING){

                        //set the current patrol point to the destination point
                        Action.this.patrolMove.setDestToPatrolPoint(artificialMemoryManager.tankIntelliDataAnalyzer.intelliDataPointsManager
                                .routePlanner.findaPatrolPoint());

                        //unlock it so the code inside this if statement can't run again until it is locked again
                        Action.this.patrolMove.unlock();

                    }

                }

            }
            //end of checkers


            //action methods
            /**
             * this methods reacts if there is a nearby object detected.
             */
            public void react(){

                    //check which sensor is triggered and react accordingly

                    //calibrate outer front sensors
                    this.calibrateJoyOuterFrontSensors();

                    //calibrate outer back sensors
                    this.calibrateJoyOuterBackSensors();

                    //calibrate left and right middle sensors
                    this.calibrateJoyXLeftAndRightSensors();

                    //calibrate inner sensors
                    this.calibrateJoyYFrontAndBackSensors();

                    //if the entity is at a point destination then act accordingly
                    this.ifAtPointAct();

            }
            //end of action methods

        }

        /**
         * AI activity state class, tells the current ai activity state.
         */
        public class AIActivityState{

            //AI activity state (current one)
            private AIACTIVITYSTATE aiactivitystate = AIACTIVITYSTATE.NOT_DOING_ANYTHING;

            //setters
            /**
             * set the state to doing nothing.
             */
            public void setAiactivitystateToNothing(){
                this.aiactivitystate = AIACTIVITYSTATE.NOT_DOING_ANYTHING;
            }
            /**
             * set the state to avoiding obstacle.
             */
            public void setAiactivitystateToAvoidObstacle(){
                this.aiactivitystate = AIACTIVITYSTATE.AVOIDING_OBSTACLE;
            }
            /**
             * set the state to when stuck.
             */
            public void setAiactivitystateToIsStuck(){
                this.aiactivitystate = AIACTIVITYSTATE.IS_STUCK;
            }
            /**
             * set the state to when patrolling.
             */
            public void setAiactivitystateToPatrolling(){
                this.aiactivitystate = AIACTIVITYSTATE.PATROLLING;
            }
            /**
             * set the state to chasing entity.
             */
            public void setAiactivitystateToChasingEntity(){
                this.aiactivitystate = AIACTIVITYSTATE.CHASING_ENTITY;
            }
            //end of setters


            //getters
            /**
             * get the ai activity state.
             */
            public AIACTIVITYSTATE getAiactivitystate(){
                return this.aiactivitystate;
            }
            //end of getters

        }

        /**
         * this class contains different methods that moves the tank in different ways.
         */
        public class ApplyMove{

            //this float stores the angle between the destination point vec and the direction of the tank
            private float angleDestToDirection = 0;

            //distance between destination point and begining point of tank
            private float distanceToDestination = 0;

            //temp vector2d
            private Vector2 tmpVec = new Vector2(0, 0);


            //setters
            /**
             * make sure the tank is steered at a specific angle.
             * @param angle
             * the maximum angle the angle to destination direction point the angle can be for the tank to stop steering.
             */
            public boolean isSteered(float angle){
                return (Math.abs(this.angleDestToDirection) < angle)? true: false;
            }
            //end of setters


            //update methods
            /**
             * this method updates the joystick when changing from one ai activity state to another.
             * @param state
             * the state that the entity's AI is in.
             */
            public void updateJoystick(AIACTIVITYSTATE state){

                //if the current state is not the same as the previous ai activity state then reset
                //the joystick.
                if(state != Action.this.getAiActivityState().getAiactivitystate()){

                    Action.this.applyMove.seizeCompleteMoveing();
                    //Gdx.app.log("s", "AI State: " + state);

                }

            }
            //end of update methods


            //getters

            //end of getters


            //move methods
            /**
             * first it makes sure the tank is directed
             * towards the destination point. Then it moves in that direction in an accelerating way.
             */
            //small variable that stores the vec between origin of body tank and destination point.
            private Vector2 steerThenMoveVec = new Vector2(0, 0);
            public void SteerThenMove(){

                //set the vec between origin tank and destination point
                this.steerThenMoveVec.set(Action.this.destinationPoint.x - TankIntelligi.this.tank.getPosition().x,
                        Action.this.destinationPoint.y - TankIntelligi.this.tank.getPosition().y );

                //first get the angle between the destination and direction of tank
                this.angleDestToDirection = TankIntelligi.this.tank.getDirection().angleRad(this.steerThenMoveVec);

                //update the distance to destination point
                calculateDistanceToPointDest();

                //check whether the tank is steered enough to start moving foreward or backward
                if(this.isSteered((float)Math.PI/15)){
                    //move foreward in a an accelerated manner
                    //this.moveForewardAccelerated();
                    this.moveAcclerateForewardToMaxFixed();
                }else{
                    //reset the foreward movement to zero if it is not well steered
                    this.seizeMovingToHaltY();
                }

                //keep steering until angle of diff to direction is zero
                this.SteerRightOrLeftMaxFixed();

            }
            /**
             * this method moves the tank to destination point in smooth way.
             * steer to destination point with fixed foreward and backward joystick
             * steer right or left with a small angle of accuracy.
             */
            //small variable that stores the vec between origin of body tank and destination point.
            private Vector2 steerSmoothVec = new Vector2(0, 0);
            public void SteerSmoothly(){

                //set the vec between origin tank and destination point
                this.steerSmoothVec.set(
                        Action.this.getDestinationPoint().x - TankIntelligi.this.tank.getPosition().x,
                        Action.this.getDestinationPoint().y - TankIntelligi.this.tank.getPosition().y
                );

                //first get the angle between the destination and direction of tank
                this.angleDestToDirection = TankIntelligi.this.tank.getDirection().angleRad(this.steerSmoothVec);

                //update the distance to destination point
                calculateDistanceToPointDest();

                //check whether the tank is steered enough to start moving foreword or backward
                if(this.isSteered((float)Math.PI/1.3f)){
                    //move foreward in a an accelerated manner
                    this.moveForewardAccelerated();
                }else{
                    //reset the foreward movement to zero if it is not well steered
                    this.seizeMovingToHaltY();
                }

                //keep steering until angle of diff to direction is zero
                this.SteerRightOrLeftMaxFixed();

            }
            //end of move methods


            //calculations methods
            /**
             * calculate distance to destination point.
             * the calculated distance is saved in the distanceToDestination var.
             */
            public void calculateDistanceToPointDest(){
                this.distanceToDestination = Action.this.destinationPoint.dst(TankIntelligi.this.tank.getPosition());
            }
            //end of calculations methods


            //right or left moves
            /**
             * aim to shoot gradually.
             * @param point
             * the point to aim towards.
             */
            public void aimToPoint(Vector2 point){

                //tmp vec will contain the vec from the own tank to point
                tmpVec.set(point.x - TankIntelligi.this.tank.getPosition().x,
                            point.y - TankIntelligi.this.tank.getPosition().y);

                //calculate the alpha angle between the body angle direction and the destination to body vec angle.
                float alpha = TankIntelligi.this.tank.getBodyAngle()*MathUtils.radiansToDegrees - tmpVec.angle();

                action.joyStickDirection.x = ((alpha - 2) * action.maxJoyStickDirection.x) / 180;

            }
            /**
             * aim to shoot at constant joystick x rate which is adjusted to be fast.
             * @param point
             * the point to aim towards.
             */
            public void aimToPointRapidly(Vector2 point){

                //tmp vec will contain the vec from the own tank to point
                tmpVec.set(point.x - TankIntelligi.this.tank.getPosition().x,
                        point.y - TankIntelligi.this.tank.getPosition().y);

                //calculate the alpha angle between the body angle direction and the destination to body vec angle.
                float alpha = TankIntelligi.this.tank.getBodyAngle()*MathUtils.radiansToDegrees - tmpVec.angle();

                if(Math.abs(alpha) < 2){
                    applyMove.seizeMovingToHaltX();
                }else{
                    joyStickDirection.x = (alpha < 0)? - maxJoyStickDirection.x/1.3f: maxJoyStickDirection.x/1.3f;
                }

            }
            /**
             * seize the tank from moving.
             * in the left or right dimensions only
             */
            public void seizeMovingToHaltX(){

                Action.this.joyStickDirection.x = 0;

            }
            /**
             * straighten the tank by steering right or left according to the angleDistToDirection.
             */
            public void SteerRightOrLeftMaxFixed(){

                if(this.angleDestToDirection < - Math.PI/20){
                    Action.this.joyStickDirection.x = Action.this.maxJoyStickDirection.x;
                }else if(this.angleDestToDirection > Math.PI/20){
                    Action.this.joyStickDirection.x = - Action.this.maxJoyStickDirection.x;
                }else{
                    this.seizeMovingToHaltX();
                }

            }
            /**
             * steer right at accelerated rate towards max speed.
             */
            public void SteerAcceleratedRightToMaxFixed(){

                if(Action.this.joyStickDirection.x < Action.this.maxJoyStickDirection.x){
                    Action.this.joyStickDirection.x += 0.1f;
                }

            }
            /**
             * steer left at accelerated rate towards max speed.
             */
            public void SteerAcceleratedLeftToMaxFixed(){

                if(Action.this.joyStickDirection.x > - Action.this.maxJoyStickDirection.x){
                    Action.this.joyStickDirection.x -= 0.1f;
                }

            }
            /**
             * steer right at accelerated rate towards maxJoy ratio.
             * if joystick x is bigger than maxJoyx then decelerate to it.
             * @param ratio
             * ratio is between 0 and 1 of the max joy x
             */
            public void SteerAcceleratedRightToRatio(float ratio){

                //if joystick x is less than maxJoyx then increase it
                if(Action.this.joyStickDirection.x < Action.this.maxJoyStickDirection.x*ratio){
                    Action.this.joyStickDirection.x += 0.1f;
                }else
                //if joystick x is bigger than maxJoyx then decrease it.
                if(Action.this.joyStickDirection.x > Action.this.maxJoyStickDirection.x*ratio){
                    Action.this.joyStickDirection.x -= 0.1f;
                }

            }
            /**
             * steer left at accelerated rate towards - maxJoy ratio.
             * if joystick x is bigger than - maxJoyx then decelerate to it.
             * @param ratio
             * ratio is between 0 and 1 of the max joy x
             */
            public void SteerAcceleratedLeftToRatio(float ratio){

                //if joystick x is less than - maxJoyx then increase it
                if(Action.this.joyStickDirection.x < - Action.this.maxJoyStickDirection.x*ratio){
                    Action.this.joyStickDirection.x += 0.1f;
                }else
                //if joystick x is bigger than - maxJoyx then decrease it.
                if(Action.this.joyStickDirection.x > - Action.this.maxJoyStickDirection.x*ratio){
                    Action.this.joyStickDirection.x -= 0.1f;
                }

            }
            //end of right or left moves


            //foreward and backward moves
            /**
             * seize the tank from moving.
             * in the foreward or backward dimensions only
             */
            public void seizeMovingToHaltY(){

                Action.this.joyStickDirection.y = 0;

            }
            /**
             * seize the tank from moving in all directions.
             */
            public void seizeCompleteMoveing(){

                Action.this.joyStickDirection.x = 0;
                Action.this.joyStickDirection.y = 0;

            }
            /**
             * this makes the tank move foreward in an accelerated way toward destination.
             * and stops when there.
             */
            private void moveForewardAccelerated(){

                //if distance is less than 5 to the destination then start decelerating.
                if(this.distanceToDestination <= 10){

                    //start decelerating
                    //and when distance is zero then stop the joystick
                    if(this.distanceToDestination <= 0.4f){
                        Action.this.joyStickDirection.y = 0;
                    }else{
                        if(Action.this.joyStickDirection.y > 0){
                            Action.this.joyStickDirection.y = Action.this.joyStickDirection.y - 0.1f;
                        }
                    }

                }else{

                    //if joystick is at max then stop incrementing the joystick
                    if(Action.this.joyStickDirection.y < Action.this.maxJoyStickDirection.y){
                        Action.this.joyStickDirection.y = Action.this.joyStickDirection.y + 0.1f;
                        //Gdx.app.log("s","stopped incrementing: joy: "+
                        //        Action.this.joyStickDirection.y+" max: "+Action.this.maxJoyStickDirection.y);
                    }

                }

            }
            /**
             * this makes the tank move foreward at a max fixed pace.
             * it accelerates as it is moving foreward.
             */
            public void moveAcclerateForewardToMaxFixed(){

                //if joystick is at max then stop incrementing the joystick
                if(Action.this.joyStickDirection.y < Action.this.maxJoyStickDirection.y/7){
                    Action.this.joyStickDirection.y = Action.this.joyStickDirection.y + 0.1f;
                }

            }
            /**
             * this makes the tank move foreward at the rate of the input pace.
             * @param rate
             * foreward rate pace measured in joystick dimensions.
             */
            public void moveForewardFixed(float rate){

            }
            /**
             * this makes the tank move backward in an accelerated way towards a destination point.
             */
            private void moveBackwardAccelerated(){

            }
            /**
             * this makes the tank move backward at a max fixed pace and
             * it accelerates as it is moving towards that fixed speed.
             */
            public void moveAcceleratedBackwardToMaxFixed(){

                //if joystick is at max then stop incrementing the joystick
                if(Action.this.joyStickDirection.y > - Action.this.maxJoyStickDirection.y){
                    Action.this.joyStickDirection.y = Action.this.joyStickDirection.y - 0.1f;
                }

            }
            /**
             * this makes the tank move backward at the rate of the input pace.
             * @param rate
             * backward rate pace measured in joystick dimensions.
             */
            public void moveBackwardFixed(float rate){

            }
            /**
             * slow down to a slow rate in the foreword direction.
             * does not matter what sign the y joystick is pointing in.
             * @param ratio
             * the ratio of maxJoyY the entity will slow down to.
             * 0 -> 1
             */
            public void SlowDownToFixedRateYForeword(float ratio){

                //check if joyY has a value bigger than the + maxJoy * ratio
                if(Action.this.joyStickDirection.y > Action.this.maxJoyStickDirection.y*ratio ){
                    Action.this.joyStickDirection.y -= 0.1f;
                    //Gdx.app.log("s","joy is too positive");
                }else
                //check if joyY has a value smaller than the + maxJoy * ratio
                if(Action.this.joyStickDirection.y < Action.this.maxJoyStickDirection.y*ratio){
                    Action.this.joyStickDirection.y += 0.1f;
                    //Gdx.app.log("s","joy is too negative");
                }

            }
            /**
             * slow down to a slow rate in the backward direction.
             * does not matter what sign the y joystick is pointing in.
             * @param ratio
             * the ratio of maxJoyY the entity will slow down to.
             * 0 -> 1
             */
            public void SlowDownToFixedRateYBackward(float ratio){

                //check if joyY has a value bigger than the + maxJoy * ratio
                if(Action.this.joyStickDirection.y < - Action.this.maxJoyStickDirection.y*ratio ){
                    Action.this.joyStickDirection.y += 0.1f;
                }else
                //check if joyY has a value smaller than the + maxJoy * ratio
                if(Action.this.joyStickDirection.y > - Action.this.maxJoyStickDirection.y*ratio){
                    Action.this.joyStickDirection.y -= 0.1f;
                }

            }
            //end of foreward and backward moves

        }

    }

    /**
     * the class below adds sensors to the ai entity.
     * these sensors are used to aid in its motility.
     */
    public class SensorsManager{

        //polygon shape
        PolygonShape polygonShape;
        //circle shape
        CircleShape circleShape;
        //fixture def
        FixtureDef fixtureDef;

        //radius ratio to tank body width
        private float width_ratio = 4;

        //tells whether the outer front left sensors are true
        private boolean[] outer_front_left_sensors = new boolean[]{
                false, false, false, false
        },
        //tells whether the outer front right sensors are true
        outer_front_right_sensors = new boolean[]{
                false, false, false, false
        },
        //tells whether the outer back left sensors are true
        outer_back_left_sensors = new boolean[]{
                false, false, false, false
        },
        //tells whether the outer back right sensors are true
        outer_back_right_sensors = new boolean[]{
                false, false, false, false
        };

        //tells whether the middle front and middle sensor is true
        private boolean outer_front_middle_sensor = false,
                outer_back_middle_sensor = false,
        //tells whether the right and left middle sensors are true
        right_middle_sensor = false,
                left_middle_sensor = false,
        //tells whether the inner back and front sensor are true
        inner_front_sensor = false,
                inner_back_sensor = false
        ;

        /**
         * initiator.
         */
        public SensorsManager(){

            polygonShape = new PolygonShape();
            circleShape = new CircleShape();
            fixtureDef = new FixtureDef();

            //set the fixture properties
            fixtureDef.isSensor = true;
            fixtureDef.friction = 0;
            fixtureDef.density = 0;
            fixtureDef.restitution = 0;
            fixtureDef.filter.categoryBits = ConstantVariables.Box2dVariables.SENSOR;
            fixtureDef.filter.maskBits = ConstantVariables.Box2dVariables.FLASH_LIGHT;
            fixtureDef.filter.groupIndex = 0;

            //add outer front sensors
            this.addOuterFrontSensors();

            //add inner front sensors
            this.addInnerFrontSensors();

            //add right and left middle sensors
            this.addRightAndLeftMiddleSensors();

            //add outer back sensors
            this.addOuterBackSensors();

            //add inner back sensors
            this.addInnerBackSensors();

            //add a big sensor around the entity to check for current objects
            this.addScopingSensor();

        }

        //setters
        /**
         * add the front outer sensors.
         */
        private void addOuterFrontSensors(){

            //add the left sensors 1
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio, tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*20 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*20 )
                    + tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio, tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_LEFT_BACK_SENSOR1);

            //add the left sensors 2
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*20 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*20 )
                            + tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*40 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*40 )
                            + tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*20 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*20 )
                            + tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_LEFT_BACK_SENSOR2);

            //add the left sensors 3
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*40 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*40 )
                            + tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*60 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*60 )
                            + tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*40 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*40 )
                            + tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_LEFT_BACK_SENSOR3);

            //add the left sensors 4
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*60 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*60 )
                            + tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*80 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*80 )
                            + tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*60 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*60 )
                            + tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_LEFT_BACK_SENSOR4);

            //add the middle sensor
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*80 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*80 )
                            + tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*100 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*100 )
                            + tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*80 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*80 )
                            + tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_MIDDLE_FRONT_SENSOR);

            //add the right sensors 4
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*100 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*100 )
                            + tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*120 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*120 )
                            + tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*100 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*100 )
                            + tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_RIGHT_FRONT_SENSOR4);

            //add the right sensors 3
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*120 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*120 )
                            + tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*140 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*140 )
                            + tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*120 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*120 )
                            + tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_RIGHT_FRONT_SENSOR3);

            //add the right sensors 2
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*140 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*140 )
                            + tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*160 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*160 )
                            + tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*140 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*140 )
                            + tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_RIGHT_FRONT_SENSOR2);

            //add the right sensors 1
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*160 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*160 )
                            + tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*180 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*180 )
                            + tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*160 ),
                    tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*160 )
                            + tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_RIGHT_FRONT_SENSOR1);

        }
        /**
         * add the front inner sensors.
         */
        private void addInnerFrontSensors(){

            //add the inner front sensor
            polygonShape.set(new float[]{
                    - tank.getBodyDimensions().x, tank.getBodyDimensions().y,       //bottom left point
                    tank.getBodyDimensions().x, tank.getBodyDimensions().y,         //bottom right point
                    tank.getBodyDimensions().x, tank.getBodyDimensions().y*1.7f,    //top right point
                    - tank.getBodyDimensions().x, tank.getBodyDimensions().y*1.7f,  //top left point
                    - tank.getBodyDimensions().x, tank.getBodyDimensions().y        //bottom left point
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.INNER_FRONT_SENSOR);

        }
        /**
         * add outer back sensors.
         */
        private void addOuterBackSensors(){

            //add the left sensors 1
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio, - tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*20 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*20 )
                            - tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, - tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio, - tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_LEFT_BACK_SENSOR1);

            //add the left sensors 2
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*20 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*20 )
                            - tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*40 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*40 )
                            - tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, - tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*20 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*20 )
                            - tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_LEFT_BACK_SENSOR2);

            //add the left sensors 3
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*40 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*40 )
                            - tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*60 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*60 )
                            - tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, - tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*40 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*40 )
                            - tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_LEFT_BACK_SENSOR3);

            //add the left sensors 4
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*60 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*60 )
                            - tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*80 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*80 )
                            - tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, - tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*60 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*60 )
                            - tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_LEFT_BACK_SENSOR4);

            //add the middle sensor
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*80 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*80 )
                            - tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*100 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*100 )
                            - tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, - tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*80 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*80 )
                            - tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_MIDDLE_BACK_SENSOR);

            //add the right sensors 4
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*100 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*100 )
                            - tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*120 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*120 )
                            - tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, - tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*100 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*100 )
                            - tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_RIGHT_BACK_SENSOR4);

            //add the right sensors 3
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*120 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*120 )
                            - tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*140 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*140 )
                            - tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, - tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*120 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*120 )
                            - tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_RIGHT_BACK_SENSOR3);

            //add the right sensors 2
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*140 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*140 )
                            - tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*160 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*160 )
                            - tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, - tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*140 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*140 )
                            - tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_RIGHT_BACK_SENSOR2);

            //add the right sensors 1
            polygonShape.set(new float[]{
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*160 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*160 )
                            - tank.getBodyDimensions().y,
                    //right most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*180 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*180 )
                            - tank.getBodyDimensions().y,
                    //vertex center of the triangle
                    0, - tank.getBodyDimensions().y,
                    //left most point
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.cos( MathUtils.degreesToRadians*160 ),
                    - tank.getBodyDimensions().x*width_ratio*(float)Math.sin( MathUtils.degreesToRadians*160 )
                            - tank.getBodyDimensions().y
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.OUTER_RIGHT_BACK_SENSOR1);

        }
        /**
         * add inner back sensors.
         */
        private void addInnerBackSensors(){

            //add the inner back sensor
            polygonShape.set(new float[]{
                    - tank.getBodyDimensions().x, - tank.getBodyDimensions().y,       //bottom left point
                    tank.getBodyDimensions().x, - tank.getBodyDimensions().y,         //bottom right point
                    tank.getBodyDimensions().x, - tank.getBodyDimensions().y*1.7f,    //top right point
                    - tank.getBodyDimensions().x, - tank.getBodyDimensions().y*1.7f,  //top left point
                    - tank.getBodyDimensions().x, - tank.getBodyDimensions().y        //bottom left point
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.INNER_BACK_SENSOR);

        }
        /**
         * adds the right and left middle sensors.
         */
        private void addRightAndLeftMiddleSensors(){

            //add the left middle sensor
            polygonShape.set(new float[]{
                    - tank.getBodyDimensions().x, - tank.getBodyDimensions().y,                     //inner back point
                    - tank.getBodyDimensions().x, tank.getBodyDimensions().y,                       //inner front point
                    - tank.getBodyDimensions().x*width_ratio, tank.getBodyDimensions().y,           //outer front point
                    - tank.getBodyDimensions().x*width_ratio, - tank.getBodyDimensions().y,         //outer back point
                    - tank.getBodyDimensions().x, - tank.getBodyDimensions().y                      //bottom left point
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.LEFT_MIDDLE_SENSOR);

            //add the right middle sensor
            polygonShape.set(new float[]{
                    tank.getBodyDimensions().x, - tank.getBodyDimensions().y,                     //inner back point
                    tank.getBodyDimensions().x, tank.getBodyDimensions().y,                       //inner front point
                    tank.getBodyDimensions().x*width_ratio, tank.getBodyDimensions().y,           //outer front point
                    tank.getBodyDimensions().x*width_ratio, - tank.getBodyDimensions().y,         //outer back point
                    tank.getBodyDimensions().x, - tank.getBodyDimensions().y                      //bottom left point
            });
            fixtureDef.shape = polygonShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.RIGHT_MIDDLE_SENSOR);

        }
        /**
         * adds the scoping sensor around the tank to search for entities or objects nearby.
         */
        private void addScopingSensor(){

            //add the scoping sensor
            circleShape.setRadius(TankMode.getTankScope(TankIntelligi.this.tankModeType));

            fixtureDef.shape = circleShape;
            tank.body.createFixture(fixtureDef).setUserData(NEARBYSENSOR.SCOPING_SENSOR);

        }
        //end of setters


        //sensors triggering
        /**
         * reset all sensors.
         */
        public void resetSensors(){
            this.inner_front_sensor = false;
            this.inner_back_sensor = false;
            this.right_middle_sensor = false;
            this.left_middle_sensor = false;
            this.outer_back_middle_sensor = false;
            this.outer_front_middle_sensor = false;
            this.outer_front_left_sensors[0] = false;
            this.outer_front_left_sensors[1] = false;
            this.outer_front_left_sensors[2] = false;
            this.outer_front_left_sensors[3] = false;
            this.outer_front_right_sensors[0] = false;
            this.outer_front_right_sensors[1] = false;
            this.outer_front_right_sensors[2] = false;
            this.outer_front_right_sensors[3] = false;
            this.outer_back_left_sensors[0] = false;
            this.outer_back_left_sensors[1] = false;
            this.outer_back_left_sensors[2] = false;
            this.outer_back_left_sensors[3] = false;
            this.outer_back_right_sensors[0] = false;
            this.outer_back_right_sensors[1] = false;
            this.outer_back_right_sensors[2] = false;
            this.outer_back_right_sensors[3] = false;
        }
        /**
         * set inner back detection.
         */
        public void setInner_back_sensor(){
            this.inner_back_sensor = true;
        }
        /**
         * deset inner back detection.
         */
        public void desetInner_back_sensor(){
            this.inner_back_sensor = false;
        }
        /**
         * set inner front detection.
         */
        public void setInner_front_sensor(){
            this.inner_front_sensor = true;
        }
        /**
         * deset inner front detection.
         */
        public void desetInner_front_sensor(){
            this.inner_front_sensor = false;
        }
        /**
         * set left middle sensor
         */
        public void setLeft_middle_sensor(){
            this.left_middle_sensor = true;
        }
        /**
         * deset left middle sensor
         */
        public void desetLeft_middle_sensor(){
            this.left_middle_sensor = false;
        }
        /**
         * set right middle sensor
         */
        public void setRight_middle_sensor(){
            this.right_middle_sensor = true;
        }
        /**
         * deset right middle sensor
         */
        public void desetRight_middle_sensor(){
            this.right_middle_sensor = false;
        }
        /**
         * set outer front middle sensor.
         */
        public void setOuter_front_middle_sensor(){
            this.outer_front_middle_sensor = true;
        }
        /**
         * deset outer front middle sensor.
         */
        public void desetOuter_front_middle_sensor(){
            this.outer_front_middle_sensor = false;
        }
        /**
         * set the outer front left sensor 1.
         */
        public void setOuter_front_left_sensors1(){
            this.outer_front_left_sensors[0] = true;
        }
        /**
         * deset the outer front left sensor 1.
         */
        public void desetOuter_front_left_sensors1(){
            this.outer_front_left_sensors[0] = false;
        }
        /**
         * set the outer front left sensor 2.
         */
        public void setOuter_front_left_sensors2(){
            this.outer_front_left_sensors[1] = true;
        }
        /**
         * deset the outer front left sensor 2.
         */
        public void desetOuter_front_left_sensors2(){
            this.outer_front_left_sensors[1] = false;
        }
        /**
         * set the outer front left sensor 3.
         */
        public void setOuter_front_left_sensors3(){
            this.outer_front_left_sensors[2] = true;
        }
        /**
         * dset the outer front left sensor 3.
         */
        public void desetOuter_front_left_sensors3(){
            this.outer_front_left_sensors[2] = false;
        }
        /**
         * set the outer front left sensor 4.
         */
        public void setOuter_front_left_sensors4(){
            this.outer_front_left_sensors[3] = true;
        }
        /**
         * dset the outer front left sensor 4.
         */
        public void desetOuter_front_left_sensors4(){
            this.outer_front_left_sensors[3] = false;
        }
        /**
         * set the outer front right sensor 1.
         */
        public void setOuter_front_right_sensors1(){
            this.outer_front_right_sensors[0] = true;
        }
        /**
         * dset the outer front right sensor 1.
         */
        public void desetOuter_front_right_sensors1(){
            this.outer_front_right_sensors[0] = false;
        }
        /**
         * set the outer front right sensor 2.
         */
        public void setOuter_front_right_sensors2(){
            this.outer_front_right_sensors[1] = true;
        }
        /**
         * deset the outer front right sensor 2.
         */
        public void desetOuter_front_right_sensors2(){
            this.outer_front_right_sensors[1] = false;
        }
        /**
         * set the outer front right sensor 3.
         */
        public void setOuter_front_right_sensors3(){
            this.outer_front_right_sensors[2] = true;
        }
        /**
         * deset the outer front right sensor 3.
         */
        public void desetOuter_front_right_sensors3(){
            this.outer_front_right_sensors[2] = false;
        }
        /**
         * set the outer front right sensor 4.
         */
        public void setOuter_front_right_sensors4(){
            this.outer_front_right_sensors[3] = true;
        }
        /**
         * deset the outer front right sensor 4.
         */
        public void desetOuter_front_right_sensors4(){
            this.outer_front_right_sensors[3] = false;
        }
        /**
         * set outer back middle sensor.
         */
        public void setOuter_back_middle_sensor(){
            this.outer_back_middle_sensor = true;
        }
        /**
         * deset outer back middle sensor.
         */
        public void desetOuter_back_middle_sensor(){
            this.outer_back_middle_sensor = false;
        }
        /**
         * set the outer back left sensor 1.
         */
        public void setOuter_back_left_sensors1(){
            this.outer_back_left_sensors[0] = true;
        }
        /**
         * deset the outer back left sensor 1.
         */
        public void desetOuter_back_left_sensors1(){
            this.outer_back_left_sensors[0] = false;
        }
        /**
         * set the outer back left sensor 2.
         */
        public void setOuter_back_left_sensors2(){
            this.outer_back_left_sensors[1] = true;
        }
        /**
         * deset the outer back left sensor 2.
         */
        public void desetOuter_back_left_sensors2(){
            this.outer_back_left_sensors[1] = false;
        }
        /**
         * set the outer back left sensor 3.
         */
        public void setOuter_back_left_sensors3(){
            this.outer_back_left_sensors[2] = true;
        }
        /**
         * dset the outer back left sensor 3.
         */
        public void desetOuter_back_left_sensors3(){
            this.outer_back_left_sensors[2] = false;
        }
        /**
         * set the outer back left sensor 4.
         */
        public void setOuter_back_left_sensors4(){
            this.outer_back_left_sensors[3] = true;
        }
        /**
         * dset the outer back left sensor 4.
         */
        public void desetOuter_back_left_sensors4(){
            this.outer_back_left_sensors[3] = false;
        }
        /**
         * set the outer back right sensor 1.
         */
        public void setOuter_back_right_sensors1(){
            this.outer_back_right_sensors[0] = true;
        }
        /**
         * dset the outer back right sensor 1.
         */
        public void desetOuter_back_right_sensors1(){
            this.outer_back_right_sensors[0] = false;
        }
        /**
         * set the outer back right sensor 2.
         */
        public void setOuter_back_right_sensors2(){
            this.outer_back_right_sensors[1] = true;
        }
        /**
         * deset the outer back right sensor 2.
         */
        public void desetOuter_back_right_sensors2(){
            this.outer_back_right_sensors[1] = false;
        }
        /**
         * set the outer back right sensor 3.
         */
        public void setOuter_back_right_sensors3(){
            this.outer_back_right_sensors[2] = true;
        }
        /**
         * deset the outer back right sensor 3.
         */
        public void desetOuter_back_right_sensors3(){
            this.outer_back_right_sensors[2] = false;
        }
        /**
         * set the outer back right sensor 4.
         */
        public void setOuter_back_right_sensors4(){
            this.outer_back_right_sensors[3] = true;
        }
        /**
         * deset the outer back right sensor 4.
         */
        public void desetOuter_back_right_sensors4(){
            this.outer_back_right_sensors[3] = false;
        }
        //end of sensors triggering


        //getters
        /**
         * this method checks whether all the front sensors are not on.
         */
        public boolean areFrontSensorsOff(){
            if(isOuter_left_front_sensor1() || isOuter_left_front_sensor2()
                || isOuter_left_front_sensor3() || isOuter_left_front_sensor4()
                    || isOuter_front_middle_sensor()
                || isOuter_right_front_sensor1() || isOuter_right_front_sensor2()
                || isOuter_right_front_sensor3() || isOuter_right_front_sensor4()
                    || isInner_front_sensor() ){
                return false;
            }else{
                return true;
            }
        }
        /**
         * tells whether there is an inner front detection.
         */
        public boolean isInner_front_sensor(){
            return this.inner_front_sensor;
        }
        /**
         * tells whether there is an inner back detection.
         */
        public boolean isInner_back_sensor(){
            return this.inner_back_sensor;
        }
        /**
         * tells whether there is a right middle detection.
         */
        public boolean isRight_middle_sensor(){
            return this.right_middle_sensor;
        }
        /**
         * tells whether there is a left middle detection.
         */
        public boolean isLeft_middle_sensor(){
            return this.left_middle_sensor;
        }
        /**
         * tells whether there is an outer left front detection 1.
         */
        public boolean isOuter_left_front_sensor1(){
            return this.outer_front_left_sensors[0];
        }
        /**
         * tells whether there is an outer left front detection 2.
         */
        public boolean isOuter_left_front_sensor2(){
            return this.outer_front_left_sensors[1];
        }
        /**
         * tells whether there is an outer left front detection 3.
         */
        public boolean isOuter_left_front_sensor3(){
            return this.outer_front_left_sensors[2];
        }
        /**
         * tells whether there is an outer left front detection 4.
         */
        public boolean isOuter_left_front_sensor4(){
            return this.outer_front_left_sensors[3];
        }
        /**
         * tells whether there is an outer right front detection 1.
         */
        public boolean isOuter_right_front_sensor1(){
            return this.outer_front_right_sensors[0];
        }
        /**
         * tells whether there is an outer right front detection 2.
         */
        public boolean isOuter_right_front_sensor2(){
            return this.outer_front_right_sensors[1];
        }
        /**
         * tells whether there is an outer right front detection 3.
         */
        public boolean isOuter_right_front_sensor3(){
            return this.outer_front_right_sensors[2];
        }
        /**
         * tells whether there is an outer right front detection 4.
         */
        public boolean isOuter_right_front_sensor4(){
            return this.outer_front_right_sensors[3];
        }
        /**
         * tells whether there is an outer front middle detection.
         */
        public boolean isOuter_front_middle_sensor(){
            return this.outer_front_middle_sensor;
        }
        /**
         * tells whether there is an outer left back detection 1.
         */
        public boolean isOuter_left_back_sensor1(){
            return this.outer_back_left_sensors[0];
        }
        /**
         * tells whether there is an outer left back detection 2.
         */
        public boolean isOuter_left_back_sensor2(){
            return this.outer_back_left_sensors[1];
        }
        /**
         * tells whether there is an outer left back detection 3.
         */
        public boolean isOuter_left_back_sensor3(){
            return this.outer_back_left_sensors[2];
        }
        /**
         * tells whether there is an outer left back detection 4.
         */
        public boolean isOuter_left_back_sensor4(){
            return this.outer_back_left_sensors[3];
        }
        /**
         * tells whether there is an outer right back detection 1.
         */
        public boolean isOuter_right_back_sensor1(){
            return this.outer_back_right_sensors[0];
        }
        /**
         * tells whether there is an outer right back detection 2.
         */
        public boolean isOuter_right_back_sensor2(){
            return this.outer_back_right_sensors[1];
        }
        /**
         * tells whether there is an outer right back detection 3.
         */
        public boolean isOuter_right_back_sensor3(){
            return this.outer_back_right_sensors[2];
        }
        /**
         * tells whether there is an outer right back detection 4.
         */
        public boolean isOuter_right_back_sensor4(){
            return this.outer_back_right_sensors[3];
        }
        /**
         * tells whether there is an outer back middle detection.
         */
        public boolean isOuter_back_middle_sensor(){
            return this.outer_back_middle_sensor;
        }
        //end of getters


        //listeners
        /**
         * reports to nearby objects sensors. (Close to the entity) When there is a contact begin.
         * @param bodyFixture
         * body fixture.
         * @param alienFixture
         * other not part of this entity's body fixtures.
         * @param worldManifold
         * collision properties like point of collision, normal collision vector, etc...
         */
        private void reportNearbyObjectsBegin(Fixture bodyFixture, Fixture alienFixture, WorldManifold worldManifold){

            switch (((NEARBYSENSOR)bodyFixture.getUserData())){
                case INNER_FRONT_SENSOR:
                    this.setInner_front_sensor();
                    break;
                case INNER_BACK_SENSOR:
                    this.setInner_back_sensor();
                    break;
                case LEFT_MIDDLE_SENSOR:
                    this.setLeft_middle_sensor();
                    break;
                case RIGHT_MIDDLE_SENSOR:
                    this.setRight_middle_sensor();
                    break;
                case OUTER_LEFT_FRONT_SENSOR1:
                    this.setOuter_front_left_sensors1();
                    break;
                case OUTER_LEFT_FRONT_SENSOR2:
                    this.setOuter_front_left_sensors2();
                    break;
                case OUTER_LEFT_FRONT_SENSOR3:
                    this.setOuter_front_left_sensors3();
                    break;
                case OUTER_LEFT_FRONT_SENSOR4:
                    this.setOuter_front_left_sensors4();
                    break;
                case OUTER_MIDDLE_FRONT_SENSOR:
                    this.setOuter_front_middle_sensor();
                    break;
                case OUTER_RIGHT_FRONT_SENSOR1:
                    this.setOuter_front_right_sensors1();
                    break;
                case OUTER_RIGHT_FRONT_SENSOR2:
                    this.setOuter_front_right_sensors2();
                    break;
                case OUTER_RIGHT_FRONT_SENSOR3:
                    this.setOuter_front_right_sensors3();
                    break;
                case OUTER_RIGHT_FRONT_SENSOR4:
                    this.setOuter_front_right_sensors4();
                    break;
                case OUTER_LEFT_BACK_SENSOR1:
                    this.setOuter_back_left_sensors1();
                    break;
                case OUTER_LEFT_BACK_SENSOR2:
                    this.setOuter_back_left_sensors2();
                    break;
                case OUTER_LEFT_BACK_SENSOR3:
                    this.setOuter_back_left_sensors3();
                    break;
                case OUTER_LEFT_BACK_SENSOR4:
                    this.setOuter_back_left_sensors4();
                    break;
                case OUTER_MIDDLE_BACK_SENSOR:
                    this.setOuter_back_middle_sensor();
                    break;
                case OUTER_RIGHT_BACK_SENSOR1:
                    this.setOuter_back_right_sensors1();
                    break;
                case OUTER_RIGHT_BACK_SENSOR2:
                    this.setOuter_back_right_sensors2();
                    break;
                case OUTER_RIGHT_BACK_SENSOR3:
                    this.setOuter_back_right_sensors3();
                    break;
                case OUTER_RIGHT_BACK_SENSOR4:
                    this.setOuter_back_right_sensors4();
                    break;
            }

            //tells that the nearby sensors has been triggered so tell the nearby class to act
            if(this.isInner_front_sensor() || this.isInner_back_sensor()
                    || this.isLeft_middle_sensor() || this.isRight_middle_sensor()
                    || this.isOuter_left_front_sensor1() || this.isOuter_left_front_sensor2()
                    || this.isOuter_left_front_sensor3() || this.isOuter_left_front_sensor4()
                    || this.isOuter_front_middle_sensor()
                    || this.isOuter_right_front_sensor1() ||  this.isOuter_right_front_sensor2()
                    || this.isOuter_right_front_sensor3() ||  this.isOuter_right_front_sensor4()
                    || this.isOuter_left_back_sensor1() ||  this.isOuter_left_back_sensor2()
                    || this.isOuter_left_back_sensor3() ||  this.isOuter_left_back_sensor4()
                    || this.isOuter_back_middle_sensor()
                    || this.isOuter_right_back_sensor1() ||  this.isOuter_right_back_sensor2()
                    || this.isOuter_right_back_sensor3() ||  this.isOuter_right_back_sensor4()){
                TankIntelligi.this.getRadar().getNearbyObjects().setHasNearbyObject();
            }

        }
        /**
         * reports to nearby objects sensors. (Close to the entity) when there is a contact end.
         * @param bodyFixture
         * body fixture.
         * @param alienFixture
         * other not part of this entity's body fixtures.
         */
        private void reportNearbyObjectsEnd(Fixture bodyFixture, Fixture alienFixture){

            switch (((NEARBYSENSOR)bodyFixture.getUserData())){
                case INNER_FRONT_SENSOR:
                    this.desetInner_front_sensor();
                    break;
                case INNER_BACK_SENSOR:
                    this.desetInner_back_sensor();
                    break;
                case LEFT_MIDDLE_SENSOR:
                    this.desetLeft_middle_sensor();
                    break;
                case RIGHT_MIDDLE_SENSOR:
                    this.desetRight_middle_sensor();
                    break;
                case OUTER_LEFT_FRONT_SENSOR1:
                    this.desetOuter_front_left_sensors1();
                    break;
                case OUTER_LEFT_FRONT_SENSOR2:
                    this.desetOuter_front_left_sensors2();
                    break;
                case OUTER_LEFT_FRONT_SENSOR3:
                    this.desetOuter_front_left_sensors3();
                    break;
                case OUTER_LEFT_FRONT_SENSOR4:
                    this.desetOuter_front_left_sensors4();
                    break;
                case OUTER_MIDDLE_FRONT_SENSOR:
                    this.desetOuter_front_middle_sensor();
                    break;
                case OUTER_RIGHT_FRONT_SENSOR1:
                    this.desetOuter_front_right_sensors1();
                    break;
                case OUTER_RIGHT_FRONT_SENSOR2:
                    this.desetOuter_front_right_sensors2();
                    break;
                case OUTER_RIGHT_FRONT_SENSOR3:
                    this.desetOuter_front_right_sensors3();
                    break;
                case OUTER_RIGHT_FRONT_SENSOR4:
                    this.desetOuter_front_right_sensors4();
                    break;
                case OUTER_LEFT_BACK_SENSOR1:
                    this.desetOuter_back_left_sensors1();
                    break;
                case OUTER_LEFT_BACK_SENSOR2:
                    this.desetOuter_back_left_sensors2();
                    break;
                case OUTER_LEFT_BACK_SENSOR3:
                    this.desetOuter_back_left_sensors3();
                    break;
                case OUTER_LEFT_BACK_SENSOR4:
                    this.desetOuter_back_left_sensors4();
                    break;
                case OUTER_MIDDLE_BACK_SENSOR:
                    this.desetOuter_back_middle_sensor();
                    break;
                case OUTER_RIGHT_BACK_SENSOR1:
                    this.desetOuter_back_right_sensors1();
                    break;
                case OUTER_RIGHT_BACK_SENSOR2:
                    this.desetOuter_back_right_sensors2();
                    break;
                case OUTER_RIGHT_BACK_SENSOR3:
                    this.desetOuter_back_right_sensors3();
                    break;
                case OUTER_RIGHT_BACK_SENSOR4:
                    this.desetOuter_back_right_sensors4();
                    break;
            }

            //if all sensor are not set then disable the has nearby object ot false
            if(!this.isInner_front_sensor() && !this.isInner_back_sensor()
                    && !this.isLeft_middle_sensor() && !this.isRight_middle_sensor()
                    && !this.isOuter_left_front_sensor1() && !this.isOuter_left_front_sensor2()
                    && !this.isOuter_left_front_sensor3() && !this.isOuter_left_front_sensor4()
                    && !this.isOuter_front_middle_sensor()
                    && !this.isOuter_right_front_sensor1() && !this.isOuter_right_front_sensor2()
                    && !this.isOuter_right_front_sensor3() && !this.isOuter_right_front_sensor4()
                    && !this.isOuter_left_back_sensor1() && !this.isOuter_left_back_sensor2()
                    && !this.isOuter_left_back_sensor3() && !this.isOuter_left_back_sensor4()
                    && !this.isOuter_back_middle_sensor()
                    && !this.isOuter_right_back_sensor1() && !this.isOuter_right_back_sensor2()
                    && !this.isOuter_right_back_sensor3() && !this.isOuter_right_back_sensor4()){
                TankIntelligi.this.getRadar().getNearbyObjects().desetHasNearbyObject();
            }

        }
        /**
         * reports a main player entity nearby begin.
         * @param bodyFixture
         * body fixture.
         * @param alienFixture
         * other not part of this entity's body fixtures.
         * @param worldManifold
         * collision properties like point of collision, normal collision vector, etc...
         */
        private void reportNearbyTankEntityBegin(Fixture bodyFixture, Fixture alienFixture, WorldManifold worldManifold){

            //check if it is a scoping sensor first
            if(bodyFixture.getUserData() == NEARBYSENSOR.SCOPING_SENSOR){

                /*
                if(alienFixture.getBody().getUserData() != null){
                    Gdx.app.log("s","----------------------------------------------->");
                    Gdx.app.log("s","Type: "+alienFixture.getBody().getUserData());
                    for (int i = 0; i < worldManifold.getPoints().length; i++){
                        Gdx.app.log("s","collision: "+worldManifold.getPoints()[i]);
                    }
                    Gdx.app.log("s","My Body: "+bodyFixture.getBody().getPosition());
                    Gdx.app.log("s","<----------------------------------------------->");
                }
                */

                //then check if the alien fixture is a tank entity
                if(alienFixture.getBody().getUserData() != null
                        && alienFixture.getBody().getUserData() instanceof Tank){
                    Tank alienTank = (Tank)alienFixture.getBody().getUserData();
                    switch (alienTank.tankProperties.tankpilot){
                        case MAIN_PLAYER:
                            radar.tankChaser.setTankToChase(alienTank);
                            break;
                    }
                }

            }

        }
        /**
         * reports a main player entity nearby end.
         * @param bodyFixture
         * body fixture.
         * @param alienFixture
         * other not part of this entity's body fixtures.
         */
        private void reportNearbyTankEntityEnd(Fixture bodyFixture, Fixture alienFixture){

            //check if it is a scoping sensor first
            if(bodyFixture.getUserData() == NEARBYSENSOR.SCOPING_SENSOR){

                //then check if the alien fixture is a tank entity
                if(alienFixture.getBody().getUserData() != null
                        && alienFixture.getBody().getUserData() instanceof Tank){
                    Tank alienTank = (Tank)alienFixture.getBody().getUserData();
                    switch (alienTank.tankProperties.tankpilot){
                        case MAIN_PLAYER:
                            radar.tankChaser.removeTankToChase();
                            break;
                    }
                }

            }

        }
        /**
         * does the pre solver for collision analyzing.
         */
        public void collisionFixtureAnalyzerPreSolver(Fixture bodyFixture, Fixture alienFixture, Contact contact){

            /*
            if(bodyFixture.getUserData() != null && bodyFixture.getUserData() instanceof NEARBYSENSOR){

                switch((NEARBYSENSOR)bodyFixture.getUserData()){
                    case SCOPING_SENSOR:
                        contact.setEnabled(false);
                    break;
                }

            }
            */

        }
        /**
         * reported fixture collision detection analyzer.
         * on begin collision.
         * @param bodyFixture
         * the fixture reported.
         * @param AlienFixture
         * the alien fixture that is not part of this entity's body.
         * @param worldManifold
         * collision properties like point of collision, normal collision vector, etc...
         */
        public void collisionFixtureAnalyzer(Fixture bodyFixture, Fixture AlienFixture, WorldManifold worldManifold){

            if(bodyFixture.getUserData() != null && bodyFixture.getUserData() instanceof NEARBYSENSOR){

                //reports any nearby tank entities. (MAIN player)
                this.reportNearbyTankEntityBegin(bodyFixture, AlienFixture, worldManifold);

                //report the nearby sensors (really close to the entity).
                //except bullets
                if( (AlienFixture.getBody().getUserData() != null && !(AlienFixture.getBody().getUserData() instanceof Weapon))
                        || AlienFixture.getBody().getUserData() == null){
                    //and do not avoid other cpu sensors
                    if( (AlienFixture.getUserData() != null && !(AlienFixture.getUserData() instanceof NEARBYSENSOR) )
                            || AlienFixture.getUserData() == null){
                        this.reportNearbyObjectsBegin(bodyFixture, AlienFixture, worldManifold);
                    }

                }

            }

        }
        /**
         * reported fixture collision detection analyzer.
         * on end collision.
         * @param bodyFixture
         * the fixture reported.
         * @param AlienFixture
         * the alien fixture that is not part of this entity's body.
         */
        public void collisionFixtureAnalyzerEnd(Fixture bodyFixture, Fixture AlienFixture){

            if(bodyFixture.getUserData() != null && bodyFixture.getUserData() instanceof NEARBYSENSOR){

                //reports any nearby tank entities. (MAIN player)
                this.reportNearbyTankEntityEnd(bodyFixture, AlienFixture);

                //report the end of contact for realy close nearby sensors.
                //except bullets
                if( (AlienFixture.getBody().getUserData() != null && !(AlienFixture.getBody().getUserData() instanceof Weapon))
                        || AlienFixture.getBody().getUserData() == null) {
                    //and do not avoid other cpu sensors
                    if( (AlienFixture.getUserData() != null && !(AlienFixture.getUserData() instanceof NEARBYSENSOR) )
                            || AlienFixture.getUserData() == null){
                        this.reportNearbyObjectsEnd(bodyFixture, AlienFixture);
                    }
                }

            }

        }
        //end of listeners

    }


    //interfaces below
    /**
     * shooting interface
     */
    public interface TankInterface{
        void shoot(Tank tank);
        void shootElectricShock(Tank tank);
    }
    //end of interfaces


    //enums
    /**
     * enum that gives the external sensors an identity.
     */
    public enum EXTERNALSENSOR{
        //identity for the nearby empty area sensor
        SEARCH_EMPTY_NEARBY_AREA_SENSOR, SEARCH_EMPTY_NEARBY_ROUTE_SENSOR
    }
    /**
     * enum that gives the nearby sensors an identity.
     */
    public enum NEARBYSENSOR{
        //outer left front sensors
        OUTER_LEFT_FRONT_SENSOR1, OUTER_LEFT_FRONT_SENSOR2,
        OUTER_LEFT_FRONT_SENSOR3, OUTER_LEFT_FRONT_SENSOR4,
        //outer right front sensors
        OUTER_RIGHT_FRONT_SENSOR1, OUTER_RIGHT_FRONT_SENSOR2,
        OUTER_RIGHT_FRONT_SENSOR3, OUTER_RIGHT_FRONT_SENSOR4,
        //middle outer front sensor
        OUTER_MIDDLE_FRONT_SENSOR,
        //inner front sensor
        INNER_FRONT_SENSOR,
        //left and right middle sensors
        LEFT_MIDDLE_SENSOR, RIGHT_MIDDLE_SENSOR,
        //outer left back sensors
        OUTER_LEFT_BACK_SENSOR1, OUTER_LEFT_BACK_SENSOR2,
        OUTER_LEFT_BACK_SENSOR3, OUTER_LEFT_BACK_SENSOR4,
        //outer right back sensors
        OUTER_RIGHT_BACK_SENSOR1, OUTER_RIGHT_BACK_SENSOR2,
        OUTER_RIGHT_BACK_SENSOR3, OUTER_RIGHT_BACK_SENSOR4,
        //middle outer back sensor
        OUTER_MIDDLE_BACK_SENSOR,
        //inner back sensor
        INNER_BACK_SENSOR,
        //Scoping Sensor
        SCOPING_SENSOR
    }
    /**
     * enum that gives the state of what is the AI is doing.
     */
    public enum AIACTIVITYSTATE{
        AVOIDING_OBSTACLE, CHASING_ENTITY, IS_STUCK,
        PATROLLING, NOT_DOING_ANYTHING
    }
    /**
     * this enum contains the identity for the search type for the spinner sensor.
     */
    public enum SEARCHTYPE{
        ESCAPE_POINT_SEARCH_POINT, NONE
    }
    //end of enums

}
