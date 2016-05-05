package com.bvisiongames.me.Maps;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.buildings.AssemblyBuilding;
import com.bvisiongames.me.entity.EntitManager;
import com.bvisiongames.me.entity.SmallBox;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.protocols.TankIntelligi;
import com.bvisiongames.me.screen.MultiGameScreen;
import com.bvisiongames.me.settings.ConstantVariables;
import com.bvisiongames.me.settings.GeneralMethods;
import com.bvisiongames.me.settings.TankMode;

/**
 * Created by ahzji_000 on 12/10/2015.
 * This level has:
 * - total CPU players of 1
 * - total of assembly buildings
 * - 1 easy mode cpu player
 */

public class Level1 extends Level{

    /**
     * sprites
     */
    private Sprite groundSprite;
    private float groundSpriteDiagonal;

    /**
     * vertices of the ground sprites.
     */
    private Vector2[] groundSpritePos;

    //tmp vector2
    private Vector2 tmpVector2 = new Vector2(0, 0);

    public Level1(){

        //set the map type to level 1
        this.setMaptype(MapManager.MAPTYPE.LEVEL1);

        //set the the ground sprite
        groundSprite = new Sprite(MultiGameScreen.Lands.getRegion("ground"));
        //set the diagonal of the groundSprite
        this.groundSpriteDiagonal = (float)Math.sqrt( groundSprite.getWidth()*groundSprite.getWidth() +
                                                        groundSprite.getHeight()*groundSprite.getHeight() );
        //set the positions of the ground sprites
        groundSpritePos = new Vector2[]{
                new Vector2(0, 0),
                new Vector2(groundSprite.getWidth(), 0),
                new Vector2(groundSprite.getWidth(), groundSprite.getHeight()),
                new Vector2(0, groundSprite.getHeight())
        };

        //set the total CPU players in this level to 1
        setTotalCPUPlayers(1);

        //set the boundary points of the map
        setBounderyPoints(new Vector2[]{
                new Vector2(0, 0),                                                                  //point 1
                new Vector2((groundSprite.getWidth() * 2) / ConstantVariables.PIXELS_TO_METERS, 0),     //point 2
                new Vector2((groundSprite.getWidth() * 2) / ConstantVariables.PIXELS_TO_METERS,
                        (groundSprite.getHeight() * 2) / ConstantVariables.PIXELS_TO_METERS),           //point 3
                new Vector2(0, (groundSprite.getHeight() * 2) / ConstantVariables.PIXELS_TO_METERS),    //point 4
                new Vector2(0, 0)                                                                   //point 5
        });

        /*
        //set the patrol points
        setPatrolPoints(new Vector2[]{
                new Vector2(groundSprite.getWidth() / (2 * ConstantVariables.PIXELS_TO_METERS),
                        groundSprite.getHeight() / (2 * ConstantVariables.PIXELS_TO_METERS) - 20),
                new Vector2(
                        groundSprite.getWidth() / (2 * ConstantVariables.PIXELS_TO_METERS) + groundSprite.getWidth() / ConstantVariables.PIXELS_TO_METERS,
                        groundSprite.getHeight() / (2 * ConstantVariables.PIXELS_TO_METERS) - 20),
                new Vector2(groundSprite.getWidth() / (2 * ConstantVariables.PIXELS_TO_METERS) + groundSprite.getWidth() / ConstantVariables.PIXELS_TO_METERS,
                        groundSprite.getHeight() / (2 * ConstantVariables.PIXELS_TO_METERS) + groundSprite.getHeight() / ConstantVariables.PIXELS_TO_METERS + 20),
                new Vector2(groundSprite.getWidth() / (2 * ConstantVariables.PIXELS_TO_METERS),
                        groundSprite.getHeight() / (2 * ConstantVariables.PIXELS_TO_METERS) + groundSprite.getHeight() / ConstantVariables.PIXELS_TO_METERS + 20)
        });
        */

        //set the boundary in float array format
        setFbounderyPoints(new float[]{
                getBounderyPoints()[0].x, getBounderyPoints()[0].y,
                getBounderyPoints()[1].x, getBounderyPoints()[1].y,
                getBounderyPoints()[2].x, getBounderyPoints()[2].y,
                getBounderyPoints()[3].x, getBounderyPoints()[3].y,
                getBounderyPoints()[4].x, getBounderyPoints()[4].y
        });

        //build the boundary in the world
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        Body body = MultiGameScreen.WORLD.createBody(bodyDef);

        ChainShape shape = new ChainShape();
        shape.createChain(getFbounderyPoints());
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10f;
        fixtureDef.friction = 1f;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);
        //end of body setup

