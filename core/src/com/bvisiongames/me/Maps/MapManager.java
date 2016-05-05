package com.bvisiongames.me.Maps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by ahzji_000 on 12/10/2015.
 * Map manager.
 */
public class MapManager {

    /**
     * level properties.
     */
    private MAPTYPE maptype;
    private Level level;


    //setters
    /**
     * set the type of map.
     * @param maptype
     * map type.
     */
    public void setMaptype(MAPTYPE maptype){
        this.maptype = maptype;
    }
    /**
     * initiate the map.
     * should be called after setting up the map type and other required variables
     */
    public void initiateMap(){
        switch (this.maptype){
            case LEVEL1:
                level = new Level1();
                break;
        }
    }
    //end of setters

    //getters
    /**
     * gets the map type.
     */
    public MAPTYPE getMaptype(){
        return this.maptype;
    }
    /**
     * gets the level instance.
     */
    public Level getLevel(){
        return this.level;
    }
    //end of getters


    //update and render methods
    public void update(){

        level.update();

    }
    public void render(SpriteBatch spriteBatch){

        level.render(spriteBatch);

    }
    public void renderTopSprites(SpriteBatch spriteBatch){
        level.renderTopSprites(spriteBatch);
    }
    //end of update and render methods


    /**
     * enum of type of maps (levels or stages)
     */
    public enum MAPTYPE{
        LEVEL1
    }

}
