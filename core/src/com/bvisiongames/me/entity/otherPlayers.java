package com.bvisiongames.me.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sultan on 8/4/2015.
 */
public class otherPlayers {

    public List<Tank> tanks = new ArrayList<Tank>();

    public otherPlayers(){}

    /**
     * add a cpu and returns the tank instance of that cpu.
     * @param initPos
     * @param angle
     * @param tankstype
     * @param id
     */
    public Tank AddPlayer(Vector2 initPos, float angle, Tank.TANKSTYPES tankstype, String id){

        Tank setVarIndividual = new Tank(id, initPos, angle , tankstype, Tank.TANKPILOT.CPU);
        this.tanks.add(setVarIndividual);

        return setVarIndividual;
    }
    public Tank getTank(float clientId){
        int len = this.tanks.size();
        for (int i = 0; i < len; i++){
            Tank individual = this.tanks.get(i);
            if(individual.tankProperties.id.equals(Float.toString(clientId))){
                return individual;
            }
        }
        return null;
    }

    public void update(){

        int len = this.tanks.size();
        for (int i = 0; i < len; i++){
            Tank individual = this.tanks.get(i);
            if(individual.getTankState()){
                individual.ai_update();
            }else{
                individual.resurect();
            }
        }

    }
    public void render(SpriteBatch spriteBatch){

        int len = this.tanks.size();
        for (int i = 0; i < len; i++){
            Tank individual = this.tanks.get(i);
            if(individual != null){
                individual.render(spriteBatch);
            }
        }

    }

    public void pause(){

        int len = this.tanks.size();
        for (int i = 0; i < len; i++){
            Tank individual = this.tanks.get(i);
            if(individual != null){
                individual.pause();
            }
        }

    }
    public void resume(){

        int len = this.tanks.size();
        for (int i = 0; i < len; i++){
            Tank individual = this.tanks.get(i);
            if(individual != null){
                individual.resume();
            }
        }

    }

    public void dispose(){

        int len = this.tanks.size();
        for (int i = 0; i < len; i++){
            Tank individual = this.tanks.get(i);
            if(individual != null){
                individual.getArtificialInt().shutdown();
                while (!individual.getArtificialInt().isDoneShuttingDown){
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //clear the list
        this.tanks.clear();

    }

}
