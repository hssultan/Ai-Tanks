package com.bvisiongames.me.Maps;

/**
 * Created by ahzji_000 on 12/10/2015.
 */
public class MapsInfo {

    /**
     * this method returns the total price of the map level.
     * @param maptype
     * takes in the maptype
     */
    public static int getTotalPrice(MapManager.MAPTYPE maptype){

        switch (maptype){
            case LEVEL1:
                return 0;
            default:
                return 0;
        }

    }

    /**
     * this method returns the number of tanks in the map level.
     * @param maptype
     * takes in the maptype
     */
    public static int getTotalTanks(MapManager.MAPTYPE maptype){

        switch (maptype){
            case LEVEL1:
                return 1;
            default:
                return 0;
        }

    }

    /**
     * this method returns the number of easy mode tanks
     * @param maptype
     * takes in the maptype
     */
    public static int getTotalEasyModeTanks(MapManager.MAPTYPE maptype){
        switch (maptype){
            case LEVEL1:
                return 1;
            default:
                return 0;
        }
    }
    /**
     * this method returns the number of medium mode tanks
     * @param maptype
     * takes in the maptype
     */
    public static int getTotalMediumModeTanks(MapManager.MAPTYPE maptype){
        switch (maptype){
            case LEVEL1:
                return 0;
            default:
                return 0;
        }
    }
    /**
     * this method returns the number of hard mode tanks
     * @param maptype
     * takes in the maptype
     */
    public static int getTotalHardModeTanks(MapManager.MAPTYPE maptype){
        switch (maptype){
            case LEVEL1:
                return 0;
            default:
                return 0;
        }
    }

}
