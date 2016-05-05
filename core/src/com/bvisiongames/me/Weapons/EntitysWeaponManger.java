package com.bvisiongames.me.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Tweenanimations.CellAnimation;
import com.bvisiongames.me.Tweenanimations.CustomStackAnimation;
import com.bvisiongames.me.animations.RadialProgressBar;
import com.bvisiongames.me.entity.*;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.Assets;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

/**
 * Created by ahzji_000 on 11/12/2015.
 */
public class EntitysWeaponManger {

    //interface for the main player
    private TextButton fire;
    private boolean isInterfaceCreated = false;
    private Table table;

    //add the buttons
    public WeaponsButtons bullet1, electricBullet;

    //bullet type
    private com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE bulletstype;

    //radial loading animation
    RadialProgressBar radialProgressBar;

    //time out between bullets when switching between them
    private int timeOut = 0;

    /**
     * @param fire
     * takes in the fire actor if this weapon manager is created to the main user
     * @param createInterface
     * takes in boolean and it checks whether the interface should be created or not
     */
    public EntitysWeaponManger(TextButton fire, boolean createInterface){

        //save the reference to the fire button to this class, so it can be disabled or enabled
        this.fire = fire;

        if(createInterface){
            createInterface();
            isInterfaceCreated = true;
        }

    }
    /**
     * this initiator is used for the cpu tanks or other tanks that does not show the interface.
     */
    public EntitysWeaponManger(){

        bullet1 = new WeaponsButtons(com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE.BULLET1);
        electricBullet = new WeaponsButtons(com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE.ELECTRICBULLET);

    }
    private void createInterface(){

        //box that holds the scroll panel
        table = new Table();
        table.setSize(Tanks.cameraWIDTH / 10, Tanks.cameraHeight / 1.5f);
        table.setPosition(Tanks.cameraWIDTH - table.getWidth() - 20,
                Tanks.cameraHeight / 2 - table.getHeight() / 2 + fire.getHeight() / 2);

        bulletstype = com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE.BULLET1;

        MultiGameScreen.stage.addActor(table);

        //setup the radial progress bar
        radialProgressBar = new RadialProgressBar(new TextureRegion(Assets.assetManager.get(Assets.GameFileNames.plainGreenString, Texture.class)));
        radialProgressBar.setPosition(new Vector3(Tanks.cameraWIDTH - 100, 100, 0));
        radialProgressBar.setColorEnabled();
        radialProgressBar.setColorGradient(new Vector3(0.43f, 0.5f, 0.61f),
                                            new Vector3(0.43f, 0.5f, 0.61f));
        radialProgressBar.setRadius(90);
        radialProgressBar.setProgressThickness(90);
        radialProgressBar.setTweenAnimation();
        radialProgressBar.setPercentage(0);
        radialProgressBar.setRadialOpacity(0.6f, 0.6f);

    }

    /**
     * @param tank
     * the tank that is shooting this bullet.
     */
    public void shoot(Tank tank){

        switch (bulletstype) {
            case BULLET1:
                EntitManager.tankInterface.shoot(tank);
                bullet1.resetReloadTimeOut();
                break;
            case ELECTRICBULLET:
                EntitManager.tankInterface.shootElectricShock(tank);
                electricBullet.resetReloadTimeOut();
                break;
        }

        removeBullet(getBulletstype());

    }

