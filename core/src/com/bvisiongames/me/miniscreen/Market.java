package com.bvisiongames.me.miniscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bvisiongames.me.Tanks;
import com.bvisiongames.me.Tweenanimations.CellAnimation;
import com.bvisiongames.me.Tweenanimations.ProgressBarAnimation;
import com.bvisiongames.me.Weapons.ElectricBullet;
import com.bvisiongames.me.Weapons.Weapon;
import com.bvisiongames.me.settings.JarCoins;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by ahzji_000 on 12/4/2015.
 * this class displays the market for the game.
 */
public class Market {

    private Stage stage;

    //tween manager
    TweenManager tweenManager;

    //market icon
    public MarketIcon marketIcon;

    //market layout
    public MarketLayout marketLayout;

    /**
     * initiator.
     */
    public Market(){

        stage = new Stage(new FitViewport(Tanks.cameraWIDTH, Tanks.cameraHeight));

    }

    //setters
    /**
     * set the tween manager.
     * @param tweenManager
     * tween manager that will be used to animate.
     */
    public void setTween(TweenManager tweenManager){
        this.tweenManager = tweenManager;
    }
    /**
     * this sets the click listener priority of this stage.
     * @param inputplexer
     * takes in the inputmultiplexer.
     */
    public void setListener(InputMultiplexer inputplexer){
        inputplexer.addProcessor(stage);
    }
    /**
     * this method just sets the processor to this stage
     */
    public void setListener(){
        Gdx.input.setInputProcessor(stage);
    }
    /**
     * set the Drawables of the market icon.
     * @param background
     * the background of the market icon.
     * @param marketIcon
     * setup the market icon.
     * @param font
     * setup the font of the numbers of coins.
     */
    public void setMarketIconDrawables(Drawable background, Drawable marketIcon, Drawable coinIcon, BitmapFont font){
        //setup the market icon
        this.marketIcon = new MarketIcon(background, marketIcon, coinIcon, font);
    }
    /**
     * set the drawables of the market icon.
     * @param bg
     * background of the layout market
     * @param backCover
     * background cover page drawable.
     */
    public void setMarketLayoutDrawables(Drawable bg, Drawable backCover){
        marketLayout = new MarketLayout(bg, backCover);
    }
    /**
     * show the market icon.
     */
    public void showMarketIcon(){
        marketIcon.show();
    }
    /**
     * hides the market icon
     */
    public void hideMarketIcon(){
        marketIcon.hide();
    }
    /**
     * show the store layout.
     */
    public void showMarketLayout(){
        marketLayout.show();
    }
    /**
     * hide the market layout.
     */
    public void hideMarketLayout(){
        marketLayout.hide();
    }
    /**
     * hide all the market UI.
     */
    public void hideAll(){
        hideMarketIcon();
        hideMarketLayout();
    }
    /**
     * show all the market UI.
     */
    public void showAll(){
        showMarketIcon();
        showMarketLayout();
    }
    //end of setters


    //getters


    //end of getters

    /**
     * update the stage
     */
    public void update(){
        stage.act();
    }

    /**
     * render the stage
     */
    public void render(){
        stage.draw();
    }

    /**
     * dispose the stage
     */
    public void dispose(){
        stage.dispose();
    }

    /**
     * this class draws the icon of the market at the top of the stack actor.
     */
    public class MarketIcon{

        //table of the market icon
        private Table table;
        private Stack stack;

        //coin label
        private Label coinLabel;

        //dimensions of market icon
        private float marketPad = 10, marketWidth = 50, marketHeight = 50;

        //dimensions of coin icon
        private float coinPad = 10, coinWidth = 40, coinHeight = 40;

