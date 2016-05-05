package com.bvisiongames.me.screen;

/**
 * Created by H on 3/27/2015.
 */
public class ScreenManager {

    private static Screen currentScreen;

    public static void setScreen(Screen screen){

        if(currentScreen != null ){
            currentScreen.dispose();
            //call the garbage collector after disposing the screen
            System.gc();
        }

        currentScreen = screen;
        currentScreen.create();

    }
    public static Screen GetCurrentScreen(){
        return currentScreen;
    }

}
