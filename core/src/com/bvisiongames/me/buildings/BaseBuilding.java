package com.bvisiongames.me.buildings;

import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.ConstantVariables;

/**
 * Created by Sultan on 9/11/2015.
 */
public class BaseBuilding extends Buildings{

    // client that uses this base
    private int clientID;

    //properties of this class
    private boolean isOccupied = false;
    private int Index;
    private BitmapFontCache numberText;

    //Dimensions
    private Vector2 dimensions;

    //sprites
    public Sprite baseSprite, baseLives;
    public Body body;

    //properties of lives
    private int numberOfLives = 0, tmpLives = 0;
    private float[] originalColors = new float[]{0,0,0};

    public BaseBuilding(Vector2 initialPos, int index){

        //set the index of this base
        Index = index + 1;
        pos = initialPos;

        //create the sprite building
        baseSprite = new Sprite(new TextureRegion(MultiGameScreen.gameSkin.getRegion("base")));
        baseSprite.setPosition(initialPos.x, initialPos.y);
        baseLives = new Sprite(new TextureRegion(MultiGameScreen.gameSkin.getRegion("rsz_yellow")));
        originalColors[0] = baseLives.getColor().r;
        originalColors[1] = baseLives.getColor().g;
        originalColors[2] = baseLives.getColor().b;

        //set some properties
        dimensions = new Vector2(baseSprite.getWidth(), baseSprite.getHeight());

        //create a line around it
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(initialPos.x / ConstantVariables.PIXELS_TO_METERS, initialPos.y / ConstantVariables.PIXELS_TO_METERS);

        body = MultiGameScreen.WORLD.createBody(bodyDef);

        shape = new ChainShape();
        corners = new float[]{
                0, 0,   //bottom left
                0, (baseSprite.getHeight())/ConstantVariables.PIXELS_TO_METERS, //top left
                ( baseSprite.getWidth())/ConstantVariables.PIXELS_TO_METERS, ( baseSprite.getHeight())/ConstantVariables.PIXELS_TO_METERS, //top right
                ( baseSprite.getWidth())/ConstantVariables.PIXELS_TO_METERS, 0 //bottom right
        };
        shape.createChain(corners);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5f;
        fixtureDef.friction = 0.5f;

        body.createFixture(fixtureDef);

        shape.dispose();

        //re-setup the corners position of the corners with relative to the world
        corners = new float[]{
                corners[0] + initialPos.x/ConstantVariables.PIXELS_TO_METERS, corners[1] + initialPos.y/ConstantVariables.PIXELS_TO_METERS,   //bottom left
                corners[2] + initialPos.x/ConstantVariables.PIXELS_TO_METERS, corners[3] + initialPos.y/ConstantVariables.PIXELS_TO_METERS, //top left
                corners[4] + initialPos.x/ConstantVariables.PIXELS_TO_METERS, corners[5] + initialPos.y/ConstantVariables.PIXELS_TO_METERS, //top right
                corners[6] + initialPos.x/ConstantVariables.PIXELS_TO_METERS, corners[7] + initialPos.y/ConstantVariables.PIXELS_TO_METERS //bottom right
        };
        //setup the Vector corners
        Vcorners = new Vector2[]{
                new Vector2(corners[0], corners[1]),   //bottom left
                new Vector2(corners[2], corners[3]), //top left
                new Vector2(corners[4], corners[5]), //top right
                new Vector2(corners[6], corners[7]) //bottom right
        };

        //bitmap
        numberText = new BitmapFontCache(MultiGameScreen.smallSizeFont);
        numberText.setColor(0.9f, 0.9f, 0.9f, 0.5f);
        numberText.setText(Integer.toString(Index),
                pos.x + baseSprite.getWidth()/2 - 5,
                pos.y - 15);

        //set the user data to this class
        body.setUserData(this);

    }

    public void setToUse(int clientid, boolean Admin){
        clientID = clientid;
        isOccupied = true;
        numberOfLives = 100;
        numberText.setAlphas(1f);
    }

    @Override
    public void update(){

    }
    @Override
    public void render(SpriteBatch batch){

        if(baseSprite != null && baseLives != null){
            baseSprite.draw(batch);
            renderLives(batch);
        }

    }

    @Override
    public void renderTop(SpriteBatch batch) {

    }

    public void renderIndex(SpriteBatch batch){
        if(numberText != null){
            numberText.draw(batch);
        }
    }
    private void renderLives(SpriteBatch batch){

        tmpLives = numberOfLives;
        //first life
        if(tmpLives > 0){
            baseLives.setPosition(pos.x + 5, pos.y + 20);
            baseLives.setColor(originalColors[0], originalColors[1], originalColors[2], 1f);
            baseLives.draw(batch);
            tmpLives-= (100/3);
        }else{
            baseLives.setPosition(pos.x + 5, pos.y + 20);
            baseLives.setColor(0.5f, 0.5f, 0.5f, 0.8f);
            baseLives.draw(batch);
        }
        //second life
        if(tmpLives > 0){
            baseLives.setPosition(pos.x + 5, baseLives.getY() + baseLives.getHeight()*1.4f);
            baseLives.setColor(originalColors[0], originalColors[1], originalColors[2], 1f);
            baseLives.draw(batch);
            tmpLives-= (100/3);
        }else{
            baseLives.setPosition(pos.x + 5, baseLives.getY() + baseLives.getHeight()*1.4f);
            baseLives.setColor(0.5f, 0.5f, 0.5f, 0.8f);
            baseLives.draw(batch);
        }
        //third life
        if(tmpLives > 0){
            baseLives.setPosition(pos.x + 5, baseLives.getY() + baseLives.getHeight()*1.4f);
            baseLives.setColor(originalColors[0], originalColors[1], originalColors[2], 1f);
            baseLives.draw(batch);
            tmpLives-= (100/3);
        }else{
            baseLives.setPosition(pos.x + 5, baseLives.getY() + baseLives.getHeight()*1.4f);
            baseLives.setColor(0.5f, 0.5f, 0.5f, 0.8f);
            baseLives.draw(batch);
        }

    }

    public boolean isOccupied(){
        return isOccupied;
    }
    public boolean isClient(int id){
        return (clientID == id)? true : false;
    }
    //returns the position of the base from the center in box2d dimensions
    //small variables
    private Vector2 tmpCenterV = new Vector2(0, 0);
    public Vector2 getCenter(){
        this.tmpCenterV.set(body.getPosition().x + baseSprite.getWidth()/(2*ConstantVariables.PIXELS_TO_METERS),
                            body.getPosition().y + baseSprite.getHeight()/(2* ConstantVariables.PIXELS_TO_METERS));
        return this.tmpCenterV;
    }
    public int getIndex(){
        return Index;
    }
    public int getClientID(){
        return clientID;
    }
    public Vector2 getDimensions(){
        return dimensions;
    }
    public float getHalfDiag(){
        return (float)Math.sqrt( Math.pow(baseSprite.getWidth(),2) + Math.pow(baseSprite.getHeight(),2) )/2;
    }
    public void updateLives(int lives){
        numberOfLives = lives;
    }

    public void release(){
        isOccupied = false;
        numberOfLives = 0;
        numberText.setAlphas(1f);
    }

}
