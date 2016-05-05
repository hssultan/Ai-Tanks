package com.bvisiongames.me.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bvisiongames.me.Tweenanimations.RadialProgressBarAnimation;
import aurelienribon.tweenengine.Tween;

/**
 * Created by ahzji_000 on 11/25/2015.
 *
 * example of using this class:
 * //initiate
 * radialProgressBar = new RadialProgressBar(new TextureRegion(new Texture("backgrounds/chocolate.jpg")));
 * //if adding animation to the radial progress bar. this method register it under the --->RadialProgressBarAnimation<--- class name
 * radialProgressBar.setTweenAnimation(MultiGameScreen.tweenManager);
 * radialProgressBar.setPosition(new Vector3(Tanks.cameraWIDTH / 2, Tanks.cameraHeight / 2, 0));
 * radialProgressBar.setRadius(200);
 * radialProgressBar.setProgressThickness(50);  //if thickness equals radius then it becomes a full circle
 * radialProgressBar.setPercentage(0);
 * radialProgressBar.setColorEnabled();   //whether colors should be enabled or not, if no color gradients added then it shows black
 * radialProgressBar.setColorGradient(new Vector3(0.7f, 0.1f, 0.1f), new Vector3(0.1f, 0.7f, 0.1f));    //add a color gradient
 *
 * //update it
 * radialProgressBar.update(viewport)
 *
 * //render it
 * radialProgressBar.render(batch)
 *
 */
public class RadialProgressBar {

    //properties of the radial progress bar
    private Vector3 position = new Vector3(0, 0, 0);
    private float startAngle = 0,   //starting angle of the radial bar
                    radius = 0;
    private TextureRegion progressBarTexture;
    private NinePatch background;
    private float padLeft = 0, padRight = 0,
                    padTop = 0, padBottom = 0;
    private float progressThickness = 0;
    private Vector2 radialOpacity = new Vector2(1, 1); //@ x: start, @ y: end

    //this tells whether some settings has been changed like gradient color, etc
    private boolean changedSettings = false;

    //percentage
    private float percentage = 0, percentAngle = 0;

    //shader properties
    ShaderProgram shaderProgram;

    //temp variables that are used to convert to pixels (position, radius, thickness)
    private Vector3 tmpPosition = new Vector3(0, 0, 0),     //contains the projected position from center
                    tmpDimensions = new Vector3(0, 0, 0);   //contains the projected radius @ x and thickness @ y

    //gradient color of the radial progress bar (2 colors total)
    private Vector3 startColor = new Vector3(0, 0, 0), endColor = new Vector3(0, 0, 0);
    //tells whether color is added or not
    private boolean isColored = false;

