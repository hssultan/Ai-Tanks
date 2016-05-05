package com.bvisiongames.me.intelligence;

import com.badlogic.gdx.math.Vector2;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.settings.TankMode;

/**
 * Created by ahzji_000 on 2/2/2016.
 */
public class PatrolPoint {

    //coordinates
    public Vector2 coordinates;

    //total energy cost
    public float totalEnergyRequired = 0;

    //number of times finding an enemy tank entity at this spot
    public int timesFindingEntities = 0;

    //direction from entity to this point
    public Vector2 direction = new Vector2(0, 0);

    /**
     * initiator.
     * @param position
     */
    public PatrolPoint(Vector2 position){

        //set the coordinates
        this.coordinates = position;

    }


    //setters
    /**
     * calculate the total energy for this point, it takes in the closest patrol point
     * prev set but if there is none then do not subtract from total energy.
     * @param scanner
     * the entity to be controlled.
     */
    public void calculateTotalEnergy(NearbyAreaScanner scanner){

        //calculate the direction vector
        this.direction.set(
                this.coordinates.x - scanner.entity.getPosition().x,
                this.coordinates.y - scanner.entity.getPosition().y
        );

        //update the total energy required based on entity direction, travel distance

        this.totalEnergyRequired =
                //calculate the rotational energy
                ((ConstantVariables.MAX_ENERGY_NEARBY_SCANNER_POINT_ANGLE)/(float)Math.PI) * Math.abs(this.direction.angleRad(scanner.entity.getDirection()))
                        +
                //calculate the distance energy
                ((ConstantVariables.MAX_ENERGY_NEARBY_SCANNER_POINT_DISTANCE)/this.coordinates.dst(scanner.entity.getPosition()))
                        *TankMode.getTankScope(TankMode.TankModeType.EASY_MODE)
        ;

    }
    //end of setters

}
