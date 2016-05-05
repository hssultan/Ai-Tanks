package com.bvisiongames.me.settings;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.bvisiongames.me.JSON.JSONArray;
import com.bvisiongames.me.Maps.MapManager;
import com.bvisiongames.me.entity.Tank;

/**
 * Created by ahzji_000 on 12/2/2015.
 * this class takes the information from the saved information in the pref.
 */
public class GameState {

    /**
     * instance of preference variable
     */
    private Preferences preferences;

    /**
     * instance for a classes that takes care of the bullets.
     */
    public Weapons weapons = new Weapons();
    /**
     * instance for a class that takes care of the maps.
     */
    public Maps maps = new Maps();

    /**
     * instance for a class that takes care of the intelligence.
     */
    public Intelligence intelligence = new Intelligence();

    /**
     * initator that takes in the preference to the information of the sessions.
     * @param preferences
     * preference variable
     */
    public GameState(Preferences preferences){
        this.preferences = preferences;
    }


    //getters
    /**
     * this method returns the number of coins.
     */
    public int getTotalCoins(){
        return this.preferences.getInteger("coins", 0);
    }
    /**
     * This method returns the user ID
     * @return userID
     */
    public int getUserID(){
        return this.preferences.getInteger("id", 0);
    }
    /**
     * this method returns the user name
     * @return userName
     */
    public String getUserName(){
        return this.preferences.getString("name", "Main Player");
    }
    /**
     * this method returns the user tank type
     */
    public String getUserTankType(){
        return this.preferences.getString("tankType", Tank.TANKSTYPES.E100.toString());
    }
    /**
     * gets the total number of kills
     */
    public int getKills(){
        return this.preferences.getInteger("score", 0);
    }
    /**
     * gets the total number of deaths
     */
    public int getDeaths(){
        return this.preferences.getInteger("deaths", 0);
    }
    /**
     * gets the CPU players JSON string.
     */
    public String getPlayers(){
        return this.preferences.getString("players","[]");
    }
    /**
     * gets the sounds state.
     * true: with sound
     * false: mute
     */
    public boolean getSoundState(){
        return this.preferences.getBoolean("mute", false);
    }
    /**
     * gets the sound level
     */
    public int getSoundLevel(){
        return this.preferences.getInteger("sound", 5);
    }
    //end of getters


    //setters
    /**
     * sets the total coins
     * @param coins
     * coins to be set to.
     */
    public void setCoins(int coins){
        this.preferences.putInteger("coins", coins);
        this.preferences.flush();
    }
    /**
     * sets the players JSON string.
     * @param players
     * takes in a players json string.
     */
    public void setPlayers(String players){
        this.preferences.putString("players", players);
        this.preferences.flush();
    }
    /**
     * sets the main player user name
     * @param name
     * takes in the name
     */
    public void setUserName(String name){
        this.preferences.putString("name", name);
        this.preferences.flush();
    }
    /**
     * sets the total number of kills
     * @param kills
     * total number of kills
     */
    public void setKills(int kills){
        this.preferences.putInteger("score", kills);
        this.preferences.flush();
    }
    /**
     * sets the total number of deaths
     * @param deaths
     * total number of deaths
     */
    public void setDeaths(int deaths){
        this.preferences.putInteger("deaths", deaths);
        this.preferences.flush();
    }
    /**
     * sets the sound state.
     * @param sound
     * true: with sound
     * false: mute
     */
    public void setSound(boolean sound){
        this.preferences.putBoolean("mute", sound);
        this.preferences.flush();
    }
    /**
     * sets the sound level
     * @param sound
     * integer sound level
     */
    public void setSoundLevel(int sound){
        this.preferences.putInteger("sound", sound);
        this.preferences.flush();
    }
    /**
     * sets the tank type.
     * @param tanktype
     * tank type in string format
     */
    public void setTankType(String tanktype){
        this.preferences.putString("tankType", tanktype);
        this.preferences.flush();
    }
    //end of setters

    /**
     * class that contains the bullets' methods
     */
    public class Weapons{

        //getters
        /**
         * gets the status of normal bullet weapon
         */
        public boolean getNormalBulletState(){
            return preferences.getBoolean("bullet_normal_state", false);
        }
        /**
         * gets the status of electric bullet weapon
         */
        public boolean getElectricBulletState(){
            return preferences.getBoolean("bullet_electric_state", false);
        }
        /**
         * gets the total number of normal bullets
         */
        public int getNormaBulletTotal(){
            return preferences.getInteger("bullet_normal_total", -1);
        }
        /**
         * gets the total number of electric bullets
         */
        public int getElectricBulletTotal(){
            return preferences.getInteger("bullet_electric_total", 0);
        }
        //end of getters

