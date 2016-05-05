package com.bvisiongames.me.settings;

import com.bvisiongames.me.entity.EntitManager;
import com.bvisiongames.me.entity.Tank;

/**
 * Created by ahzji_000 on 1/9/2016.
 * this class sets up the level tank properties. like total bullets number.
 */
public class TankBulletsProperty{

    //Total normal bullets------------------>
    //total normal bullets easy mode
    public int Total_Easy_E100_Normal_bullet = 0,
            Total_Easy_KV2_Normal_bullet = 0,
            Total_Easy_M6_Normal_bullet = 0,
            Total_Easy_PZKPFWIV_Normal_bullet = 0,
            Total_Easy_PZKPFWIVG_Normal_bullet = 0,
            Total_Easy_T34_Normal_bullet = 0,
            Total_Easy_VK3601H_Normal_bullet = 0;

    //total normal bullets medium mode
    public int Total_Medium_E100_Normal_bullet = 0,
            Total_Medium_KV2_Normal_bullet = 0,
            Total_Medium_M6_Normal_bullet = 0,
            Total_Medium_PZKPFWIV_Normal_bullet = 0,
            Total_Medium_PZKPFWIVG_Normal_bullet = 0,
            Total_Medium_T34_Normal_bullet = 0,
            Total_Medium_VK3601H_Normal_bullet = 0;

    //total normal bullets hard mode
    public int Total_Hard_E100_Normal_bullet = 0,
            Total_Hard_KV2_Normal_bullet = 0,
            Total_Hard_M6_Normal_bullet = 0,
            Total_Hard_PZKPFWIV_Normal_bullet = 0,
            Total_Hard_PZKPFWIVG_Normal_bullet = 0,
            Total_Hard_T34_Normal_bullet = 0,
            Total_Hard_VK3601H_Normal_bullet = 0;
    //End of Total normal bullets------------------>


    //Total electric bullets------------------>
    //total electric bullets easy mode
    public int Total_Easy_E100_Electric_bullet = 0,
            Total_Easy_KV2_Electric_bullet = 0,
            Total_Easy_M6_Electric_bullet = 0,
            Total_Easy_PZKPFWIV_Electric_bullet = 0,
            Total_Easy_PZKPFWIVG_Electric_bullet = 0,
            Total_Easy_T34_Electric_bullet = 0,
            Total_Easy_VK3601H_Electric_bullet = 0;

    //total electric bullets medium mode
    public int Total_Medium_E100_Electric_bullet = 0,
            Total_Medium_KV2_Electric_bullet = 0,
            Total_Medium_M6_Electric_bullet = 0,
            Total_Medium_PZKPFWIV_Electric_bullet = 0,
            Total_Medium_PZKPFWIVG_Electric_bullet = 0,
            Total_Medium_T34_Electric_bullet = 0,
            Total_Medium_VK3601H_Electric_bullet = 0;

    //total electric bullets hard mode
    public int Total_Hard_E100_Electric_bullet = 0,
            Total_Hard_KV2_Electric_bullet = 0,
            Total_Hard_M6_Electric_bullet = 0,
            Total_Hard_PZKPFWIV_Electric_bullet = 0,
            Total_Hard_PZKPFWIVG_Electric_bullet = 0,
            Total_Hard_T34_Electric_bullet = 0,
            Total_Hard_VK3601H_Electric_bullet = 0;
    //End of Total electric bullets------------------>

