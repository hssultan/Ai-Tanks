package com.bvisiongames.me.intelligence;

import com.bvisiongames.me.Weapons.Weapon;
import com.bvisiongames.me.Weapons.EntitysWeaponManger;

/**
 * Created by ahzji_000 on 1/28/2016.
 * the updateFitness is called first before any other method.
 */
public class WeaponSelecter {

    //this variable keeps track of the state of the round
    private boolean isRoundDone = true;

    //these variables keeps track of the fitness for the current round
    public float bullet1Fitness = 0,
                electricFitness = 0;

    //these variables keeps track of shot bullets
    public int totalBullet1Shot = 0,
                totalElectricShot = 0;

    //this keeps track of how many weapons are valid
    public int validWeapons = 0;

    //this calculates the max allowed total bullets shot per round
    public int maxTotalBulletsRound = 0;

    //# of rounds
    public long rounds = 0;

    //this array keeps track of all availability of weapons
    //false means the state of the specific weapon is not valid.
    private boolean[] weaponsState = new boolean[]{
            true, true
    };

    //weapon manager
    private EntitysWeaponManger weaponManager;


    //getters

    //end of getters


    //setters
    /**
     * set the weapon manager to the high priority weapon
     */
    public void setPreferedWeapon(EntitysWeaponManger weaponManager){

        //check bullet1
        if(this.weaponsState[0]
                && this.weaponManager.bullet1 != null && this.weaponManager.bullet1.hasBullets()
                && (bullet1Fitness*this.maxTotalBulletsRound)/(bullet1Fitness + electricFitness) > totalBullet1Shot ){
            weaponManager.setBulletstype(Weapon.BULLETSTYPE.BULLET1);
        }else
        if (this.weaponsState[0]
                        && this.weaponManager.bullet1 != null && this.weaponManager.bullet1.hasBullets()
                        && (bullet1Fitness*this.maxTotalBulletsRound)/(bullet1Fitness + electricFitness) <= totalBullet1Shot){
            this.weaponsState[0] = false;
        }else
        //check electric
        if(this.weaponsState[1]
                && this.weaponManager.electricBullet != null && this.weaponManager.electricBullet.hasBullets()
                && (electricFitness*this.maxTotalBulletsRound)/(bullet1Fitness + electricFitness) > totalElectricShot ){
            weaponManager.setBulletstype(Weapon.BULLETSTYPE.ELECTRICBULLET);
        }else
        if (this.weaponsState[1]
                && this.weaponManager.electricBullet != null && this.weaponManager.electricBullet.hasBullets()
                && (electricFitness*this.maxTotalBulletsRound)/(bullet1Fitness + electricFitness) <= totalElectricShot){
            this.weaponsState[1] = false;
        }else{
            isRoundDone = true;
        }

    }
    /**
     * this method updates the total fitness for each weapon and keeps track of the round lifecycle.
     * this method should be called before the checkPriority method.
     * @param weaponManager
     * @param bullet1Fitness
     * bullet fitness.
     * @param electricFitness
     * electric fitness.
     */
    public void updateFitness(EntitysWeaponManger weaponManager, float bullet1Fitness, float electricFitness){

        if(isRoundDone){

            //reset the total bullets shot
            this.totalBullet1Shot = 0;
            this.totalElectricShot = 0;

            //reset the state of weapons
            this.weaponsState[0] = true;
            this.weaponsState[1] = true;

            //set up the weapon manager
            if(this.weaponManager == null){
                this.weaponManager = weaponManager;
            }
            //update the total valid weapon types
            validWeapons = weaponManager.getTotalWeaponTypes();
            //update the fitness for each weapon used
            this.bullet1Fitness = bullet1Fitness;
            this.electricFitness = electricFitness;

            //calculate the max total bullets that can be shot in this round
            this.maxTotalBulletsRound = (int)( - ( (20 - (float)validWeapons)/ Math.exp(Math.sqrt(0.02f*rounds)) ) + 20);

            //count the rounds
            this.rounds++;

            //lock this round
            this.isRoundDone = false;

        }

    }
    /**
     * resets this class.
     */
    public void reset(){
        this.totalElectricShot = 0;
        this.totalBullet1Shot = 0;
        this.isRoundDone = true;
    }
    //end of setters


    //listeners
    /**
     * gets notified of the bullet that was shot.
     * @param bulletType
     */
    public void notifyShot(Weapon.BULLETSTYPE bulletType){

        switch (bulletType){
            case BULLET1:
                totalBullet1Shot++;
                break;
            case ELECTRICBULLET:
                totalElectricShot++;
                break;
        }

    }
    //end of listeners

}
