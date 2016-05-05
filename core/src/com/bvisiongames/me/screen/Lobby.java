package com.bvisiongames.me.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bvisiongames.me.JSON.JSONArray;
import com.bvisiongames.me.Maps.MapManager;
import com.bvisiongames.me.Maps.MapsInfo;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Tweenanimations.CellAnimation;
import com.bvisiongames.me.Tweenanimations.ProgressBarAnimation;
import com.bvisiongames.me.Tweenanimations.Box2dActorAnimation;
import com.bvisiongames.me.camera.OrthoCamera;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.Weapons.Weapon;
import com.bvisiongames.me.miniscreen.Market;
import com.bvisiongames.me.settings.Assets;
import java.util.ArrayList;
import java.util.List;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Sultan on 6/28/2015.
 */
public class Lobby extends Screen implements InputProcessor {

    OrthoCamera camera;

    //ui objects (stages and skins)
    public static Stage stage;

    //bitmapfonts
    public static BitmapFont pageTitleFont, roomTitleFont, roomTotalFont,
                                panelOptionFontBtn;

    //panels of the lobby
    CreateStagesPanel createStagesPanel;
    ChooseMainTank chooseMainTank;
    ChooseBullets chooseBullets;

    //tween manager for animation
    public static TweenManager tweenManager;
    //------------------------------->

    //this class is the market
    public static Market market;

    @Override
    public void dispose() {

        //stage dispose
        stage.dispose();

        //dispose the small panels
        chooseMainTank.dispose();

        market.dispose();

    }

    @Override
    public void create() {

        //initialize the camera
        camera = new OrthoCamera();
        //set and reset the viewport width and height
        camera.update();
        camera.resize();

        //create the stage
        stage = new Stage(new FitViewport(Tanks.cameraWIDTH, Tanks.cameraHeight));

        //initiate the player store file
        Tanks.gameState.setPlayers(new JSONArray().toString());

        //initiate the tween manager
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ProgressBarAnimation());
        Tween.registerAccessor(Actor.class, new Box2dActorAnimation());
        Tween.registerAccessor(Cell.class, new CellAnimation());

        //fonts setup
        setUpFonts();

        //add the create room and show
        createStagesPanel = new CreateStagesPanel();
        chooseMainTank = new ChooseMainTank();
        chooseBullets = new ChooseBullets();

        //add the bottom ui
        setupBottomUI();

        //set up the market after the screen
        setupMarket();

        //the ui listeners
        addUIListeners();

        //set the on out focus to disable input cursor
        setOnOutFocus();

