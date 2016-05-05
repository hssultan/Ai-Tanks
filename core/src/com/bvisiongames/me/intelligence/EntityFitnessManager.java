package com.bvisiongames.me.intelligence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahzji_000 on 2/1/2016.
 */
public class EntityFitnessManager {


    //points that holds probability of fitness for bullets to be fired from at the target entity.
    public List<PointProperties> EntityAttackPoints = new ArrayList<PointProperties>();
    //this tells whether this entity is lethal or not.
    public boolean lethalityOfEntity = false;
    //Overall Fitness toward this entity
    public int TotalFitness = 0;
    //sum of all fitness in all points
    public int SumFitness = 0;

    /**
     * initiator.
     */
    public EntityFitnessManager(){}


    //setters
    /**
     * calculate the total fitness by getting the average fitness of all point Properties.
     */
    public void calculateTotalFitness(){

        int length = EntityAttackPoints.size();
        for(int i = 0; i < length; i++){

            //update the overall fitness for the individual point
            EntityAttackPoints.get(i).calculateOverallFitness();

            //then add it to the sum fitness
            this.SumFitness += EntityAttackPoints.get(i).overAllFitness;

        }

        //then divide the sum by the length
        this.TotalFitness = this.SumFitness/length;

    }
    //end of setters


    //getters

    //end of getters


}
