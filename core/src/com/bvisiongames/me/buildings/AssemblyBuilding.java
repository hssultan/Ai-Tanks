package com.bvisiongames.me.buildings;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.bvisiongames.me.Tweenanimations.BodyAnimation;
import com.bvisiongames.me.effects.EnergyBall;
import com.bvisiongames.me.entity.EntitManager;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.Assets;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.settings.GeneralMethods;
import java.util.ArrayList;
import java.util.List;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

/**
 * Created by ahzji_000 on 12/14/2015.
 */
public class AssemblyBuilding extends Buildings {

    /**
     * width and height of this building.
     * in pixels.
     */
    public static final float WIDTH = 400, HEIGHT = 400;

    //visible rendering
    private boolean shouldRender = true;

    /**
     * side the building is facing.
     */
    private SIDEFACING side;

    /**
     * tank list of tanks being assembled.
     */
    private List<Tank> tankList = new ArrayList<Tank>();

    /**
     * sprites.
     */
    private AssemblyDrawables
                    bottomGround,        //base of the building
                    building1,          //building1: is the building that produce the tank body parts
                    building1Runway,    //building1Runway: is the runway that the tank body parts come out on.
                    crane,              //crane of the building
                    building2,          //building2: is the building that everything is put together
                    groundMap           //final destination ground on the map
    ;

    /**
     * energy ball animation
     */
    private EnergyBall topDownEnergyBall;

    /**
     * assembler instance class.
     */
    private Assembler assembler = new Assembler();

    //saves the diagonal of this tank in pixels
    private float diagonal = 0;

    //position in pixels
    private Vector2 positionPixels;

    /**
     * intitiator
     * @param position
     * position of the base in sprite dimensions (pixels).
     * @param side
     * Which direction the building is facing the map boundery to put the tank in the map.
     */
    public AssemblyBuilding(Vector2 position, SIDEFACING side){

        //set the position in pixels add half the width and height
        this.positionPixels = new Vector2(position);
        this.positionPixels.x += WIDTH/2;
        this.positionPixels.y += HEIGHT/2;

        //initiate the position
        this.pos = new Vector2(position.x/ConstantVariables.PIXELS_TO_METERS, position.y/ConstantVariables.PIXELS_TO_METERS);
        //initiate the side facing
        this.side = side;

        //add corners for this building
        this.corners = new float[]{
                0,0,                                                                              //point one
                WIDTH/ ConstantVariables.PIXELS_TO_METERS, 0,                                       //point two
                WIDTH/ConstantVariables.PIXELS_TO_METERS, HEIGHT/ConstantVariables.PIXELS_TO_METERS,  //point three
                0, HEIGHT/ConstantVariables.PIXELS_TO_METERS,                                       //point four
                0,0                                                                               //point five
        };
        this.Vcorners = new Vector2[]{
                new Vector2(this.corners[0], this.corners[1]),   //point one
                new Vector2(this.corners[2], this.corners[3]),   //point two
                new Vector2(this.corners[4], this.corners[5]),   //point three
                new Vector2(this.corners[6], this.corners[7]),   //point four
                new Vector2(this.corners[8], this.corners[9])    //point five
        };

        //solve for the diagonal
        this.diagonal = (float)Math.sqrt( WIDTH*WIDTH + HEIGHT*HEIGHT );

        //create the body of this base
        //add a body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(this.getPosition());
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = MultiGameScreen.WORLD.createBody(bodyDef);

        //add a shape
        shape = new ChainShape();
        shape.createChain(this.corners);

        //add a fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.friction = 1;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        //end of body setup

        //Assembling setup construction
        new AssembleContruction(position);

        //initiate the energy ball animation
        this.topDownEnergyBall = new EnergyBall(building2.getPosition().cpy(), false, 0);
        this.topDownEnergyBall.hide();
        EntitManager.effectsList.add(this.topDownEnergyBall);

    }

