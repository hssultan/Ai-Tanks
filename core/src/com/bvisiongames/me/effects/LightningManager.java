package com.bvisiongames.me.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bvisiongames.me.settings.Assets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahzji_000 on 2/8/2016.
 */
public class LightningManager {

    //number of lightnings
    public List<Lightning> lightnings = new ArrayList<Lightning>();

    /**
     * initiator.
     */
    public LightningManager(){}

    /**
     * update method
     * @param delta
     */
    public void update(float delta){

        int length = this.lightnings.size();
        for (int i = 0; i < length; i++){
            if(this.lightnings.get(i).isVisible){
                this.lightnings.get(i).update(delta);
            }
        }

    }

    /**
     * render method
     * @param batch
     */
    public void render(SpriteBatch batch){

        int length = this.lightnings.size();
        for (int i = 0; i < length; i++){
            if(this.lightnings.get(i).isVisible){
                this.lightnings.get(i).render(batch);
            }
        }

    }

    /**
     * add lightnings.
     */
    public void addLightnings(){
        this.lightnings.add(new Lightning());
    }
    /**
     * activate a lightning.
     * @param start
     * @param end
     */
    public void activate(Vector2 start, Vector2 end, int timeMilliSecond){

        int length = this.lightnings.size();
        for(int i = 0; i < length; i++){
            if(!this.lightnings.get(i).isVisible){
                this.lightnings.get(i).activate(start, end, timeMilliSecond);
                break;
            }
        }

    }
    /**
     * activate a lightning.
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public void activate(float startX, float startY, float endX, float endY, int timeMilliSecond){

        int length = this.lightnings.size();
        for(int i = 0; i < length; i++){
            if(!this.lightnings.get(i).isVisible){
                this.lightnings.get(i).activate(startX, startY, endX, endY, timeMilliSecond);
                break;
            }
        }

    }

    /**
     * dispose
     */
    public void dispose(){
        this.lightnings.clear();
    }

    /**
     * class of an individual lightning.
     */
    public class Lightning{

        //visibility of lightning
        public boolean isVisible = true;

        //start and end points
        public Vector2 startPoint = new Vector2(0, 0), endPoint = new Vector2(0, 0);

        //lightning effect
        public GFX lightning = new GFX();

        //time in millisecond
        private int timeMillisecond = 0,
                    framesCount = 0;

        /**
         * initiator.
         */
        public Lightning(){

            //set the texture
            lightning.setTexture(new Sprite(Assets.GameFileNames.getGameSkin().getRegion("progress")).getTexture());

        }

        /**
         * update method
         * @param delta
         */
        public void update(float delta){

            if(this.framesCount <= ((float)this.timeMillisecond/delta)/1000 ){
                //increment the frames
                this.framesCount++;
            }else{
                //reset the framcount and make this spark invisible
                this.framesCount = 0;
                this.isVisible = false;
            }

        }

        /**
         * render method
         * @param batch
         */
        public void render(SpriteBatch batch){

            this.lightning.drawChainLightningRandomBetweenPoints(batch,
                    startPoint, endPoint, endPoint, 2, 2, Color.WHITE, Color.WHITE);

        }

        /**
         * activate the lightning
         */
        public void activate(Vector2 start, Vector2 end, int timeMillisecond){

            this.isVisible = true;
            this.framesCount = 0;
            this.timeMillisecond = timeMillisecond;

            //set the start and end points
            this.startPoint.set(start.x, start.y);
            this.endPoint.set(end.x, end.y);

        }
        /**
         * activate the lightning
         */
        public void activate(float startX, float startY, float endX, float endY, int timeMillisecond) {

            this.isVisible = true;
            this.framesCount = 0;
            this.timeMillisecond = timeMillisecond;

            //set the start and end points
            this.startPoint.set(startX, startY);
            this.endPoint.set(endX, endY);

        }

    }

}
