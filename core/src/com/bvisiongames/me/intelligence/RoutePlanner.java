package com.bvisiongames.me.intelligence;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.bvisiongames.me.entity.Entity;
import com.bvisiongames.me.settings.TankMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahzji_000 on 1/9/2016.
 * this class plans a route to arrive to a target destination.
 */
public class RoutePlanner {

    //the entity being routed
    public Entity entity;

    //scanner
    public NearbyAreaScanner scanner;

    //points
    //all patrol points that has been created
    public List<PatrolPoint> patrolPoints = new ArrayList<PatrolPoint>();

    /**
     * initiator
     */
    public RoutePlanner(){}


    //setter methods
    /**
     * set the entity being routed.
     * @param entity
     * the entity being routed.
     */
    public void setEntity(Entity entity){
        //set the entity being routed
        this.entity = entity;
    }
    /**
     * set the scanner.
     * @param scanner
     */
    public void setScanner(NearbyAreaScanner scanner){
        this.scanner = scanner;
    }
    /**
     * initiate some objects.
     */
    public void initiateObjects(){}
    /**
     * gets notified when an entity is in scope.
     * @param entity
     */
    public void notifyOfEntityNearby(Entity entity){

        if(entity != null){

            //count if there are any points in the circle
            int count = 0;

            //set the scope
            this.tmpCircle.set(entity.getPosition(), TankMode.getTankScope(TankMode.TankModeType.EASY_MODE));

            //loop through all the patrol points and check whether there is one near this entity's position
            int length = patrolPoints.size();
            for(int i = 0; i < length; i++){

                if(tmpCircle.contains(this.patrolPoints.get(i).coordinates)){
                    count++;
                    this.patrolPoints.get(i).timesFindingEntities++;
                }

            }

            //if there are no points then add a new one at the entity's position
            if(count == 0){
                this.patrolPoints.add(new PatrolPoint(entity.getPosition().cpy()));
            }

        }

    }
    //end of setter methods


    //getter methods
    /**
     * find a patrol point that has the lowest energy to move to.
     * if there is a point then no nearby area scanner is required.
     */
    //tmp variables
    private Circle tmpCircle = new Circle(0, 0, 0);
    public PatrolPoint tmpChosenPoint;
    public PatrolPoint findCloseLowestEnergyPatrolPoint(){

        //set the circle to the entity
        this.tmpCircle.set(entity.getPosition(), TankMode.getTankScope(TankMode.TankModeType.EASY_MODE));

        int length = this.patrolPoints.size();
        for(int i = 0; i < length; i++){

            //find a close and lowest energy patrol point
            if(this.tmpCircle.contains(this.patrolPoints.get(i).coordinates)){

                //update the total energy for the contained patrol points
                this.patrolPoints.get(i).calculateTotalEnergy(this.scanner);

                //set the null tmpChosenPoint
                if(this.tmpChosenPoint == null){
                    this.tmpChosenPoint = this.patrolPoints.get(i);
                }else
                //if the prev tmp chosen patrol point is not contained
                if(this.tmpChosenPoint != null
                        && !this.tmpCircle.contains(this.tmpChosenPoint.coordinates)){
                    this.tmpChosenPoint = this.patrolPoints.get(i);
                }

                //check the lowest energy patrol point
                if(this.patrolPoints.get(i).totalEnergyRequired < this.tmpChosenPoint.totalEnergyRequired){
                    //set the tmpChosen patrol point
                    this.tmpChosenPoint = this.patrolPoints.get(i);
                }

            }

        }
        return this.tmpChosenPoint;
    }
    /**
     * this method finds a point that has high rate of finding an entity to target.
     */
    public PatrolPoint findHighProbabilityEntityPatrolPoint(){

        //first null tmpchosen
        this.tmpChosenPoint = null;

        //set the circle to the entity
        this.tmpCircle.set(entity.getPosition(), TankMode.getTankScope(TankMode.TankModeType.EASY_MODE));

        int length = this.patrolPoints.size();
        for(int i = 0; i < length; i++){

            //find a close patrol point
            if(this.tmpCircle.contains(this.patrolPoints.get(i).coordinates)){

                //set the null tmpChosenPoint
                if(this.tmpChosenPoint == null){
                    this.tmpChosenPoint = this.patrolPoints.get(i);
                }else
                //if the prev tmp chosen patrol point is not contained
                if(this.tmpChosenPoint != null
                        && !this.tmpCircle.contains(this.tmpChosenPoint.coordinates)){
                    this.tmpChosenPoint = this.patrolPoints.get(i);
                }

                //check the lowest energy patrol point
                if(this.patrolPoints.get(i).timesFindingEntities < this.tmpChosenPoint.timesFindingEntities){
                    //set the tmpChosen patrol point
                    this.tmpChosenPoint = this.patrolPoints.get(i);
                }

            }

        }

        return tmpChosenPoint;
    }
    //end of getter methods


    //scanners
    /**
     * analyze the surroundings for patrol points and come up with a best plan to move around.
     * if there is no patrol points, then create a new one using the scanner area.
     */
    public Vector2 findaPatrolPoint(){

        //run the close patrol point scanner
        PatrolPoint chosenPatrolPoint =  this.findHighProbabilityEntityPatrolPoint();

        //run the scanner first
        this.scanner.resetPoints();
        this.scanner.scopeFreeAreaPoints( (chosenPatrolPoint != null)? chosenPatrolPoint.coordinates : null );

        return this.scanner.lowestEnergyPoint.coordinates;

    }
    //end of scanners

}