        //setters
        /**
         * sets the total number of normal bullets saved
         * @param number
         * number of bullets for the normal bullet type
         */
        public void setNormaBulletTotal(int number) {
            preferences.putInteger("bullet_normal_total", number);
            preferences.flush();
        }
        /**
         * sets the total number of electric bullets saved
         * @param number
         * number of bullets for the electric bullet type
         */
        public void setElectricBulletTotal(int number){
            preferences.putInteger("bullet_electric_total", number);
            preferences.flush();
        }
        /**
         * sets the state of the normal bullet
         * @param state
         * state: true->chosen, false->not chosen
         */
        public void setNormalBulletState(boolean state){
            preferences.putBoolean("bullet_normal_state", state);
            preferences.flush();
        }
        /**
         * sets the state of the electric bullet
         * @param state
         * state: true->chosen, false->not chosen
         */
        public void setElectricBulletState(boolean state){
            preferences.putBoolean("bullet_electric_state", state);
            preferences.flush();
        }
        //end of setters

    }

    /**
     * class that contains the maps' methods
     */
    public class Maps{

        //setters
        /**
         * sets the level (Map Type) that is chosen.
         * @param level
         * the chosen level (Map Type)
         */
        public void setLevel(MapManager.MAPTYPE level){
            preferences.putString("chosen_map", level.toString());
            preferences.flush();
        }
        /**
         * set the level (Map Type) to purchased.
         * @param level
         * the level to be purchased.
         */
        public void setMapTypPurchased(MapManager.MAPTYPE level){

            if(isPurchased(level)){
                return;
            }

            JSONArray holder = new JSONArray(preferences.getString("purchased_maps","[]"));
            holder.put(level.toString());

            preferences.putString("purchased_maps", holder.toString());
            preferences.flush();

        }
        //end of setters

        //getters
        /**
         * gets the chosen level (Map Type).
         * returns a maptype from the enum.
         */
        public MapManager.MAPTYPE getLevel(){
            return MapManager.MAPTYPE.valueOf(preferences.getString("chosen_map", MapManager.MAPTYPE.LEVEL1.toString()));
        }
        //end of getters

        //checkers
        /**
         * checks whether a map type is purchased.
         * @param level
         */
        public boolean isPurchased(MapManager.MAPTYPE level){

            JSONArray holder = new JSONArray(preferences.getString("purchased_maps", "[]"));
            for(int i = 0; i < holder.length(); i++){

                if(holder.get(i).equals(level.toString())){
                    return true;
                }

            }

            return false;
        }
        //end of checkers

    }

    /**
     * this class contains the intelligence's methods
     */
    public class Intelligence{

        //a json instance that converts from array to string and vice versa.
        private Json json = new Json();

        //setters
        /**
         * saves the initial attack points positioned by angle around the enemy tank.
         * a jsonarray is saved in string format.
         * @param level
         * the level that these data are saved in.
         * @param tankstypes
         * the data for the tank type.
         */
        public void setInitAttckProb(MapManager.MAPTYPE level, Tank.TANKSTYPES tankstypes,
                                     TankMode.TankModeType tankModeType,
                                     com.bvisiongames.me.intelligence.IntelliDataPointsManager data){
            preferences.putString("level_"+level.toString()+"_"+tankstypes.toString()+"_"+tankModeType.toString(), json.toJson(data));
        }
        /**
         * save data by flushing preference.
         */
        public void saveData(){
            preferences.flush();
        }
        //end of setters


        //getters
        /**
         * gets the data points for this particular level of the specified tank.
         * @param level
         * the level these data are in.
         * @param tankstypes
         * the tank types.
         */
        public com.bvisiongames.me.intelligence.IntelliDataPointsManager
                getInitAttckProb(MapManager.MAPTYPE level, Tank.TANKSTYPES tankstypes, TankMode.TankModeType tankModeType){
            return json.fromJson(com.bvisiongames.me.intelligence.IntelliDataPointsManager.class,
                                preferences.getString("level_"+level.toString()+"_"+tankstypes.toString()
                                        +"_"+tankModeType.toString()));
        }
        //end of getters

    }

}