        /**
         * initiator.
         */
        public MarketIcon(Drawable bg, Drawable marketIcon, Drawable coinIcon, BitmapFont font){

            //add the main table
            this.table = new Table();
            this.stack = new Stack();
            this.stack.setHeight(70);

            //add the background
            this.stack.add(new Image(bg));

            //add the market icon
            this.table.add(new Image(marketIcon))
                    .width(marketWidth).height(marketHeight).pad(marketPad);

            //add the coin icon
            this.table.add(new Image(coinIcon))
                    .width(coinWidth).height(coinHeight).pad(coinPad);

            //add the coin label
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = font;
            coinLabel = new Label(""+Tanks.jarCoins.getCoins(), labelStyle);
            this.table.add(coinLabel);

            refreshTable();

            //add the listener
            this.stack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(marketLayout.isVisible()){
                        hideMarketLayout();
                    }else{
                        showMarketLayout();
                    }
                }
            });

            //add it to the stage
            this.stack.add(this.table);
            stage.addActor(this.stack);

        }

        //setters
        /**
         * refreshes the position and width of the table
         */
        private void refreshTable(){
            this.stack.setWidth( marketWidth + marketPad*2 + coinWidth + coinPad*2 + coinLabel.getWidth() + coinPad*2 );
            //set the position of this table
            this.stack.setPosition(stage.getWidth() - this.stack.getWidth(),
                    stage.getHeight() - this.stack.getHeight());
        }
        /**
         * set the coins to display
         * @param coins
         * coins to be displayed.
         */
        public void setCoins(int coins){
            this.coinLabel.setText(""+coins);
            refreshTable();
        }
        /**
         * shows the market icon
         */
        public void show(){
            table.setVisible(true);
        }
        /**
         * hides the market icon
         */
        public void hide(){
            table.setVisible(false);
        }
        //end of setters

        //getters
        /**
         * get the stack actor.
         */
        public Stack getHolder(){
            return this.stack;
        }
        //end of getters


    }

    /**
     * this class draws the store layout that displays the items that will be sold.
     */
    public class MarketLayout{

        //table that holds everything in it.
        private Stack stack;
        private Table table, holder; //master table and the holder table for the tabs

        //this actor is the background cover
        private Image bgCover;

        //tables for each items type
        private Table weaponTable, coinTable;
        public ItemTabScroll coinsTab, weaponTab;

        //total coins display in the market layout
        private Label titleCoins;

        //interface to communicate any bought items
        private Communicator Interface;

        /**
         * initiator.
         * add the objects to the market layout by calling the methods listed:
         * - setTitle
         * - setTabs (coinsTab,
         * - setCoinTab (displays the total coins)
         * - setCoinDisplay
         * - setScrollPane
         * - addWeaponScrollTable ||
         * - addLowerButtons
         * @param background
         * The background image that will display.
         * @param backgroundCover
         * the background cover page.
         */
        public MarketLayout(Drawable background, Drawable backgroundCover){

            //initiate the tabs and scroll table
            this.coinsTab = new ItemTabScroll();
            this.weaponTab = new ItemTabScroll();

            //setup the background cover
            this.bgCover = new Image(backgroundCover);
            this.bgCover.setSize(Tanks.cameraWIDTH, Tanks.cameraHeight);
            this.bgCover.getColor().a = 0.7f;
            this.bgCover.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    hideMarketLayout();
                }
            });

            //initiate the table and the stack
            this.stack = new Stack();
            this.stack.setSize(Tanks.cameraWIDTH, Tanks.cameraHeight / 1.3f);
            this.stack.setPosition(Tanks.cameraWIDTH / 2 - this.stack.getWidth() / 2,
                    Tanks.cameraHeight / 2 - this.stack.getHeight() / 2);
            this.stack.add(new Image(background));
            this.table = new Table();
            this.stack.add(this.table);


            //add the table to the stage and the backgroundCover
            stage.addActor(this.bgCover);
            stage.addActor(this.stack);

            this.stack.setVisible(false);

        }

        //setters
        /**
         * set the title of the market layout title.
         * @param titleTxt
         * the string to be displayed as a title.
         * @param font
         * the font of the title.
         */
        public void setTitle(String titleTxt, BitmapFont font){
            //add the title
            Label.LabelStyle titleStyle = new Label.LabelStyle();
            titleStyle.font = font;
            Label title = new Label(titleTxt, titleStyle);
            this.table.add(title).padBottom(20).colspan(2);
            this.table.row();

            //also add the holder table for the tabs
            this.holder = new Table();
            //add the holder to the table
            this.table.add(this.holder).colspan(2);
            this.table.row();

        }
        /**
         * setup the coin display in the market layout in the tabs table (holder)
         * @param bg
         * background of the tab.
         * @param icon
         * icon of the tab.
         * @param coins
         * the total number of coins.
         * @param font
         * the font of the total coins displayed.
         */
        public void setCoinTab(Drawable bg, Drawable icon, String coins, BitmapFont font){

            //setup the coin display in the market layout
            Image titleCoinImg = new Image(icon);
            Label.LabelStyle titleCoinStyle = new Label.LabelStyle();
            titleCoinStyle.font = font;
            titleCoinStyle.fontColor = Color.WHITE;
            titleCoins = new Label(coins, titleCoinStyle);
            //add it to a table
            Table coinTable = new Table();
            coinTable.setBackground(bg);
            coinTable.getBackground().setTopHeight(15);
            coinTable.getBackground().setBottomHeight(15);
            coinTable.getBackground().setRightWidth(30);
            coinTable.getBackground().setLeftWidth(30);
            coinTable.add(titleCoinImg).width(titleCoins.getHeight()).height(titleCoins.getHeight());
            coinTable.add(titleCoins);

            //add the coin display table to the holder table
            this.holder.add(coinTable);

        }
        /**
         * set up the listeners for the tabs.
         */
        public void setListeners(){

            coinsTab.getTab().addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    if(!coinsTab.isOn()){
                        coinsTab.showTable();
                    }
                    if(weaponTab.isOn()){
                        weaponTab.hideTable();
                    }

                }
            });

            weaponTab.getTab().addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    if(!weaponTab.isOn()){
                        weaponTab.showTable();
                    }
                    if(coinsTab.isOn()){
                        coinsTab.hideTable();
                    }

                }
            });

        }
        /**
         * setup the tables for each type items.
         */
        public void setItemsTables(){

            this.coinTable = new Table();
            this.weaponTable = new Table();

            Stack stack = new Stack();
            stack.add(this.coinTable);
            stack.add(this.weaponTable);

            this.table.add(stack).colspan(2);
        }
        /**
         * add the weapon table in the scroll Pane.
         * @param Bgskin
         * the skin that the layout will look for the asset resources for the background.
         * @param iconSkin
         * the skin that the layout will use to look for the icon resource.
         * @param fontTitle
         * font of the title of this section.
         * @param font
         * font of the text inside each item.
         */
        public void addWeaponScrollTable(Skin Bgskin, Skin iconSkin, BitmapFont fontTitle, BitmapFont font){

            Table scrollTable = new Table();
            ScrollPane scrollPane = new ScrollPane(scrollTable);

            //add electric bullet
            new UnitItem(
                    new NinePatchDrawable(new NinePatch(Bgskin.getRegion("marineup"), 5,5,5,5)),
                    new NinePatchDrawable(new NinePatch(iconSkin.getRegion("electric_icon"))),
                    new NinePatchDrawable(new NinePatch(Bgskin.getRegion("coin"))),
                    Weapon.BULLETSTYPE.ELECTRICBULLET, scrollTable, Tanks.gameState.weapons.getElectricBulletTotal(), font,
                    ElectricBullet.PRICE, ElectricBullet.INFO);

            //add the title for the weapon store
            Label.LabelStyle style = new Label.LabelStyle();
            style.font = fontTitle;
            style.fontColor = Color.GRAY;
            Label title = new Label(" Weapons", style);

            //add the objects to the scroll table
            this.weaponTable.add(title).padBottom(20).align(Align.left);
            this.weaponTable.row();
            this.weaponTable.add(scrollPane);

            //set the weapon table to associate with the weapon tab.
            this.weaponTab.setTable(this.weaponTable);

            //and hide the weapon table and set the alpha to 0
            //this.weaponTable.setVisible(false);
            //this.weaponTable.getColor().a = 0;

        }
        /**
         * add the coin table in the scroll Pane
         * @param Bgskin
         * the skin that the layout will look for the asset resources for the background.
         * @param iconSkin
         * the skin that the layout will use to look for the icon resource.
         * @param fontTitle
         * font of the title of this section.
         * @param font
         * font of the text inside each item.
         */
        public void addCoinScrollTable(Skin Bgskin, Skin iconSkin, BitmapFont fontTitle, BitmapFont font){

            Table scrollTable = new Table();
            ScrollPane scrollPane = new ScrollPane(scrollTable);

            //add coin item
            new UnitItem(
                    new NinePatchDrawable(new NinePatch(Bgskin.getRegion("marineup"), 5,5,5,5)),
                    new NinePatchDrawable(new NinePatch(Bgskin.getRegion("coin"))),
                    new NinePatchDrawable(new NinePatch(Bgskin.getRegion("coin"))),
                    Weapon.BULLETSTYPE.COIN, scrollTable, Tanks.jarCoins.getCoins(), font,
                    JarCoins.PRICE, JarCoins.INFO);

            //add the title for the weapon store
            Label.LabelStyle style = new Label.LabelStyle();
            style.font = fontTitle;
            style.fontColor = Color.GRAY;
            Label title = new Label(" Coins", style);

            //add the objects to the scroll table
            this.coinTable.add(title).padBottom(20).align(Align.left);
            this.coinTable.row();
            this.coinTable.add(scrollPane);

            //set the coins table to associate with the coins tab.
            this.coinsTab.setTable(this.coinTable);

        }
        /**
         * add the lower buttons suchs as close.
         * @param font
         * the font of the buttons.
         * @param up
         * the drawable of the up button.
         * @param down
         * the drawable of the down button.
         */
        public void addLowerButtons(BitmapFont font, Drawable up, Drawable down){
            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
            style.font = font;
            style.fontColor = Color.WHITE;
            style.up = up;
            style.down = down;
            style.up.setLeftWidth(30);
            style.up.setRightWidth(30);
            style.up.setTopHeight(10);
            style.up.setBottomHeight(10);
            style.down.setLeftWidth(30);
            style.down.setRightWidth(30);
            style.down.setTopHeight(10);
            style.down.setBottomHeight(10);
            style.downFontColor = Color.GRAY;
            TextButton close = new TextButton("Close", style);
            this.table.row();
            this.table.add(close).align(Align.left).padLeft(50);
            close.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    hideMarketLayout();
                }
            });
        }
        /**
         * hides the layout
         */
        public void hide(){
            bgCover.setVisible(false);
            //animate the hiding of the layout
            Tween.to(this.stack, ProgressBarAnimation.POSITION_XY, 0.5f)
                    .target(this.stack.getX(), - this.stack.getHeight())
                    .setCallbackTriggers(TweenCallback.END)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            if (type == TweenCallback.END) {
                                stack.setVisible(false);
                            }
                        }
                    })
                    .start(tweenManager);
        }
        /**
         * shows the layout
         */
        public void show(){
            bgCover.setVisible(true);
            this.stack.setVisible(true);
            //animate the showing of the layout
            Tween.to(this.stack, ProgressBarAnimation.POSITION_XY, 0.5f)
                    .target(this.stack.getX(), Tanks.cameraHeight / 2 - this.stack.getHeight() / 2)
                    .start(tweenManager);
        }
        /**
         * sets the coin display in the market layout.
         * @param coins
         * total number of coins to display in string format.
         */
        private void setMarketTotalCoins(String coins){
            this.titleCoins.setText(coins);
        }
        /**
         * set the interface.
         * @param Interface
         * interface Communicator
         */
        public void setInterface(Communicator Interface){
            this.Interface = Interface;
        }
        //end of setters

        //getters
        /**
         * gets the coin tab in the layout.
         */
        public ItemTabScroll getCoinsTab(){
            return this.coinsTab;
        }
        /**
         * checks whether the market layout is visible or not.
         */
        public boolean isVisible(){
            return this.stack.isVisible();
        }
        //end of getters

        //class that creates a tab and a scrollpane
        public class ItemTabScroll{

            /**
             * tabs variables.
             */
            private Image selectImg;
            final Stack Tab = new Stack(); //the stack that contains everything
            private boolean clicked = false;
            private Table itemTable;

            /**
             * adds the tabs.
             * @param bg
             * background of the tab.
             * @param bgSelect
             * drawable when tab is selected.
             * @param Icon
             * icon to be displayed inside the tab.
             * @param pad
             * an array of float that needs 4 float inside, starting from top, left, bottom, right.
             */
            public void addTab(Drawable bg, Drawable bgSelect, Drawable Icon,
                               float[] pad){

                selectImg = new Image(bgSelect);

                Tab.add(new Image(bg));
                //add a selected drawable image
                selectImg.getColor().a = 0;
                Tab.add(selectImg);
                //setup a mini table to display the icon of the coins tab
                Table tmpTable = new Table();
                tmpTable.add(new Image(Icon))
                        .width(50 - 5).height(50 - 5).pad(10);
                Tab.add(tmpTable);

                //add this to the holder table tab
                holder.add(Tab).width(200).pad(pad[0], pad[1], pad[2], pad[3]);

            }

            /**
             * returns the tab actor.
             */
            public Stack getTab(){
                return this.Tab;
            }

            /**
             * add the table that it will hide or show.
             *
             */
            public void setTable(Table itemTable){
                this.itemTable = itemTable;
            }

            /**
             * show the associated table.
             */
            public void showTable(){

                Tween.to(this.selectImg, ProgressBarAnimation.ALPHA, 0.5f)
                        .target(0)
                        .start(tweenManager);

                this.itemTable.setVisible(true);
                Tween.to(this.itemTable, ProgressBarAnimation.ALPHA, 0.5f)
                        .target(1)
                        .start(tweenManager);

                this.clicked = true;

            }
            /**
             * hide the associated table.
             */
            public void hideTable(){

                Tween.to(this.selectImg, ProgressBarAnimation.ALPHA, 0.5f)
                        .target(1)
                        .start(tweenManager);

                Tween.to(this.itemTable, ProgressBarAnimation.ALPHA, 0.5f)
                        .target(0)
                        .setCallbackTriggers(TweenCallback.END)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                if (type == TweenCallback.END) {
                                    itemTable.setVisible(false);
                                }
                            }
                        })
                        .start(tweenManager);

                this.clicked = false;

            }

            /**
             * checks whether the Tab has been clicked.
             */
            public boolean isOn(){
                return this.clicked;
            }

            /**
             * turn off the click.
             */
            public void clickOn(){
                showTable();
            }

            /**
             * turn on the click.
             */
            public void clickOff(){
                hideTable();
            }

        }
        //class that creates each button weapon
        private class UnitItem{

            //button's bullet type
            public Weapon.BULLETSTYPE type;

            //stack
            private Stack stack;

            //background
            private Image bg;

            //icon
            private Image icon;

            //number of items displayed
            private Label number;

            //expansion ratio
            private float expansionX = 20, expansionY = 5;

            //total number of bullets
            private int totalBullets = 0;

            //reference to the table containing this stack
            Table table;

            //price tag
            private float PRICE = 0;
            //item info
            private String INFO = "";

            //this tells whether the button has been disabled or enabled
            private boolean enabled = true;

            //this variable prevents from overanimating the clicking of the unitbutton
            private boolean animating = false;

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
             * @param font
             * font for the price tag.
             */
            public UnitItem(
                    NinePatchDrawable bg, NinePatchDrawable icon, Drawable currency,
                    final Weapon.BULLETSTYPE type, Table table, int totalBullets, BitmapFont font,
                    final float PRICE, String INFO){

                this.PRICE = PRICE;
                this.INFO = INFO;

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
                labelStyle.font = font;
                labelStyle.fontColor = Color.WHITE;
                number = new Label(""+( (totalBullets >= 0)?totalBullets: "infinite" ),labelStyle);

                //add a table that will contain the image to the bullet1 icon
                Table iconTable = new Table();
                iconTable.add(this.icon)
                        .width(this.icon.getWidth() * 1.5f).height(this.icon.getHeight() * 1.5f).colspan(2);
                iconTable.row();
                //add other info of this item.
                addOtherItemInfo(currency, iconTable, font);
                iconTable.row();
                //add the amount currently collected
                switch (this.type){
                    case COIN:
                        break;
                    default:
                        iconTable.add(number).align(Align.right).padTop(20).colspan(2);
                        break;
                }
                //add the images to the stack
                stack.add(this.bg);
                stack.add(iconTable);
                //add it to the weapons display manager
                this.table.add(stack)
                        .width(190).height(250)
                        .padBottom(20).padLeft(10).padRight(10).align(Align.left);

                stack.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        switch (type){
                            case COIN:

                                break;
                            default:
                                if (enabled && Tanks.jarCoins.hasEnough((int)PRICE)) {
                                    buyWithCoins();
                                }
                                break;
                        }
                    }
                });


            }


            /**
             * add other item info.
             * @param Dcurrency
             * Drawable of the currency.
             * @param iconTable
             * icon Table that these info will be added to.
             * @param font
             * font of the text to be displayed.
             */
            private void addOtherItemInfo(Drawable Dcurrency, Table iconTable, BitmapFont font){

                //add the price tag
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = font;
                labelStyle.fontColor = Color.ORANGE;
                Label priceTag = new Label(""+( (this.type != Weapon.BULLETSTYPE.COIN)? (int)this.PRICE: this.PRICE )+" ", labelStyle);
                iconTable.add(priceTag).padTop(20).align(Align.right);
                //add the currency
                Image currency = new Image(Dcurrency);
                iconTable.add(currency).padTop(20).align(Align.left)
                        .width(priceTag.getHeight()/1.3f).height(priceTag.getHeight() / 1.3f);
                iconTable.row();

                Label.LabelStyle labelStyle1 = new Label.LabelStyle();
                labelStyle1.font = font;
                labelStyle1.fontColor = Color.WHITE;
                Label infoTag = new Label(this.INFO, labelStyle1);
                iconTable.add(infoTag).colspan(2);

            }
            /**
             * buy the clicked unit item.
             */
            private void buyWithCoins(){

                //animate the button
                animateClicked();

                //add more bullets to this button to show we have more of this type of bullet
                this.totalBullets++;
                this.number.setText("" + totalBullets);

                //subtract from the total coins the user has
                Tanks.jarCoins.setCoins(Tanks.jarCoins.getCoins() - (int) PRICE);
                //save the total coins to the preference
                Tanks.gameState.setCoins(Tanks.jarCoins.getCoins());

                //tell the market layout's coin display the new number of coins
                setMarketTotalCoins(" "+Tanks.jarCoins.getCoins());

                //set the total items to the gamestate
                switch (type){
                    case ELECTRICBULLET:
                            Tanks.gameState.weapons.setElectricBulletTotal(totalBullets);
                        break;
                }

                //tell the market icon the new total coins
                marketIcon.setCoins(Tanks.jarCoins.getCoins());

                //call the interface method boughtitem if the interface is not null
                if(Interface != null){
                    Interface.onItemBought(this.type, this.totalBullets);
                }

            }
            /**
             * animate the button to selected.
             */
            public void animateClicked(){

                if(!animating){
                    animating = true;
                    Tween.to(this.table.getCell(this.stack), CellAnimation.WIDTH_HEIGHT, 0.2f)
                            .target(this.stack.getWidth() + expansionX, this.stack.getHeight() + expansionY)
                            .setCallbackTriggers(TweenCallback.END)
                            .setCallback(new TweenCallback() {
                                @Override
                                public void onEvent(int type, BaseTween<?> source) {
                                    if (type == TweenCallback.END) {
                                        Tween.to(table.getCell(stack), CellAnimation.WIDTH_HEIGHT, 0.2f)
                                                .target(stack.getWidth() - expansionX, stack.getHeight() - expansionY)
                                                .setCallbackTriggers(TweenCallback.END)
                                                .setCallback(new TweenCallback() {
                                                    @Override
                                                    public void onEvent(int type, BaseTween<?> source) {
                                                        if (type == TweenCallback.END) {
                                                            animating = false;
                                                        }
                                                    }
                                                })
                                                .start(tweenManager);
                                    }
                                }
                            })
                            .start(tweenManager);
                }

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
             * get the price tag of this item
             */
            public float getPRICE(){
                return PRICE;
            }
            /**
             * sets the number of bullets
             * @param number
             * total number of bullets to be displayed
             */
            public void setTotalBullets(int number){
                this.totalBullets = number;
            }
            /**
             * set the price tag of this item.
             * @param price
             * price this item.
             */
            public void setPRICE(int price){
                this.PRICE = price;
            }

        }

    }

    public interface Communicator{
        void onItemBought(Weapon.BULLETSTYPE itemtype, int totalNumberOfItems);
    }

}