        //setup the camera bounderies
        setCbounderyPoints(new Vector2[]{
                new Vector2( - groundSprite.getWidth(), - groundSprite.getHeight()),
                new Vector2( groundSprite.getWidth()*3, - groundSprite.getHeight()),
                new Vector2( groundSprite.getWidth()*3, groundSprite.getHeight()*3),
                new Vector2( - groundSprite.getWidth(), groundSprite.getHeight()*3)
        });

        //setup the assembly buildings
        setAssemblyBuildings(new AssemblyBuilding[1]);
        getAssemblyBuildings()[0] = new AssemblyBuilding(
                        new Vector2( groundSpritePos[1].x + groundSprite.getWidth(),
                                        Tanks.cameraHeight),
                                    AssemblyBuilding.SIDEFACING.LEFT);

        //call the add cpu players here after everything has been setup
        addCPUS();

        //add other entities into the map
        addOthers();

    }

    //adds other entities into the map
    private void addOthers(){

        //add a small box
        MultiGameScreen.objectUpdater.addEntity(new SmallBox(
                new Vector2(200, 100),
                0,
                SmallBox.BOXTYPE.box_2x2,
                true

        ));

    }

    @Override
    void addCPUS() {

        //initiate a tank 0 with easy mode of AI
        Tank tank1 = EntitManager.otherTanks.AddPlayer(new Vector2(50, Tanks.cameraHeight), 0, Tank.TANKSTYPES.E100, String.valueOf(0.0f));
        //add the artificial intellegence
        tank1.addAI(new TankIntelligi(
                tank1, TankMode.TankModeType.EASY_MODE
        ));
        //flag it so it start off in the assembler
        tank1.flagToDestroy();

        /*
        //initiate a tank 1 with easy mode of AI
        Tank tank2 = EntitManager.otherTanks.AddPlayer(
                new Vector2(100, Tanks.cameraHeight), 0,
                Tank.TANKSTYPES.E100, String.valueOf(1.0f));
        //add the artificial intellegence
        tank2.addAI(new TankIntelligi(
                tank2, TankMode.TankModeType.EASY_MODE
        ));
        //flag it so it start off in the assembler
        tank2.flagToDestroy();
        */

    }

    @Override
    void update() {

        for(int i = 0; i < getAssemblyBuildings().length; i++){
            AssemblyBuilding assemblyBuilding = this.getAssemblyBuildings()[i];
            assemblyBuilding.update();
        }

    }

    @Override
    void render(SpriteBatch spriteBatch) {

        //render the ground sprites
        groundSprite.setPosition(groundSpritePos[0].x, groundSpritePos[0].y);
        this.tmpVector2.set(groundSpritePos[0].x + groundSprite.getWidth()/2, groundSpritePos[0].y + groundSprite.getHeight()/2);
        if(GeneralMethods.isInsideCameraView(this.tmpVector2, groundSpriteDiagonal/2, MultiGameScreen.camera.frustum)){
            groundSprite.draw(spriteBatch);
        }
        groundSprite.setPosition(groundSpritePos[1].x, groundSpritePos[1].y);
        this.tmpVector2.set(groundSpritePos[1].x + groundSprite.getWidth()/2, groundSpritePos[1].y + groundSprite.getHeight()/2);
        if(GeneralMethods.isInsideCameraView(this.tmpVector2, groundSpriteDiagonal/2, MultiGameScreen.camera.frustum)){
            groundSprite.draw(spriteBatch);
        }
        groundSprite.setPosition(groundSpritePos[2].x, groundSpritePos[2].y);
        this.tmpVector2.set(groundSpritePos[2].x + groundSprite.getWidth() / 2, groundSpritePos[2].y + groundSprite.getHeight() / 2);
        if(GeneralMethods.isInsideCameraView(this.tmpVector2, groundSpriteDiagonal/2, MultiGameScreen.camera.frustum)){
            groundSprite.draw(spriteBatch);
        }
        groundSprite.setPosition(groundSpritePos[3].x, groundSpritePos[3].y);
        this.tmpVector2.set(groundSpritePos[3].x + groundSprite.getWidth() / 2, groundSpritePos[3].y + groundSprite.getHeight() / 2);
        if(GeneralMethods.isInsideCameraView(this.tmpVector2, groundSpriteDiagonal/2, MultiGameScreen.camera.frustum)){
            groundSprite.draw(spriteBatch);
        }

        //render the bottom to hte ground sprites
        for(int i = 0; i < getAssemblyBuildings().length; i++){
            AssemblyBuilding assemblyBuilding = getAssemblyBuildings()[i];
            assemblyBuilding.render(spriteBatch);
        }

    }

    @Override
    void renderTopSprites(SpriteBatch spriteBatch) {

        //render top sprites
        for(int i = 0; i < getAssemblyBuildings().length; i++){
            AssemblyBuilding assemblyBuilding = getAssemblyBuildings()[i];
            assemblyBuilding.renderTop(spriteBatch);
        }

    }

}
