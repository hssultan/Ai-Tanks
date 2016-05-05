package com.bvisiongames.me.rewards;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahzji_000 on 12/17/2015.
 */
public class CoinRewardManager {

    private List<CoinReward> coins = new ArrayList<CoinReward>();

    //setters
    /**
     * add coins.
     * @param coin
     */
    public void addCoin(CoinReward coin){
        this.coins.add(coin);
    }
    /**
     * remove a coin and flag it to be disposed.
     * @param coin
     */
    public void removeCoin(CoinReward coin){
        coin.dispose();
        this.coins.remove(coin);
    }
    /**
     * dispose this class.
     */
    public void dispose(){
        for(int i = 0; i < this.coins.size(); i++){
            this.coins.get(i).dispose();
        }
        this.coins.clear();
    }
    //end of setters


    //getters
    /**
     * get the coins list.
     */
    public List<CoinReward> getCoins(){
        return this.coins;
    }
    //end of getters


    //update the coins
    /**
     * tmp variables that has been allocated outside the method to lower gc activity.
     */
    private int i = 0;
    public void update(){

        for (i = 0; i < this.coins.size(); i++){
            if(this.coins.get(i).isAlive()){
                this.coins.get(i).update();
            }else{
                this.removeCoin(this.coins.get(i));
            }
        }

    }

}
