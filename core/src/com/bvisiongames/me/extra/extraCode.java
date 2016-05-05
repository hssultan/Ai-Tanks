package com.bvisiongames.me.extra;

/**
 * Created by ahzji_000 on 11/6/2015.
 */
public class extraCode {
    /*
        ScrollPane addedScroll;
        Image tank1, tank2, tank3, tank4, tank5, tank6;
        private void setupAddGui(TextButton.TextButtonStyle buttonStyle){
            //add the table
            addTankTable = new Table();
            bgPanel.add(addTankTable)
                    .width(bgPanel.getWidth())
                    .padBottom(paddingBottom)
                    .padTop(paddingTop);

            //add the tanks images
            tank1 = new Image(Assets.MenuFileNames.getTanksSkin().getRegion("KV2"));
            tank1.getColor().a = 1;
            tank1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    resetTanksAlpha();
                    otherTanksChosen = "KV2";
                    Tween.to(tank1, ProgressBarAnimation.ALPHA, 0.3f)
                            .target(1)
                            .start(tweenManager);
                }
            });
            addTankTable.add(tank1)
                    .padLeft(paddingLeft);

            tank2 = new Image(Assets.MenuFileNames.getTanksSkin().getRegion("PZKPFWIVG"));
            tank2.getColor().a = 0.5f;
            tank2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    resetTanksAlpha();
                    otherTanksChosen = "PZKPFWIVG";
                    Tween.to(tank2, ProgressBarAnimation.ALPHA, 0.3f)
                            .target(1)
                            .start(tweenManager);
                }
            });
            addTankTable.add(tank2)
                    .padLeft(paddingLeft);

            tank3 = new Image(Assets.MenuFileNames.getTanksSkin().getRegion("VK3601H"));
            tank3.getColor().a = 0.5f;
            tank3.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    resetTanksAlpha();
                    otherTanksChosen = "VK3601H";
                    Tween.to(tank3, ProgressBarAnimation.ALPHA, 0.3f)
                            .target(1)
                            .start(tweenManager);
                }
            });
            addTankTable.add(tank3)
                    .padLeft(paddingLeft);

            tank4 = new Image(Assets.MenuFileNames.getTanksSkin().getRegion("PZKPFWIV"));
            tank4.getColor().a = 0.5f;
            tank4.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    resetTanksAlpha();
                    otherTanksChosen = "PZKPFWIV";
                    Tween.to(tank4, ProgressBarAnimation.ALPHA, 0.3f)
                            .target(1)
                            .start(tweenManager);
                }
            });
            addTankTable.add(tank4)
                    .padLeft(paddingLeft);

            tank5 = new Image(Assets.MenuFileNames.getTanksSkin().getRegion("E100"));
            tank5.getColor().a = 0.5f;
            tank5.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    resetTanksAlpha();
                    otherTanksChosen = "E100";
                    Tween.to(tank5, ProgressBarAnimation.ALPHA, 0.3f)
                            .target(1)
                            .start(tweenManager);
                }
            });
            addTankTable.add(tank5)
                    .padLeft(paddingLeft);

            tank6 = new Image(Assets.MenuFileNames.getTanksSkin().getRegion("M6"));
            tank6.getColor().a = 0.5f;
            tank6.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    resetTanksAlpha();
                    otherTanksChosen = "M6";
                    Tween.to(tank6, ProgressBarAnimation.ALPHA, 0.3f)
                            .target(1)
                            .start(tweenManager);
                }
            });
            addTankTable.add(tank6)
                    .padLeft(paddingLeft);

            addTankTable.row(); //-----------------------------------------> new line

            Table addTableSettings = new Table();
            addTankTable.add(addTableSettings).colspan(6);
            //add the difficulty a tank can have
            Label.LabelStyle difficultyLabelStyle = new Label.LabelStyle();
            difficultyLabelStyle.font = roomTotalFont;
            Label difficultyLabel = new Label("Difficulty: ", difficultyLabelStyle);
            addTableSettings.add(difficultyLabel).align(Align.left).padLeft(paddingLeft);
            //add the display of the chosen difficulty
            Label.LabelStyle chosenDifficultyStyle = new Label.LabelStyle();
            chosenDifficultyStyle.font = roomTotalFont;
            final Label chosenDifficulty = new Label("Easy", chosenDifficultyStyle);
            //add the slider
            Slider.SliderStyle difficultySliderStyle = new Slider.SliderStyle();
            difficultySliderStyle.knob = Assets.MenuFileNames.getMenuSkin().getDrawable("circleup");
            difficultySliderStyle.disabledKnob = Assets.MenuFileNames.getMenuSkin().getDrawable("circledown");
            difficultySliderStyle.background = Assets.MenuFileNames.getMenuSkin().getDrawable("slider");
            final Slider difficultySlider = new Slider(0, 2, 1, false, difficultySliderStyle);
            difficultySlider.addListener(new ClickListener() {
                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    if (difficultySlider.getValue() == 0) {
                        chosenDifficulty.setText("Easy");
                    } else if (difficultySlider.getValue() == 1) {
                        chosenDifficulty.setText("Medium");
                    } else if (difficultySlider.getValue() == 2) {
                        chosenDifficulty.setText("Hard");
                    }
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (difficultySlider.getValue() == 0) {
                        chosenDifficulty.setText("Easy");
                    } else if (difficultySlider.getValue() == 1) {
                        chosenDifficulty.setText("Medium");
                    } else if (difficultySlider.getValue() == 2) {
                        chosenDifficulty.setText("Hard");
                    }
                    return true;
                }
            });
            addTableSettings.add(difficultySlider)
                    .width(300)
                    .padLeft(paddingLeft);
            addTableSettings.add(chosenDifficulty)
                            .padLeft(paddingLeft)
                            .width(100);

            //add the add button
            addTankTable.row();

            //add here the add button
            //add the add button for adding cpu tanks
            final TextButton add = new TextButton("Add", buttonStyle);
            addTankTable.add(add)
                    .align(Align.right).colspan(6);

            //add the added tanks to the game
            final Table scrollTable = new Table();
            addedScroll = new ScrollPane(scrollTable);
            bgPanel.row();
            bgPanel.add(addedScroll).width(bgPanel.getWidth()).height(150).colspan(6).fill().expand();
            bgPanel.row();

            add.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    JSONArray holder = new JSONArray(Tanks.gameState.getPlayers());

                    if (!add.isDisabled()) {

                        addTank(scrollTable, add, holder, otherTanksChosen, (int) difficultySlider.getValue());

                    }

                    holder = new JSONArray(Tanks.gameState.getPlayers());
                    if(holder.length() > 0){
                        create.setDisabled(false);
                    }
                    if(holder.length() >= 5){
                        add.setDisabled(true);
                    }

                    TotalPlayers.setText("Total Players: "+holder.length());

                }
            });

            performClick(add);

        }
        private void resetTanksAlpha(){
            tank1.getColor().a = 0.5f;
            tank2.getColor().a = 0.5f;
            tank3.getColor().a = 0.5f;
            tank4.getColor().a = 0.5f;
            tank5.getColor().a = 0.5f;
            tank6.getColor().a = 0.5f;
        }
        private void addTank(Table scroller, final TextButton add, JSONArray holder, final String type, final int difficulty){

            //save the converted difficulty
            final String cdifficulty;

            JSONObject obj = new JSONObject();
            //properties of the tank
            final int id = holder.length(); //id of tank
            obj.put("id", id);                  //id
            obj.put("type",type);               //type
            //convert the difficulty int to tankModetype
            switch (difficulty){
                case 0:
                    obj.put("difficulty", TankMode.TankModeType.EASY_MODE.toString());  //difficulty: 0->2; 0:easy, 1: medium, 2: hard
                    cdifficulty = TankMode.TankModeType.EASY_MODE.toString();
                    break;
                case 1:
                    obj.put("difficulty", TankMode.TankModeType.MEDIUM_MODE.toString());  //difficulty: 0->2; 0:easy, 1: medium, 2: hard
                    cdifficulty = TankMode.TankModeType.MEDIUM_MODE.toString();
                    break;
                case 2:
                    obj.put("difficulty", TankMode.TankModeType.HARD_MODE.toString());  //difficulty: 0->2; 0:easy, 1: medium, 2: hard
                    cdifficulty = TankMode.TankModeType.HARD_MODE.toString();
                    break;
                default:
                    obj.put("difficulty", TankMode.TankModeType.EASY_MODE.toString());  //difficulty: 0->2; 0:easy, 1: medium, 2: hard
                    cdifficulty = TankMode.TankModeType.EASY_MODE.toString();
                    break;
            }
            obj.put("status",true);             //status: alive->true, dead->false
            obj.put("score",0);                 //score: start with 0

            //save them to the holder and then add them to the prefered file
            holder.put(obj);
            Tanks.gameState.setPlayers(holder.toString());

            //then add the ui
            final Table object = new Table();
            object.setBackground(new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("paneloption"), 5, 5, 5, 5)));
            Image img = new Image(Assets.MenuFileNames.getTanksSkin().getRegion(type.toString()));
            object.add(img);
            TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
            buttonStyle.font = panelOptionFontBtn;
            buttonStyle.up = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("redup"), 5,5, 5, 5));
            buttonStyle.up.setLeftWidth(50);
            buttonStyle.up.setRightWidth(50);
            buttonStyle.down = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("reddown"), 5,5,5, 5));
            buttonStyle.down.setLeftWidth(50);
            buttonStyle.down.setRightWidth(50);
            TextButton close = new TextButton("delete", buttonStyle);
            close.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    object.reset();
                    object.remove();
                    JSONArray hold = new JSONArray(Tanks.gameState.getPlayers());
                    for(int i = 0; i < hold.length(); i++){
                        JSONObject ob = new JSONObject(hold.get(i).toString());
                        if(ob.getString("type").equals(type) && ob.get("difficulty").equals(cdifficulty)
                                && ob.getInt("id") == id){
                            hold.remove(i);
                        }
                    }
                    Tanks.gameState.setPlayers(hold.toString());
                    //if there are no added other tanks then disable the create button
                    if(hold.length() > 0){
                        create.setDisabled(false);
                    }else{
                        create.setDisabled(true);
                    }
                    if(hold.length() < 5){
                        add.setDisabled(false);
                    }
                    TotalPlayers.setText("Total Players: "+hold.length());
                }
            });
            object.add(close).padLeft(paddingLeft);

            //add the difficulty of the tank
            Label.LabelStyle difficultyLabelStyle = new Label.LabelStyle();
            difficultyLabelStyle.font = roomTotalFont;
            Label difficultyLabel = new Label("Easy", difficultyLabelStyle);
            if(difficulty == 0){
                difficultyLabel.setText("Easy");
            }else if(difficulty == 1){
                difficultyLabel.setText("Medium");
            }else if(difficulty == 2){
                difficultyLabel.setText("Hard");
            }

            object.row();
            object.add(difficultyLabel).colspan(2);
            scroller.add(object).colspan(6).align(Align.left);
            scroller.layout();
            addedScroll.layout();
            addedScroll.setScrollY(scroller.getHeight());

        }
*/