    //update and render methods
    @Override
    public void update() {

        assembler.update();

        //check if it should be rendered
        if(GeneralMethods.isInsideCameraView( this.positionPixels ,
                                                this.diagonal/2,
                                                MultiGameScreen.camera.frustum)){
            shouldRender = true;
        }else{
            shouldRender = false;
        }

    }
    @Override
    public void render(SpriteBatch batch) {

        if(bottomGround != null && building1Runway != null
                && building2 != null && groundMap != null
                && shouldRender){
            bottomGround.draw(batch);
            building1Runway.draw(batch);
            building2.draw(batch);
            groundMap.draw(batch);
        }

    }

    @Override
    public void renderTop(SpriteBatch batch) {

        if(building1 != null && crane != null
                && shouldRender){
            building1.draw(batch);
            crane.draw(batch);
        }

    }

    //end of update and render methods

    //setters
    /**
     * add a tank to the tank list to be assembled.
     * and set the position to inside building1
     * @param tank
     * the tank to be added and reassembled.
     */
    public void addTankToAssemble(Tank tank){
        tank.body.setTransform(
                (this.building1.getX() + this.building1.getWidth() / 2) / ConstantVariables.PIXELS_TO_METERS,
                (this.building1.getY() + this.building1.getHeight() / 2) / ConstantVariables.PIXELS_TO_METERS,
                0);
        this.tankList.add(tank);
    }
    //end of setters


    //getters

    //end of getters

    /**
     * Construct the assembling unit position and direction they are facing.
     */
    public class AssembleContruction{

        private Vector2 position;

        public AssembleContruction(Vector2 position){

            //initiate the position
            this.position = position;

            //setup the ground base
            setupGround();

            //setup building1
            setupBuilding1();

            //crane setup
            setupCrane();

            //setup building2
            setupBuilding2();

            //setup ground Map
            setupGroundMap();

        }

        /**
         * setup the ground base sprite.
         */
        private void setupGround(){
            bottomGround = new AssemblyDrawables(
                    new NinePatchDrawable(new NinePatch(Assets.GameFileNames.getBunkersSkin().getRegion("bunkers_2x2-06"),
                            30,30,30,30)),
                    new Vector2(WIDTH, HEIGHT)
            );
            bottomGround.setPosition(position.x, position.y);
        }

        /**
         * setup building1 and the runway.
         */
        private void setupBuilding1(){
            building1 = new AssemblyDrawables(
                    new NinePatchDrawable(new NinePatch(Assets.GameFileNames.getBunkersSkin().getRegion("bunkers_2x2"),
                            20,20,20,20)),
                                               new Vector2(WIDTH / 2.4f, HEIGHT / 2) );
            building1Runway = new AssemblyDrawables(
                    new NinePatchDrawable(new NinePatch(Assets.GameFileNames.getBunkersSkin().getRegion("bunkers_2x1"),
                            20,20,20,20)),
                    new Vector2(WIDTH - building1.getWidth()/1.2f, building1.getHeight()/3) );
            switch (side){
                case RIGHT:
                    building1.setPosition(position.x + 10, position.y + HEIGHT / 2 - building1.getHeight() / 2);
                    building1Runway.setPosition(
                            building1.getX() + building1.getWidth()/1.3f,
                            building1.getY() + building1.getHeight()/2 - building1Runway.getHeight()/2);
                    break;
                case LEFT:
                    building1.setPosition(
                            position.x + WIDTH - building1.getWidth() - 10,
                            position.y + HEIGHT / 2 - building1.getHeight() / 2);
                    building1Runway.setPosition(
                            position.x,
                            building1.getY() + building1.getHeight()/2 - building1Runway.getHeight()/2);
                    break;
            }
        }