    /**
     * method that returns if it is ready to shoot the selected bullet type or not
     */
    public boolean isReadyToShoot(){
        if(isInterfaceCreated){
            switch (bulletstype){
                case BULLET1:
                    return (bullet1 != null)? bullet1.isReadyToShoot() : false;
                case ELECTRICBULLET:
                    return (electricBullet != null)? electricBullet.isReadyToShoot() : false;
                default:
                    return false;
            }
        }else{
            if(timeOut >= 100){
                switch (bulletstype){
                    case BULLET1:
                        return (bullet1 != null)? bullet1.isReadyToShoot() : false;
                    case ELECTRICBULLET:
                        return (electricBullet != null)? electricBullet.isReadyToShoot() : false;
                    default:
                        return false;
                }
            }else{
                return false;
            }
        }
    }
    /**
     * returns true if the selected bullet type has enough bullets.
     */
    public boolean hasEnoughBullets(){
        switch (bulletstype){
            case ELECTRICBULLET:
                if(electricBullet != null && electricBullet.hasBullets()){
                    return true;
                }
            case BULLET1:
                if(bullet1 != null && bullet1.hasBullets()){
                    return true;
                }
        }

        return false;
    }
    //getters
    /**
     *returns the selected bullet type
     * @return bulletstype
     */
    public com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE getBulletstype(){
        return bulletstype;
    }
    /**
     * this method gets the total weapon types setup for this entity.
     */
    public int getTotalWeaponTypes(){
        int total = 0;

        if(bullet1 != null && bullet1.totalBullets > 0){
            total++;
        }
        if(electricBullet != null && electricBullet.totalBullets > 0){
            total++;
        }

        return total;
    }
    //end of getters