    /*
    //below are methods to manually checks for collisions of these bullets
    //these methods below are for collision------------->
    //this method is to detect if a bullet collided with a base building
    public boolean isCollideWithBase(BaseBuilding baseBuilding){

        if(polygonPolylineOverlaps(new Polygon(getVertices(body.getPosition(), beginAngle, bullet)),
                new Polyline(getVertices(baseBuilding.body.getPosition(), 0, baseBuilding.baseSprite)))){
            return true;
        }

        return false;
    }
    public boolean isCollideWithWalls(Vector2[] mapCorners){

        if(polygonPolyLineVoverlaps(new Polygon(getVertices(body.getPosition(), beginAngle, bullet)), mapCorners)){
            return true;
        }

        return false;
    }

    //this function checks if a polygon intersect with a circle
    public boolean circlPolyOverlaps(Polygon polygon, Circle circle) {
        float []vertices=polygon.getTransformedVertices();
        Vector2 center=new Vector2(circle.x, circle.y);
        float squareRadius=circle.radius*circle.radius;
        for (int i=0;i<vertices.length;i+=2){
            if (i==0){
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
                    return true;
            } else {
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[i-2], vertices[i-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
                    return true;
            }
        }
        return false;
    }
    //this function checks if a polygon intersect with a circle
    public boolean circlPolyLineOverlaps(Polyline polyline, Circle circle) {
        float []vertices=polyline.getTransformedVertices();
        Vector2 center=new Vector2(circle.x, circle.y);
        float squareRadius=circle.radius*circle.radius;
        for (int i=0;i<vertices.length;i+=4){
            if(Intersector.intersectSegmentCircle(
                    new Vector2(vertices[i], vertices[i + 1]),
                    new Vector2(vertices[i + 2], vertices[i + 3]), center, squareRadius)){
                return true;
            }
        }
        return false;
    }
    //this function checks wether a polyline intersect
    public boolean polygonPolylineOverlaps(Polygon polygon, Polyline polyline){

        float [] vertices = polyline.getTransformedVertices();
        for(int i = 0; i < vertices.length; i+=4){
            if(Intersector.intersectSegmentPolygon(new Vector2(vertices[i], vertices[i + 1]),
                    new Vector2(vertices[i + 2], vertices[i + 3]), polygon)){
                return true;
            }
        }

        return false;
    }
    //this method checks if a polygon overlaps vertices given in vector2d array
    public boolean polygonPolyLineVoverlaps(Polygon polygon, Vector2[] vectors){

        for(int i = 0; i < vectors.length - 1; i++){
            if(Intersector.intersectSegmentPolygon( vectors[i],
                    vectors[i+1], polygon)){
                return true;
            }
        }
        return false;
    }
    //this method returns the positions in floats of vertices in dimension of box2d
    private float[] getVertices(Vector2 pos, float angle, Sprite sprite){
        float rod = (float) ((sprite.getHeight() / (2* ConstantVariables.PIXELS_TO_METERS)) / Math.sin(Math.atan(sprite.getHeight() / sprite.getWidth())));

        float[] vertices = new float[]{

                rod * (float) Math.cos(angle + ((float)Math.atan(sprite.getHeight() / sprite.getWidth()))) + pos.x
                ,rod * (float) Math.sin(angle + ((float)Math.atan(sprite.getHeight() / sprite.getWidth()))) + pos.y

                ,rod * (float) Math.cos(angle - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + pos.x
                ,rod * (float) Math.sin(angle - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + pos.y

                ,rod * (float) Math.cos(angle + ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + pos.x
                ,rod * (float) Math.sin(angle + ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + pos.y

                ,rod * (float) Math.cos(angle - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + 2 * Math.PI) + pos.x
                ,rod * (float) Math.sin(angle - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + 2 * Math.PI) + pos.y

        };


        return vertices;
    }
    //end of methods for collision---------------------->
    */
    /*

    package com.bvisiongames.me.protocols;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.bvisiongames.me.buildings.Buildings;
import com.bvisiongames.me.entity.EntitManager;
import com.bvisiongames.me.entity.SmallBox;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.Weapons.Weapon;
import com.bvisiongames.me.Weapons.WeaponManger;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.TankMode;
import java.util.ArrayList;
import java.util.List;

    public class TankArtificialInt {

        //class properties
        public Brain brain;
        private boolean pause = false,
                freeze = false; //freeze is for pausing the intelli and is not affected by the pause and resume methods (override methods)

        public Vector2 targetPoint, position, destinationPoint;

        //instances that contains methods to be used
        Brain.Obstacle obstacle;
        public Brain.Move move;
        Brain.BehaviorListener behavior;
        Brain.Enemy enemy;

        //properties for targeting and taking down enemies
        private Body body;
        public Tank tank;
        //level of this tank
        public TankMode.TankModeType tankModeType;

        //properties for moving around an obstacle
        List<Body> otherObject = new ArrayList<Body>();

        //properties of targeted tank bodies
        List<Tank> otherTargetTanks = new ArrayList<Tank>();

        //sensors radiuses
        public float obstacleSensoreRadius = 20f,
                objectSensorRadius = 100f;

        //tank difficulty properties
        float range = 0;
        Vector2 tankSteering = new Vector2(0, 0);

        //this variable synchronizes this thread's methods with the main thread.
        private boolean synced = false;

        public TankArtificialInt(Body body, TankMode.TankModeType tankDifficulty){

            //set the class properties like body of the tank and it's class to be controlled
            this.body = body;
            this.tank = (Tank)body.getUserData();
            this.tankModeType = tankDifficulty;

            //start the AI below
            brain = new Brain();
            brain.start();

            //add the base building to initiate the exiting of the tank from its starting base
            //brain.setObstacleObject(tank.getTankBase().body, 0);

            switch (tankDifficulty){
                case EASY_MODE:
                    range = objectSensorRadius/2;
                    tankSteering = new Vector2(EntitManager.controllers.joystick.GetMaxVectorDimensions().x*TankMode.getTankRatioMovementX(tankDifficulty),
                            EntitManager.controllers.joystick.GetMaxVectorDimensions().y*TankMode.getTankRatioMovementY(tankDifficulty));
                    break;
                case MEDIUM_MODE:
                    range = objectSensorRadius/1.3f;
                    tankSteering = new Vector2(EntitManager.controllers.joystick.GetMaxVectorDimensions().x*TankMode.getTankRatioMovementX(tankDifficulty),
                            EntitManager.controllers.joystick.GetMaxVectorDimensions().y*TankMode.getTankRatioMovementY(tankDifficulty));
                    break;
                case HARD_MODE:
                    range = objectSensorRadius*1.2f;
                    tankSteering = new Vector2(EntitManager.controllers.joystick.GetMaxVectorDimensions().x*TankMode.getTankRatioMovementX(tankDifficulty),
                            EntitManager.controllers.joystick.GetMaxVectorDimensions().y*TankMode.getTankRatioMovementY(tankDifficulty));
                    break;
                default:
                    range = objectSensorRadius/2;
                    tankSteering = new Vector2(EntitManager.controllers.joystick.GetMaxVectorDimensions().x*TankMode.getTankRatioMovementX(tankDifficulty),
                            EntitManager.controllers.joystick.GetMaxVectorDimensions().y*TankMode.getTankRatioMovementY(tankDifficulty));
                    break;
            }

        }

        //lifecycle methods---------->
        //methods that controls the lifecycle of this thread
        *
         * shutdowns this thread.

        public void shutDown(){

            brain.shutDown();

        }
        *
         * pauses this thread by skipping methods.

        public void Pause(){
            pause = true;
        }
        *
         * resume this thread by allowing methods to resume.

        public void Resume(){
            pause = false;
        }
        //end of the controls for the lifecycle of this thread

        //freezing methods
        *
         * pauses this thread but is not affected by the lifecycle of the app.

        public void freeze(){
            this.freeze = true;
        }
        *
         * resumes this thread but is not affected by the lifecycle of the app.

        public void defreeze(){
            this.freeze = false;
        }
        *
         * checks whether it has been frozen.

        public boolean isFrozen(){
            return this.freeze;
        }
        //end of freezing methods
        //end of lifecycle methods--------------------->

        //setters
        *
         * this method sync this thread with the main thread.

        public void sync(){
            this.synced = true;
        }
        *
         * this method desync this thread with the main thread.
         * it disables the methods from firing until this thread syncs with the update on the main thread.

        public void desync(){
            this.synced = false;
        }
        //end of setter


        //getters
        *
         * this method tells whether the main thread has completed one cycle.

        public boolean hasSynced(){
            return this.synced;
        }
        //end of getters

        *
         * this class is the brain running sub methods for the intelli

        private class Brain extends Thread implements Runnable{

            //class properties
            private boolean status = false;
            private boolean starting = false;

            //joystick vector direction
            Vector2 joystickVector = new Vector2(0, 0);

            //patrol properties
            private Vector2 currentPatrolPoint;
            private int currentPatrolIndex = 0;

            //weapon manager
            WeaponManger weaponManager;

            public Brain(){

                //weapon manager and the update method is called in the behavior class
                //and set up the bullet type in the enemy class
                weaponManager = new WeaponManger();

                //set the classes for the methods
                obstacle = new Obstacle();
                move = new Move();
                behavior = new BehaviorListener();
                enemy = new Enemy();

                status = true;
                //check if there is a base setup
                if(tank.getTankBase() != null){
                    starting = true;
                }else{
                    starting = false;
                }

            }
            //function that returns a normal vector
            private synchronized List<Body> getObstacleObject(){
                return otherObject;
            }
            //function that returns target tank
            private synchronized List<Tank> getTargetTank(){
                return otherTargetTanks;
            }
            private synchronized void setTargetTank(Tank tank, int index){
                if(tank == null){
                    getTargetTank().remove(index);
                }else{
                    if(!getTargetTank().contains(tank)){
                        getTargetTank().add(tank);
                    }
                }
            }
            //this sets the target tanks list
            //small variables in the method below
            private int addNewListI = 0;
            private void addNewList(List<Tank> tanks){

                for(addNewListI = 0; addNewListI < tanks.size(); addNewListI++){
                    setTargetTank(tanks.get(addNewListI), 0);
                }

            }
            //function that sets obstacleNormal
            private synchronized void setObstacleObject(Body body, int index){
                if(body != null){
                    if(!getObstacleObject().contains(body))
                        getObstacleObject().add(body);
                }else{
                    getObstacleObject().remove(index);
                }
            }

            @Override
            public void run() {

                while (status){

                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //if paused then don't apply states
                    if(!isFrozen()){
                        //check if there is an obstacle first near the body
                        if(getObstacleObject().size() > 0){

                            MoveAroundObstacle(); //<-------------------------------------------XXXX

                        }else if(getTargetTank().size() > 0){
                            //then check if there is a target to pursue

                            MoveTowardEnemy();  //<-------------------------------------------XXXX

                        }else if(!starting){
                            //else if there are no tanks then move around the map

                            PatrolAround(); //<-------------------------------------------XXXX

                        }

                        position = tank.getPosition().cpy();

                        //listen for any misbehavior
                        behavior.Listen();  //<-------------------------------------------

                        //methods below are for syncing with the main thread
                        //update the weapon manager instance when the main thread has synced with this thread
                        if(hasSynced()) {
                            //update the weapon manager
                            weaponManager.update();
                            //update the effects cool down here so it is synced with the main thread.
                            tank.syncShockTankInPlace();
                            desync();
                        }

                    }


                    //update the joystick of the body
                    if (status){
                        tank.update(joystickVector, EntitManager.controllers.joystick.GetMaxVectorDimensions());
                    }

                }

            }
            //methods for the control of the body
            private void MoveAroundObstacle(){

                //this makes sure the tank exits the base building
                if(getObstacleObject().get(0).getUserData() instanceof Buildings) {

                    //if the building type is BaseBuilding
                    if (getObstacleObject().get(0).getUserData() instanceof Buildings) {

                        if (obstacle.checkIfInside()) {
                            //if the tank is inside a base
                            obstacle.exitBase();

                        } else if (
                                obstacle.checkIfHitting()) {
                            //if there is only a touch
                            obstacle.avoidBaseBuilding();

                        } else {
                            //once it exits or it is outside near the base try to get away from it
                            //a function inside the obstacle class should be able to calculate and avoid it
                            obstacle.resetTankProperties();

                        }

                        //else reset and let the tank do other things
                    } else {
                        obstacle.resetTankProperties();
                    }

                    //else if it is a small box
                }else if(getObstacleObject().get(0).getUserData() instanceof SmallBox){

                    obstacle.resetTankProperties();

                }else{
                    //once it exits or it is outside near the base try to get away from it
                    obstacle.resetTankProperties();
                }

            }
            private void MoveTowardEnemy(){

                enemy.GetNearTank();
                if(enemy.isCloseEnoughToShoot()  && !pause){
                    enemy.aimToShoot();
                }

            }
            //small variables for the patrol around method
            private int patrolAroundI = 0;
            private void PatrolAround(){

                if(currentPatrolPoint == null){

                    if(EntitManager.mapManager != null &&
                            EntitManager.mapManager.getLevel() != null &&
                            EntitManager.mapManager.getLevel().getPatrolPoints() != null){
                        currentPatrolPoint = EntitManager.mapManager.getLevel().getPatrolPoints()[currentPatrolIndex];
                        for(patrolAroundI = 0;
                            patrolAroundI < EntitManager.mapManager.getLevel().getPatrolPoints().length; patrolAroundI++){
                            if( currentPatrolPoint.dst(body.getPosition().x, body.getPosition().y)
                                    > EntitManager.mapManager.getLevel().getPatrolPoints()[patrolAroundI].dst(body.getPosition().x, body.getPosition().y)
                                    ){
                                currentPatrolPoint = EntitManager.mapManager.getLevel().getPatrolPoints()[patrolAroundI];
                                currentPatrolIndex = patrolAroundI;
                            }
                        }
                    }

                }else{

                    if(move.isAtPoint()){
                        if(currentPatrolIndex == EntitManager.mapManager.getLevel().getPatrolPoints().length - 1){
                            currentPatrolIndex = 0;
                            currentPatrolPoint = EntitManager.mapManager.getLevel().getPatrolPoints()[currentPatrolIndex];
                            move.MoveToPoint(currentPatrolPoint);
                        }else{
                            currentPatrolIndex++;
                            currentPatrolPoint = EntitManager.mapManager.getLevel().getPatrolPoints()[currentPatrolIndex];
                            move.MoveToPoint(currentPatrolPoint);
                        }
                    }else{
                        move.MoveToPoint(currentPatrolPoint);
                    }

                }


                if(currentPatrolPoint != null){
                    //set the destination point
                    destinationPoint = currentPatrolPoint.cpy();
                }

            }

            *
             * shutdown this thread.

            public void shutDown(){

                status = false;

            }

            //this class takes care of the methods for the move around obstacle case
            public class Obstacle{

                //methods for sensors and check methods
                //this checks if it is inside the BaseBuilding
                //small variables for the checkIfInside method
                private float[] checkIfInsideBaseVertices;
                private Polygon checkIfInsidePolygon;
                public boolean checkIfInside(){
                    checkIfInsideBaseVertices = ((Buildings)getObstacleObject().get(0).getUserData()).corners;

                    checkIfInsidePolygon = new Polygon(checkIfInsideBaseVertices);
                    if(checkIfInsidePolygon.contains(tank.corners[0].x, tank.corners[0].y) //top right
                            ){
                        return true;
                    }
                    if(checkIfInsidePolygon.contains(tank.corners[1].x, tank.corners[1].y) //bottom right
                            ){
                        return true;
                    }

                    if(checkIfInsidePolygon.contains(tank.corners[2].x, tank.corners[2].y) //bottom left
                            ){
                        return true;
                    }

                    if(checkIfInsidePolygon.contains(tank.corners[3].x, tank.corners[3].y) //bottom left)
                            ){
                        return true;
                    }


                    return false;
                }
                //this method checks if the tank intersects with another building
                //small variables for the checkIfHittingMethod
                private Buildings checkIfHittingBuilding;
                private int checkIfHittingI = 0;
                public boolean checkIfHitting(){

                    checkIfHittingBuilding = ((Buildings) getObstacleObject().get(0).getUserData());

                    for(checkIfHittingI = 0; checkIfHittingI < checkIfHittingBuilding.Vcorners.length - 1; checkIfHittingI+=2){
                        if(Intersector.intersectSegmentCircle(
                                checkIfHittingBuilding.Vcorners[checkIfHittingI],
                                checkIfHittingBuilding.Vcorners[checkIfHittingI+1],
                                body.getPosition(), obstacleSensoreRadius*obstacleSensoreRadius)){
                            return true;
                        }

                    old but still not sure
                    if(new Circle(body.getPosition(), obstacleSensoreRadius).contains(building.corners[i], building.corners[i + 1])){
                        return true;
                    }

                    }

                    return false;
                }

                //methods for actions
                public void exitBase(){
                    //do some calculations to exit the base based on the angle of the tank and the base
                    //calibrate the x component of the joystick vector
                    joystickVector.set(
                            (EntitManager.controllers.joystick.GetMaxVectorDimensions().y / 3) *
                                    (float) Math.cos(Math.PI / 2 - (Math.PI + (getObstacleObject().get(0).getAngle() - body.getAngle()))),
                            (EntitManager.controllers.joystick.GetMaxVectorDimensions().y / 3) *
                                    (float) Math.sin(Math.PI / 2 - (Math.PI + (getObstacleObject().get(0).getAngle() - body.getAngle())))
                    );

                }
                public void resetTankProperties(){
                    joystickVector.set(0, 0);
                    starting = false;
                    setObstacleObject(null, 0);
                }
                //small variables for the avoidBuilding method
                private Buildings avoidBuildingBuilding;
                private Vector2 AvoidBaseBuildingOverAllVector;
                private int avoidBaseBuildingI;
                private float avoidBaseBuildingAverageDist;
                private Vector2 avoidBaseBuildingTmp;
                public void avoidBaseBuilding(){

                    avoidBuildingBuilding = ((Buildings) getObstacleObject().get(0).getUserData());

                    AvoidBaseBuildingOverAllVector = new Vector2(0, 0);
                    avoidBaseBuildingAverageDist = obstacleSensoreRadius;

                    //then loop through all its side and find the side that it intersect with
                    for( avoidBaseBuildingI = 0; avoidBaseBuildingI < avoidBuildingBuilding.Vcorners.length - 1; avoidBaseBuildingI+=2){
                        if(Intersector.intersectSegmentCircle(
                                avoidBuildingBuilding.Vcorners[avoidBaseBuildingI],
                                avoidBuildingBuilding.Vcorners[avoidBaseBuildingI + 1],
                                body.getPosition(), obstacleSensoreRadius * obstacleSensoreRadius)){
                            avoidBaseBuildingTmp = new Vector2(0, 0);
                            Intersector.nearestSegmentPoint(
                                    avoidBuildingBuilding.Vcorners[avoidBaseBuildingI],
                                    avoidBuildingBuilding.Vcorners[avoidBaseBuildingI + 1],
                                    body.getPosition(), avoidBaseBuildingTmp);
                            avoidBaseBuildingTmp.set(body.getPosition().x - avoidBaseBuildingTmp.x, body.getPosition().y - avoidBaseBuildingTmp.y);
                            avoidBaseBuildingAverageDist = (avoidBaseBuildingAverageDist + avoidBaseBuildingTmp.len())/2;
                            AvoidBaseBuildingOverAllVector.set(avoidBaseBuildingTmp.x + AvoidBaseBuildingOverAllVector.x, avoidBaseBuildingTmp.y + AvoidBaseBuildingOverAllVector.y);
                        }
                    }

                    if(Intersector.intersectSegmentCircle(
                            avoidBuildingBuilding.Vcorners[0],
                            avoidBuildingBuilding.Vcorners[avoidBuildingBuilding.Vcorners.length-1],
                            body.getPosition(), obstacleSensoreRadius * obstacleSensoreRadius)){
                        avoidBaseBuildingTmp = new Vector2(0, 0);
                        Intersector.nearestSegmentPoint(
                                avoidBuildingBuilding.Vcorners[0],
                                avoidBuildingBuilding.Vcorners[avoidBuildingBuilding.Vcorners.length-1],
                                body.getPosition(), avoidBaseBuildingTmp);
                        avoidBaseBuildingTmp.set(body.getPosition().x - avoidBaseBuildingTmp.x, body.getPosition().y - avoidBaseBuildingTmp.y);
                        avoidBaseBuildingAverageDist = (avoidBaseBuildingAverageDist + avoidBaseBuildingTmp.len())/2;
                        AvoidBaseBuildingOverAllVector.set(avoidBaseBuildingTmp.x + AvoidBaseBuildingOverAllVector.x, avoidBaseBuildingTmp.y + AvoidBaseBuildingOverAllVector.y);
                    }

                    calculateObsPoint(AvoidBaseBuildingOverAllVector, avoidBaseBuildingAverageDist);


                }
                //method below calculates the point in which the body should be heading in
                private void calculateObsPoint(Vector2 arrow, float averageDist){

                    //calculate the vector from this tank to the destination point
                    Vector2 destinationVector = new Vector2(destinationPoint.x - body.getPosition().x,
                            destinationPoint.y - body.getPosition().y),
                            P180Vector = arrow.cpy(), N180Vector = arrow.cpy();
                    P180Vector.setAngle(P180Vector.angle() + 90);
                    N180Vector.setAngle(N180Vector.angle() - 90);

                    if(getAngleVectors(P180Vector, destinationVector) < getAngleVectors(N180Vector, destinationVector)){
                        arrow = P180Vector.cpy();
                    }else{
                        arrow = N180Vector.cpy();
                    }

                    arrow.x += body.getPosition().x;
                    arrow.y += body.getPosition().y;

                    targetPoint = arrow.cpy();

                    move.MoveToPointAway(arrow, averageDist, tank.BodyDimensions.y + 2, obstacleSensoreRadius);

                }

                //method to calculate things
                //get radius of rectangular object by making half diagonal the radius
                public float getRectRadius(float width, float height){
                    return (float)Math.sqrt( Math.pow(width,2) + Math.pow(height,2) )/2;
                }
                //this method returns the positions of vertices in pixels
                //this vertice returns the positions for the tank body
                private Vector2[] getVertices(Body body, Sprite sprite){

                    Vector2[] vertices = new Vector2[]{
                            new Vector2(0, 0), new Vector2(0, 0), //top right, bottom right
                            new Vector2(0, 0), new Vector2(0, 0)  //bottom left, top left
                    };
                    float rod = (float) ((sprite.getHeight() / 2) / Math.sin(Math.atan(sprite.getHeight() / sprite.getWidth())));

                    vertices[0].x = rod * (float) Math.cos(body.getAngle() + ((float)Math.atan(sprite.getHeight() / sprite.getWidth()))) + body.getPosition().x*MultiGameScreen.PIXELS_TO_METERS;
                    vertices[0].y = rod * (float) Math.sin(body.getAngle() + ((float)Math.atan(sprite.getHeight() / sprite.getWidth()))) + body.getPosition().y*MultiGameScreen.PIXELS_TO_METERS;

                    vertices[3].x = rod * (float) Math.cos(body.getAngle() - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + body.getPosition().x*MultiGameScreen.PIXELS_TO_METERS;
                    vertices[3].y = rod * (float) Math.sin(body.getAngle() - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + body.getPosition().y*MultiGameScreen.PIXELS_TO_METERS;

                    vertices[2].x = rod * (float) Math.cos(body.getAngle() + ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + body.getPosition().x*MultiGameScreen.PIXELS_TO_METERS;
                    vertices[2].y = rod * (float) Math.sin(body.getAngle() + ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + body.getPosition().y*MultiGameScreen.PIXELS_TO_METERS;

                    vertices[1].x = rod * (float) Math.cos(body.getAngle() - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + 2 * Math.PI) + body.getPosition().x*MultiGameScreen.PIXELS_TO_METERS;
                    vertices[1].y = rod * (float) Math.sin(body.getAngle() - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + 2 * Math.PI) + body.getPosition().y*MultiGameScreen.PIXELS_TO_METERS;

                    return vertices;
                }
                //this returns the positions for the baseBuildings in pixels too
                private float[] getVerticesF(Body body, Sprite sprite){

                    float rod = (float) ((sprite.getHeight() / 2) / Math.sin(Math.atan(sprite.getHeight() / sprite.getWidth())));

                    float[] vertices = new float[]{
                            //top right
                            rod * (float) Math.cos(body.getAngle() + ((float)Math.atan(sprite.getHeight() / sprite.getWidth()))) + body.getPosition().x*MultiGameScreen.PIXELS_TO_METERS + sprite.getWidth()/2,
                            rod * (float) Math.sin(body.getAngle() + ((float)Math.atan(sprite.getHeight() / sprite.getWidth()))) + body.getPosition().y*MultiGameScreen.PIXELS_TO_METERS + sprite.getHeight()/2,
                            //bottom right
                            rod * (float) Math.cos(body.getAngle() - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + 2 * Math.PI) + body.getPosition().x*MultiGameScreen.PIXELS_TO_METERS + sprite.getWidth()/2,
                            rod * (float) Math.sin(body.getAngle() - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + 2 * Math.PI) + body.getPosition().y*MultiGameScreen.PIXELS_TO_METERS + sprite.getHeight()/2,
                            //bottom left
                            rod * (float) Math.cos(body.getAngle() + ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + body.getPosition().x*MultiGameScreen.PIXELS_TO_METERS + sprite.getWidth()/2,
                            rod * (float) Math.sin(body.getAngle() + ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + body.getPosition().y*MultiGameScreen.PIXELS_TO_METERS + sprite.getHeight()/2,
                            //top left
                            rod * (float) Math.cos(body.getAngle() - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + body.getPosition().x*MultiGameScreen.PIXELS_TO_METERS + sprite.getWidth()/2,
                            rod * (float) Math.sin(body.getAngle() - ((float)Math.atan(sprite.getHeight() / sprite.getWidth())) + Math.PI) + body.getPosition().y*MultiGameScreen.PIXELS_TO_METERS + sprite.getHeight()/2
                    };

                    return vertices;
                }
                //this function calculates the angle degree between two vectors
                private float getAngleVectors(Vector2 vector1, Vector2 vector2){
                    return (float)Math.acos( (vector1.dot(vector2))/(vector1.len()*vector2.len()) )* MathUtils.radiansToDegrees;
                }
            }
            //this class takes care of moving the tank towards a point with out hitting any object
            public class Move{

                private boolean isAtPoint = false;
                public Vector2 targetPoint = new Vector2(0, 0);

                //small variables foe moveToPoint method
                private Vector2 moveToPointDir;
                private float moveToPointDirAngle = 0, moveToPointBodyAngle = 0,
                        moveToPointAngleDiff = 0;
                public void MoveToPoint(Vector2 point){
                    targetPoint = point;
                    //stop the joystick when the tank body is at the point location
                    if( body.getPosition().dst(point) < 17 ){

                        joystickVector.x = 0;
                        joystickVector.y = 0;
                        isAtPoint = true;

                    }else{

                        moveToPointDir = new Vector2(point.x - body.getPosition().x, point.y - body.getPosition().y);
                        moveToPointDirAngle = trimAngle(calculateAngleVec(moveToPointDir));
                        moveToPointBodyAngle = trimAngle((float)(Math.PI/2 + body.getAngle()));
                        moveToPointAngleDiff = trimAngle(moveToPointBodyAngle - moveToPointDirAngle);
                        joystickVector.x = tankSteering.x *
                                (float)Math.sin( moveToPointAngleDiff );

                        if( Math.abs( moveToPointAngleDiff ) > -Math.PI/20 && Math.abs( moveToPointAngleDiff ) < Math.PI/20
                                && moveToPointDir.len() <= obstacleSensoreRadius*2.5f){
                            joystickVector.y = tankSteering.y;
                        }else if(moveToPointDir.len() > obstacleSensoreRadius*2.5f){
                            joystickVector.y = tankSteering.y;
                        }

                        isAtPoint = false;
                    }

                }
                public boolean isAtPoint(){
                    return isAtPoint;
                }
                //this function does not care about turning before driving:
                //takes a point to get to destination, a maxdistance between the two bodies
                //and a the current distance between the bodies
                //small variables for the below method
                private Vector2 moveToPointAwayDir;
                private float moveToPointAwayDirAngle = 0, moveToPointAwayBodyAngle = 0,
                        moveToPointAwayAngleDiff = 0;
                public void MoveToPointAway(Vector2 point, float currentDist, float minDist, float maxDist){

                    targetPoint = point.cpy();

                    //stop the joystick when the tank body is at the point location
                    if(body.getPosition().x > point.x - 1 && body.getPosition().x < point.x + 1
                            &&
                            body.getPosition().y > point.y - 1 && body.getPosition().y < point.y + 1 ){

                        joystickVector.x = 0;
                        joystickVector.y = 0;

                    }else{

                        moveToPointAwayDir = new Vector2(point.x - body.getPosition().x, point.y - body.getPosition().y);
                        moveToPointAwayDirAngle = trimAngle(calculateAngleVec(moveToPointAwayDir));
                        moveToPointAwayBodyAngle = trimAngle((float)(Math.PI/2 + body.getAngle()));
                        moveToPointAwayAngleDiff = trimAngle(moveToPointAwayBodyAngle - moveToPointAwayDirAngle);

                        joystickVector.x = tankSteering.x *
                                (float)Math.sin( moveToPointAwayAngleDiff );

                        joystickVector.y = tankSteering.y*(currentDist/maxDist);

                    }

                }
                //this moves to the point at the precise coordinates without approximation
                //small variables for the below method
                private Vector2 moveToPrecPointDir;
                private float moveToPrecPointDirAngle = 0, moveToPrecPointBodyAngle = 0,
                        moveToPrecPointAngleDiff = 0;
                public void MoveToPrecPoint(Vector2 point){

                    targetPoint = point;

                    //stop the joystick when the tank body is at the point location
                    if( body.getPosition().dst(point) < 2 ){

                        joystickVector.x = 0;
                        joystickVector.y = 0;

                    }else{

                        moveToPrecPointDir = new Vector2(point.x - body.getPosition().x, point.y - body.getPosition().y);
                        moveToPrecPointDirAngle = trimAngle(calculateAngleVec(moveToPrecPointDir));
                        moveToPrecPointBodyAngle = trimAngle((float)(Math.PI/2 + body.getAngle()));
                        moveToPrecPointAngleDiff = trimAngle(moveToPrecPointBodyAngle - moveToPrecPointDirAngle);
                        joystickVector.x = tankSteering.x *
                                (float)Math.sin( moveToPrecPointAngleDiff );
                        if( Math.abs( moveToPrecPointAngleDiff ) > -Math.PI/20 && Math.abs( moveToPrecPointAngleDiff ) < Math.PI/20
                                && moveToPrecPointDir.len() <= obstacleSensoreRadius*2.5f){
                            joystickVector.y = tankSteering.y;
                        }else if(moveToPrecPointDir.len() > obstacleSensoreRadius*2.5f){
                            joystickVector.y = tankSteering.y;
                        }
                    }

                }
                //this move to the point by reversing to it
                //small variables for the below method
                private Vector2 MoveToPointReverseDir;
                private float MoveToPointReverseDirAngle = 0, MoveToPointReverseBodyAngle = 0,
                        MoveToPointReverseAngleDiff = 0;
                public void MoveToPointReverse(Vector2 point){
                    //targetPoint = point;

                    //stop the joystick when the tank body is at the point location
                    if( body.getPosition().dst(point) < 2 ){

                        joystickVector.x = 0;
                        joystickVector.y = 0;

                    }else{

                        MoveToPointReverseDir = new Vector2(point.x - body.getPosition().x, point.y - body.getPosition().y);
                        MoveToPointReverseDirAngle = trimAngle(calculateAngleVec(MoveToPointReverseDir));
                        MoveToPointReverseBodyAngle = trimAngle((float)(Math.PI/2 + body.getAngle()));
                        MoveToPointReverseAngleDiff = trimAngle(MoveToPointReverseBodyAngle - MoveToPointReverseDirAngle);
                        joystickVector.x = -tankSteering.x *
                                (float)Math.sin( MoveToPointReverseAngleDiff );
                        if(Math.abs(MoveToPointReverseAngleDiff) < Math.PI/20 + Math.PI){
                            joystickVector.y = -tankSteering.y;
                        }else{
                            joystickVector.y = 0;
                        }

                    }
                }

            }
            //this class takes care of moving towards the enemy and shooting it down
            public class Enemy{

                //chosen tank
                Vector2 pos;

                public Enemy(){

                    targetPoint = new Vector2(0,0);
                    position = new Vector2(0,0);

                    //setup the bullet type
                    weaponManager.setBulletstype(Weapon.BULLETSTYPE.ELECTRICBULLET);
                    weaponManager.addBullet(Weapon.BULLETSTYPE.BULLET1);
                    weaponManager.addBullet(Weapon.BULLETSTYPE.ELECTRICBULLET);

                }
                //this function gets a point that is close to the enemy
                private Vector2 angularVec;
                public void GetNearTank(){

                    angularVec = new Vector2(body.getPosition().x - pos.x,
                            body.getPosition().y - pos.y);
                    if(angularVec.len() > range){
                        angularVec.setLength(range);
                    }

                    //get the point to move to
                    targetPoint = new Vector2(angularVec.x + pos.x,
                            angularVec.y + pos.y);

                    findCloseGround();

                    move.MoveToPrecPoint(targetPoint);

                }

                //finds a point to shoot the enemy by making sure there are no obstacle in between
                //small variables for the aimToShoot method
                private Vector2 aimToShootDir;
                private float aimToShootDirAngle = 0, aimToShootBodyAngle = 0,
                        aimToShootAngleDiff = 0;
                public void aimToShoot(){

                    //first check if the line segment from the enemy tank to this tank intersect with any static buildings
                    aimToShootDir = new Vector2(pos.x - body.getPosition().x, pos.y - body.getPosition().y);
                    aimToShootDirAngle = trimAngle(calculateAngleVec(aimToShootDir));
                    aimToShootBodyAngle = trimAngle((float)(Math.PI/2 + body.getAngle()));
                    aimToShootAngleDiff = trimAngle(aimToShootBodyAngle - aimToShootDirAngle);

                    joystickVector.x = (EntitManager.controllers.joystick.GetMaxVectorDimensions().x/2) *
                            (float)Math.sin( aimToShootAngleDiff );

                    if( Math.abs(aimToShootAngleDiff) < Math.PI/20 &&
                            weaponManager.isReadyToShoot() ){
                        //make sure the thread is going through the loop so if it got shutdown it does not
                        //shoot a bullet and that bullet does not have an instance of tank that got disposed
                        if(status && !tank.isElectricallyShocked()){
                            weaponManager.shoot(body, tank.tank);
                        }
                    }

                    //check whether the easy mode is out of electric bullets and if it is out then switch to normal bullets
                    if(!weaponManager.electricBullet.hasBullets()){
                        weaponManager.setBulletstype(Weapon.BULLETSTYPE.BULLET1);
                    }

                }
                //get close to the enemy and make sure the point to move to is inside the map and not
                //target toward inside the static base
                //small variables for the findCloseGround method
                private Polygon findCloseGroundPolygon;
                private void findCloseGround(){

                    findCloseGroundPolygon = new Polygon(EntitManager.mapManager.getLevel().getFbounderyPoints());
                    if(!findCloseGroundPolygon.contains(targetPoint.x, targetPoint.y)
                            || isIntersectionObject()) {
                        do {
                            angularVec.setAngle(angularVec.angle() + 5);
                            targetPoint.x = angularVec.x + pos.x;
                            targetPoint.y = angularVec.y + pos.y;
                            findCloseGroundPolygon = new Polygon(EntitManager.mapManager.getLevel().getFbounderyPoints());
                        } while (!findCloseGroundPolygon.contains(targetPoint.x, targetPoint.y)
                                || isIntersectionObject());

                    }
                }

                //this method checks if the target point to move to is inside another object (static) for now it is a base
                //small variables foe the isIntersectionObject
                private int isIntersectionObjectI = 0, isIntersectionObjectS = 0;
                private boolean isIntersectionObject(){

                    targetPoint.x = angularVec.x + pos.x;
                    targetPoint.y = angularVec.y + pos.y;

                    if(MultiGameScreen.entityManager.getBaseBuildingList() != null){
                        for(isIntersectionObjectI = 0; isIntersectionObjectI < MultiGameScreen.entityManager.getBaseBuildingList().length;
                            isIntersectionObjectI++){

                            for (isIntersectionObjectS = 0;
                                 isIntersectionObjectS < MultiGameScreen.entityManager.getBaseBuildingList()[isIntersectionObjectI].Vcorners.length - 1;
                                 isIntersectionObjectS++){

                                if(Intersector.intersectSegments(
                                        MultiGameScreen.entityManager.getBaseBuildingList()[isIntersectionObjectI].Vcorners[isIntersectionObjectS],
                                        MultiGameScreen.entityManager.getBaseBuildingList()[isIntersectionObjectI].Vcorners[isIntersectionObjectS+1],
                                        targetPoint, pos, null)){
                                    return true;
                                }

                            }

                        }
                    }
                    return false;
                }
                //this tells that if we are close enough to start aiming at the enemy
                public boolean isCloseEnoughToShoot(){
                    if( body.getPosition().dst(targetPoint) <= 3){
                        return true;
                    }
                    return false;
                }

            }
            //this class contains the methods that makes sure the tank is behaving properly
            public class BehaviorListener{

                //variables for pushing back
                private boolean isPushingBack = false;
                //variables for the ai booting
                private boolean isStarting = false;

                public BehaviorListener(){

                    //the starting variable disable the stuck method from activating
                    isStarting = true;
                    lastPos = new Vector2(body.getPosition().x,body.getPosition().y);

                }

                public void Listen(){

                    //when the ai boots and the tank has enought time to exit the base then start listening for behavior
                    if(!isStarting){

                        //clear the tanks list
                        getTargetTank().clear();
                        //clear the old position of the tank that this tank is aiming at
                        enemy.pos = null;

                        //listen for other tanks that are near my scope to track down
                        trackCloseTanks();

                        //listen for static objects close like the obstacles
                        trackCloseObstacles();

                        //correct the steering if heading toward a wall of the map
                        trackCloseWall();

                        //find a close a tank to target in enemy class
                        chooseCloseTank();

                        //listen for if the tank is tank in the same place
                        //also make sure that the tank is not chasing other enemy tanks
                        if( (getTargetTank().size() < 1 && !tank.isElectricallyShocked()
                                && !pause && (isPushingBack || isStuck())) ){
                            pushBack();
                        }else if ( (getTargetTank().size() >= 1 && !tank.isElectricallyShocked()
                                && !pause && (isPushingBack || isStuckDuringChase())) ){
                            pushBack();
                        }

                    }

                    //once 3 seconds has passed then enable the listen
                    if(isStarting){
                        if((System.nanoTime() - startTime)/1000000000 > 3){
                            isStarting = false;
                        }
                    }
                }


                //this chooses the closest enemy tank and targets it
                //small variables for the below method
                private int chooseCloseTankI = 0;
                public void chooseCloseTank(){

                    for(chooseCloseTankI = 0;  chooseCloseTankI < getTargetTank().size(); chooseCloseTankI++){

                        if(enemy.pos == null){
                            enemy.pos = new Vector2(getTargetTank().get(chooseCloseTankI).getPosition().x, getTargetTank().get(chooseCloseTankI).getPosition().y);
                            position = enemy.pos.cpy();
                            //set the destination point
                            destinationPoint = enemy.pos.cpy();
                        }
                        //make sure the tank enemy is closer than prev tank and is within bound
                        if( enemy.pos.dst(body.getPosition()) > getTargetTank().get(chooseCloseTankI).getPosition().dst(body.getPosition())){
                            enemy.pos = getTargetTank().get(chooseCloseTankI).getPosition().cpy();
                            position = enemy.pos.cpy();
                            //set the destination point
                            destinationPoint = enemy.pos.cpy();
                        }

                    }

                }

                //this method checks if the tank is stuck
                private long startTime = System.nanoTime();
                private Vector2 lastPos;
                private boolean isStuck(){

                    boolean result = false;

                    if((System.nanoTime() - startTime)/1000000000 > 1) {
                        startTime = System.nanoTime();
                        if (lastPos.dst(body.getPosition()) < 1) {
                            isPushingBack = true;
                            result = true;
                        }
                        if (!isPushingBack) {
                            startTime = System.nanoTime();
                            lastPos = new Vector2(body.getPosition().x, body.getPosition().y);
                        }
                    }

                    return result;
                }
                private boolean isStuckDuringChase(){

                    boolean result = false;

                    if((System.nanoTime() - startTime)/1000000000 > 1) {

                        if ( lastPos.dst(body.getPosition()) < 1
                                && intersectStaticObjects() ) {
                            isPushingBack = true;
                            result = true;
                        }
                        if (!isPushingBack) {
                            startTime = System.nanoTime();
                            lastPos = new Vector2(body.getPosition().x, body.getPosition().y);
                        }

                    }

                    return result;
                }
                //this method checks if an objects intersect with other static bodies
                private boolean intersectStaticObjects(){

                    //check with intersection with static base buildings
                    if(MultiGameScreen.entityManager.getBaseBuildingList() != null){
                        for(int i = 0; i < MultiGameScreen.entityManager.getBaseBuildingList().length; i++){

                            if(circlPolyLineOverlaps(new Polyline(MultiGameScreen.entityManager.getBaseBuildingList()[i].corners),
                                    new Circle(body.getPosition(),
                                            obstacle.getRectRadius(tank.tank.getWidth()/MultiGameScreen.PIXELS_TO_METERS,
                                                    tank.tank.getHeight()/MultiGameScreen.PIXELS_TO_METERS)))){
                                return true;
                            }

                        }
                    }

                    return false;
                }

                //this method pushes the tank back
                private Vector2 dir, //dir is the direction of displacement of the tank body
                //small variables for the method below
                pushBackTargetVec ;
                private void pushBack(){

                    //calculate the direction of displacement of the tank
                    dir = new Vector2(obstacleSensoreRadius*1.5f*(float)Math.round(Math.cos(body.getAngle() + Math.PI / 2)*100)/100,
                            obstacleSensoreRadius*1.5f*(float)Math.round(Math.sin(body.getAngle() + Math.PI / 2)*100)/100);
                    pushBackTargetVec = new Vector2(targetPoint.x - body.getPosition().x, targetPoint.y - body.getPosition().y);

                    //move to a point that is not inside a base or a box
                    if(pushBackTargetVec.y < 0 && pushBackTargetVec.x < dir.x){

                        move.MoveToPointReverse(findFreeZonePoint(new Vector2(dir.x + body.getPosition().x, dir.y + body.getPosition().y), false));

                    }else if(pushBackTargetVec.y < 0 && pushBackTargetVec.x > dir.x){

                        move.MoveToPointReverse(findFreeZonePoint(new Vector2(dir.x + body.getPosition().x, dir.y + body.getPosition().y), false));

                    }else if(pushBackTargetVec.y > 0 && pushBackTargetVec.x > dir.x){

                        move.MoveToPointReverse(findFreeZonePoint(new Vector2(dir.x + body.getPosition().x, dir.y + body.getPosition().y), true));

                    }else if(pushBackTargetVec.y > 0 && pushBackTargetVec.x < dir.x){

                        move.MoveToPointReverse(findFreeZonePoint(new Vector2(dir.x + body.getPosition().x, dir.y + body.getPosition().y), false));

                    }

                    if(lastPos.dst(body.getPosition()) >= obstacleSensoreRadius*1.5f){
                        isPushingBack = false;
                    }

                }

                //this method checks if the index of the vector pertruding from the center of the tank body
                //is inside another static object such as base
                //takes in the point and returns a new one
                //also it takes a boolean that indicates if the angle should incremented or decremented
                private Vector2 findFreeZonePoint(Vector2 v, boolean increment){

                    if(circleVector2dAOverlaps(EntitManager.mapManager.getLevel().getBounderyPoints(), new Circle(v.x, v.y, obstacleSensoreRadius))
                            || !new Polygon(EntitManager.mapManager.getLevel().getFbounderyPoints()).contains(v.x,v.y)
                            || isIntersectionObject(v)) {
                        do {
                            if (increment) {
                                dir.setAngle(dir.angle() + 5);
                            } else {
                                dir.setAngle(dir.angle() - 5);
                            }

                            v.x = dir.x + body.getPosition().x;
                            v.y = dir.y + body.getPosition().y;
                        } while (
                                circleVector2dAOverlaps(EntitManager.mapManager.getLevel().getBounderyPoints(), new Circle(v.x, v.y, obstacleSensoreRadius))
                                        || !new Polygon(EntitManager.mapManager.getLevel().getFbounderyPoints()).contains(v.x,v.y)
                                        || isIntersectionObject(v));

                    }
                    return v;
                }
                //this method checks if the target point to move to is inside another object (static)
                private boolean isIntersectionObject(Vector2 v){

                    if(MultiGameScreen.entityManager.getBaseBuildingList() != null){
                        for(int i = 0; i < MultiGameScreen.entityManager.getBaseBuildingList().length; i++){

                            for(int x = 0; x < MultiGameScreen.entityManager.getBaseBuildingList()[i].Vcorners.length - 1; x+=2){

                                if(Intersector.intersectSegmentCircle(
                                        MultiGameScreen.entityManager.getBaseBuildingList()[i].Vcorners[x],
                                        MultiGameScreen.entityManager.getBaseBuildingList()[i].Vcorners[x+1],
                                        v, obstacleSensoreRadius*obstacleSensoreRadius)){
                                    return true;
                                }

                            }

                        }
                    }
                    return false;
                }
                //this function listens for the presence of other tank within the scope of visibility
                private void trackCloseTanks(){

                    //addNewList(EntitManager.otherTanks.getPosOfCloseTanks(new Circle(tank.getPosition().x, tank.getPosition().y, objectSensorRadius),
                    //        tank));

                }
                //this function listens for any close static buildings
                //small variables for the method below
                private int trackCloseObstaclesI = 0, trackCloseObstaclesS = 0;
                private void trackCloseObstacles(){

                    if(MultiGameScreen.entityManager.getBaseBuildingList() != null){
                        for(trackCloseObstaclesI = 0;
                            trackCloseObstaclesI < MultiGameScreen.entityManager.getBaseBuildingList().length; trackCloseObstaclesI++){

                            for(trackCloseObstaclesS = 0;
                                trackCloseObstaclesS < MultiGameScreen.entityManager.getBaseBuildingList()[trackCloseObstaclesI].Vcorners.length - 1; trackCloseObstaclesS+=2){
                                if(Intersector.intersectSegmentCircle(
                                        MultiGameScreen.entityManager.getBaseBuildingList()[trackCloseObstaclesI].Vcorners[trackCloseObstaclesS],
                                        MultiGameScreen.entityManager.getBaseBuildingList()[trackCloseObstaclesI].Vcorners[trackCloseObstaclesS+1],
                                        body.getPosition(), obstacleSensoreRadius * obstacleSensoreRadius)){
                                    setObstacleObject(MultiGameScreen.entityManager.getBaseBuildingList()[trackCloseObstaclesI].body, 0);
                                }

                            }

                        }
                    }

                }
                //this function listens for any close walls and changes the joystick x to make the tank
                //driving parallel to the wall
                //this method uses lineWall to get the angle of the wall line
                private Vector2 lineWall = new Vector2(0, 0),
                        startWall = new Vector2(0,0), endWall = new Vector2(0,0);
                private void trackCloseWall(){

                    //check if the tank is close to a wall
                    if(circleVector2dAOverlaps(EntitManager.mapManager.getLevel().getBounderyPoints(),
                            new Circle(body.getPosition().x, body.getPosition().y, obstacleSensoreRadius/1.2f))){

                        //figure out a way to make the joystick x change so that the tank does not hit the wall
                        if( (float)Math.acos( lineWall.dot(body.getLinearVelocity())/(lineWall.len()*body.getLinearVelocity().len()) )
                                > Math.PI/2){
                            //if angle is bigger than 90 then turn right
                            joystickVector.x = (EntitManager.controllers.joystick.GetMaxVectorDimensions().x/2)
                                    *((tank.tank.getHeight()/(2*MultiGameScreen.PIXELS_TO_METERS))/
                                    Intersector.distanceSegmentPoint(startWall, endWall, body.getPosition()));

                        }else if( (float)Math.acos( lineWall.dot(body.getLinearVelocity())/(lineWall.len()*body.getLinearVelocity().len()) )
                                < Math.PI/2){
                            //if angle is less than 90 then turn left
                            joystickVector.x = -(EntitManager.controllers.joystick.GetMaxVectorDimensions().x/2)
                                    *((tank.tank.getHeight()/(2*MultiGameScreen.PIXELS_TO_METERS))/
                                    Intersector.distanceSegmentPoint(startWall, endWall, body.getPosition()));

                        }

                    }

                }

                //this function checks if a polygon intersect with a circle
                public boolean circlPolyOverlaps(Polygon polygon, Circle circle) {
                    float []vertices=polygon.getTransformedVertices();
                    Vector2 center=new Vector2(circle.x, circle.y);
                    float squareRadius=circle.radius*circle.radius;
                    for (int i=0;i<vertices.length;i+=2){
                        if (i==0){
                            if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length-2], vertices[vertices.length-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
                                return true;
                        } else {
                            if (Intersector.intersectSegmentCircle(new Vector2(vertices[i-2], vertices[i-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
                                return true;
                        }
                    }
                    return false;
                }
                //this function checks if a polygon intersect with a circle
                //small variables for the below method
                private float[] circlPolyLineOverlapsVertices;
                private int circlPolyLineOverlapsI = 0;
                private Vector2 circlPolyLineOverlapsCenter;
                public boolean circlPolyLineOverlaps(Polyline polyline, Circle circle) {
                    circlPolyLineOverlapsVertices = polyline.getTransformedVertices();
                    circlPolyLineOverlapsCenter = new Vector2(circle.x, circle.y);
                    float squareRadius=circle.radius*circle.radius;
                    for (circlPolyLineOverlapsI = 0;circlPolyLineOverlapsI<circlPolyLineOverlapsVertices.length - 2;
                         circlPolyLineOverlapsI+=2){
                        if(Intersector.intersectSegmentCircle(
                                new Vector2(circlPolyLineOverlapsVertices[circlPolyLineOverlapsI], circlPolyLineOverlapsVertices[circlPolyLineOverlapsI + 1]),
                                new Vector2(circlPolyLineOverlapsVertices[circlPolyLineOverlapsI + 2],
                                        circlPolyLineOverlapsVertices[circlPolyLineOverlapsI + 3]),
                                circlPolyLineOverlapsCenter, squareRadius)){
                            return true;
                        }
                    }
                    return false;
                }
                //this function checks if a polyline intersect with a circle but instead of using polyline
                //as a class we use vector 2d array
                //small variables for the method below
                private Vector2 circleVecOverlapsCenter;
                private float circleVecOverlapsSquareRadius;
                private int circleVecOverlapsI = 0;
                public boolean circleVector2dAOverlaps(Vector2[] vertices, Circle circle){
                    circleVecOverlapsCenter = new Vector2(circle.x, circle.y);
                    circleVecOverlapsSquareRadius = circle.radius*circle.radius;
                    for (circleVecOverlapsI = 0; circleVecOverlapsI < vertices.length - 1; circleVecOverlapsI++){
                        if(Intersector.intersectSegmentCircle(
                                vertices[circleVecOverlapsI],
                                vertices[circleVecOverlapsI + 1], circleVecOverlapsCenter, circleVecOverlapsSquareRadius)){
                            lineWall = new Vector2((float)Math.round((vertices[circleVecOverlapsI + 1].x - vertices[circleVecOverlapsI].x)*100)/100,
                                    (float)Math.round((vertices[circleVecOverlapsI + 1].y - vertices[circleVecOverlapsI].y)*100)/100);
                            startWall = new Vector2(vertices[circleVecOverlapsI].x, vertices[circleVecOverlapsI].y);
                            endWall = new Vector2(vertices[circleVecOverlapsI + 1].x, vertices[circleVecOverlapsI + 1].y);
                            return true;
                        }
                    }

                    return false;
                }

                //this method returns the positions in floats of vertices in dimension of box2d
                //Vector2 dimensions: in pixels
                private float[] getVertices(Vector2 pos, float angle, Vector2 dimensions){
                    float rod = (float) ((dimensions.y / (2*MultiGameScreen.PIXELS_TO_METERS)) / Math.sin(Math.atan(dimensions.y / dimensions.x)));

                    float[] vertices = new float[]{

                            rod * (float) Math.cos(angle + ((float)Math.atan(dimensions.y / dimensions.x))) + pos.x
                            ,rod * (float) Math.sin(angle + ((float)Math.atan(dimensions.y / dimensions.x))) + pos.y

                            ,rod * (float) Math.cos(angle - ((float)Math.atan(dimensions.y / dimensions.x)) + Math.PI) + pos.x
                            ,rod * (float) Math.sin(angle - ((float)Math.atan(dimensions.y / dimensions.x)) + Math.PI) + pos.y

                            ,rod * (float) Math.cos(angle + ((float)Math.atan(dimensions.y / dimensions.x)) + Math.PI) + pos.x
                            ,rod * (float) Math.sin(angle + ((float)Math.atan(dimensions.y / dimensions.x)) + Math.PI) + pos.y

                            ,rod * (float) Math.cos(angle - ((float)Math.atan(dimensions.y / dimensions.x)) + 2 * Math.PI) + pos.x
                            ,rod * (float) Math.sin(angle - ((float)Math.atan(dimensions.y / dimensions.x)) + 2 * Math.PI) + pos.y

                    };


                    return vertices;
                }

            }

            //other useful methods that calculates the angle of a vector
            //and timAngle trim an angle of a vector
            //these methods were created because it helps in getting the angle between vectors
            // that helps in steering the tank
            public float calculateAngleVec(Vector2 vector2){
                return ( Math.atan2(vector2.y,vector2.x)< 0 )?
                        (float)(Math.atan2(vector2.y, vector2.x) + Math.PI*2)
                        :
                        (float)Math.atan2(vector2.y, vector2.x)
                        ;
            }
            public float trimAngle(float angle){

                if(angle <= -Math.PI*2){
                    angle = trimAngle(angle + (float) Math.PI * 2);
                }else if(angle >= Math.PI*2){
                    angle = trimAngle(angle - (float) Math.PI * 2);
                }

                return angle;
            }

        }

        *
         * interface used to shoot bullets.



    }


    */

}
