package com.bvisiongames.me.Maps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bvisiongames.me.buildings.AssemblyBuilding;
import com.bvisiongames.me.buildings.BaseBuilding;

/**
 * Created by ahzji_000 on 12/10/2015.
 */
public abstract class Level {

    //map type
    private MapManager.MAPTYPE maptype;

    //array of vector2d for the map boundery points
    private Vector2[] bounderyPoints;
    //array of float for the map boundery points
    private float[] FbounderyPoints;
    //max map bounds
    private Vector2[] CbounderyPoints;

    //patrol points of the cpu tanks
    //private Vector2[] patrolPoints;

    //base building list
    private BaseBuilding[] baseBuildings;
    //total cpu players
    private int totalCPUPlayers = 0;

    //Assembly buildings list
    private AssemblyBuilding[] assemblyBuildings;

    //bases that assemble the cpu tanks outside the map boundery

    //main abstract
    abstract void addCPUS();
    //end of main abstract

    //update and render abstracts
    abstract void update();
    abstract void render(SpriteBatch spriteBatch);              //render ground sprites
    abstract void renderTopSprites(SpriteBatch spriteBatch);    //render above ground sprites
    //end of update and render abstracts

    //getters
    /**
     * returns the basebuilding list.
     */
    public BaseBuilding[] getBaseBuildings(){
        return baseBuildings;
    }
    /**
     * returns the assemblyBuilding list
     */
    public AssemblyBuilding[] getAssemblyBuildings(){
        return assemblyBuildings;
    }
    /**
     * returns the total cpu players in the game level.
     */
    public int getTotalCPUPlayers(){
        return this.totalCPUPlayers;
    }
    /**
     * returns the boundery points of the map.
     */
    public Vector2[] getBounderyPoints(){
        return this.bounderyPoints;
    }
    /**
     * returns the boundery points of the map in a float array.
     */
    public float[] getFbounderyPoints(){
        return this.FbounderyPoints;
    }
    /**
     * get the patrol points in vector2d array.
     */
    /*
    public Vector2[] getPatrolPoints(){
        return this.patrolPoints;
    }
    */
    /**
     * get the camera boundery vector2d array,
     * in pixels dimensions.
     */
    public Vector2[] getCbounderyPoints(){
        return this.CbounderyPoints;
    }
    /**
     * gets the map type.
     */
    public MapManager.MAPTYPE getMaptype(){
        return this.maptype;
    }
    //end of getters


    //setters
    /**
     * set boundery float array points.
     * @param FbounderyPoints
     * array of float of the boundery points.
     */
    public void setFbounderyPoints(float[] FbounderyPoints){
        this.FbounderyPoints = FbounderyPoints;
    }
    /**
     * set the base Building list
     * @param baseBuildings
     */
    public void setBaseBuildings(BaseBuilding[] baseBuildings){
        this.baseBuildings = baseBuildings;
    }
    /**
     * set the assembly building list
     * @param assemblyBuildings
     */
    public void setAssemblyBuildings(AssemblyBuilding[] assemblyBuildings){
        this.assemblyBuildings = assemblyBuildings;
    }
    /**
     * set the total cpu players in the game.
     * @param totalCPUPlayers
     * total cpu players in the game level.
     */
    public void setTotalCPUPlayers(int totalCPUPlayers){
        this.totalCPUPlayers = totalCPUPlayers;
    }
    /**
     * set the boundery points of the map
     * @param bounderyPoints
     * is an array of Vector2 of the boundery points of the map.
     */
    public void setBounderyPoints(Vector2[] bounderyPoints){
        this.bounderyPoints = bounderyPoints;
    }
    /**
     * set the patrol points in vector2d array.
     * @param patrolPoints
     * patrol points in vector2d array format.
     */
    /*
    public void setPatrolPoints(Vector2[] patrolPoints){
        this.patrolPoints = patrolPoints;
    }
    */
    /**
     * set the camera boundery of the map.
     * @param CbounderyPoints
     * camera boundery points of the map in array Vector2d format forming a total of 4,
     * in pixels dimensions.
     */
    public void setCbounderyPoints(Vector2[] CbounderyPoints){
        this.CbounderyPoints = CbounderyPoints;
    }
    /**
     * sets the map type variable
     * @param maptype
     * map type to be set to.
     */
    public void setMaptype(MapManager.MAPTYPE maptype){
        this.maptype = maptype;
    }
    //end of setters

}
