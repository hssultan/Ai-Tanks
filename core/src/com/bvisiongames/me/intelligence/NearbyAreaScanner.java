package com.bvisiongames.me.intelligence;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.protocols.TankIntelligi;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.settings.TankMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahzji_000 on 2/3/2016.
 * this is a nearby scanner class that uses box2d and hashmap to store the 12 angles,
 * and rates these points based on scope distance.
 */
public class NearbyAreaScanner {

    //entity to be controlled, the entity has to be a tank.
    public Tank entity;

    //array list that contains the list of angle points around the entity
    public List<ScannersPoint> arrayPoints = new ArrayList<ScannersPoint>(12);

    //size ratio of max radius
    public float ratio = 0.6f;

    //lowest and highest energy demanding points
    public ScannersPoint lowestEnergyPoint, highestEnergyPoint;

    //tmp point
    private ScannersPoint tmpPoint;

    //saves the collision state
    private boolean hasCollided = false;
    //report collision
    private RayCastCallback rayCastCallback = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if( fixture != null
                    && !(fixture.getUserData() != null && fixture.getUserData() instanceof TankIntelligi.NEARBYSENSOR)
                    && !(fixture.getUserData() != null && fixture.getUserData() instanceof TankIntelligi.EXTERNALSENSOR)
                    && !(fixture.getBody().getUserData() != null && fixture.getBody().getUserData() == entity) ){
                hasCollided = true;
            }else{
                return -1;
            }
            return 0;
        }
    };

    /**
     * initiator.
     * @param entity
     * the entity to be controlled.
     */
    public NearbyAreaScanner(Tank entity){

        //initiate the entity
        this.entity = entity;

        //set tmp point
        this.tmpPoint = new ScannersPoint(this, 0, entity);

        //setup the points in array list
        float a = 0;
        for(int i = 0; i < 12; i++, a+= ConstantVariables.FloatPI / 6) {
            this.arrayPoints.add(new ScannersPoint(this, a, entity));
        }

    }


    //setters
    /**
     * scope around and set free positions for the points around the entity.
     * @param closePatrolPoint
     * a close patrol point.
     */
    public void scopeFreeAreaPoints(Vector2 closePatrolPoint){

        //loop through all points
        int size = this.arrayPoints.size();
        for(int i = 0; i < size; i++){

            //the instance of the point
            ScannersPoint Point = this.arrayPoints.get(i);
            Point.isAccessible = true;
            //starting ratio
            float Ratio = ratio;

            while ( Point.isAccessible && ( isThereCollision(Point.coordinates, entity.getPosition())
                                            || isFreeToMovePoint(Point, Ratio) )  ){

                if(Ratio > 0.25f){
                    //decrement the Ratio
                    Ratio -= 0.05f;
                }else{
                    //if Ratio went below 0.2 then make the point inaccessible
                    Point.isAccessible = false;
                }

            }

            //update the coordinates
            Point.updateCoordinates(entity, TankMode.getTankScope(TankMode.TankModeType.EASY_MODE)*Ratio);
            //update the total energy
            Point.updateTotalEnergy(closePatrolPoint, TankMode.getTankScope(TankMode.TankModeType.EASY_MODE)*Ratio);

            //set the highest energy point
            if(this.highestEnergyPoint == null){
                this.highestEnergyPoint = Point;
            }
            //set lowest energy point
            if(this.lowestEnergyPoint == null){
                this.lowestEnergyPoint = Point;
            }

        }

        //loop through all points again to get the lowest energy and highest energy
        for(int i = 0; i < size; i++ ){

            ScannersPoint Point = this.arrayPoints.get(i);
            Point.updateTotalEnergy(null, Point.coordinates.dst(entity.getPosition()));

            if(Point.isAccessible && lowestEnergyPoint.totalEnergyRequired > Point.totalEnergyRequired){
                this.lowestEnergyPoint = Point;
            }
            if(Point.isAccessible && highestEnergyPoint.totalEnergyRequired < Point.totalEnergyRequired){
                this.highestEnergyPoint = Point;
            }

        }

    }
    //end of setters


    //getters
    /**
     * this checks if there is a collision between points, but excludes sensors and the main entity.
     */
    private boolean isThereCollision(Vector2 point1, Vector2 point2){

        hasCollided = false;

        //check the direction from point1 to point2
        MultiGameScreen.WORLD.rayCast(rayCastCallback, point1, point2);

        return hasCollided;
    }
    /**
     * this checks whether the point provided has free surrounding area.
     * @param point
     * @param startingRatio
     */
    private boolean isFreeToMovePoint(ScannersPoint point, float startingRatio){

        hasCollided = false;

        //first copy the point to tmppoint
        this.tmpPoint.cpy(point);

        //stretch far the point a little
        this.tmpPoint.updateCoordinates(entity, TankMode.getTankScope(TankMode.TankModeType.EASY_MODE)*(startingRatio + 0.1f));

        //test the tmp point
        MultiGameScreen.WORLD.rayCast(rayCastCallback, this.tmpPoint.coordinates, entity.getPosition());

        return hasCollided;
    }
    //end of getters


    //update the points
    /**
     * reset the points to their original position.
     */
    public void resetPoints(){

        int size = this.arrayPoints.size();
        for(int i = 0; i < size; i++ ){
            this.arrayPoints.get(i).isAccessible = true;
            //add the point
            this.arrayPoints.get(i).updateCoordinates(this.entity, TankMode.getTankScope(TankMode.TankModeType.EASY_MODE)*ratio );
            this.arrayPoints.get(i).updateTotalEnergy(null, TankMode.getTankScope(TankMode.TankModeType.EASY_MODE)*ratio );
        }

    }
    //end of point update


}
