package com.bvisiongames.me.intelligence;

import com.badlogic.gdx.math.Vector2;
import com.bvisiongames.me.settings.TankMode;

/**
 * Created by ahzji_000 on 12/29/2015.
 * class that holds properties of each point around the tank enemy entity.
 */
public class PointProperties {

    //radius of point
    public float radius = 0;
    //angle of point
    public float angle = 0;
    //fitness of firing a missile of normal bullet
    public float normalBulletFitness = 5;
    //fitness of firing a missile of electric bullet
    public float electricBulletFitness = 5;
    //overall fitness of this point
    public float overAllFitness = 5;
    //fitness to be subtracted
    public float subFitness = 0;

    //coordinates of the point in vector2 form
    public Vector2 coordinates = new Vector2(0, 0);

    /**
     * initiator
     */
    public PointProperties(){}

    //setters
    /**
     * add or subtract fitness based on the distance to it from the tank.
     * @param enemyPos
     * @param ownPos
     * @param r
     * is the radius of the furthest point from the targeted tank.
     */
    public void addOrSubFitnessVisibility(Vector2 enemyPos, Vector2 ownPos, float r){

        float c = enemyPos.dst(ownPos), d = coordinates.dst(ownPos),
                maxDist = 2 * r, currentDist = d - (c - r);

        this.subFitness = (int)((float)this.overAllFitness* (currentDist / maxDist) );

    }
    /**
     * recalculate the over all fitness
     */
    public void recalculateOverallFitness(){
        this.overAllFitness = (this.normalBulletFitness + this.electricBulletFitness)/2;
    }
    /**
     * makes this point unfit and at the center of the body.
     */
    public void resetFitnessToUnfit(){
        angle = 0;
        radius = 0.1f;
        overAllFitness = -1;
        electricBulletFitness = -1;
        normalBulletFitness = -1;
    }
    /**
     * set this point's properties to the supplied point.
     * @param point
     */
    public void setPoint(PointProperties point){
        this.radius = point.radius;
        this.angle = point.angle;
        this.normalBulletFitness = point.normalBulletFitness;
        this.electricBulletFitness = point.electricBulletFitness;
        this.overAllFitness = point.overAllFitness;
        this.coordinates = point.coordinates;
    }
    /**
     * calculate the overall fitness for this point.
     */
    public void calculateOverallFitness(){
        this.overAllFitness = (this.electricBulletFitness + this.normalBulletFitness)/2;
    }
    /**
     * increment the fitness of firing normal bullet from this point.
     */
    public void incrementNormalBulletFitness(TankMode.TankModeType tankModeType){
        switch (tankModeType){
            case EASY_MODE:
                this.normalBulletFitness += TankMode.EASY_BULLET1_INCREMENT;
                break;
            case MEDIUM_MODE:
                this.normalBulletFitness += TankMode.MEDIUM_BULLET1_INCREMENT;
                break;
            case HARD_MODE:
                this.normalBulletFitness += TankMode.HARD_BULLET1_INCREMENT;
                break;
        }
    }
    /**
     * decrement the fitness of firing normal bullet from this point.
     */
    public void decrementNormalBulletFitness(TankMode.TankModeType tankModeType){
        if(normalBulletFitness > 0){
            switch (tankModeType){
                case EASY_MODE:
                    this.normalBulletFitness -= TankMode.EASY_BULLET1_DECREMENT;
                    break;
                case MEDIUM_MODE:
                    this.normalBulletFitness -= TankMode.MEDIUM_BULLET1_DECREMENT;
                    break;
                case HARD_MODE:
                    this.normalBulletFitness -= TankMode.HARD_BULLET1_DECREMENT;
                    break;
            }
        }
    }
    /**
     * increment the fitness of firing electric bullet from this point.
     */
    public void incrementElectricBulletFitness(TankMode.TankModeType tankModeType){
        switch (tankModeType){
            case EASY_MODE:
                this.electricBulletFitness += TankMode.EASY_ELECTRIC_INCREMENT;
                break;
            case MEDIUM_MODE:
                this.electricBulletFitness += TankMode.MEDIUM_ELECTRIC_INCREMENT;
                break;
            case HARD_MODE:
                this.electricBulletFitness += TankMode.HARD_ELECTRIC_INCREMENT;
                break;
        }
    }
    /**
     * decrement the fitness of firing electric bullet from this point.
     */
    public void decrementElectricBulletFitness(TankMode.TankModeType tankModeType){
        if(this.electricBulletFitness > 0){
            switch (tankModeType){
                case EASY_MODE:
                    this.electricBulletFitness -= TankMode.EASY_ELECTRIC_DECREMENT;
                    break;
                case MEDIUM_MODE:
                    this.electricBulletFitness -= TankMode.MEDIUM_ELECTRIC_DECREMENT;
                    break;
                case HARD_MODE:
                    this.electricBulletFitness -= TankMode.HARD_ELECTRIC_DECREMENT;
                    break;
            }
        }
    }
    /**
     * set the angle of this point in pi.
     * @param angle
     * angle of the point in radian.
     */
    public void setAngle(float angle){
        this.angle = angle;
    }
    /**
     * set the radius of this point.
     * @param radius
     * radius from the enemy tank's point center.
     */
    public void setRadius(float radius){
        this.radius = radius;
    }
    //end of setters


    //getters
    /**
     * gets the fitness of normal bullet.
     */
    public float getNormalBulletFitness(){
        return this.normalBulletFitness;
    }
    /**
     * gets the fitness of electric bullet.
     */
    public float getElectricBulletFitness(){
        return this.electricBulletFitness;
    }
    /**
     * gets the overall fitness of this point.
     */
    public float getOverAllFitness(){
        return this.overAllFitness;
    }
    /**
     * get the radius of this point.
     */
    public float getRadius(){
        return this.radius;
    }
    /**
     * get the angle of this point in radian.
     */
    public float getAngle(){
        return this.angle;
    }
    /**
     * calculates the coordinates of this point.
     * @param targetTankPos
     * position of the targeted tank entity.
     * @param tankDirection
     * direciton of tank in radian.
     */
    public Vector2 calculateCoordinates(Vector2 targetTankPos, float tankDirection){
        if(targetTankPos != null && !Float.isNaN(tankDirection) ){
            this.coordinates.set(targetTankPos.x + radius*(float)Math.cos(angle+tankDirection),
                    targetTankPos.y + radius*(float)Math.sin(angle+tankDirection));
        }
        return this.coordinates;
    }
    /**
     * has same point properties.
     * @param point
     */
    public boolean hasSameProperties(PointProperties point){
        if(this.angle == point.angle && this.radius == point.radius
                && this.overAllFitness == point.overAllFitness && this.normalBulletFitness == point.normalBulletFitness
                && this.electricBulletFitness == point.electricBulletFitness){
            return true;
        }else{
            return false;
        }
    }
    //end of getters

}