        /**
         * setup the crane at the end of the assembly.
         */
        private void setupCrane(){
            crane = new AssemblyDrawables(
                    new NinePatchDrawable(new NinePatch(Assets.GameFileNames.getBunkersSkin().getRegion("bunkers_2x1"), 5,5,5,5)),
                    new Vector2(building1Runway.getWidth()/3, building1Runway.getWidth()/3)
            );
            switch (side){
                case RIGHT:
                    crane.setPosition(building1Runway.getX() + building1Runway.getWidth() - crane.getWidth()/2,
                                        building1Runway.getY() + building1Runway.getHeight()/2 - crane.getHeight()/2);
                    break;
                case LEFT:
                    crane.setPosition(
                            building1Runway.getX() - crane.getWidth()/2,
                            building1Runway.getY() + building1Runway.getHeight()/2 - crane.getHeight()/2);
                    break;
            }

        }

        /**
         * setup building2.
         */
        private void setupBuilding2(){
            building2 = new AssemblyDrawables(
                    new NinePatchDrawable(new NinePatch(Assets.GameFileNames.getBunkersSkin().getRegion("bunker_round_top_4way"))),
                    new Vector2(building1.getWidth()/1.3f, building1.getWidth()/1.3f)
            );
            switch (side){
                case RIGHT:
                    building2.setPosition(building1Runway.getX() + building1Runway.getWidth()/2 - building2.getWidth()/2,
                            building1Runway.getY() + building1Runway.getHeight()/2 - building2.getHeight()/2);
                    break;
                case LEFT:
                    building2.setPosition(building1Runway.getX() + building1Runway.getWidth()/2 - building2.getWidth()/2,
                            building1Runway.getY() + building1Runway.getHeight()/2 - building2.getHeight()/2);
                    break;
            }
        }

        /**
         * setup ground Map sprite that is placed on the map.
         */
        private void setupGroundMap(){

            groundMap = new AssemblyDrawables(
                    new NinePatchDrawable(new NinePatch(Assets.GameFileNames.getBunkersSkin().getRegion("bunker_1x1_bottom"), 5,5,5,5)),
                    new Vector2(100, 100)
            );

            switch (side){
                case RIGHT:
                    groundMap.setPosition(position.x + WIDTH,
                                        position.y + HEIGHT/2 - groundMap.getHeight()/2);
                    break;
                case LEFT:
                    groundMap.setPosition(position.x - groundMap.getWidth(),
                            position.y + HEIGHT/2 - groundMap.getHeight()/2);
                    break;
            }

        }

    }

    /**
     * assembler class animation
     */
    public class Assembler{

        //assembly booleans
        private boolean isAssembled = false,
                        isAssembling = false;

        //assembly trigger
        private boolean isAssemblyTriggered = true;

        //checkers
        /**
         * this method returns whether there is assembling.
         */
        public boolean isAssembling(){
            return isAssembling;
        }
        /**
         * this method tells whether the assembling of hte tank is done.
         */
        public boolean isDoneAssembling(){
            return isAssembled;
        }
        //end of checkers

        //setters

        //end of setters


        //getters

        //end of getters


        //update method
        public void update(){

            if(!tankList.isEmpty()){

                isAssembling = true;
                isAssembled = false;

                if(isAssemblyTriggered){
                    isAssemblyTriggered = false;

                    //start the tween by pushing the body from building1
                    animateBodyBuilding1ToBuilding2();

                }

            }else{

                isAssembling = false;
                isAssembled = true;

            }

        }
        //end of update


