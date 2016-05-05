package com.bvisiongames.me.intelligence;

import com.badlogic.gdx.Gdx;
import com.bvisiongames.me.Maps.MapManager;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.entity.Entity;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.settings.TankMode;

/**
 * Created by ahzji_000 on 12/27/2015.
 * this class saves the data for each level's adaptive intelligence.
 */
public class ArtificialMemoryManager {

    public TankIntelliDataAnalyzer tankIntelliDataAnalyzer;

    //setters

    //end of setters


    //getters
    /**
     * this method loads data based on the level.
     * and returns an instance of memory data for the type of tank.
     * @param level
     * level of the game.
     * @param tankType
     * the memory for the tank type.
     */
    public TankIntelliDataAnalyzer loadData(MapManager.MAPTYPE level, Tank.TANKSTYPES tankType,
                                                TankMode.TankModeType tankModeType, Entity entity, //TankIntelligi.ExternalSensors.EmptyCircleAreaSensor spinner
                                                NearbyAreaScanner scanner
    ){
        this.tankIntelliDataAnalyzer = new TankIntelliDataAnalyzer(level, tankType, tankModeType, entity, //spinner
                                                                    scanner);
        return this.tankIntelliDataAnalyzer;
    }
    //end of getters


    /**
     * this class holds the information in the memory for the intelli.
     * analyze the data and do some calculations to set a best way of attack.
     */
    public class TankIntelliDataAnalyzer{

        //data point manager instance for the initial point to attack the enemy tank.
        public IntelliDataPointsManager intelliDataPointsManager;

        //maptype and tank type instances and tank mode(easy, medium or hard)
        public MapManager.MAPTYPE maptype;
        public Tank.TANKSTYPES tankstypes;
        public TankMode.TankModeType tankModeType;

        //entity to be controlled and used by the route planner
        public Entity entity;
        //spinner to be used by the route planner
        //public TankIntelligi.ExternalSensors.EmptyCircleAreaSensor spinner;
        //Nearby area scanner
        public NearbyAreaScanner scanner;

        /**
         * initiator that sets the initial stuff for this tank.
         * @param maptype
         * the map level type for this tank entity.
         * @param tankType
         * the tank type entity.
         */
        public TankIntelliDataAnalyzer(MapManager.MAPTYPE maptype, Tank.TANKSTYPES tankType,
                                                    TankMode.TankModeType tankModeType,
                                                    Entity entity, //TankIntelligi.ExternalSensors.EmptyCircleAreaSensor spinner
                                                    NearbyAreaScanner scanner
        ){

            //initiate the maptype and tank type and tank mode
            this.maptype = maptype;
            this.tankstypes = tankType;
            this.tankModeType = tankModeType;

            //set the spinner and the entity
            this.entity = entity;
            this.scanner = scanner;

            //save the data from the files into the memory so it can be retrieved faster.
            this.intelliDataPointsManager = Tanks.gameState.intelligence.getInitAttckProb(maptype, tankType, tankModeType);
            //recalibrate the points of the intelli point manager
            this.recalibrateIntelliDataManager();

            //set the spinner and the entity to be routed of the route planner
            //this.intelliDataPointsManager.routePlanner.spinner = spinner;
            this.intelliDataPointsManager.routePlanner.setScanner(scanner);
            this.intelliDataPointsManager.routePlanner.setEntity(entity);
            this.intelliDataPointsManager.routePlanner.initiateObjects();

        }

        //setters
        /**
         * recalibrate the intellidata point manager points if it is empty.
         */
        private void recalibrateIntelliDataManager(){

            //if the points has not been set then set them
            if(this.intelliDataPointsManager == null){
                Gdx.app.log("s","it is null");
                Gdx.app.log("s","setting up a data point manager...");
                this.intelliDataPointsManager = new IntelliDataPointsManager();
                this.intelliDataPointsManager.setMaptype(this.maptype);
                this.intelliDataPointsManager.setTankModeType(this.tankModeType);
                this.intelliDataPointsManager.setTankstypes(this.tankstypes);
                this.intelliDataPointsManager.initializePoints();
                Gdx.app.log("s", "done setting up points!");
                Gdx.app.log("s", "saving the new data points...");
                Tanks.gameState.intelligence.setInitAttckProb(this.maptype, this.tankstypes,
                                                                this.tankModeType,
                                                                this.intelliDataPointsManager);
                Tanks.gameState.intelligence.saveData();
                Gdx.app.log("s","saved!");

            }else{
                Gdx.app.log("s","it is not null\n");
            }

        }
        //end of setters


        //getters
        /**
         * gets the total fitness of this AI.
         * it is the sum of total fitnesses of all enemy entities over the number of entities that can be targeted.
         */
        public int getFitness(){
            intelliDataPointsManager.tankEntityFitnessManager.calculateTotalFitness();
            intelliDataPointsManager.smallBoxEntityFitnessManager.calculateTotalFitness();
            return (intelliDataPointsManager.tankEntityFitnessManager.TotalFitness
                    +intelliDataPointsManager.smallBoxEntityFitnessManager.TotalFitness)
                    /2;
        }
        //end of getters

    }

}