        //apply the post methods for the panels
        createStagesPanel.postInitialize();

    }
    private void setupBottomUI(){

        TextButton.TextButtonStyle backStyle = new TextButton.TextButtonStyle();
        backStyle.font = panelOptionFontBtn;
        backStyle.up = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("blueup"), 5,5,5,5));
        backStyle.up.setLeftWidth(50);
        backStyle.up.setRightWidth(50);
        backStyle.down = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown"), 5,5,5,5));
        backStyle.down.setLeftWidth(50);
        backStyle.down.setRightWidth(50);
        TextButton back = new TextButton("Menu", backStyle);
        back.setPosition(90, back.getHeight() + 10);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.setScreen(new MenuScreen());
            }
        });

        stage.addActor(back);

    }
    private void setupMarket(){
        market = new Market();
        //set the tween manager
        market.setTween(tweenManager);
        //setup the market icon drawables
        market.setMarketIconDrawables(
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("blueup"), 5, 5, 5, 5)),
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("cart"))),
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("coin"))),
                panelOptionFontBtn);

        //setup the market layout drawables
        market.setMarketLayoutDrawables(
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("panel"), 5, 5, 5, 5)),
                new NinePatchDrawable(new NinePatch(Assets.assetManager.get(Assets.MenuFileNames.blackBgString, Texture.class))));
        market.marketLayout.setTitle("Store", pageTitleFont);
        //add the coin tab
        market.marketLayout.coinsTab.addTab(
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("blueup"), 5, 5, 5, 5)),
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown"), 5, 5, 5, 5)),
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("coin"), 5, 5, 5, 5)),
                new float[]{0, 0, 0, 100});
        //add the weapon tab
        market.marketLayout.weaponTab.addTab(
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("blueup"), 5, 5, 5, 5)),
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown"), 5, 5, 5, 5)),
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("artillary"), 5, 5, 5, 5)),
                new float[]{0, 0, 0, 200});
        //add the coin display tab
        market.marketLayout.setCoinTab(
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("blueup"), 5,5,5,5)),
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("coin"))),
                " "+Tanks.jarCoins.getCoins(), panelOptionFontBtn);
        //add listeners to the tabs
        market.marketLayout.setListeners();
        //set up the items tables
        market.marketLayout.setItemsTables();
        market.marketLayout.addWeaponScrollTable(
                Assets.MenuFileNames.getMenuSkin(), Assets.MenuFileNames.getBulletSkin(),
                roomTitleFont, panelOptionFontBtn);
        market.marketLayout.addCoinScrollTable(
                Assets.MenuFileNames.getMenuSkin(), Assets.MenuFileNames.getBulletSkin(),
                roomTitleFont, panelOptionFontBtn);
        market.marketLayout.addLowerButtons(panelOptionFontBtn,
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("blueup"), 5,5,5,5)),
                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown"), 5,5,5,5)));

        //adjust the visibilty of the market objects
        //show the market icon
        market.showMarketIcon();
        //hide the market layout
        market.hideMarketLayout();

        //animate the display of tabs so it appears that it has been preset.
        market.marketLayout.coinsTab.clickOn(); //show that coins tab is clicked on
        market.marketLayout.weaponTab.clickOff(); //show that weapon tab is clicked off

        //add the interface listener for any item bought from the market
        market.marketLayout.setInterface(new Market.Communicator() {
            @Override
            public void onItemBought(Weapon.BULLETSTYPE itemtype, int totalNumberOfItems) {
                chooseBullets.setBulletNumber(itemtype, totalNumberOfItems);
            }
        });

    }
    private void addUIListeners(){

        //add the listeners below
        //set the input listener
        InputMultiplexer multi = new InputMultiplexer();
        market.setListener(multi);
        multi.addProcessor(this);
        multi.addProcessor(stage);
        Gdx.input.setInputProcessor(multi);

    }
    private void setOnOutFocus(){
        //this disable textfield input focus
        stage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField)){
                    stage.setKeyboardFocus(null);
                }
                return false;
            }
        });
    }
    private void setUpFonts(){

        pageTitleFont = Assets.assetManager.get(Assets.MenuFileNames.LobbyFonts.pageTitleFontString, BitmapFont.class);

        roomTitleFont = Assets.assetManager.get(Assets.MenuFileNames.LobbyFonts.roomTitleFontString, BitmapFont.class);

        //set the font file for the number of players in the box
        roomTotalFont = Assets.assetManager.get(Assets.MenuFileNames.LobbyFonts.roomTotalFontString, BitmapFont.class);

        panelOptionFontBtn = Assets.assetManager.get(Assets.MenuFileNames.LobbyFonts.panelOptionFontBtnString, BitmapFont.class);

    }

    //this class is panel for adding CPU Tanks to the game and mainly stage
    public class CreateStagesPanel{

        //add a table
        private Table bgPanel;

        //initiate the search option ui's
        private Label Titlelabel,
                        TotalPlayers;
        public TextButton create, back;

        //prompt instance
        private Prompt prompt;

        //choose stage
        private ChooseStage chooseStage;

        public CreateStagesPanel(){

            //Button Styles
            TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
            buttonStyle.font = panelOptionFontBtn;
            buttonStyle.up = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("blueup"), 5,5, 5, 5));
            buttonStyle.up.setLeftWidth(50);
            buttonStyle.up.setRightWidth(50);
            buttonStyle.down = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown"), 5,5,5, 5));
            buttonStyle.down.setLeftWidth(50);
            buttonStyle.down.setRightWidth(50);
            buttonStyle.disabled = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown"), 5,5,5, 5));
            buttonStyle.disabled.setLeftWidth(50);
            buttonStyle.disabled.setRightWidth(50);

            //background panel table
            bgPanel = new Table();
            bgPanel.setBackground(new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("panel"), 5,5,5,5)));
            bgPanel.setSize(Tanks.cameraWIDTH / 1.5f, Tanks.cameraHeight / 1.5f);
            bgPanel.setPosition(Tanks.cameraWIDTH / 2 - bgPanel.getWidth() / 2, Tanks.cameraHeight / 2 - bgPanel.getHeight() / 2);

            //add the title
            Label.LabelStyle TitleLabelStyle = new Label.LabelStyle();
            TitleLabelStyle.font = pageTitleFont;
            Titlelabel = new Label("Create Room", TitleLabelStyle); //setup the title of the search option settings
            Titlelabel.setPosition(bgPanel.getX() + bgPanel.getWidth() / 2 - Titlelabel.getWidth() / 2,
                    bgPanel.getY() + bgPanel.getHeight() + 10);

            //add the number of cpu that will be in the game
            Label.LabelStyle PlayerTitleStyle = new Label.LabelStyle();
            PlayerTitleStyle.font = roomTitleFont;

            TotalPlayers = new Label("Stage", PlayerTitleStyle);
            bgPanel.add(TotalPlayers).colspan(2);
            bgPanel.row();

            //initiate the create button before it is used
            create = new TextButton("Next", buttonStyle);
            create.setDisabled(true);
            //initiate the back button
            back = new TextButton("back", buttonStyle);

            //add the choose stage ui
            chooseStage = new ChooseStage(bgPanel);

            //add the buttons
            bgPanel.row();
            create.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!create.isDisabled()) {
                        //hide this gui
                        hide();

                        chooseBullets.show();
                    }

                }
            });
            bgPanel.add(back);
            bgPanel.add(create);

            //add the buttons
            back.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    createStagesPanel.hide();
                    chooseMainTank.showA();
                }
            });

            //initiate the loading box and the create status label
            Label.LabelStyle inputLabelStyle = new Label.LabelStyle();
            inputLabelStyle.font = roomTitleFont;

            stage.addActor(bgPanel);
            stage.addActor(Titlelabel);

            hide(0.1f);

            //add the prompt
            this.prompt = new Prompt(stage);

        }
        public void render(SpriteBatch spriteBatch){}
        public void hide(){
            Tween.to(bgPanel, Box2dActorAnimation.ALPHA, 1f)
                    .target(0)
                    .start(tweenManager);
            Tween.to(back, Box2dActorAnimation.ALPHA, 1f)
                    .target(0)
                    .start(tweenManager);
        }
        public void show(){
            Tween.to(bgPanel, Box2dActorAnimation.ALPHA, 1f)
                    .target(1)
                    .start(tweenManager);
            Tween.to(back, Box2dActorAnimation.ALPHA, 1f)
                    .target(1)
                    .start(tweenManager);
        }
        public void hide(float time){
            Tween.to(bgPanel, Box2dActorAnimation.ALPHA, time)
                    .target(1)
                    .start(tweenManager);
            Tween.to(back, Box2dActorAnimation.ALPHA, time)
                    .target(1)
                    .start(tweenManager);
        }
        public void show(float time){
            Tween.to(bgPanel, Box2dActorAnimation.ALPHA, time)
                    .target(1)
                    .start(tweenManager);
            Tween.to(back, Box2dActorAnimation.ALPHA, time)
                    .target(1)
                    .start(tweenManager);
        }

        /**
         * post method that will be called after everything is initialized.
         */
        public void postInitialize(){

            this.chooseStage.setInitialProperties();

        }

        /**
         * class that creates a choose stage scroll pane.
         */
        private class ChooseStage{

            //table holding everything
            private Table masterTable;

            //scroll pane
            private ScrollPane scrollPane;

            //stage unit manager
            private StageUnitManager stageUnitManager = new StageUnitManager();

            public ChooseStage(Table table){
                this.masterTable = table;
                this.createScroll();
            }
            /**
             * this method takes care of creating the scroll pane.
             */
            private void createScroll(){

                Table scrollTable = new Table();

                //add the scroll pane
                this.scrollPane = new ScrollPane(scrollTable);

                //add units
                for(MapManager.MAPTYPE maptype : MapManager.MAPTYPE.values()){
                    StageUnit stageUnit = new StageUnit(scrollTable, maptype);
                    if(Tanks.gameState.maps.isPurchased(maptype)){
                        stageUnit.unlock();
                    }else{
                        stageUnit.lock();
                    }
                    //then add it to the stage unit manager
                    this.stageUnitManager.addStageUnit(stageUnit);
                }

                //add scroll table to the master table
                this.masterTable.add(this.scrollPane)
                        .width(this.masterTable.getWidth()).height(this.masterTable.getHeight() / 1.5f)
                        .colspan(2);

            }

            /**
             * this method sets the initial state of the choose scroll stage.
             * it is called after tween manager has been initialized and other classes has been initialized.
             */
            public void setInitialProperties(){

                //hide the prompt
                prompt.hide();

            }

            /**
             * class that manages the stage units.
             */
            private class StageUnitManager{

                private List<StageUnit> list = new ArrayList<StageUnit>();

                /**
                 * this method adds to the list.
                 * @param unit
                 * stage unit to be added to hte stage unit manager.
                 */
                public void addStageUnit(StageUnit unit){
                    this.list.add(unit);
                }
                /**
                 * removes the supplied unit from the manager.
                 * @param unit
                 * the unit to be removed.
                 */
                public void removeStageUnit(StageUnit unit){
                    this.list.remove(unit);
                }
                /**
                 * this method calls select on the item supplied.
                 * @param unit
                 * the unit that has needs to be selected.
                 */
                public void selectStageUnit(StageUnit unit){
                    if(unit != null){
                        //call deselect on all the units
                        for(int i = 0; i < this.list.size(); i++){
                            this.list.get(i).deselect();
                        }
                        //call select on the unit chosen
                        unit.select();
                    }
                }
                /**
                 * this method calls the deselect on the item supplied.
                 * @param unit
                 * the unit that needs to be deselected.
                 */
                public void deselectStageUnit(StageUnit unit){
                    unit.deselect();
                }

                /**
                 * get the stageUnit with supplied maptype.
                 * @param maptype
                 * the maptype of the map.
                 */
                public StageUnit getStageUnit(MapManager.MAPTYPE maptype){
                    for(int i = 0; i < this.list.size(); i++){
                        if(this.list.get(i).getMaptype().toString().equals(maptype.toString())){
                            return this.list.get(i);
                        }
                    }
                    return null;
                }

            }

            /**
             * this class creates the unit stage.
             */
            private class StageUnit{

                private MapManager.MAPTYPE maptype;
                private Stack miniStack;
                private Table miniTable;
                private Table locked;   //locked cover table

                //image of blue down that will be animated
                private Image deselectedImg;

                private ITEMSTATE itemstate = ITEMSTATE.LOCKED;
                private boolean selected = false;

                private Image cover;

                //this variable locks the animation when selecting
                private boolean isAnimating = false;

                //expansion ratio
                private Vector2 expansionRatio = new Vector2(20, 0);

                public StageUnit(Table holder, MapManager.MAPTYPE maptype){

                    this.maptype = maptype;

                    //initiate mini tables and stack
                    miniTable = new Table();
                    miniStack = new Stack();
                    //when selected image is blue up
                    miniStack.add(new Image(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("orangeup"), 5, 5, 5, 5)));
                    //when deselected image is blue down
                    deselectedImg = new Image(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("orangedown"), 5, 5, 5, 5));
                    deselectedImg.getColor().a = 1f;
                    miniStack.add(deselectedImg);

                    //add the stack to show the image of the map
                    Stack stack = new Stack();
                    //add the top cover
                    this.cover = new Image(Assets.assetManager.get(Assets.MenuFileNames.blackBgString,Texture.class));
                    this.cover.getColor().a = 0.6f;
                    if(Assets.MenuFileNames.getLandIcons().has(maptype.toString(), TextureRegion.class)){
                        stack.add(new Image(Assets.MenuFileNames.getLandIcons().getRegion(maptype.toString())));
                    }
                    stack.add(this.cover);

                    //add the locked table
                    locked = new Table();
                    Label.LabelStyle lockedStyleLabel = new Label.LabelStyle();
                    lockedStyleLabel.font = roomTitleFont;
                    lockedStyleLabel.fontColor = Color.WHITE;
                    locked.add(new Label("LOCKED", lockedStyleLabel));
                    locked.row();
                    Label.LabelStyle priceLockedLabel = new Label.LabelStyle();
                    priceLockedLabel.font = panelOptionFontBtn;
                    priceLockedLabel.fontColor = Color.WHITE;
                    locked.add(new Label(MapsInfo.getTotalPrice(maptype)+" coins", priceLockedLabel));
                    stack.add(locked);

                    //setup the title of the map
                    Label.LabelStyle titleStyle = new Label.LabelStyle();
                    titleStyle.font = roomTitleFont;
                    Label title = new Label(maptype.toString(), titleStyle);

                    //add the actors to the mini table
                    miniTable.add(stack).width(230).height(230);
                    miniTable.row();
                    miniTable.add(title).padTop(15);

                    //add minitable to ministack
                    miniStack.add(miniTable);

                    //add the minitable to the holder table
                    holder.add(miniStack).width(250).height(300);

                    //add listener to the stack
                    miniStack.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            //first check if it has been unlocked
                            if(StageUnit.this.isLocked()){
                                //then check if there is enough coins
                                if(Tanks.jarCoins.hasEnough(MapsInfo.getTotalPrice(StageUnit.this.getMaptype()))){
                                    prompt.show(
                                            "Unlock "+getMaptype().toString()+"?\nPrice: "+MapsInfo.getTotalPrice(getMaptype())+" coins",
                                            "UNLOCK", "CANCEL", BUYSTATE.COINS, 150, StageUnit.this);
                                }else{
                                    prompt.show(
                                            "Get More Coins?\nPrice: "+MapsInfo.getTotalPrice(getMaptype())+" coins",
                                            "GET COINS", "CANCEL", BUYSTATE.MONEY, 200, StageUnit.this);
                                }
                            }else{
                                stageUnitManager.selectStageUnit(StageUnit.this);
                            }

                        }
                    });

                }

                //getters
                /**
                 * gets the map type.
                 */
                public MapManager.MAPTYPE getMaptype(){
                    return this.maptype;
                }
                /**
                 * gets the item lock state.
                 */
                public ITEMSTATE getItemstate(){
                    return this.itemstate;
                }
                //end of getters

                //setters
                /**
                 * lock this item.
                 */
                public void lock(){
                    this.itemstate = ITEMSTATE.LOCKED;
                    this.cover.setVisible(true);
                    this.locked.setVisible(true);
                }
                /**
                 * unock this item.
                 */
                public void unlock(){
                    this.itemstate = ITEMSTATE.UNLOCKED;
                    Tanks.gameState.maps.setMapTypPurchased(getMaptype());
                    this.cover.setVisible(false);
                    this.locked.setVisible(false);
                }
                /**
                 * this method deselects this unit.
                 */
                public void deselect(){
                    if( isSelected() && !isAnimating){
                        this.selected = false;
                        isAnimating = true;
                        //tween the size of the stack
                        Tween.to(this.miniStack, ProgressBarAnimation.ALLDIMEN, 0.3f)
                                .target(this.miniStack.getWidth() - expansionRatio.x, this.miniStack.getHeight() - expansionRatio.y)
                                .setCallbackTriggers(TweenCallback.END)
                                .setCallback(new TweenCallback() {
                                    @Override
                                    public void onEvent(int type, BaseTween<?> source) {
                                        if(type == TweenCallback.END){
                                            isAnimating = false;
                                            create.setDisabled(true);
                                        }
                                    }
                                })
                                .start(tweenManager);
                        //tween the deselected image
                        Tween.to(this.deselectedImg, ProgressBarAnimation.ALPHA, 0.3f)
                                .target(1)
                                .start(tweenManager);
                    }
                }
                /**
                 * this method selects this unit.
                 */
                public void select(){
                    if( !isSelected() && !isAnimating){
                        this.selected = true;
                        isAnimating = true;
                        //Tanks.gameState.maps.setLevel(StageUnit.this.getMaptype());
                        //tween the size of the stack
                        Tween.to(this.miniStack, ProgressBarAnimation.ALLDIMEN, 0.3f)
                                .target(this.miniStack.getWidth() + expansionRatio.x, this.miniStack.getHeight() + expansionRatio.y)
                                .setCallbackTriggers(TweenCallback.END)
                                .setCallback(new TweenCallback() {
                                    @Override
                                    public void onEvent(int type, BaseTween<?> source) {
                                        if(type == TweenCallback.END){
                                            isAnimating = false;
                                            create.setDisabled(false);
                                        }
                                    }
                                })
                                .start(tweenManager);
                        //tween the deselected image
                        Tween.to(this.deselectedImg, ProgressBarAnimation.ALPHA, 0.3f)
                                .target(0)
                                .start(tweenManager);
                    }
                }
                //end of setters

                //checkers
                /**
                 * checks whether the item is locked
                 */
                public boolean isLocked(){
                    switch (this.itemstate){
                        case LOCKED:
                            return true;
                        case UNLOCKED:
                            return false;
                        default:
                            return true;
                    }
                }
                /**
                 * checks whether this item is selected.
                 */
                public boolean isSelected(){
                    return this.selected;
                }
                //end of checkers

            }

        }

        /**
         * this class creates a pop up to prompt for buy using coins or buy coins if not enough coins.
         */
        public class Prompt{

            //the holder Stack of the prompt
            private Stack holder;

            //background cover
            private Image bgCover;

            //note or question to the client
            private Label question;

            //text buttons
            private TextButton buy, cancel;

            //buy state
            private BUYSTATE buystate = BUYSTATE.COINS;
            //map item to be bought
            private ChooseStage.StageUnit stageUnit;

            public Prompt(Stage holder){

                //setup the stack or holder
                this.holder = new Stack();
                this.holder.add(new Image(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("panel"), 5, 5, 5, 5)));

                //setup the background cover
                this.bgCover = new Image(Assets.assetManager.get(Assets.MenuFileNames.blackBgString, Texture.class));
                this.bgCover.setSize(Tanks.cameraWIDTH, Tanks.cameraHeight);
                this.bgCover.getColor().a = 0.7f;
                this.bgCover.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Prompt.this.hide();
                    }
                });

                //minitable
                Table miniTable = new Table();

                //setup a question asking whether to buy this map.
                Label.LabelStyle questionStyle= new Label.LabelStyle();
                questionStyle.font = roomTitleFont;
                this.question = new Label("Note to be asked",questionStyle);
                this.question.setWrap(true);
                //add the question or note to the mini table
                miniTable.add(this.question)
                        .padBottom(50)
                        .width(250).colspan(2);
                miniTable.row();

                //setup two buttons two buttons
                TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
                btnStyle.font = panelOptionFontBtn;
                btnStyle.fontColor = Color.WHITE;
                btnStyle.up = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("blueup"),5,5,5,5));
                btnStyle.down = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown"),5,5,5,5));
                btnStyle.up.setRightWidth(30);
                btnStyle.up.setLeftWidth(30);
                btnStyle.up.setTopHeight(10);
                btnStyle.up.setBottomHeight(10);
                btnStyle.down.setRightWidth(30);
                btnStyle.down.setLeftWidth(30);
                btnStyle.down.setTopHeight(10);
                btnStyle.down.setBottomHeight(10);
                btnStyle.downFontColor = Color.GRAY;
                this.buy = new TextButton("buy", btnStyle);
                this.cancel = new TextButton("cancel", btnStyle);
                //add the buttons to mini Table
                miniTable.add(this.buy).width(150).padRight(75);
                miniTable.add(this.cancel).width(150);

                //add actors
                this.holder.add(miniTable);
                holder.addActor(this.bgCover);
                holder.addActor(this.holder);


                //add listeners
                this.buy.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        switch (buystate) {
                            case COINS:
                                //check if enough coins and if this map is purchased and the stage item is unlocked
                                if (Prompt.this.stageUnit.isLocked() &&
                                        !Tanks.gameState.maps.isPurchased(Prompt.this.stageUnit.getMaptype()) &&
                                        Tanks.jarCoins.hasEnough(MapsInfo.getTotalPrice(Prompt.this.stageUnit.getMaptype()))) {
                                    //subtract price from coins
                                    Tanks.jarCoins.setCoins(Tanks.jarCoins.getCoins() - MapsInfo.getTotalPrice(Prompt.this.stageUnit.getMaptype()));
                                    //save the new coins number
                                    Tanks.gameState.setCoins(Tanks.jarCoins.getCoins());
                                    //unlock the stage item
                                    Prompt.this.stageUnit.unlock();
                                    Prompt.this.stageUnit.select();
                                    Prompt.this.hide();
                                }
                                break;
                            case MONEY:
                                performClick(market.marketLayout.getCoinsTab().getTab());
                                performClick(market.marketIcon.getHolder());
                                performClick(cancel);
                                break;
                        }
                    }
                });
                this.cancel.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Prompt.this.hide();
                    }
                });

                //resize and reposition the holder stack
                this.holder.setSize(400, this.buy.getHeight() + 100 + this.question.getHeight());
                this.holder.setPosition(
                        Tanks.cameraWIDTH / 2 - this.holder.getWidth() / 2,
                        -this.holder.getHeight() * 1.2f);

            }

            /**
             * shows the prompt stack.
             * @param question
             * question ot be asked.
             * @param left
             * left is the buy or purchase coins button text button.
             * @param right
             * right is the cancel button.
             * @param state
             * state of the buy button.
             * @param height
             * adding height to the prompt layout.
             * @param itemStage
             * item instance to be bought
             */
            public void show(String question, String left, String right,
                             BUYSTATE state, float height, ChooseStage.StageUnit itemStage){

                this.stageUnit = itemStage;
                this.question.setText(question);
                this.buy.setText(left);
                this.cancel.setText(right);
                this.buystate = state;
                //resize the prompt layout to a new size
                this.holder.setSize(400, this.buy.getHeight() + height);

                Tween.to(this.holder, ProgressBarAnimation.POSITION_XY, 0.5f)
                        .target(this.holder.getX(), Tanks.cameraHeight/2 - this.holder.getHeight()/2)
                        .setCallbackTriggers(TweenCallback.END)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                if(type == TweenCallback.END){
                                    bgCover.setVisible(true);
                                }
                            }
                        })
                        .start(tweenManager);

            }
            /**
             * hides the prompt stack.
             */
            public void hide(){

                this.bgCover.setVisible(false);
                Tween.to(this.holder, ProgressBarAnimation.POSITION_XY, 0.5f)
                        .target(this.holder.getX(), -this.holder.getHeight() * 1.2f)
                        .start(tweenManager);

            }

        }

    }
    //this class is the panel for adding the main player tank
    private class ChooseMainTank{

        Image bg, ChoosingBorder;
        ScrollPane optScrollPane;
        private Table optTable;
        private Label title;

        //table one
        Table tank1T, tank2T, tank3T,
                tank4T, tank5T, tank6T, tank7T;

        //progress bar image
        private Image speedprogess, armorprogress;
        //max dimensions for progress bar
        private float maxProgressBarWidth = 200;
        //labels for speed progressbar
        private Label speedLabel, speedValue,
                armorLabel, armorValue; //added the reload units to tell the client

        //text buttons
        private TextButton select;

        //padding between the tanks tables left and right
        float paddingLeft = 40, paddingRight = 40,
                paddingTop = 40, paddingBottom = 40;

        public ChooseMainTank(){

            //add a black background
            bg = new Image();
            bg.setDrawable(new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("panel"), 5, 5, 5, 5)));
            bg.setSize(Tanks.cameraWIDTH, Tanks.cameraHeight / 1.5f);
            bg.setPosition(0, Tanks.cameraHeight / 2f - bg.getHeight() / 2f);
            bg.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {}
            });

            //call the setup tanks
            setupChooseTanks();

            //add a title
            Label.LabelStyle titleStyle = new Label.LabelStyle();
            titleStyle.font = roomTitleFont;
            title = new Label("Choose a Tank", titleStyle);
            title.setPosition(Tanks.cameraWIDTH / 2 - title.getWidth() / 2,
                    bg.getY() + bg.getHeight() - title.getHeight() - bg.getHeight() / 10);

            //add the tank options scroll panel
            optScrollPane = new ScrollPane(optTable);
            optScrollPane.setSize(bg.getWidth(), 125);
            optScrollPane.setPosition(bg.getX() + 10, title.getY() - optScrollPane.getHeight() - paddingTop);

            //add the border
            ChoosingBorder = new Image();
            ChoosingBorder.setDrawable(new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("border"), 10, 10, 10, 10)));
            ChoosingBorder.setVisible(false);

            //set up the progress bars GUI
            setupProgressBars();

            //add the select button
            TextButton.TextButtonStyle selectStyle = new TextButton.TextButtonStyle();
            selectStyle.font = panelOptionFontBtn;
            selectStyle.up = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("blueup"), 10,10,10,10));
            selectStyle.down = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown"), 10,10,10,10));
            selectStyle.disabled = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown"), 10,10,10,10));
            selectStyle.pressedOffsetX = -1;
            selectStyle.pressedOffsetY = -1;
            select = new TextButton("Select", selectStyle);
            select.padLeft(30).padRight(30);
            select.setPosition(bg.getX() + bg.getWidth() - select.getWidth() - paddingRight, bg.getY() + paddingBottom);
            select.setDisabled(true);
            select.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(!select.isDisabled()){
                        //proceed to the next screen (choose a bullet) by calling hide on this class and disposing it
                        hideA();

                        //proceed to the adding other tanks
                        createStagesPanel.show();
                    }
                }
            });

            //add the actors to stage
            stage.addActor(bg);
            //add everything after
            stage.addActor(title);
            stage.addActor(optScrollPane);
            //add the progress bars with their labels and values to stage
            stage.addActor(speedprogess);
            stage.addActor(speedLabel);
            stage.addActor(speedValue);
            stage.addActor(armorprogress);
            stage.addActor(armorLabel);
            stage.addActor(armorValue);
            //add the select button
            stage.addActor(select);

            //add the border on top of everything
            stage.addActor(ChoosingBorder);

        }
        private void setupChooseTanks(){

            optTable = new Table();

            //style the names for each tank
            Label.LabelStyle tankNameStyle = new Label.LabelStyle();
            tankNameStyle.font = roomTitleFont;

            //add tank one
            tank1T = new Table();
            tank1T.padLeft(paddingLeft);
            tank1T.padRight(paddingRight);
            final Image tank1 = new Image();
            tank1.setDrawable(Assets.MenuFileNames.getTanksSkin().getDrawable(Tank.TANKSTYPES.E100.toString()));
            tank1T.add(tank1);
            tank1T.row();
            tank1T.add(new Label(Tank.TANKSTYPES.E100.toString(), tankNameStyle));
            tank1T.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Tanks.gameState.setTankType(Tank.TANKSTYPES.E100.toString());
                    //add the animation to the border
                    animateBorder(tank1T.getX(), optScrollPane.getY(), tank1T.getWidth(), tank1T.getHeight());
                }
            });

            //add tank two
            tank2T = new Table();
            tank2T.padLeft(paddingLeft);
            tank2T.padRight(paddingRight);
            Image tank2 = new Image();
            tank2.setDrawable(Assets.MenuFileNames.getTanksSkin().getDrawable(Tank.TANKSTYPES.KV2.toString()));
            tank2T.add(tank2);
            tank2T.row();
            tank2T.add(new Label(Tank.TANKSTYPES.KV2.toString(), tankNameStyle));
            tank2T.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Tanks.gameState.setTankType(Tank.TANKSTYPES.KV2.toString());
                    //add the animation to the border
                    animateBorder(tank2T.getX(), optScrollPane.getY(), tank2T.getWidth(), tank2T.getHeight());
                }
            });

            //add tank three
            tank3T = new Table();
            tank3T.padLeft(paddingLeft);
            tank3T.padRight(paddingRight);
            Image tank3 = new Image();
            tank3.setDrawable(Assets.MenuFileNames.getTanksSkin().getDrawable(Tank.TANKSTYPES.M6.toString()));
            tank3T.add(tank3);
            tank3T.row();
            tank3T.add(new Label(Tank.TANKSTYPES.M6.toString(), tankNameStyle));
            tank3T.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Tanks.gameState.setTankType(Tank.TANKSTYPES.M6.toString());
                    //add the animation to the border
                    animateBorder(tank3T.getX(), optScrollPane.getY(), tank3T.getWidth(), tank3T.getHeight());
                }
            });

            //add tank four
            tank4T = new Table();
            tank4T.padLeft(paddingLeft);
            tank4T.padRight(paddingRight);
            Image tank4 = new Image();
            tank4.setDrawable(Assets.MenuFileNames.getTanksSkin().getDrawable(Tank.TANKSTYPES.PZKPFWIV.toString()));
            tank4T.add(tank4);
            tank4T.row();
            tank4T.add(new Label(Tank.TANKSTYPES.PZKPFWIV.toString(), tankNameStyle));
            tank4T.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Tanks.gameState.setTankType(Tank.TANKSTYPES.PZKPFWIV.toString());
                    //add the animation to the border
                    animateBorder(tank4T.getX(), optScrollPane.getY(), tank4T.getWidth(), tank4T.getHeight());
                }
            });

            //add tank five
            tank5T = new Table();
            tank5T.padLeft(paddingLeft);
            tank5T.padRight(paddingRight);
            Image tank5 = new Image();
            tank5.setDrawable(Assets.MenuFileNames.getTanksSkin().getDrawable(Tank.TANKSTYPES.PZKPFWIVG.toString()));
            tank5T.add(tank5);
            tank5T.row();
            tank5T.add(new Label(Tank.TANKSTYPES.PZKPFWIVG.toString(), tankNameStyle));
            tank5T.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Tanks.gameState.setTankType(Tank.TANKSTYPES.PZKPFWIVG.toString());
                    //add the animation to the border
                    animateBorder(tank5T.getX(), optScrollPane.getY(), tank5T.getWidth(), tank5T.getHeight());
                }
            });

            //add tank six
            /* Disabled this tank for later
            Table tank6T = new Table();
            tank6T.padLeft(30);
            tank6T.padRight(30);
            Image tank6 = new Image();
            tank6.setDrawable(TanksSkin.getDrawable(Tank.TANKSTYPES.T34.toString()));
            tank6T.add(tank6);
            tank6T.row();
            tank6T.add(new Label(Tank.TANKSTYPES.T34.toString(), tankNameStyle));
            */

            //add tank seven
            tank7T = new Table();
            tank7T.padLeft(paddingLeft);
            tank7T.padRight(paddingRight);
            Image tank7 = new Image();
            tank7.setDrawable(Assets.MenuFileNames.getTanksSkin().getDrawable(Tank.TANKSTYPES.VK3601H.toString()));
            tank7T.add(tank7);
            tank7T.row();
            tank7T.add(new Label(Tank.TANKSTYPES.VK3601H.toString(), tankNameStyle));
            tank7T.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Tanks.gameState.setTankType(Tank.TANKSTYPES.VK3601H.toString());
                    //add the animation to the border
                    animateBorder(tank7T.getX(), optScrollPane.getY(), tank7T.getWidth(), tank7T.getHeight());
                }
            });

            //add all the tanks to the main table
            optTable.add(tank1T);
            optTable.add(tank2T);
            optTable.add(tank3T);
            optTable.add(tank4T);
            optTable.add(tank5T);
            //optTable.add(tank6T); Tank 6 is no longer available available in this version of the game
            optTable.add(tank7T);

        }
        private void setupProgressBars(){
            //label styles for labels of progress bars
            Label.LabelStyle progressLabelStyles = new Label.LabelStyle();
            progressLabelStyles.font = roomTitleFont;

            //speed GUI'S
            //speed label
            speedLabel = new Label("Speed ", progressLabelStyles);
            speedLabel.setPosition(bg.getX() + paddingLeft, optScrollPane.getY() - 40 - paddingTop);
            //speed progress bar
            speedprogess = new Image();
            speedprogess.setDrawable(new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("progress"), 5, 5, 5, 5)));
            speedprogess.setPosition(speedLabel.getX() + speedLabel.getWidth() + paddingLeft*4, speedLabel.getY());
            speedprogess.setSize(10, speedLabel.getHeight());
            //speed value label
            speedValue = new Label("0", progressLabelStyles);
            speedValue.setPosition(speedprogess.getX() + maxProgressBarWidth + paddingLeft/3, speedprogess.getY());

            //armor GUI'S
            armorLabel = new Label("Armor ", progressLabelStyles);
            armorLabel.setPosition(bg.getX() + paddingLeft, speedLabel.getY() - 40 - paddingTop);
            //armor progress bar
            armorprogress = new Image();
            armorprogress.setDrawable(new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("progress"), 5, 5, 5, 5)));
            armorprogress.setPosition(speedprogess.getX(), armorLabel.getY());
            armorprogress.setSize(10, armorLabel.getHeight());
            //armor value label
            armorValue = new Label("0", progressLabelStyles);
            armorValue.setPosition(speedValue.getX(), armorprogress.getY());

        }
        private void animateBorder(float x, float y, float width, float height){
            //plus padding left 30 / 3 = 10px
            x+=8;
            //add padding left and right
            x -= 5;
            width += 10;
            //add padding to the top and bottom
            y -= 10;
            height += 30;
            //set visibility of choosing bar
            ChoosingBorder.setVisible(true);
            //enable the select button
            select.setDisabled(false);

            //animate the progress bars
            animateProgressBars();

            //animate the choosing box surrounding the chosen tank
            Tween.to(ChoosingBorder, Box2dActorAnimation.POSITION_XY, 0.5f)
                    .target(x, y)
                    .start(tweenManager);
            Tween.to(ChoosingBorder, Box2dActorAnimation.ALLDIMEN, 0.5f)
                    .target(width, height)
                    .start(tweenManager);

        }
        private void animateProgressBars(){

            //speed animation
            //set the value of the speed value
            Tween.to(speedValue, Box2dActorAnimation.NUMBER, 0.5f)
                    .target( Tank.GetMaxTankSpeed(Tank.TANKSTYPES.valueOf(Tanks.gameState.getUserTankType()) ) )
                    .start(tweenManager);
            //animate the speed progress bar change
            Tween.to(speedprogess, Box2dActorAnimation.WIDTH, 0.5f)
                    .target( (Tank.GetMaxTankSpeed(Tank.TANKSTYPES.valueOf(Tanks.gameState.getUserTankType())) * maxProgressBarWidth)/100 )
                    .start(tweenManager);

            //armor animation
            //set the value of the armor value
            Tween.to(armorValue, Box2dActorAnimation.NUMBER, 0.5f)
                    .target( Tank.GetMaxTankArmor(Tank.TANKSTYPES.valueOf(Tanks.gameState.getUserTankType())) )
                    .start(tweenManager);
            //animate the speed progress bar change
            Tween.to(armorprogress, Box2dActorAnimation.WIDTH, 0.5f)
                    .target((Tank.GetMaxTankArmor(Tank.TANKSTYPES.valueOf(Tanks.gameState.getUserTankType())) * maxProgressBarWidth) / 100)
                    .start(tweenManager);

        }

        public void hide(){
            title.setVisible(false);
            bg.setVisible(false);
            ChoosingBorder.setVisible(false);
            select.setVisible(false);
            //hide the progress bars
            speedLabel.setVisible(false);
            speedprogess.setVisible(false);
            speedValue.setVisible(false);
            armorprogress.setVisible(false);
            armorLabel.setVisible(false);
            armorValue.setVisible(false);
            //hide the scroll pane
            optScrollPane.setVisible(false);
        }
        public void show(){
            title.setVisible(true);
            bg.setVisible(true);
            ChoosingBorder.setVisible(true);
            select.setVisible(true);
            //hide the progress bars
            speedLabel.setVisible(true);
            speedprogess.setVisible(true);
            speedValue.setVisible(true);
            armorprogress.setVisible(true);
            armorLabel.setVisible(true);
            armorValue.setVisible(true);
            //hide the scroll pane
            optScrollPane.setVisible(true);
        }
        public void hideA(){
            Tween.to(title, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(0)
                    .setCallbackTriggers(TweenCallback.END)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            if(type == TweenCallback.END){
                                hide();
                            }
                        }
                    })
                    .start(tweenManager);
            Tween.to(bg, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(0)
                    .start(tweenManager);
            Tween.to(ChoosingBorder, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(0)
                    .start(tweenManager);
            Tween.to(select, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(0)
                    .start(tweenManager);
            Tween.to(speedLabel, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(0)
                    .start(tweenManager);
            Tween.to(speedprogess, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(0)
                    .start(tweenManager);
            Tween.to(speedValue, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(0)
                    .start(tweenManager);
            Tween.to(armorprogress, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(0)
                    .start(tweenManager);
            Tween.to(armorLabel, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(0)
                    .start(tweenManager);
            Tween.to(armorValue, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(0)
                    .start(tweenManager);
            Tween.to(optScrollPane, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(0)
                    .start(tweenManager);
        }
        public void showA(){
            Tween.to(title, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(1)
                    .setCallbackTriggers(TweenCallback.BEGIN)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            if (type == TweenCallback.BEGIN) {
                                show();
                            }
                        }
                    })
                    .start(tweenManager);
            Tween.to(bg, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(1)
                    .start(tweenManager);
            Tween.to(ChoosingBorder, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(1)
                    .start(tweenManager);
            Tween.to(select, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(1)
                    .start(tweenManager);
            Tween.to(speedLabel, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(1)
                    .start(tweenManager);
            Tween.to(speedprogess, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(1)
                    .start(tweenManager);
            Tween.to(speedValue, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(1)
                    .start(tweenManager);
            Tween.to(armorprogress, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(1)
                    .start(tweenManager);
            Tween.to(armorLabel, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(1)
                    .start(tweenManager);
            Tween.to(armorValue, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(1)
                    .start(tweenManager);
            Tween.to(optScrollPane, Box2dActorAnimation.ALPHA, 0.5f)
                    .target(1)
                    .start(tweenManager);
        }

        public void dispose(){

            //first clear the listeners
            title.clear();
            bg.clear();
            ChoosingBorder.clear();
            select.clear();
            //hide the progress bars
            speedLabel.clear();
            speedprogess.clear();
            speedValue.clear();
            armorprogress.clear();
            armorLabel.clear();
            armorValue.clear();
            //hide the scroll pane
            optScrollPane.clear();

            //remove the actors
            title.remove();
            bg.remove();
            ChoosingBorder.remove();
            select.remove();
            //hide the progress bars
            speedLabel.remove();
            speedprogess.remove();
            speedValue.remove();
            armorprogress.remove();
            armorLabel.remove();
            armorValue.remove();
            //hide the scroll pane
            optScrollPane.remove();
        }

    }
    //this class is panel for choosing the bullets
    private class ChooseBullets{

        Table mainTable;

        Label totalButtons;

        bulletButton normalBtn, electricBtn;

        int maxNumberOfBulletTypes = 2;

        public ChooseBullets(){

            /**
             * setup the gui for choosing bullets
             */
            mainTable = new Table();
            mainTable.setSize(Tanks.cameraWIDTH / 1.3f, Tanks.cameraHeight / 1.5f);
            mainTable.setPosition(Tanks.cameraWIDTH / 2 - mainTable.getWidth() / 2,
                    Tanks.cameraHeight / 2 - mainTable.getHeight() / 2);
            mainTable.setBackground(new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("panel"), 5, 5, 5, 5)));

            //add a title
            Label.LabelStyle totalButtonsStyle = new Label.LabelStyle();
            totalButtonsStyle.font = roomTitleFont;
            Label title = new Label("Choose Bullets to carry", totalButtonsStyle);
            mainTable.add(title).padBottom(20).colspan(2);
            mainTable.row();

            //add the total bullets that can be held

            totalButtons = new Label("Total Bullets held: 0/"+maxNumberOfBulletTypes, totalButtonsStyle);
            //add it to the main table
            mainTable.add(totalButtons).padBottom(20).colspan(2);
            mainTable.row();

            //add the scroll panel
            Table scrolTable = new Table();
            ScrollPane scrollPane = new ScrollPane(scrolTable);
            mainTable.add(scrollPane).width(mainTable.getWidth()/1.5f).height(mainTable.getHeight()/2.5f).colspan(2);

            //add the buttons
            for(Weapon.BULLETSTYPE bulletstype : Weapon.BULLETSTYPE.values()){
                addBullet(bulletstype, scrolTable);
            }

            //add the buttons for next and back
            mainTable.row();
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font = panelOptionFontBtn;
            textButtonStyle.fontColor = Color.WHITE;
            textButtonStyle.up = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("blueup"), 5,5,5,5));
            textButtonStyle.down = new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("bluedown"), 5,5,5,5));
            textButtonStyle.up.setTopHeight(10);
            textButtonStyle.up.setBottomHeight(10);
            textButtonStyle.up.setLeftWidth(40);
            textButtonStyle.up.setRightWidth(40);
            textButtonStyle.down.setTopHeight(10);
            textButtonStyle.down.setBottomHeight(10);
            textButtonStyle.down.setLeftWidth(40);
            textButtonStyle.down.setRightWidth(40);
            textButtonStyle.downFontColor = Color.GRAY;
            TextButton back = new TextButton("back", textButtonStyle), next = new TextButton("Start", textButtonStyle);
            mainTable.add(back).padRight(100).padLeft(-100).padTop(20);
            mainTable.add(next).padLeft(100).padRight(-100).padTop(20);
            //add listeners for buttons
            back.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    hide();
                    createStagesPanel.show();
                }
            });
            next.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    hide();
                    Assets.disposeMenuAssets();
                    ScreenManager.setScreen(new MultiGameScreen());
                }
            });
            //end of the buttons

            //set the total bullets selected to be displayed on the screen
            totalButtons.setText("Total Bullets held: " + getTotalBulletsAdded() + "/" + maxNumberOfBulletTypes);
            //finally add the stage
            stage.addActor(mainTable);
            //hide this panel
            hide();

        }

        //add the individual weapon bullet buttons
        private void addBullet(Weapon.BULLETSTYPE bulletstype, Table scrollTable){
            switch (bulletstype){
                case BULLET1:
                        normalBtn = new bulletButton(new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("marineup"), 5,5,5,5)),
                                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getBulletSkin().getRegion("bullet1_icon"))),
                                bulletstype, scrollTable, Tanks.gameState.weapons.getNormaBulletTotal());
                    break;
                case ELECTRICBULLET:
                        electricBtn = new bulletButton(new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getMenuSkin().getRegion("marineup"), 5,5,5,5)),
                                new NinePatchDrawable(new NinePatch(Assets.MenuFileNames.getBulletSkin().getRegion("electric_icon"))),
                                bulletstype, scrollTable, Tanks.gameState.weapons.getElectricBulletTotal());
                    break;
            }
        }
        /**
         * sets the total number shown on the weapon bullet button.
         * @param bulletstype
         *
         * @param total
         */
        public void setBulletNumber(Weapon.BULLETSTYPE bulletstype, int total){
            switch (bulletstype){
                case ELECTRICBULLET:
                    electricBtn.setTotalBullets(total);
                    break;
            }
        }

        public void hide(){
            mainTable.setVisible(false);
        }
        public void show(){
            mainTable.setVisible(true);
        }

        //class that creates each button weapon
        private class bulletButton{

            //button's bullet type
            public Weapon.BULLETSTYPE type;

            //stack
            private Stack stack;

            //background
            private Image bg;

            //icon
            private Image icon;

            //number of bullets
            Label number;

            //expansion ratio
            private float expansionX = 10, expansionY = 10;

            //total number of bullets
            private int totalBullets = 0;

            //reference to the table containing this stack
            Table table;

            //this tells whether the button has been disabled or enabled
            private boolean enabled = true;

            /**
             * weapon button for interface. this generates a button for the particular bullet type.
             * @param bg
             * background for the button
             * @param icon
             * icon to be inserted to display the bullet type
             * @param type
             * bullet's type
             * @param table
             * table that this button will placed in.
             */
            public bulletButton(NinePatchDrawable bg, NinePatchDrawable icon,
                                final Weapon.BULLETSTYPE type, Table table, int totalBullets){

                this.table = table;
                this.totalBullets = totalBullets;

                //create the stack
                stack = new Stack();

                //set images
                this.bg = new Image(bg);
                this.icon = new Image(icon);

                //set the bullet type
                this.type = type;

                //set the label holding the number of bullets
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = panelOptionFontBtn;
                labelStyle.fontColor = Color.ORANGE;
                number = new Label(""+( (totalBullets >= 0)?totalBullets: "infinite" ),labelStyle);

                //add a table that will contain the image to the bullet1 icon
                Table iconTable = new Table();
                iconTable.add(this.icon)
                        .width(this.icon.getWidth() * 1.3f).height(this.icon.getHeight() * 1.3f);
                iconTable.row();
                iconTable.add(number);
                //add the images to the stack
                stack.add(this.bg);
                stack.add(iconTable);
                //add it to the weapons display manager
                this.table.add(stack)
                        .width(100).height(120)
                        .padBottom(20).padLeft(10).padRight(10);

                stack.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                            if (enabled) {
                                Disable();
                                animateDeselected();
                                saveState();
                            } else if (getTotalBulletsAdded() < maxNumberOfBulletTypes){
                                Enable();
                                animateSelected();
                                saveState();
                            }
                            totalButtons.setText("Total Bullets held: "+getTotalBulletsAdded()+"/"+maxNumberOfBulletTypes);
                    }
                });

                //disable all the buttons except the normal bullet button
                switch (this.type){
                    case BULLET1:
                        break;
                    default:
                        Disable();
                        this.table.getCell(this.stack).width(100 - expansionY).height(120 - expansionX);
                        break;
                }

                saveState();

            }

            /**
             * animate the button to selected.
             */
            public void animateSelected(){

                Tween.to(this.table.getCell(this.stack), CellAnimation.WIDTH_HEIGHT, 0.2f)
                        .target(this.stack.getWidth() + expansionX, this.stack.getHeight() + expansionY)
                        .start(tweenManager);

            }
            /**
             * animate the button to deselected
             */
            public void animateDeselected(){

                Tween.to(this.table.getCell(this.stack), CellAnimation.WIDTH_HEIGHT, 0.2f)
                        .target(this.stack.getWidth() - expansionX, this.stack.getHeight() - expansionY)
                        .start(tweenManager);

            }

            /**
             * enables the effect for enabled button.
             */
            public void Enable(){
                stack.getColor().a = 1;
                this.enabled = true;
            }

            /**
             * disable the effect for disabled button.
             */
            public void Disable(){
                stack.getColor().a = 0.7f;
                this.enabled = false;
            }

            /**
             * checks whether the button is enabled or not. (pressed or not)
             */
            public boolean isEnabled(){
                return enabled;
            }

            /**
             * gets the total number of bullets
             */
            public int getTotalBullets(){
                return totalBullets;
            }

            /**
             * sets the number of bullets
             * @param number
             * total number of bullets to be displayed
             */
            public void setTotalBullets(int number){
                this.totalBullets = number;
                this.number.setText(""+number);
            }

            private void saveState(){

                switch (this.type){
                    case BULLET1:
                        Tanks.gameState.weapons.setNormalBulletState(this.enabled);
                        break;
                    case ELECTRICBULLET:
                        Tanks.gameState.weapons.setElectricBulletState(this.enabled);
                        break;
                }

            }

        }

        //count the number of bullets added
        private int getTotalBulletsAdded(){
            int total = 0;

            //check normal buton
            if(normalBtn.isEnabled()){
                total++;
            }

            //check electric button
            if(electricBtn.isEnabled()){
                total++;
            }

            return total;
        }

    }
    //end of panels--->

    //usefull methods
    public void performClick(Actor actor) {
        Array<EventListener> listeners = actor.getListeners();
        for(int i=0;i<listeners.size;i++)
        {
            if(listeners.get(i) instanceof ClickListener){
                ((ClickListener)listeners.get(i)).clicked(null, 0, 0);
            }
        }
    }
    //end of usefull methods

    @Override
    public void update() {

        camera.update();

        //update the market
        market.update();

        stage.act();

        tweenManager.update(Gdx.graphics.getDeltaTime());

    }

    @Override
    public void render(SpriteBatch sb) {

        Gdx.gl.glClearColor(0.34f, 0.03f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(camera.combined);

        stage.draw();

        createStagesPanel.render(sb);

        //render the market on top of everything
        market.render();

    }

    @Override
    public void resize(int width, int height) {
        camera.resize();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /**
     * unit item map buy state
     */
    public enum BUYSTATE{
        COINS, MONEY
    }
    /**
     * unit item map state
     */
    public enum ITEMSTATE{
        LOCKED, UNLOCKED
    }

}
