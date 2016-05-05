package com.bvisiongames.me.protocols;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bvisiongames.me.entity.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahzji_000 on 2/13/2016.
 */
public class ObjectUpdate extends Thread implements Runnable {


    //start of boolean variables
    public boolean status = false; //the status of this thread
    private boolean paused = false; //status of pause of this thread
    private boolean synced = false; //syncing variable with other threads
    public boolean isDoneShuttingDown = false; //save the state of when this thread is done shutting down
    //end of boolean variables

    //entities
    public List<Entity> entities = new ArrayList<Entity>();

    /**
     * initiator.
     */
    public ObjectUpdate(){

        //make the thread run
        this.status = true;

    }

    //start of getters
    /**
     * check whether this thread's methods are synced with some methods in another thread.
     */
    public boolean isSynced(){
        return this.synced;
    }
    /**
     * checks whether this thread is paused.
     */
    public boolean isPaused(){
        return this.paused;
    }
    //end of getters

    //start of setters
    /**
     * add entity to the game.
     * @param entity
     */
    public void addEntity(Entity entity){
        this.entities.add(entity);
    }
    /**
     * pause the methods of this thread.
     */
    public void Pause(){
        this.paused = true;
    }
    /**
     * resumes the methods of this thread.
     */
    public void Resume(){
        this.paused = false;
    }
    /**
     * shutdown this thread.
     */
    public void shutdown(){
        this.status = false;
        this.isDoneShuttingDown = false;
    }
    /**
     * sync that is called in the other threads to sync some methods here.
     */
    public void sync(){
        this.synced = true;
    }
    /**
     * reset the synced after some methods has been called and are synced with outside thread.
     * it is called after the methods that are being synced inside this thread.
     */
    public void desync(){
        this.synced = false;
    }
    //end of setters

    @Override
    public void run() {

        while (this.status){

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //if synced
            if(this.isSynced()){

                if(!this.isPaused()){

                    //update the entities
                    int length = entities.size();
                    for(int i = 0; i < length; i++){
                        if(entities.get(i).status){
                            entities.get(i).update();
                        }
                    }
                    //end of entities update

                }

                //desync this thread
                this.desync();
            }
            //end of synced

        }

    }

    /**
     * render entities.
     * @param batch
     */
    public void drawEntities(SpriteBatch batch){

        int length = this.entities.size();
        for (int i = 0; i < length; i++){
            if(this.entities.get(i).status){
                this.entities.get(i).render(batch);
            }
        }

    }

    /**
     * dispose
     */
    public void dispose(){

        //call the shutdown
        this.shutdown();

        while (this.isDoneShuttingDown){
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
