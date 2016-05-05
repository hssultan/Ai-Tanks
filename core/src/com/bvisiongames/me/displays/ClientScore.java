package com.bvisiongames.me.displays;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Tweenanimations.ClientScoreAnimation;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.Assets;
import aurelienribon.tweenengine.Tween;

/**
 * Created by Sultan on 8/13/2015.
 */
public class ClientScore {

    //table holding everything
    private Table table;
    private Label kills, coins;

    //integers for the values of each labels
    private int totalCoins = 0, totalKills = 0;

    //dimensions of icons
    private ActorsCellsDimensions coinDimensions = new ActorsCellsDimensions(
            40, 40, new float[]{
                        0, 25, 0, 0  //padding: top, right, bottom, left
                    }
    ),
                                  killDimensions = new ActorsCellsDimensions(
            80, 40, new float[]{
                        0, 0, 0, 0 //padding: top, right, bottom, left
                    }
    );

    //small padding between icons and total numbers
    private float smallPadding = 5;

    //update state of kills and coins
    private boolean killsIsUpdated = true,
                    coinsIsUpdated = true;

    public ClientScore(){

        //set total integers
        this.totalCoins = Tanks.gameState.getTotalCoins();
        this.totalKills = 0;

        this.table = new Table();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = MultiGameScreen.topBarGameFont;
        kills = new Label(Integer.toString(totalKills), style){
            @Override
            public void act(float delta) {
                super.act(delta);

                if(killsIsUpdated){
                    setText(Integer.toString(ClientScore.this.totalKills));
                    ClientScore.this.reset();
                    killsIsUpdated = false;
                }

            }
        };
        coins = new Label(Integer.toString(totalCoins), style){
            @Override
            public void act(float delta) {
                super.act(delta);

                if(coinsIsUpdated){
                    setText(Integer.toString(ClientScore.this.totalCoins));
                    coinsIsUpdated = false;
                }

            }
        };

        //add the icon of the coins first and then the coins label
        this.table.add(new Image(Assets.GameFileNames.getGameSkin().getRegion("coin")))
                .width(coinDimensions.getSize().x).height(coinDimensions.getSize().y)
                .padRight(smallPadding);
        this.table.add(coins).pad(
                coinDimensions.getPadding()[0],
                coinDimensions.getPadding()[3],
                coinDimensions.getPadding()[2],
                coinDimensions.getPadding()[1]
        );

        //add the icon of tank
        this.table.add(new Image(Assets.GameFileNames.getGameSkin().getRegion("star")))
                .width(killDimensions.getSize().x).height(killDimensions.getSize().y)
                .padRight(smallPadding);
        this.table.add(kills).pad(
                killDimensions.getPadding()[0],
                killDimensions.getPadding()[3],
                killDimensions.getPadding()[2],
                killDimensions.getPadding()[1]
        );

        //reposition the table
        this.reset();

        MultiGameScreen.stage.addActor(this.table);

    }

    public void reset(){
        this.table.layout();
        this.table.setPosition(Tanks.cameraWIDTH - this.table.getWidth()
                        - coinDimensions.getSize().x - killDimensions.getSize().x
                        - coinDimensions.getPadding()[3] - coinDimensions.getPadding()[1]
                        - killDimensions.getPadding()[3] - killDimensions.getPadding()[1]
                        - smallPadding * 2,
                Tanks.cameraHeight - coinDimensions.getSize().y);
    }
    public void hide(){

        this.table.setVisible(false);

    }
    public void show(){

        this.table.setVisible(true);

    }

    //setters
    /**
     * set the total coins.
     * @param coins
     * total number of coins.
     */
    public void setTotalCoins(int coins){
        if(this.totalCoins != coins){
            this.totalCoins = coins;
            this.coinsIsUpdated = true;
        }
    }
    /**
     * set the total kills.
     * @param kills
     * total number of kills.
     */
    public void setTotalKills(int kills){
        if(this.totalKills != kills){
            this.totalKills = kills;
            this.killsIsUpdated = true;
        }
    }
    /**
     * animate the total coins using tweens.
     * @param coins
     */
    public void AnimateCoinsChange(int coins){

        Tween.to(this, ClientScoreAnimation.COINS, 0.5f)
                .target(coins)
                .start(MultiGameScreen.tweenManager);

    }
    /**
     * animate the total kills using tweens.
     * @param kills
     * the new number of kills
     */
    public void AnimateKillsChange(int kills){

        Tween.to(this, ClientScoreAnimation.KILLS, 0.5f)
                .target(kills)
                .start(MultiGameScreen.tweenManager);

    }
    //end of setters

    //getters
    /**
     * gets the total number of coins.
     */
    public int getTotalCoins(){
        return this.totalCoins;
    }
    /**
     * gets the total number of kills.
     */
    public int getTotalKills(){
        return this.totalKills;
    }
    /**
     * get the position of this label relative to the stage dimensions.
     */
    //small variables
    private Vector2 getPosVec = new Vector2(0, 0);
    public Vector2 getPosition(){
        this.getPosVec.set(this.table.getX(), this.table.getY());
        return this.getPosVec;
    }
    /**
     * get the position of this label relative to the stage and the spriteBatch camera positions.
     */
    //small variables
    private Vector2 getRPosV = new Vector2(0, 0);
    public Vector2 getRPosition(){

        getRPosV.set(0, 0);

        getRPosV.add(
                getPosition().x - this.table.getWidth() - MultiGameScreen.stage.getCamera().position.x,
                getPosition().y - this.table.getHeight() - MultiGameScreen.stage.getCamera().position.y
        );
        getRPosV.add(
                MultiGameScreen.camera.position.x,
                MultiGameScreen.camera.position.y
        );

        return getRPosV;
    }
    //end of getters


    /**
     * below class saves up the width, height, paddings of actors.
     */
    private class ActorsCellsDimensions{

        private Vector2 width_height;
        private float[] padding;

        /**
         * initiator
         * @param width
         * @param height
         * @param padding
         * paddings (Top, Right, Bottom, Left)
         */
        public ActorsCellsDimensions(float width, float height, float[] padding){
            this.width_height = new Vector2(width, height);
            this.padding = padding;
        }

        //setters
        /**
         * sets the paddings
         */
        public void setPadding(float[] padding){
            this.padding = padding;
        }
        /**
         * sets the width and height (size)
         */
        public void setSize(float width, float height){
            this.width_height.set(width, height);
        }
        //end of setters


        //getters
        /**
         * gets the width and height
         */
        public Vector2 getSize(){
            return this.width_height;
        }
        /**
         * gets the paddings.
         * [0] : Top,
         * [1] : Right,
         * [2] : Bottom,
         * [3] : Left
         */
        public float[] getPadding(){
            return this.padding;
        }
        //end of getters

    }

}
