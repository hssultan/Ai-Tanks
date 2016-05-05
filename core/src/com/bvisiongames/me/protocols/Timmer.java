package com.bvisiongames.me.protocols;

import com.badlogic.gdx.Gdx;

/**
 * Created by ahzji_000 on 2/13/2016.
 */
public class Timmer {

    //time out millisecond
    private int timeOut = 0;

    //frame counter
    private int frameCount = 0;

    //time in nano second
    private long nanSec;

    /**
     * initiator
     * @param milliSecond
     */
    public Timmer(int milliSecond){

        this.timeOut = milliSecond;

    }

    /**
     * start
     */
    public void start(){

        this.nanSec = System.nanoTime();

    }

    /**
     * end
     */
    public void end(float delta){

        if( frameCount <= ( ((float)timeOut/delta) )/1000 ){
            frameCount++;
        }else{
            //end of timeout
            //reset frame count
            frameCount = 0;

            //do things here below
            Gdx.app.log("s","time it took (micro sec): "+( (System.nanoTime() - this.nanSec)/1000 ));

        }

    }

}
