package com.bvisiongames.me.intelligence;

import com.badlogic.gdx.math.Vector2;
import com.bvisiongames.me.entity.Entity;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.settings.TankMode;

/**
 * class that creates a point angle that has vector2 as a point, and stores the energy required to move in the
 * point's direction.
 */
public class ScannersPoint {

    //scanner controlling this point
    private NearbyAreaScanner scanner;
    //vector direction
    public Vector2 direction;
    //tmp vector2
    private Vector2 tmpVec = new Vector2(0, 0);
    //coordinates
    public Vector2 coordinates = new Vector2(0, 0);

    //energy required by the tank entity to move in this point direction.
    public float totalEnergyRequired = 0;

    //state of this point is it accessible
    public boolean isAccessible = true;

    //tells

    /**
     * initiator.
     * @param angleDirection
     * the angle of the direction vector in radians.
     * @param entity
     * the relative entity.
     */
    public ScannersPoint(NearbyAreaScanner scanner, float angleDirection, Tank entity){

        //scanner
        this.scanner = scanner;

        //direction
        this.direction = new Vector2( (float)Math.cos(angleDirection),
                (float)Math.sin(angleDirection) );

        //set the coordinates relative to the entity position
        //first rescale the direction vector to the max scoping easy mode radius.
        this.tmpVec.set(this.direction);
        this.tmpVec.scl(TankMode.getTankScope(TankMode.TankModeType.EASY_MODE) * scanner.ratio);
        //solve for the coordinates using the relative body position of the entity
        this.coordinates = new Vector2( this.tmpVec.x + entity.getPosition().x,
                this.tmpVec.y + entity.getPosition().y );

        //calculate the total energy required to move in that direction
        this.totalEnergyRequired =
                //calculate the energy required for the rotation
                (ConstantVariables.MAX_ENERGY_NEARBY_SCANNER_POINT_ANGLE/ConstantVariables.FloatPI) * Math.abs(direction.angleRad(entity.getDirection()))
                        +
                        //calculate the energy required for the max distance to travel
                        ConstantVariables.MAX_ENERGY_NEARBY_SCANNER_POINT_DISTANCE
        ;

    }


    //setters
    /**
     * reset the point properties.
     * @param angleDirection
     * @param entity
     */
    public void resetPointProperties(float angleDirection, Entity entity){

        //direction
        if(this.direction == null){
            this.direction = new Vector2( (float)Math.cos(angleDirection), (float)Math.sin(angleDirection) );
        }else{
            this.direction.set((float)Math.cos(angleDirection), (float)Math.sin(angleDirection));
        }

        //set the coordinates relative to the entity position
        //first rescale the direction vector to the max scoping easy mode radius.
        this.tmpVec.set(this.direction);
        this.tmpVec.scl(TankMode.getTankScope(TankMode.TankModeType.EASY_MODE)* scanner.ratio);
        //solve for the coordinates using the relative body position of the entity
        if(this.coordinates == null){
            this.coordinates = new Vector2( this.tmpVec.x + entity.getPosition().x,
                    this.tmpVec.y + entity.getPosition().y );
        }else{
            this.coordinates.set(this.tmpVec.x + entity.getPosition().x, this.tmpVec.y + entity.getPosition().y);
        }

        //calculate the total energy required to move in that direction
        this.totalEnergyRequired =
                //calculate the energy required for the rotation
                (ConstantVariables.MAX_ENERGY_NEARBY_SCANNER_POINT_ANGLE/ConstantVariables.FloatPI) * Math.abs(direction.angleRad(entity.getDirection()))
                        +
                        //calculate the energy required for the max distance to travel
                        ConstantVariables.MAX_ENERGY_NEARBY_SCANNER_POINT_DISTANCE
        ;

    }
    /**
     * update the coordinates
     * @param entity
     * the relative entity
     * @param distance
     * the distance to be placed on from the relative entity position.
     */
    public void updateCoordinates(Tank entity, float distance){

        //update the coordinates
        //first rescale the direction vector to the max scoping easy mode radius.
        this.tmpVec.set(this.direction);
        this.tmpVec.setLength(distance);
        //solve for the coordinates using the relative body position of the entity
        this.coordinates.set( this.tmpVec.x + entity.getPosition().x,
                this.tmpVec.y + entity.getPosition().y );

    }
    /**
     * update the total energy for this point, it takes in the closest patrol point
     * prev set but if there is none then do not subtract from total energy.
     * @param closestPatrolPoint
     * the closest patrol point.
     * @param distance
     * travel distance from entity body position.
     */
    public void updateTotalEnergy(Vector2 closestPatrolPoint, float distance){

        //update the total energy required based on entity direction, travel distance

        //if there is close patrol point
        if(closestPatrolPoint != null){

            this.totalEnergyRequired =
                    //calculate the rotational energy
                    (ConstantVariables.MAX_ENERGY_NEARBY_SCANNER_POINT_ANGLE/ConstantVariables.FloatPI) * Math.abs(direction.angleRad(scanner.entity.getDirection()))
                            +
                    //calculate the distance energy
                    (ConstantVariables.MAX_ENERGY_NEARBY_SCANNER_POINT_DISTANCE/distance)*(TankMode.getTankScope(TankMode.TankModeType.EASY_MODE)* scanner.ratio)
                            +
                    //subtract energy between close and this point
                    (ConstantVariables.MAX_ENERGY_NEARBY_SCANNER_POINT_DISTANCE/closestPatrolPoint.dst(this.coordinates))*TankMode.getTankScope(TankMode.TankModeType.EASY_MODE)
            ;

        }else{
            //else if there is no close patrol point

            this.totalEnergyRequired =
                    //calculate the rotational energy
                    (ConstantVariables.MAX_ENERGY_NEARBY_SCANNER_POINT_ANGLE/ConstantVariables.FloatPI) * Math.abs(direction.angleRad(scanner.entity.getDirection()))
                            +
                    //calculate the distance energy
                    (ConstantVariables.MAX_ENERGY_NEARBY_SCANNER_POINT_DISTANCE/distance)*(TankMode.getTankScope(TankMode.TankModeType.EASY_MODE)* scanner.ratio)
            ;

        }

    }
    /**
     * this method copies this instance to the properties of the provided point.
     * @param point
     */
    public void cpy(ScannersPoint point){
        this.direction.set(point.direction);
        this.totalEnergyRequired = point.totalEnergyRequired;
        this.coordinates.set(point.coordinates);
        this.isAccessible = point.isAccessible;
    }
    //end of setters


    //getters

    //end of getters

}