        //animation methods
        /**
         * push the tank from building1.
         */
        //small variables
        private TweenCallback animateBodyBuilding2CallBack = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                if(type == TweenCallback.END){
                    animateBodyBuilding2ToMap();
                    AssemblyBuilding.this.topDownEnergyBall.restart();
                    AssemblyBuilding.this.topDownEnergyBall.show();
                    AssemblyBuilding.this.topDownEnergyBall.setPos(
                            building2.getPosition().x + building2.getWidth() / 2 - AssemblyBuilding.this.topDownEnergyBall.getWidth() / 2,
                            building2.getPosition().y + building2.getHeight() / 2 - AssemblyBuilding.this.topDownEnergyBall.getHeight() / 2);
                }
            }
        };
        private void animateBodyBuilding1ToBuilding2(){
            //before animation begins rotate the tank so it is facing out to the map
            switch (side){
                case RIGHT:
                    tankList.get(0).body.setTransform(tankList.get(0).body.getPosition(), -(float) Math.PI / 2);
                    break;
                case LEFT:
                    tankList.get(0).body.setTransform(tankList.get(0).body.getPosition(), (float) Math.PI / 2);
                    break;
            }

            Tween.to(tankList.get(0).body, BodyAnimation.POSITION_XY, 4)
                    .target((building2.getX() + building2.getWidth()/2)/ConstantVariables.PIXELS_TO_METERS,
                            (building2.getY() + building2.getHeight()/2)/ConstantVariables.PIXELS_TO_METERS)
                    .setCallbackTriggers(TweenCallback.END)
                    .setCallback(animateBodyBuilding2CallBack)
                    .start(MultiGameScreen.tweenManager);

        }
        /**
         * pause the tank in building2 and then animate its movement to the map.
         */
        //small variables
        private TweenCallback animateBuilding2ToMapCallBack = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                if(type == TweenCallback.END){
                    PostAssemble();
                }
            }
        };
        private void animateBodyBuilding2ToMap(){

            Tween.to(tankList.get(0).body, BodyAnimation.POSITION_XY, 4)
                    .target((groundMap.getX() + groundMap.getWidth()/2)/ConstantVariables.PIXELS_TO_METERS,
                            (groundMap.getY() + groundMap.getHeight()/2)/ ConstantVariables.PIXELS_TO_METERS)
                    .delay(3)
                    .setCallbackTriggers(TweenCallback.END)
                    .setCallback(animateBuilding2ToMapCallBack)
                    .start(MultiGameScreen.tweenManager);

        }
        /**
         * activate the assembled tank and remove it from the list.
         */
        private void PostAssemble(){
            tankList.get(0).activateTank();
            tankList.remove(0);
            //reenable the assembly of tanks
            isAssemblyTriggered = true;
        }
        //end of animation methods

    }

    /**
     * assembly Drawables.
     */
    public class AssemblyDrawables{

        private Vector2 width_height = new Vector2(0, 0),
                        position = new Vector2(0, 0);
        private NinePatchDrawable ninePatchDrawable;

        public AssemblyDrawables(NinePatchDrawable ninePatchDrawable, Vector2 width_height){
            this.ninePatchDrawable = ninePatchDrawable;
            this.width_height.set(width_height.x, width_height.y);
        }

        //setters
        /**
         * set the position of this image.
         */
        public void setPosition(float x, float y){
            this.position.set(x, y);
        }
        /**
         * sets the width of this image
         */
        public void setWidth(float x){
            this.width_height.x = x;
        }
        /**
         * sets the height of this image
         */
        public void setHeight(float y){
            this.width_height.y = y;
        }
        /**
         * set the size of this image
         */
        public void setSize(float x, float y){
            this.width_height.set(x, y);
        }
        //end of setters


        //getters
        /**
         * gets the width of this image.
         */
        public float getWidth(){
            return this.width_height.x;
        }
        /**
         * gets the height of this image.
         */
        public float getHeight(){
            return this.width_height.y;
        }
        /**
         * get x coordinate of this image.
         */
        public float getX(){
            return this.position.x;
        }
        /**
         * get y coordinate of this image.
         */
        public float getY(){
            return this.position.y;
        }

        /**
         * get the x and y coordinates of this image.
         */
        public Vector2 getPosition(){
            return this.position;
        }
        //end of getters

        //draw method
        public void draw(Batch batch){
            this.ninePatchDrawable.draw(batch, this.position.x, this.position.y, this.width_height.x, this.width_height.y);
        }

    }

    /**
     * enum telling which side the building is facing.
     */
    public enum SIDEFACING{
        DOWN, UP, RIGHT, LEFT
    }

}