    /**
     * @param progressBarTexture
     * sets the progress bar inside the radial progress bar. Should be plain square or gradient square.
     * @param background
     * sets the background of the progress bar
     */
    public RadialProgressBar(TextureRegion progressBarTexture, NinePatch background){

        //set the progress bar properties
        this.progressBarTexture = progressBarTexture;
        this.background = background;

        //initiate the shader program
        shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/RadialProgress.vsh"),
                                            Gdx.files.internal("shaders/RadialProgress.fsh"));

    }
    /**
     * @param progressBarTexture
     * sets the progress bar inside the radial progress bar
     */
    public RadialProgressBar(TextureRegion progressBarTexture){

        //set the progress bar properties
        this.progressBarTexture = progressBarTexture;

        //initiate the shader program
        shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/RadialProgress.vsh"),
                Gdx.files.internal("shaders/RadialProgress.fsh"));

    }

    //setters
    /**
     * this method registers the tween for this class.
     */
    public void setTweenAnimation(){
        Tween.registerAccessor(RadialProgressBar.class, new RadialProgressBarAnimation());
    }
    /**
     * @param position
     * sets the position of the radial progress bar from the center
     */
    public void setPosition(Vector3 position){
        this.position = position;
        this.tmpPosition.set(position.x, position.y, position.y);
    }
    /**
     * @param radius
     * sets the radius of the progress bar
     */
    public void setRadius(float radius){
        this.radius = radius;
        this.tmpDimensions.x = radius;
    }
    /**
     * @param percentage
     * sets the percentage of the radial progress bar (0-100)%
     */
    public void setPercentage(float percentage){
        this.percentage = percentage;
        this.percentAngle = (percentage* (float)Math.PI*2)/100;
    }
    /**
     * @param padLeft
     * padding from left
     */
    public void setPadLeft(float padLeft){
        this.padLeft = padLeft;
    }
    /**
     * @param padRight
     * padding from right
     */
    public void setPadRight(float padRight){
        this.padRight = padRight;
    }
    /**
     * @param padTop
     * padding from top
     */
    public void setPadTop(float padTop){
        this.padTop = padTop;
    }
    /**
     * @param padBottom
     * padding from bottom
     */
    public void setPadBottom(float padBottom){
        this.padBottom = padBottom;
    }
    /**
     * sets the thickness of progress bar
     * @param thickness
     * thickness of the progress bar
     */
    public void setProgressThickness(float thickness){
        this.progressThickness = thickness;
        this.tmpDimensions.y = thickness;
    }
    /**
     * Add color gradient to the radial progress bar.
     * For plain color add the same color into both of the arguments
     * @param startColor
     * set the starting color gradient of the radial progress bar
     * @param endColor
     * set the ending color gradient of the radial progress bar
     */
    public void setColorGradient(Vector3 startColor, Vector3 endColor){
        this.startColor = startColor;
        this.endColor = endColor;
        changedSettings = true;
    }
    /**
     * @param startAngle
     * sets the starting angle in PI.
     * The radial bar will fill counter clockwise.
     * Going from 0->2PI would move the starting angle clockwise (opposite of radial bar fill direction
     */
    public void setStartAngle(float startAngle){
        this.startAngle = startAngle;
        changedSettings = true;
    }
    /**
     * @param startE
     * opacity of the starting color or end of the radial bar
     * @param endE
     * opacity of the ending color or end of the radial bar
     */
    public void setRadialOpacity(float startE, float endE){
        this.radialOpacity.set(startE, endE);
        changedSettings = true;
    }
    /**
     * add color to the radial bar
     */
    public void setColorEnabled(){
        this.isColored = true;
        changedSettings = true;
    }
    /**
     * remove color to the radial bar
     */
    public void setColorDisabled(){
        this.isColored = false;
        changedSettings = true;
    }
    //end of setters

    //getters
    /**
     * get the position of the radial progress bar from the center
     * @return position
     */
    public Vector3 getPosition(){
        return position;
    }
    /**
     * gets the radius of the progress bar
     * @return radius
     */
    public float getRadius(){
        return radius;
    }
    /**
     * gets the percentage complete of the radial progress bar (0-100)%
     * @return percentage
     */
    public float getPercentage(){
        return percentage;
    }
    /**
     *@return progressThickness
     * return the progress bar thickness in pixels
     */
    public float getProgressThickness(){
        return progressThickness;
    }
    /**
     * @return startAngle
     * returns the starting angle in PI
     */
    public float getStartAngle(){
        return startAngle;
    }
    /**
     * @return radialOpacity
     * returns the opacity in vector2: at x: start End
     *                                 at y: end End
     */
    public Vector2 getRadialOpacity(){
        return radialOpacity;
    }
    //end of getters

    /**
     * updates the Radial progress bar properties
     */
    public void update(Viewport viewport){

        shaderProgram.begin();

        //project variables

        //project position
        if(tmpPosition.x == position.x || tmpPosition.y == position.y){
            viewport.project(tmpPosition);
            shaderProgram.setUniformf("u_center", tmpPosition.x, tmpPosition.y);
        }
        //project dimensions (radius and thickness
        if( tmpDimensions.x == radius || tmpDimensions.y == progressThickness){
            viewport.project(tmpDimensions);
            shaderProgram.setUniformf("u_circle", tmpDimensions.x, tmpDimensions.y);
        }

        //send the color gradients
        if(changedSettings){
            changedSettings = false;
            //send if colors should be added or not
            shaderProgram.setUniformi("u_addColor", (this.isColored)? 1 : 0);
            //send colors gradient
            shaderProgram.setUniformf("u_color1", this.startColor);
            shaderProgram.setUniformf("u_color2", this.endColor);
            //send starting angle
            shaderProgram.setUniformf("u_startAngle", startAngle);
            //send the opacity
            shaderProgram.setUniformf("u_alpha", this.radialOpacity.x, this.radialOpacity.y);
        }

        //send the percent completion
        shaderProgram.setUniformf("u_angle", this.percentAngle);

        //end of projections

        shaderProgram.end();

    }
    /**
     * renderer of the progress bar on the screen
     * @param Batch
     * spritebatch to draw to
     */
    public void render(Batch Batch){

        Batch.begin();

        if(background != null){
            background.draw(Batch,
                    getPosition().x - getRadius() - padLeft, getPosition().y - getRadius() - padBottom,
                    getRadius()*2 + padRight, getRadius()*2 + padTop);
        }

        Batch.setShader(shaderProgram);
        if(progressBarTexture != null){
            Batch.draw(progressBarTexture,getPosition().x - getRadius(), getPosition().y - getRadius(),
                            getRadius()*2, getRadius()*2 );
        }
        Batch.setShader(null);

        Batch.end();

    }

}