    //setters
    /**
     * set the bullet type to the next bullet type if it is not null and has enough bullets.
     * starting with bullet1, electric,...etc
     */
    public void switchToNextType(){

        switch (bulletstype){
            case BULLET1:
                if(electricBullet != null)
                    setBulletstype(Weapon.BULLETSTYPE.ELECTRICBULLET);
                break;
            case ELECTRICBULLET:
                if(bullet1 != null)
                    setBulletstype(Weapon.BULLETSTYPE.BULLET1);
                break;
        }

    }
    /**
     * set bullet weapon buttons.
     * @param bullet_1
     * bullet1 normal bullet (boolean var)
     * @param electric
     * electric bullet  (boolean var)
     */
    public void setBulletsBtn(boolean bullet_1, boolean electric){
        //add the types of bullets
        //add the bullet1 button
        if(bullet_1){
            bullet1 = new WeaponsButtons(
                    new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("marineup"), 5,5,5,5)),
                    new NinePatchDrawable(new NinePatch(MultiGameScreen.Bullets.getRegion("bullet1_icon"), 5,5,5,5)),
                    com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE.BULLET1);
        }

        //add the regular button for Electric Bullet
        if(electric){
            electricBullet = new WeaponsButtons(
                    new NinePatchDrawable(new NinePatch(MultiGameScreen.gameSkin.getRegion("marineup"), 5,5,5,5)),
                    new NinePatchDrawable(new NinePatch(MultiGameScreen.Bullets.getRegion("electric_icon"), 5,5,5,5)),
                    com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE.ELECTRICBULLET);
        }

    }
    /**
     * set the bullet type of the weapon manager
     * @param bulletstype
     * takes in bullet type
     */
    public void setBulletstype(com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE bulletstype){
        if(this.bulletstype != bulletstype){
            this.bulletstype = bulletstype;
            this.timeOut = 0;
            if(isInterfaceCreated){
                switch (bulletstype){
                    case BULLET1:
                        if(bullet1.hasBullets()){
                            bullet1.animateSelected();
                        }
                        break;
                    case ELECTRICBULLET:
                        if(electricBullet.hasBullets()){
                            electricBullet.animateSelected();
                        }
                        break;
                }
            }
        }
    }
    /**
     * adds a bullet to the bullet box for the player tank
     * @param bulletstype
     * the bullet type that will be added
     */
    public void addBullet(com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE bulletstype){
        if(isInterfaceCreated){
            switch (bulletstype){
                case ELECTRICBULLET:
                    electricBullet.totalBullets++;
                    electricBullet.animateIncreaseInBullets();
                    break;
                case BULLET1:
                    bullet1.totalBullets++;
                    bullet1.animateIncreaseInBullets();
                    break;
            }
        }else{
            switch (bulletstype){
                case ELECTRICBULLET:
                    electricBullet.totalBullets++;
                    break;
                case BULLET1:
                    bullet1.totalBullets++;
                    break;
            }
        }
    }
    /**
     *remove a bullet from the type of chosen bullet types to be removed
     * @param bulletstype
     * the bullet type that will be removed
     */
    public void removeBullet(com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE bulletstype){
        if(isInterfaceCreated){
            switch (bulletstype){
                case ELECTRICBULLET:
                    if(electricBullet.hasBullets()){
                        if(electricBullet.totalBullets > 1){
                            electricBullet.decrementTotalBullets(1);
                            electricBullet.animateShoot();
                        }else{
                            electricBullet.totalBullets = 0;
                            electricBullet.animateDeselected();
                            electricBullet.Disable();
                            Tanks.gameState.weapons.setElectricBulletTotal(0);
                        }
                    }
                    break;
                case BULLET1:
                    if(bullet1.hasBullets()){
                        bullet1.animateShoot();
                    }
                    break;
            }
        }else{
            switch (bulletstype){
                case ELECTRICBULLET:
                    if(electricBullet.hasBullets()){
                        if(electricBullet.totalBullets > 1){
                            electricBullet.decrementTotalBullets(1);
                        }else{
                            electricBullet.totalBullets = 0;
                        }
                    }
                    break;
            }
        }
    }
    //end of setters

    /**
     * updates the weapon manager.
     */
    public void update(){

        //update the bullet buttons
        if(bullet1 != null)
            bullet1.update();

        if(electricBullet != null)
            electricBullet.update();
        //end of bullet buttons update

        //update timeout
        if(timeOut < 100)
            timeOut++;

        if(isInterfaceCreated){

            radialProgressBar.update(MultiGameScreen.stage.getViewport());

            //update the radial progress bar
            switch (getBulletstype()){
                case BULLET1:
                    if(bullet1 != null && bullet1.hasBullets()){
                        radialProgressBar.setPercentage(
                                ((bullet1.reloadTimeOut/Gdx.graphics.getFramesPerSecond())/ Bullet.getReloadingTimeSec(getBulletstype()))*100 );
                    }else{
                        radialProgressBar.setPercentage(0);
                    }
                    break;
                case ELECTRICBULLET:
                    if(electricBullet != null && electricBullet.hasBullets()){
                        radialProgressBar.setPercentage(
                                ((electricBullet.reloadTimeOut/Gdx.graphics.getFramesPerSecond())/ Bullet.getReloadingTimeSec(getBulletstype()))*100
                        );
                    }else{
                        radialProgressBar.setPercentage(0);
                    }
                    break;
            }

            if(isReadyToShoot()){
                fire.setDisabled(false);
            }else{
                fire.setDisabled(true);
            }

        }

    }

    //visibility methods
    public void hide(){
        if(isInterfaceCreated){
            table.setVisible(false);
        }
    }
    public void show(){
        if(isInterfaceCreated){
            table.setVisible(true);
        }
    }
    //end of visibility methods

    /**
     * render the sprites and textures.
     * @param spriteBatch
     * sprite batch
     */
    public void render(SpriteBatch spriteBatch){

        spriteBatch.end();

        radialProgressBar.render(MultiGameScreen.stage.getBatch());

        spriteBatch.begin();

    }

    /**
     * Weapon button class. Can be holder of bullets and interface or just holder for bullters for one particular bullet type.
     */
    public class WeaponsButtons{

        //button's bullet type
        public com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE type;

        //stack
        private CustomStack stack;

        //background
        private Image bg;

        //icon
        private Image icon;

        //expansion ratio
        private float expansionX = 10, expansionY = 10;

        //total number of bullets
        public int totalBullets = 0;

        //state of this bullet button selected or not
        public WEAPONBUTTONSTATE state = WEAPONBUTTONSTATE.DESELECTED;

        //padding
        float padding = 20;

        //disabled or enabled button
        private boolean enabled = false;

        private float reloadTimeOut = 0;

        /**
         * weapon button for interface. this generates a button for the particular bullet type.
         * @param bg
         * background for the button
         * @param icon
         * icon to be inserted to display the bullet type
         * @param type
         * bullet's type
         */
        public WeaponsButtons(NinePatchDrawable bg, NinePatchDrawable icon, final com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE type){

            //create the stack
            stack = new CustomStack();

            //set images
            this.bg = new Image(bg);
            this.icon = new Image(icon);

            //set the bullet type
            this.type = type;

            //add a table that will contain the image to the bullet1 icon
            Table iconTable = new Table();
            iconTable.add(this.icon)
                    .width(this.icon.getWidth() * 1.3f).height(this.icon.getHeight() * 1.3f);
            //add the images to the stack
            stack.add(this.bg);
            stack.add(iconTable);
            //add it to the weapons display manager
            table.add(stack)
                    .width(table.getWidth() - 20).height(table.getHeight()/5 - 20)
                    .padBottom(20).padLeft(10).padRight(10);
            table.row();

            Disable();

            //add the listeners
            this.stack.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(hasBullets()){
                        switch (type) {
                            case BULLET1:
                                bulletstype = com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE.BULLET1;
                                animateSelected();
                                if(electricBullet != null)
                                    electricBullet.animateDeselected();
                                break;
                            case ELECTRICBULLET:
                                bulletstype = com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE.ELECTRICBULLET;
                                animateSelected();
                                if(bullet1 != null)
                                    bullet1.animateDeselected();
                                break;
                        }
                    }
                }
            });

        }
        /**
         * This initiator holds the bullets for the specific bullet type. no interface is generated.
         * @param type
         * bullet type
         */
        public WeaponsButtons(com.bvisiongames.me.Weapons.Weapon.BULLETSTYPE type){
            //set the bullet type
            this.type = type;
        }

        //animations
        /**
         * animate the button to selected.
         */
        public void animateSelected(){

            this.state = WEAPONBUTTONSTATE.SELECTED;
            Tween.to(table.getCell(this.stack), CellAnimation.WIDTH_HEIGHT, 0.2f)
                    .target(table.getWidth() - padding + expansionX, table.getHeight() / 5 - padding + expansionY)
                    .start(MultiGameScreen.tweenManager);

        }
        /**
         * animate the button to deselected
         */
        public void animateDeselected(){

            this.state = WEAPONBUTTONSTATE.DESELECTED;
            Tween.to(table.getCell(this.stack), CellAnimation.WIDTH_HEIGHT, 0.2f)
                    .target(table.getWidth() - padding, table.getHeight() / 5 - padding)
                    .start(MultiGameScreen.tweenManager);

        }

        /**
         * animate shoot button.
         * uses tween animation to animate the size of the cell of this button.
         * also the glow effect is called here.
         */
        //small variable for animate shoot callback
        private TweenCallback animateShootCallback = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                if (type == TweenCallback.END) {
                    animateSelected();
                    animateDeGlow();
                }
            }
        };
        public void animateShoot(){
            //animate the size of the button
            Tween.to(table.getCell(this.stack), CellAnimation.WIDTH_HEIGHT, 0.2f)
                    .target(table.getWidth() - expansionX*1.5f, table.getHeight() / 5 - expansionY*1.5f)
                    .setCallbackTriggers(TweenCallback.END)
                    .setCallback(animateShootCallback)
                    .start(MultiGameScreen.tweenManager);

            //add the glow effect animation
            animateGlow();

        }

        /**
         * glow tween animation to the button.
         */
        //small animate glow variables
        private TweenCallback animateGlowCallback = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                if(type == TweenCallback.BEGIN){
                    stack.addGlow();
                    stack.setGlowRatio(0.5f);
                }
            }
        };
        public void animateGlow(){
            Tween.to(this.stack, CustomStackAnimation.BRIGHTNESS_GLOW_RATIO, 0.2f)
                    .target(0.7f, 1.5f)
                    .setCallbackTriggers(TweenCallback.BEGIN)
                    .setCallback(animateGlowCallback)
                    .start(MultiGameScreen.tweenManager);
        }
        /**
         * remove the glow effect in tween animation
         */
        //small variables for animate deglow
        private TweenCallback animateDeglowCallback = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                if(type == TweenCallback.END){
                    stack.removeGlow();
                    stack.setGlowRatio(0.5f);
                }
            }
        };
        public void animateDeGlow(){
            Tween.to(this.stack, CustomStackAnimation.BRIGHTNESS_GLOW_RATIO, 0.2f)
                    .target(0.5f, 0.5f)
                    .setCallbackTriggers(TweenCallback.END)
                    .setCallback(animateDeglowCallback)
                    .start(MultiGameScreen.tweenManager);
        }

        /**
         * animate the button to show increase in bullets.
         */
        //small variable
        private TweenCallback animateIncreaseBulletsCallBack = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                if (type == TweenCallback.END
                        && state == WEAPONBUTTONSTATE.SELECTED) {
                    animateSelected();
                } else if (type == TweenCallback.END
                        && state == WEAPONBUTTONSTATE.DESELECTED) {
                    animateDeselected();
                }
            }
        };
        public void animateIncreaseInBullets(){

            //enable the button before animating if it is enabled
            Enable();

            //then animate
            Tween.to(table.getCell(this.stack), CellAnimation.WIDTH_HEIGHT, 0.3f)
                    .target(table.getWidth() - padding + expansionX * 2f, table.getHeight() / 5 - padding + expansionY * 2f)
                    .setCallbackTriggers(TweenCallback.END)
                    .setCallback(animateIncreaseBulletsCallBack)
                    .start(MultiGameScreen.tweenManager);

        }
        //end of animation methods


        //setters
        /**
         * enables the effect for enabled button.
         */
        public void Enable(){
            enabled = true;
        }

        /**
         * disable the effect for disabled button.
         */
        public void Disable(){
            enabled = false;
        }
        /**
         * perform a click on the button.
         */
        public void performClick() {
            Array<EventListener> listeners = this.stack.getListeners();
            for(int i=0;i<listeners.size;i++)
            {
                if(listeners.get(i) instanceof ClickListener){
                    ((ClickListener)listeners.get(i)).clicked(null, 0, 0);
                }
            }
        }
        /**
         * decrement the number of bullets for this button.
         * @param decrement
         * the total number the total bullets will be decremented in integer form.
         */
        public void decrementTotalBullets(int decrement){
            this.totalBullets = this.totalBullets - decrement;
            //update the gamestate for the number of bullets for this type
            switch (this.type){
                case ELECTRICBULLET:
                    Tanks.gameState.weapons.setElectricBulletTotal(this.totalBullets);
                    break;
            }
        }
        /**
         * resets the reload time out to zero.
         */
        public void resetReloadTimeOut(){
            this.reloadTimeOut = 0;
        }
        //end of setters

        //getters
        /**
         * checks whether this bullet holder still contains bullets.
         * @return whether there is enough bullets
         */
        public boolean hasBullets(){
            return (totalBullets>0)? true : false;
        }
        /**
         * is ready to shoot.
         */
        public boolean isReadyToShoot(){

            if(reloadTimeOut / Gdx.graphics.getFramesPerSecond() >=
                    Bullet.getReloadingTimeSec(this.type)){
                return true;
            }

            return false;
        }
        //end of getters


        /**
         * updates this bullet holder.
         */
        public void update(){

            if(reloadTimeOut/Gdx.graphics.getFramesPerSecond()
                    < Bullet.getReloadingTimeSec(this.type)
                    && this.hasBullets()){
                reloadTimeOut++;
            }

            if(this.stack != null){
                this.stack.update(enabled, this.state == WEAPONBUTTONSTATE.DESELECTED? false: true);
            }

        }

    }

    /**
     * Custom stack class to customize the shaders effects on the this button
     */
    public class CustomStack extends Stack{

        private boolean enabled = true, circleGlow = false;
        private float contrast = 1f,      // 0: black, 1: normal, 2: more colors are added
                        brightness = 0.5f,  //0: black, 0.5: normal brightness, 1: maximum brightness
                        radiusSpan = 0.5f;  //radius of the glow effect
        private Vector3 coordinates = new Vector3(0, 0, 0),
                        dimensions = new Vector3(0, 0, 0);

        private ShaderProgram shaderProgram;

        private boolean selected = false;

        public CustomStack(){

            this.shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/WeapBtnColor.vsh"),
                                                Gdx.files.internal("shaders/WeapBtnColor.fsh"));

        }

        public void update(boolean enabled, boolean selected){
            this.enabled = enabled;
            this.selected = selected;
        }

        /**
         * override the draw method in stack actor.
         * @param batch
         * @param parentAlpha
         */
        @Override
        public void draw(Batch batch, float parentAlpha) {

            //end the batch
            batch.end();

                this.shaderProgram.begin();
                //set the enable and disable color properties
                if (enabled) {
                    this.shaderProgram.setUniformi("enabled", 1);
                } else {
                    this.shaderProgram.setUniformi("enabled", 0);
                }
                //set up the glow circle
                if (circleGlow) {
                    this.shaderProgram.setUniformi("u_glow", 1);
                } else {
                    this.shaderProgram.setUniformi("u_glow", 0);
                }
                //set up the selected effect
                this.shaderProgram.setUniformi("u_selected", this.selected? 1 : 0);
                //send the contrast and the brightness
                this.shaderProgram.setUniformf("contrast", contrast);
                this.shaderProgram.setUniformf("brightness", brightness);
                //send the glow span radius
                this.shaderProgram.setUniformf("u_radius_ratio", radiusSpan);
                //update the X and Y coordinates
                coordinates.set(getX(), getY(), getZIndex());
                getStage().getCamera().project(coordinates);
                //update the dimensions
                this.dimensions.set(getWidth(), getHeight(), 0);
                getStage().getCamera().project(this.dimensions);
                //send the x and y coordinates and width and height
                this.shaderProgram.setUniformf("u_coordinates", coordinates.x, coordinates.y);
                this.shaderProgram.setUniformf("u_dimensions", dimensions.x, dimensions.y);
                this.shaderProgram.end();

            //start the batch
            batch.begin();

                batch.setShader(this.shaderProgram);
                super.draw(batch, parentAlpha);
                batch.setShader(null);

        }

        //setters
        /**
         * set contrast by adding more colors to the stack.
         * @param contrast
         * 0: for black, 1: normal, more than 1 heads in brighter colors
         */
        public void setContrast(float contrast){
            this.contrast = contrast;
        }
        /**
         *adds more brightness.
         * @param brightness
         * 0: dark colors to black, 1: very bright-white
         */
        public void setBrightness(float brightness){
            this.brightness = brightness;
        }
        /**
         * add a glow radius effect when brightness is increased.
         * @param glowRadiusRatio
         * 0: smallest, 1: full circle inside the button box.
         */
        public void setGlowRatio(float glowRadiusRatio){
            this.radiusSpan = glowRadiusRatio;
        }

        /**
         * adds the glow effect
         */
        public void addGlow(){
            circleGlow = true;
        }
        /**
         * removes the glow effect
         */
        public void removeGlow(){
            circleGlow = false;
        }
        //end of setters

        //getters
        /**
         * returns the current contrast.
         * @return
         */
        public float getContrast(){
            return contrast;
        }
        /**
         * returns the current brightness.
         * @return
         */
        public float getBrightness(){
            return brightness;
        }
        /**
         * returns the current glow ratio.
         * @return
         */
        public float getGlowRatio(){
            return radiusSpan;
        }
        //end of getters

    }

    /**
     * Button's State.
     */
    public enum WEAPONBUTTONSTATE{
        SELECTED, DESELECTED
    }

}
