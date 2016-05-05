package com.bvisiongames.me.settings;

/**
 * Created by ahzji_000 on 12/2/2015.
 * This class controls the number of coins in the game.
 */
public class JarCoins {

    /**
     * COIN INFO.
     */
    public static final float PRICE = 1.99f;
    public static final String INFO = "Buy coins";

    /**
     * number of coins in the game
     */
    private int coins = 0;

    /**
     * initiator that gets the number of coins the user has saved in his/her session.
     */
    public JarCoins(int startingCoins){

        //get the number of coins in the saved session
        this.coins = startingCoins;

    }

    //getters
    /**
     * gets the number of coins.
     * @return coins
     */
    public int getCoins(){
        return coins;
    }
    //end of getters

    //setters
    /**
     * adds coins to the total coins.
     * @param coins
     * coins to be added.
     */
    public void addCoins(int coins){
        this.coins += coins;
    }
    /**
     * sets the number of coins in the game.
     * @param coins
     * takes in the number of coins to set the coins to.
     */
    public void setCoins(int coins){
        this.coins = coins;
    }
    //end of setters

    //methods that checks
    /**
     * This method checks whether the user has enough coins to purchase the selected item with it's price coins.
     * @param coins
     * takes in the price coin.
     * @return boolean
     * returns if the user has enough coins
     */
    public boolean hasEnough(int coins){
        if(this.coins >= coins){
            return true;
        }
        return false;
    }
    //end of checks methods

}