        //getter methods
        /**
         * gets the total number of normal bullets for a tank entity.
         * @param tankMode
         * @param tankType
         */
        public static int getTotalNormalBulletsLevel(TankMode.TankModeType tankMode, Tank.TANKSTYPES tankType){
                if(tankMode == TankMode.TankModeType.EASY_MODE){

                   switch (tankType){
                           case E100:
                                   return EntitManager.tankBulletsProperty.Total_Easy_E100_Normal_bullet;
                           case KV2:
                                   return EntitManager.tankBulletsProperty.Total_Easy_KV2_Normal_bullet;
                           case M6:
                                   return EntitManager.tankBulletsProperty.Total_Easy_M6_Normal_bullet;
                           case PZKPFWIV:
                                   return EntitManager.tankBulletsProperty.Total_Easy_PZKPFWIV_Normal_bullet;
                           case PZKPFWIVG:
                                   return EntitManager.tankBulletsProperty.Total_Easy_PZKPFWIVG_Normal_bullet;
                           case T34:
                                   return EntitManager.tankBulletsProperty.Total_Easy_T34_Normal_bullet;
                           case VK3601H:
                                   return EntitManager.tankBulletsProperty.Total_Easy_VK3601H_Normal_bullet;
                   }

                }else
                if(tankMode == TankMode.TankModeType.MEDIUM_MODE){

                        switch (tankType){
                                case E100:
                                        return EntitManager.tankBulletsProperty.Total_Medium_E100_Normal_bullet;
                                case KV2:
                                        return EntitManager.tankBulletsProperty.Total_Medium_KV2_Normal_bullet;
                                case M6:
                                        return EntitManager.tankBulletsProperty.Total_Medium_M6_Normal_bullet;
                                case PZKPFWIV:
                                        return EntitManager.tankBulletsProperty.Total_Medium_PZKPFWIV_Normal_bullet;
                                case PZKPFWIVG:
                                        return EntitManager.tankBulletsProperty.Total_Medium_PZKPFWIVG_Normal_bullet;
                                case T34:
                                        return EntitManager.tankBulletsProperty.Total_Medium_T34_Normal_bullet;
                                case VK3601H:
                                        return EntitManager.tankBulletsProperty.Total_Medium_VK3601H_Normal_bullet;
                        }

                }else
                if(tankMode == TankMode.TankModeType.HARD_MODE){

                        switch (tankType){
                                case E100:
                                        return EntitManager.tankBulletsProperty.Total_Hard_E100_Normal_bullet;
                                case KV2:
                                        return EntitManager.tankBulletsProperty.Total_Hard_KV2_Normal_bullet;
                                case M6:
                                        return EntitManager.tankBulletsProperty.Total_Hard_M6_Normal_bullet;
                                case PZKPFWIV:
                                        return EntitManager.tankBulletsProperty.Total_Hard_PZKPFWIV_Normal_bullet;
                                case PZKPFWIVG:
                                        return EntitManager.tankBulletsProperty.Total_Hard_PZKPFWIVG_Normal_bullet;
                                case T34:
                                        return EntitManager.tankBulletsProperty.Total_Hard_T34_Normal_bullet;
                                case VK3601H:
                                        return EntitManager.tankBulletsProperty.Total_Hard_VK3601H_Normal_bullet;
                        }

                }

                return 0;
        }
        /**
         * gets the total number of electric bullets for a tank entity.
         * @param tankMode
         * @param tankType
         */
        public static int getTotalElectricBulletsLevel(TankMode.TankModeType tankMode, Tank.TANKSTYPES tankType){
                if(tankMode == TankMode.TankModeType.EASY_MODE){

                        switch (tankType){
                                case E100:
                                        return EntitManager.tankBulletsProperty.Total_Easy_E100_Electric_bullet;
                                case KV2:
                                        return EntitManager.tankBulletsProperty.Total_Easy_KV2_Electric_bullet;
                                case M6:
                                        return EntitManager.tankBulletsProperty.Total_Easy_M6_Electric_bullet;
                                case PZKPFWIV:
                                        return EntitManager.tankBulletsProperty.Total_Easy_PZKPFWIV_Electric_bullet;
                                case PZKPFWIVG:
                                        return EntitManager.tankBulletsProperty.Total_Easy_PZKPFWIVG_Electric_bullet;
                                case T34:
                                        return EntitManager.tankBulletsProperty.Total_Easy_T34_Electric_bullet;
                                case VK3601H:
                                        return EntitManager.tankBulletsProperty.Total_Easy_VK3601H_Electric_bullet;
                        }

                }else
                if(tankMode == TankMode.TankModeType.MEDIUM_MODE){

                        switch (tankType){
                                case E100:
                                        return EntitManager.tankBulletsProperty.Total_Medium_E100_Electric_bullet;
                                case KV2:
                                        return EntitManager.tankBulletsProperty.Total_Medium_KV2_Electric_bullet;
                                case M6:
                                        return EntitManager.tankBulletsProperty.Total_Medium_M6_Electric_bullet;
                                case PZKPFWIV:
                                        return EntitManager.tankBulletsProperty.Total_Medium_PZKPFWIV_Electric_bullet;
                                case PZKPFWIVG:
                                        return EntitManager.tankBulletsProperty.Total_Medium_PZKPFWIVG_Electric_bullet;
                                case T34:
                                        return EntitManager.tankBulletsProperty.Total_Medium_T34_Electric_bullet;
                                case VK3601H:
                                        return EntitManager.tankBulletsProperty.Total_Medium_VK3601H_Electric_bullet;
                        }

                }else
                if(tankMode == TankMode.TankModeType.HARD_MODE){

                        switch (tankType){
                                case E100:
                                        return EntitManager.tankBulletsProperty.Total_Hard_E100_Electric_bullet;
                                case KV2:
                                        return EntitManager.tankBulletsProperty.Total_Hard_KV2_Electric_bullet;
                                case M6:
                                        return EntitManager.tankBulletsProperty.Total_Hard_M6_Electric_bullet;
                                case PZKPFWIV:
                                        return EntitManager.tankBulletsProperty.Total_Hard_PZKPFWIV_Electric_bullet;
                                case PZKPFWIVG:
                                        return EntitManager.tankBulletsProperty.Total_Hard_PZKPFWIVG_Electric_bullet;
                                case T34:
                                        return EntitManager.tankBulletsProperty.Total_Hard_T34_Electric_bullet;
                                case VK3601H:
                                        return EntitManager.tankBulletsProperty.Total_Hard_VK3601H_Electric_bullet;
                        }

                }

                return 0;
        }
        //end of getter methods

}
