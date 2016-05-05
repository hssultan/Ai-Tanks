package com.bvisiongames.me.intelligence;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bvisiongames.me.Maps.MapManager;
import com.bvisiongames.me.Weapons.EntitysWeaponManger;
import com.bvisiongames.me.entity.Entity;
import com.bvisiongames.me.entity.SmallBox;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.settings.*;

/**
 * Created by ahzji_000 on 12/29/2015.
 *
 * this class organizes and holds the data points of each tank's artificial intelli.
 * this is for the initial tanks to attack an enemy entity.
 *
 */
public class IntelliDataPointsManager {

    //------->begin of targets to attack<--------
    //tank entity fitness manager
    public EntityFitnessManager tankEntityFitnessManager = new EntityFitnessManager();
    //small box entity fitness manager
    public EntityFitnessManager smallBoxEntityFitnessManager = new EntityFitnessManager();
    //chosen entity fitness manager
    public EntityFitnessManager chosenEntityFitnessManager; //<--------------CHOSEN Entity to Target--------------->
    //------>end of targets to attack<--------


    //------>begin of route planner<-------
    public RoutePlanner routePlanner = new RoutePlanner();
    //------>end of route planner<---------


    //maptype and tanktype and tank mode type
    public MapManager.MAPTYPE maptype;
    public Tank.TANKSTYPES tankstypes;
    public TankMode.TankModeType tankModeType;

    //target point
    public PointProperties targetPoint;

    //weapon selector
    public WeaponSelecter weaponSelecter = new WeaponSelecter();


    /**
     * initiator
     */
    public IntelliDataPointsManager(){

        //reset the weapon selector to starting variables
        weaponSelecter.reset();

        //calculate the total fitness of tank entity fitness managers
        if(this.tankEntityFitnessManager.EntityAttackPoints.size() != 0){
            this.tankEntityFitnessManager.calculateTotalFitness();
        }
        //calculate the total fitness of small box entity fitness managers
        if(this.smallBoxEntityFitnessManager.EntityAttackPoints.size() != 0){
            this.smallBoxEntityFitnessManager.calculateTotalFitness();
        }

    }


    //setters
    /**
     * set map type.
     * @param maptype
     */
    public void setMaptype(MapManager.MAPTYPE maptype){
        this.maptype = maptype;
    }
    /**
     * set tanks type.
     * @param tankstypes
     */
    public void setTankstypes(Tank.TANKSTYPES tankstypes){
        this.tankstypes = tankstypes;
    }
    /**
     * set tank mode type
     * @param tankModeType
     */
    public void setTankModeType(TankMode.TankModeType tankModeType){
        this.tankModeType = tankModeType;
    }
    /**
     * initialize the points. they have never been set.
     * adding them to the points list.
     * start by the outer circle that contains a total of 20 points distributed with equal arc distance on the circle.
     * then move to the inner circles that contain less points but with the same arc distance as the outer most circle.
     */
    public void initializePoints(){

        //initialize the points for the tank target entity
        float angleInc = 0,
                arcLength = TankMode.getAttackTankScope(this.tankModeType) * (18* MathUtils.degreesToRadians);

        //point property instance
        PointProperties tmppoint;

        //loop through the circles radius, starting from the outer most one.
        //i: is the current radius of the circle we are on.
        for(float i = TankMode.getAttackTankScope(this.tankModeType);
            i > TankMode.getNearbyScope() ; i -= 2){
            //calculate a new angle incrementer
            angleInc = arcLength / i;
            for(float a = 0; a < 2*Math.PI; a+= angleInc){
                //add the points for the tank entity manager
                tmppoint = new PointProperties();
                tmppoint.setAngle(a);
                tmppoint.setRadius(i);
                this.tankEntityFitnessManager.EntityAttackPoints.add(tmppoint);
                //add the points for the small box entity manager
                tmppoint = new PointProperties();
                tmppoint.setAngle(a);
                tmppoint.setRadius(i);
                this.smallBoxEntityFitnessManager.EntityAttackPoints.add(tmppoint);
            }

        }

        //then calculate the total fitness
        this.tankEntityFitnessManager.calculateTotalFitness();
        this.smallBoxEntityFitnessManager.calculateTotalFitness();

    }
    //end of setters


    //getters
    /**
     * this returns the entity that has higher priority in fitness of this AI.
     */
    public Entity getHighPriorityEntity(Entity oldEntity, Entity newEntity){

        //update the total fitness for the new Entity fitness manager
        if(newEntity instanceof Tank){
            this.tankEntityFitnessManager.calculateTotalFitness();
        }else
        if(newEntity instanceof SmallBox){
            this.smallBoxEntityFitnessManager.calculateTotalFitness();
        }

        if(oldEntity == null){
            //if it is a tank
            if(newEntity instanceof Tank){
                this.chosenEntityFitnessManager = this.tankEntityFitnessManager;
            }else
            //else if it is a small box
            if(newEntity instanceof SmallBox){
                this.chosenEntityFitnessManager = this.smallBoxEntityFitnessManager;
            //else by default a tank
            }else{
                this.chosenEntityFitnessManager = this.tankEntityFitnessManager;
            }

            if(this.chosenEntityFitnessManager.lethalityOfEntity){
                return newEntity;
            }else{
                return null;
            }
        }else{

            if(this.chosenEntityFitnessManager.TotalFitness < this.tankEntityFitnessManager.TotalFitness
                    && this.tankEntityFitnessManager.lethalityOfEntity){
                this.chosenEntityFitnessManager = this.tankEntityFitnessManager;
            }
            if(this.chosenEntityFitnessManager.TotalFitness < this.smallBoxEntityFitnessManager.TotalFitness
                    && this.smallBoxEntityFitnessManager.lethalityOfEntity){
                this.chosenEntityFitnessManager = this.smallBoxEntityFitnessManager;
            }

            //return the entity based on the chosen entity manager type
            //if lethal return the entity, if not then return null
            if(this.chosenEntityFitnessManager.lethalityOfEntity){
                if(this.chosenEntityFitnessManager == this.tankEntityFitnessManager){
                    if(oldEntity instanceof Tank){
                        return oldEntity;
                    }else {
                        return newEntity;
                    }
                }else
                if(this.chosenEntityFitnessManager == this.smallBoxEntityFitnessManager){
                    if(oldEntity instanceof SmallBox){
                        return oldEntity;
                    }else {
                        return newEntity;
                    }
                }else{
                    if(oldEntity instanceof Tank){
                        return oldEntity;
                    }else {
                        return newEntity;
                    }
                }
            }else{
                return null;
            }
            //end of returning the entity

        }

    }
    /**
     * enables the lethality of the entity manager based on the entity that produced the harm.
     * @param entity
     * the entity that caused the harm.
     */
    public void enableLethality(Entity entity){

        if(entity instanceof Tank){
            if(!this.tankEntityFitnessManager.lethalityOfEntity){
                this.tankEntityFitnessManager.lethalityOfEntity = true;
            }
        }else
        if(entity instanceof SmallBox){
            if(!this.smallBoxEntityFitnessManager.lethalityOfEntity){
                this.smallBoxEntityFitnessManager.lethalityOfEntity = true;
            }
        }

    }
    //end of getters


    //search methods
    /**
     * find a point with the highest fitness.
     * it also takes into consideration which side the point is at (behind the tank or infornt of it).
     * the further behind the lower fitness it has.
     * @param targetTankPos
     * the position of the targeted tank.
     * @param tankDirection
     * direction of the targeted tank entity.
     * @param own
     * own tank reference.
     */
    public PointProperties findHighFitness(Vector2 targetTankPos, float tankDirection, Tank own){

        //initialize the target point by referring it to the first point
        this.targetPoint = chosenEntityFitnessManager.EntityAttackPoints.get(0);

        int lenghth = chosenEntityFitnessManager.EntityAttackPoints.size();
        for(int i = 0; i < lenghth; i++){

            //update the coordinates of points and the subfitness
            chosenEntityFitnessManager.EntityAttackPoints.get(i).calculateCoordinates(targetTankPos, tankDirection);
            chosenEntityFitnessManager.EntityAttackPoints.get(i).recalculateOverallFitness();
            chosenEntityFitnessManager.EntityAttackPoints.get(i).addOrSubFitnessVisibility(targetTankPos,
                    own.getPosition(),
                    TankMode.getAttackTankScope(this.tankModeType));

            if(!GeneralMethods.isThereCollisionTank(chosenEntityFitnessManager.EntityAttackPoints.get(i).coordinates, targetTankPos)){
                if( (int)(this.targetPoint.overAllFitness - this.targetPoint.subFitness) <
                        (int)(chosenEntityFitnessManager.EntityAttackPoints.get(i).overAllFitness - chosenEntityFitnessManager.EntityAttackPoints.get(i).subFitness) ){
                    this.targetPoint = chosenEntityFitnessManager.EntityAttackPoints.get(i);
                }
            }

        }

        return this.targetPoint;
    }
    /**
     * finds the best weapon to use for this attack point.
     * takes into consideration each weapon fitness and prioritize based on fitness.
     * add the sum of the fitness of all the weapons and calculates a percentage for each weapon used.
     * all the number of bullets shot should equal to 10 bullets in each round the weapon changes.
     * @param weaponManger
     * takes the weapon manager to check if there are enough bullets of the chosen type.
     * @param attackPoint
     * the point in which the weapon will be chosen.
     */
    public void findHighFitnessWeaponType(EntitysWeaponManger weaponManger, PointProperties attackPoint){

        //max bullets shot per round is 10
        if(weaponManger.hasEnoughBullets()){

            //update the weapon selector
            weaponSelecter.updateFitness(weaponManger, attackPoint.normalBulletFitness,
                                                        attackPoint.electricBulletFitness);

            //choose the best suited weapon
            weaponSelecter.setPreferedWeapon(weaponManger);

        }else{
            weaponManger.switchToNextType();
        }

    }
    //end of search methods


}
