package view.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import view.game.Controller.EmpireMenuController;
import view.game.Controller.GameMenuController;
import view.game.Controller.ShopMenuController;
import view.game.Controller.TradeMenuController;
import view.game.Enums.BuildingType;
import view.game.Enums.UnitType;
import view.game.Model.Building;
import view.game.Model.Empire;
import view.game.Model.Game;
import view.game.Model.Goods.Armoury;
import view.game.Model.Goods.FoodStock;
import view.game.Model.Goods.Resources;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static view.game.GameMenuGDX.stage;

public class Menu {
    private static ArrayList<Label> scribeReports;
    private ArrayList<Button> buildButtons;
    private ArrayList<Button>[] buildMenus;//0-> castle - 1->industry - 2-> food - 3->processor - 4->house - 5-> weapon
    private ArrayList<Button> showInfoButtons;
    private ArrayList<Label> showInfoLabels;
    private ArrayList<Button> units;
    private ArrayList<SelectBox> chooseRateSelectBoxes;
    private ArrayList<Slider> chooseRateSliders;
    private ArrayList<Button> chooseRateButtons;
    private ArrayList<Button> tradeMenuButtons;
    private ArrayList<SelectBox> tradeMenuSelectBoxes;
    private ArrayList<TextField> tradeMenuTextFields;
    private ArrayList<Button> objectsForTradeRequest;
    private ArrayList<ScrollPane> scrollPanes;
    private ArrayList<Button> shopButtons;
    private ArrayList<Label> shopLabels;

    public Menu() {
        buildButtons = new ArrayList<>();
        buildMenus = new ArrayList[6];
        for (int i = 0; i < buildMenus.length; i++) {
            buildMenus[i] = new ArrayList<>();
        }
        shopButtons = new ArrayList<>();
        shopLabels = new ArrayList<>();
        showInfoButtons = new ArrayList<>();
        showInfoLabels = new ArrayList<>();
        chooseRateSelectBoxes = new ArrayList<>();
        chooseRateSliders = new ArrayList<>();
        chooseRateButtons = new ArrayList<>();
        tradeMenuButtons = new ArrayList<>();
        tradeMenuSelectBoxes = new ArrayList<>();
        tradeMenuTextFields = new ArrayList<>();
        objectsForTradeRequest = new ArrayList<>();
        scribeReports = new ArrayList<>();
        scrollPanes = new ArrayList<>();
        units = new ArrayList<>();
        //image for menu background
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        float screenWidth = gd.getDisplayMode().getWidth();
        Image image = new Image(new Texture(Gdx.files.internal("aa.png")));
        image.setSize(screenWidth, 300);
        GameMenuGDX.stage.addActor(image);

        makeRemoveButton();

        //add 4 button for menus : empireInfo - shop - trade - build
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("MenuButtons.atlas"));
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("settingsButton.atlas"));
        ImageButton.ImageButtonStyle shopButtonStyle = new ImageButton.ImageButtonStyle();
        shopButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("shop"));
        final Button shopButton = new ImageButton(shopButtonStyle);
        shopButton.setPosition(1502, 94);
        GameMenuGDX.stage.addActor(shopButton);

        ImageButton.ImageButtonStyle tradeButtonStyle = new ImageButton.ImageButtonStyle();
        tradeButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("trade"));
        final Button tradeButton = new ImageButton(tradeButtonStyle);
        tradeButton.setPosition(1615, 25);
        GameMenuGDX.stage.addActor(tradeButton);

        ImageButton.ImageButtonStyle infoButtonStyle = new ImageButton.ImageButtonStyle();
        infoButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("info"));
        final Button infoButton = new ImageButton(infoButtonStyle);
        infoButton.setPosition(1503, 25);
        GameMenuGDX.stage.addActor(infoButton);

        ImageButton.ImageButtonStyle buildButtonStyle = new ImageButton.ImageButtonStyle();
        buildButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("build"));
        final Button buildButton = new ImageButton(buildButtonStyle);
        buildButton.setPosition(1385, 25);
        GameMenuGDX.stage.addActor(buildButton);

        TextButton textButton = new TextButton("next turn", new Skin(Gdx.files.internal("assets/default.json")));
        textButton.setPosition(287, 17);
        textButton.setColor(0.5F, .51F, 1, 5);
        GameMenuGDX.stage.addActor(textButton);


        ImageButton.ImageButtonStyle settingsButtonStyle = new ImageButton.ImageButtonStyle();
        settingsButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("settings"));
        final Button settingsButton = new ImageButton(settingsButtonStyle);
        settingsButton.setPosition(405, 10);
        GameMenuGDX.stage.addActor(settingsButton);


        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                String text = "Attack -> A   Build -> B   Select -> S   Drop -> D   Copy -> C\nPaste -> V   Clipboard -> X   Info -> I   Zoom in -> 3   Zoom out -> 2\nMove up -> Up   Move down -> down   Move left -> left   Move right -> right";
                addLabel(stage, text, 419, 100, 1.2f, com.badlogic.gdx.graphics.Color.BLACK, skin);
            }
        });

        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    String out = GameMenuController.nextTurn();
                    Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                    Dialog dialog = new Dialog("", skin);
                    dialog.text(out);
                    GameMenuGDX.stage1.addActor(dialog);
                    TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                    dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                    dialog.show(GameMenuGDX.stage1);
                    dialog.setPosition(0, 0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean bool = false;
                for (Building building : GameMenuController.getCurrentEmpire().getBuildings()) {
                    if (building.getBuildingType().equals(BuildingType.MARKET)) {
                        clearMenu();
                        shopMenu(GameMenuGDX.stage);
                        bool = true;
                        break;
                    }
                }
                if (!bool) {
                    Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                    Dialog dialog = new Dialog("", skin);
                    dialog.text("you should already have a market");
                    GameMenuGDX.stage1.addActor(dialog);
                    TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                    dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                    dialog.show(GameMenuGDX.stage1);
                    dialog.setPosition(0, 0);
                }
            }
        });
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                infoMenu(GameMenuGDX.stage);
            }
        });
        buildButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                buildMenu(GameMenuGDX.stage);
            }
        });
        tradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                tradeMenu(GameMenuGDX.stage);
            }
        });
    }

    public static void scribeReport() {
        for (int i = 0; i < scribeReports.size(); i++) {
            scribeReports.get(i).remove();
        }

        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));

        int population = GameMenuController.getCurrentEmpire().getFrom100();
        int population1 = GameMenuController.getCurrentEmpire().getEmployedPeople() + GameMenuController.getCurrentEmpire().getUnemployedPeople();
        int maxPopulation = GameMenuController.getCurrentEmpire().getMaxPopulation();
        int gold = GameMenuController.getCurrentEmpire().getResources().getGold();


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");
        Label populationLabel = new Label(String.valueOf(population), labelStyle);
        populationLabel.setFontScale(1.3f);
        populationLabel.setPosition(1150, 112);
        populationLabel.setColor(Color.BLACK);
        stage.addActor(populationLabel);

        scribeReports.add(populationLabel);

        Label goldLabel = new Label(String.valueOf(gold), labelStyle);
        goldLabel.setFontScale(1.3f);
        goldLabel.setPosition(1118, 80);
        goldLabel.setColor(Color.BLACK);
        stage.addActor(goldLabel);

        scribeReports.add(goldLabel);

        Label maxPopulationLabel = new Label(population1 + "/" + maxPopulation, labelStyle);
        maxPopulationLabel.setFontScale(1.3f);
        maxPopulationLabel.setPosition(1110, 48);
        maxPopulationLabel.setColor(Color.BLACK);
        stage.addActor(maxPopulationLabel);

        scribeReports.add(maxPopulationLabel);

    }

    private void makeRemoveButton() {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("remove.atlas"));
        ImageButton.ImageButtonStyle shopButtonStyle = new ImageButton.ImageButtonStyle();
        shopButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("remove"));
        final Button button = new ImageButton(shopButtonStyle);
        button.setPosition(1320, 160);
        GameMenuGDX.stage.addActor(button);
        button.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                button.moveBy(x - button.getWidth() / 2, y - button.getHeight() / 2);
            }
        });
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Gdx.input.isKeyPressed(Input.Keys.B)) {
                    int j = GameMenuGDX.getPositionJ((GameMenuGDX.getCursorX()), (((GameMenuGDX.getCursorY()))));
                    int i = GameMenuGDX.getPositionI((GameMenuGDX.getCursorX()), (((GameMenuGDX.getCursorY()))));
                    String out = GameMenuController.removeBuilding(i, j);
                    if (out.equals("success")) {
                        button.setPosition(1340, 135);
                    }
                    Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                    Dialog dialog = new Dialog("", skin);
                    dialog.text(out);
                    GameMenuGDX.stage1.addActor(dialog);
                    TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                    dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                    dialog.show(GameMenuGDX.stage1);
                    dialog.setPosition(0, 0);

                }
            }
        });
    }

    public void clearMenu() {
        clearBuildMenu();
        for (int i = 0; i < buildButtons.size(); i++) {
            buildButtons.get(i).remove();
        }

        for (int i = 0; i < showInfoButtons.size(); i++) {
            showInfoButtons.get(i).remove();
        }

        for (int i = 0; i < showInfoLabels.size(); i++) {
            showInfoLabels.get(i).remove();
        }
        for (int i = 0; i < units.size(); i++) {
            units.get(i).remove();
        }
        for (Button button : shopButtons) {
            button.remove();
        }

        for (Label label : shopLabels) {
            label.remove();
        }
        for (int i = 0; i < chooseRateSelectBoxes.size(); i++) {
            chooseRateSelectBoxes.get(i).remove();
        }

        for (int i = 0; i < chooseRateSliders.size(); i++) {
            chooseRateSliders.get(i).remove();
        }

        for (int i = 0; i < chooseRateButtons.size(); i++) {
            chooseRateButtons.get(i).remove();
        }

        for (int i = 0; i < tradeMenuButtons.size(); i++) {
            tradeMenuButtons.get(i).remove();
        }

        for (int i = 0; i < tradeMenuSelectBoxes.size(); i++) {
            tradeMenuSelectBoxes.get(i).remove();
        }

        for (int i = 0; i < tradeMenuTextFields.size(); i++) {
            tradeMenuTextFields.get(i).remove();
        }

        for (int i = 0; i < objectsForTradeRequest.size(); i++) {
            objectsForTradeRequest.get(i).remove();
        }

        for (int i = 0; i < scrollPanes.size(); i++) {
            scrollPanes.get(i).remove();
        }
    }

    public void buildMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("buildMenu.atlas"));
        ImageButton.ImageButtonStyle foodButtonStyle = new ImageButton.ImageButtonStyle();
        foodButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("food"));
        final Button foodButton = new ImageButton(foodButtonStyle);
        foodButton.setPosition(580, 0);
        stage.addActor(foodButton);
        foodButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearBuildMenu();
                foodBuild();
            }
        });
        ImageButton.ImageButtonStyle industryButtonStyle = new ImageButton.ImageButtonStyle();
        industryButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("industry"));
        final Button industryButton = new ImageButton(industryButtonStyle);
        industryButton.setPosition(500, 0);
        stage.addActor(industryButton);
        industryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearBuildMenu();
                industryBuild();
            }
        });
        ImageButton.ImageButtonStyle castleButtonStyle = new ImageButton.ImageButtonStyle();
        castleButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("castle"));
        final Button castleButton = new ImageButton(castleButtonStyle);
        castleButton.setPosition(420, 0);
        stage.addActor(castleButton);
        castleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearBuildMenu();
                castleBuild();
            }
        });
        ImageButton.ImageButtonStyle processorButtonStyle = new ImageButton.ImageButtonStyle();
        processorButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("processor"));
        final Button processorButton = new ImageButton(processorButtonStyle);
        processorButton.setPosition(660, 0);
        stage.addActor(processorButton);
        processorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearBuildMenu();
                processorBuild();
            }
        });
        ImageButton.ImageButtonStyle houseButtonStyle = new ImageButton.ImageButtonStyle();
        houseButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("house"));
        final Button houseButton = new ImageButton(houseButtonStyle);
        houseButton.setPosition(740, 0);
        stage.addActor(houseButton);
        houseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearBuildMenu();
                houseBuild();
            }
        });
        ImageButton.ImageButtonStyle weaponButtonStyle = new ImageButton.ImageButtonStyle();
        weaponButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("weapon"));
        final Button weaponButton = new ImageButton(weaponButtonStyle);
        weaponButton.setPosition(820, 0);
        stage.addActor(weaponButton);
        weaponButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearBuildMenu();
                weaponBuild();
            }
        });
        buildButtons.clear();
        buildButtons.add(weaponButton);
        buildButtons.add(castleButton);
        buildButtons.add(processorButton);
        buildButtons.add(houseButton);
        buildButtons.add(industryButton);
        buildButtons.add(foodButton);
    }

    private void industryBuild() {
        buildMenus[1].clear();
        final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("industryButton.atlas"));
        buildMenus[1].add(makeButton(430, 75, "Market", textureAtlas));
        buildMenus[1].add(makeButton(520, 75, "IronMine", textureAtlas));
        buildMenus[1].add(makeButton(610, 75, "Quarry", textureAtlas));
        buildMenus[1].add(makeButton(700, 75, "OxTether", textureAtlas));
        buildMenus[1].add(makeButton(790, 75, "PitchRig", textureAtlas));
        buildMenus[1].add(makeButton(880, 75, "StockPile", textureAtlas));
        buildMenus[1].add(makeButton(970, 75, "Woodcutter", textureAtlas));
    }

    private void castleBuild() {
        buildMenus[0].clear();
        final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("castleButton.atlas"));
        buildMenus[0].add(makeButton(420, 62, "BigStoneGatehouse", textureAtlas));
        buildMenus[0].add(makeButton(473, 62, "SmallStoneGatehouse", textureAtlas));
        buildMenus[0].add(makeButton(420, 125, "DrawBridge", textureAtlas));
        buildMenus[0].add(makeButton(473, 115, "KillingPit", textureAtlas));
        buildMenus[0].add(makeButton(526, 62, "LookoutTower", textureAtlas));
        buildMenus[0].add(makeButton(579, 62, "PerimeterTower", textureAtlas));
        buildMenus[0].add(makeButton(632, 62, "DefenciveTurret", textureAtlas));
        buildMenus[0].add(makeButton(685, 62, "SquareTower", textureAtlas));
        buildMenus[0].add(makeButton(738, 62, "RoundTower", textureAtlas));
        buildMenus[0].add(makeButton(791, 62, "ShortWall", textureAtlas));
        buildMenus[0].add(makeButton(844, 62, "TallWall", textureAtlas));
        buildMenus[0].add(makeButton(897, 62, "CagedWarDogs", textureAtlas));
        buildMenus[0].add(makeButton(950, 62, "OilSmelter", textureAtlas));
        buildMenus[0].add(makeButton(1003, 62, "Stable", textureAtlas));
    }

    private void processorBuild() {
        buildMenus[3].clear();
        final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("processorButton.atlas"));
        buildMenus[3].add(makeButton(580, 62, "Bakery", textureAtlas));
        buildMenus[3].add(makeButton(450, 62, "FoodStock", textureAtlas));
        buildMenus[3].add(makeButton(710, 62, "Brewery", textureAtlas));
        buildMenus[3].add(makeButton(840, 62, "Mill", textureAtlas));
        buildMenus[3].add(makeButton(970, 62, "Inn", textureAtlas));
    }

    public void MercenaryPostMenu() {
        clearMenu();
        units.clear();
        final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("unitButtons.atlas"));
        units.add(makeButton(420, 55, "Slaves", textureAtlas));
        units.add(makeButton(500, 55, "Slinger", textureAtlas));
        units.add(makeButton(580, 55, "ArcherBow", textureAtlas));
        units.add(makeButton(660, 55, "HorseArcher", textureAtlas));
        units.add(makeButton(740, 55, "Assassin", textureAtlas));
        units.add(makeButton(820, 55, "ArabianSwordsman", textureAtlas));
        units.add(makeButton(900, 55, "FireThrower", textureAtlas));
        units.add(makeButton(980, 55, "BlackMonk", textureAtlas));
    }

    public void BarrackMenu() {
        clearMenu();
        units.clear();
        final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("unitButtons.atlas"));
        units.add(makeButton(420, 55, "Archer", textureAtlas));
        units.add(makeButton(500, 55, "Crossbowman", textureAtlas));
        units.add(makeButton(580, 55, "Spearman", textureAtlas));
        units.add(makeButton(660, 55, "Pikeman", textureAtlas));
        units.add(makeButton(740, 55, "Maceman", textureAtlas));
        units.add(makeButton(820, 55, "Swordsman", textureAtlas));
        units.add(makeButton(900, 55, "Knight", textureAtlas));
    }

    public void EngineerGuildMenu() {
        clearMenu();
        units.clear();
        final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("unitButtons.atlas"));
        units.add(makeButton(600, 55, "Engineer", textureAtlas));
        units.add(makeButton(800, 55, "Ladderman", textureAtlas));
    }

    public void MachineMenu() {
        clearMenu();
        units.clear();
        final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("unitButtons.atlas"));
        units.add(makeButton(430, 55, "PortableShield", textureAtlas));
        units.add(makeButton(530, 55, "BatteringRam", textureAtlas));
        units.add(makeButton(630, 55, "Trebuchet", textureAtlas));
        units.add(makeButton(730, 55, "Catapult", textureAtlas));
        units.add(makeButton(830, 55, "FireBallista", textureAtlas));
        units.add(makeButton(930, 55, "SiegeTower", textureAtlas));//TODO add logic!!
    }

    private void houseBuild() {
        buildMenus[4].clear();
        final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("houseButton.atlas"));
        buildMenus[4].add(makeButton(580, 62, "Church", textureAtlas));
        buildMenus[4].add(makeButton(450, 62, "Cathedral", textureAtlas));
        buildMenus[4].add(makeButton(710, 62, "Apothecary", textureAtlas));
        buildMenus[4].add(makeButton(840, 62, "Hovel", textureAtlas));
        buildMenus[4].add(makeButton(970, 62, "WaterPot", textureAtlas));

    }

    private void weaponBuild() {
        buildMenus[5].clear();
        final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("weaponButton.atlas"));
        buildMenus[5].add(makeButton(420, 75, "Armoury", textureAtlas));
        buildMenus[5].add(makeButton(498, 75, "MercenaryPost", textureAtlas));
        buildMenus[5].add(makeButton(576, 75, "Barrack", textureAtlas));
        buildMenus[5].add(makeButton(654, 75, "EngineerGuild", textureAtlas));
        buildMenus[5].add(makeButton(732, 75, "Fletcher", textureAtlas));
        buildMenus[5].add(makeButton(810, 75, "PoleTurner", textureAtlas));
        buildMenus[5].add(makeButton(888, 75, "Armourer", textureAtlas));
        buildMenus[5].add(makeButton(966, 75, "BlackSmith", textureAtlas));

    }

    public void clearBuildMenu() {
        for (int i = 0; i < buildMenus.length; i++) {
            for (int j = 0; j < buildMenus[i].size(); j++) {
                buildMenus[i].get(j).remove();
            }
        }
    }

    public void foodBuild() {
        final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("foodButton.atlas"));
        buildMenus[2].clear();
        buildMenus[2].add(makeButton(450, 70, "HopsFarm", textureAtlas));
        buildMenus[2].add(makeButton(970, 70, "AppleOrchard", textureAtlas));
        buildMenus[2].add(makeButton(580, 70, "DairyFarm", textureAtlas));
        buildMenus[2].add(makeButton(710, 70, "HuntingPost", textureAtlas));
        buildMenus[2].add(makeButton(840, 70, "WheatFarm", textureAtlas));
    }

    private void BuildBuildings(Button button, String name, int x, int y) {
        int j = GameMenuGDX.getPositionJ((GameMenuGDX.getCursorX()), (((GameMenuGDX.getCursorY()))));
        int i = GameMenuGDX.getPositionI((GameMenuGDX.getCursorX()), (((GameMenuGDX.getCursorY()))));
        String out = GameMenuController.dropBuilding(i, j, name);
        if (out.equals("success")) {
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("Buildings.atlas"));
            Sprite sprite = new Sprite(textureAtlas.findRegion(name));
            sprite.setPosition(((j + i) * 15) - (GameMenuGDX.MAP_WIDTH / 2), (j - i) * 8 - 8);
            GameMenuGDX.sprites[i][j] = sprite;
            button.setPosition(x, y);
        } else {
            Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
            Dialog dialog = new Dialog("", skin);
            dialog.text(out);
            GameMenuGDX.stage1.addActor(dialog);
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
            dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
            dialog.show(GameMenuGDX.stage1);
            dialog.setPosition(0, 0);
        }
    }

    private void dropUnits(Button button, String name, int x, int y) {
        int j = GameMenuGDX.getPositionJ((GameMenuGDX.getCursorX()), (((GameMenuGDX.getCursorY()))));
        int i = GameMenuGDX.getPositionI((GameMenuGDX.getCursorX()), (((GameMenuGDX.getCursorY()))));
        String out = GameMenuController.dropUnit(i, j, name);
        if (out.equals("success")) {
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("units.atlas"));
            Sprite sprite = new Sprite(textureAtlas.findRegion(name));
            sprite.setPosition(((j + i) * 15) - (GameMenuGDX.MAP_WIDTH / 2), (j - i) * 8 - 8);
            GameMenuGDX.units.add(sprite);
            GameMenuController.map.getMap()[i][j].getUnits().get(GameMenuController.map.getMap()[i][j].getUnits().size() - 1).setSprite(sprite);
            button.setPosition(x, y);
        } else {
            Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
            Dialog dialog = new Dialog("", skin);
            dialog.text(out);
            GameMenuGDX.stage1.addActor(dialog);
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
            dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
            dialog.show(GameMenuGDX.stage1);
            dialog.setPosition(0, 0);
        }
    }

    private Button makeButton(int x, int y, String name, TextureAtlas textureAtlas) {
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion(name));
        final Button Button = new ImageButton(ButtonStyle);
        clickForButtons(Button, x, y, name);
        return Button;
    }

    private void clickForButtons(final Button button, final int xP, final int yP, final String name) {
        button.setPosition(xP, yP);
        GameMenuGDX.stage.addActor(button);
        button.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                button.moveBy(x - button.getWidth() / 2, y - button.getHeight() / 2);
            }
        });
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Gdx.input.isKeyPressed(Input.Keys.B)) {
                    if (BuildingType.getBuildingByName(name) != null)
                        BuildBuildings(button, name, xP, yP);
                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    if (UnitType.getUnitByName(name) != null)
                        dropUnits(button, name, xP, yP);
                }
            }
        });
    }

    public void shopMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle meatButtonStyle = new ImageButton.ImageButtonStyle();
        meatButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("meat"));
        final Button meatButton = new ImageButton(meatButtonStyle);
        meatButton.setPosition(440, 130);
        stage.addActor(meatButton);
        shopButtons.add(meatButton);
        meatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                meatMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle cheeseButtonStyle = new ImageButton.ImageButtonStyle();
        cheeseButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("cheese"));
        final Button cheeseButton = new ImageButton(cheeseButtonStyle);
        cheeseButton.setPosition(510, 130);
        stage.addActor(cheeseButton);
        shopButtons.add(cheeseButton);
        cheeseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                cheeseMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle appleButtonStyle = new ImageButton.ImageButtonStyle();
        appleButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("apple"));
        final Button appleButton = new ImageButton(appleButtonStyle);
        appleButton.setPosition(580, 130);
        stage.addActor(appleButton);
        shopButtons.add(appleButton);
        appleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                appleMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle hopButtonStyle = new ImageButton.ImageButtonStyle();
        hopButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("hop"));
        final Button hopButton = new ImageButton(hopButtonStyle);
        hopButton.setPosition(650, 130);
        stage.addActor(hopButton);
        shopButtons.add(hopButton);
        hopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                hopMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle woodButtonStyle = new ImageButton.ImageButtonStyle();
        woodButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("wood"));
        final Button woodButton = new ImageButton(woodButtonStyle);
        woodButton.setPosition(720, 130);
        stage.addActor(woodButton);
        shopButtons.add(woodButton);
        woodButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                woodMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle stoneButtonStyle = new ImageButton.ImageButtonStyle();
        stoneButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("stone"));
        final Button stonButton = new ImageButton(stoneButtonStyle);
        stonButton.setPosition(790, 130);
        stage.addActor(stonButton);
        shopButtons.add(stonButton);
        stonButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                stoneMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle spearButtonStyle = new ImageButton.ImageButtonStyle();
        spearButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("spear"));
        final Button spearButton = new ImageButton(spearButtonStyle);
        spearButton.setPosition(860, 130);
        stage.addActor(spearButton);
        shopButtons.add(spearButton);
        spearButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                spearMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle bowButtonStyle = new ImageButton.ImageButtonStyle();
        bowButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("bow"));
        final Button bowButton = new ImageButton(bowButtonStyle);
        bowButton.setPosition(930, 130);
        stage.addActor(bowButton);
        shopButtons.add(bowButton);
        bowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                bowMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle maceButtonStyle = new ImageButton.ImageButtonStyle();
        maceButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("mace"));
        final Button maceButton = new ImageButton(maceButtonStyle);
        maceButton.setPosition(1000, 130);
        stage.addActor(maceButton);
        shopButtons.add(maceButton);
        maceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                maceMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle leatherArmorButtonStyle = new ImageButton.ImageButtonStyle();
        leatherArmorButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("leatherArmor"));
        final Button leatherArmorButton = new ImageButton(leatherArmorButtonStyle);
        leatherArmorButton.setPosition(1070, 130);
        stage.addActor(leatherArmorButton);
        shopButtons.add(leatherArmorButton);
        leatherArmorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                leatherArmorMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle aleButtonStyle = new ImageButton.ImageButtonStyle();
        aleButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("ale"));
        final Button aleButton = new ImageButton(aleButtonStyle);
        aleButton.setPosition(440, 50);
        stage.addActor(aleButton);
        shopButtons.add(aleButton);
        aleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                aleMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle wheatButtonStyle = new ImageButton.ImageButtonStyle();
        wheatButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("wheat"));
        final Button wheatButton = new ImageButton(wheatButtonStyle);
        wheatButton.setPosition(510, 50);
        stage.addActor(wheatButton);
        shopButtons.add(wheatButton);
        wheatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                wheatMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle breadButtonStyle = new ImageButton.ImageButtonStyle();
        breadButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("bread"));
        final Button breadButton = new ImageButton(breadButtonStyle);
        breadButton.setPosition(580, 50);
        stage.addActor(breadButton);
        shopButtons.add(breadButton);
        breadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                breadMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle pitchButtonStyle = new ImageButton.ImageButtonStyle();
        pitchButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("pitch"));
        final Button pitchButton = new ImageButton(pitchButtonStyle);
        pitchButton.setPosition(650, 50);
        stage.addActor(pitchButton);
        shopButtons.add(pitchButton);
        pitchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                pitchMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle crossbowButtonStyle = new ImageButton.ImageButtonStyle();
        crossbowButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("crossbow"));
        final Button crossbowButton = new ImageButton(crossbowButtonStyle);
        crossbowButton.setPosition(720, 50);
        stage.addActor(crossbowButton);
        shopButtons.add(crossbowButton);
        crossbowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                crossbowMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle pikeButtonStyle = new ImageButton.ImageButtonStyle();
        pikeButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("pike"));
        final Button pikeButton = new ImageButton(pikeButtonStyle);
        pikeButton.setPosition(790, 50);
        stage.addActor(pikeButton);
        shopButtons.add(pikeButton);
        pikeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                pikeMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle swortButtonStyle = new ImageButton.ImageButtonStyle();
        swortButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("swort"));
        final Button swortButton = new ImageButton(swortButtonStyle);
        swortButton.setPosition(860, 50);
        stage.addActor(swortButton);
        shopButtons.add(swortButton);
        swortButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                swortMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle metalArmorButtonStyle = new ImageButton.ImageButtonStyle();
        metalArmorButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("metalArmor"));
        final Button metalArmorButton = new ImageButton(metalArmorButtonStyle);
        metalArmorButton.setPosition(930, 50);
        stage.addActor(metalArmorButton);
        shopButtons.add(metalArmorButton);
        metalArmorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                metalArmorMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle ironButtonStyle = new ImageButton.ImageButtonStyle();
        ironButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("iron"));
        final Button ironButton = new ImageButton(ironButtonStyle);
        ironButton.setPosition(1000, 50);
        stage.addActor(ironButton);
        shopButtons.add(ironButton);
        ironButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                ironMenu(stage);
            }
        });
        ImageButton.ImageButtonStyle flourButtonStyle = new ImageButton.ImageButtonStyle();
        flourButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("flour"));
        final Button flourButton = new ImageButton(flourButtonStyle);
        flourButton.setPosition(1070, 50);
        stage.addActor(flourButton);
        shopButtons.add(flourButton);
        flourButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                flourMenu(stage);
            }
        });
    }

    public void meatMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("meat"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getFoodStock().getFoodAmount("meat")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 6", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("meat"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                meatMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 8", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("meat"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                meatMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void cheeseMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("cheese"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getFoodStock().getFoodAmount("cheese")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 6", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("cheese"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                cheeseMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 8", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("cheese"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                cheeseMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void appleMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("apple"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getFoodStock().getFoodAmount("apple")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 6", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("apple"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                appleMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 8", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("apple"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                appleMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void hopMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("hop"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getResourceAmount("hop")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 12", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("hop"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                hopMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 15", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("hop"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                hopMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void woodMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("wood"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getResourceAmount("wood")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 3", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("wood"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                woodMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 4", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("wood"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                woodMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void stoneMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("stone"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getResourceAmount("stone")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 11", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("stone"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                stoneMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 14", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("stone"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                stoneMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void spearMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("spear"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount("spear")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 16", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("spear"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                spearMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 20", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("spear"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                spearMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void bowMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("bow"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount("bow")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 25", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("bow"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                bowMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 31", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("bow"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                bowMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void maceMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("mace"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount("mace")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 48", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("mace"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                maceMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 58", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("mace"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                maceMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void leatherArmorMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("leatherArmor"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount("leatherArmor")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 20", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("leatherArmor"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                leatherArmorMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 25", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("leatherArmor"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                leatherArmorMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void aleMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("ale"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getResourceAmount("ale")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 16", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("ale"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                aleMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 20", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("ale"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                aleMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void wheatMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("wheat"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getResourceAmount("wheat")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 20", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("wheat"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                wheatMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 23", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("wheat"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                wheatMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void breadMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("bread"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getFoodStock().getFoodAmount("bread")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 6", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("bread"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                breadMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 8", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("bread"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                breadMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void pitchMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("pitch"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getResourceAmount("pitch")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 16", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("pitch"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                pitchMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 20", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("pitch"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                pitchMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void crossbowMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("crossbow"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount("crossbow")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 48", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("crossbow"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                crossbowMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 58", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("crossbow"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                crossbowMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void pikeMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("pike"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount("pike")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 32", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("pike"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                pikeMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 36", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("pike"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                pikeMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void swortMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("swort"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount("sword")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 48", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("sword"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                swortMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 58", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("sword"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                swortMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void metalArmorMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("metalArmor"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getArmouryAmount("metalArmor")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 48", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("metalArmor"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                metalArmorMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 58", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("metalArmor"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                metalArmorMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void ironMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("iron"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getResourceAmount("iron")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 36", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("iron"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                ironMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 45", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("iron"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                ironMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }

    public void flourMenu(final Stage stage) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("shopMenu.atlas"));
        ImageButton.ImageButtonStyle ButtonStyle = new ImageButton.ImageButtonStyle();
        ButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("flour"));
        final Button Button = new ImageButton(ButtonStyle);
        Button.setPosition(460, 70);
        stage.addActor(Button);
        shopButtons.add(Button);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("coin.atlas"));
        ImageButton.ImageButtonStyle coinButtonStyle = new ImageButton.ImageButtonStyle();
        coinButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("coin"));
        final Button coinButton = new ImageButton(coinButtonStyle);
        coinButton.setPosition(990, 50);
        stage.addActor(coinButton);
        shopButtons.add(coinButton);
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        Label label = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getResourceAmount("flour")), skin);
        label.setFontScale(2);
        label.setPosition(460, 50);
        stage.addActor(label);
        shopLabels.add(label);
        Label coin = new Label(String.valueOf(GameMenuController.getCurrentEmpire().getResources().getGold()), skin);
        coin.setFontScale(2);
        coin.setPosition(1000, 20);
        stage.addActor(coin);
        shopLabels.add(coin);
        Button sell = new TextButton("sell : 36", skin);
        sell.setColor(Color.BLACK);
        sell.setPosition(700, 100);
        stage.addActor(sell);
        shopButtons.add(sell);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.sell("flour"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                flourMenu(stage);
            }
        });
        Button buy = new TextButton("buy : 45", skin);
        buy.setColor(Color.BLACK);
        buy.setPosition(700, 50);
        stage.addActor(buy);
        shopButtons.add(buy);
        buy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                Dialog dialog = new Dialog("", skin);
                dialog.text(ShopMenuController.buy("flour"));
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
                clearMenu();
                flourMenu(stage);
            }
        });
        TextureAtlas textureAtlas2 = new TextureAtlas(Gdx.files.internal("back.atlas"));
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas2.findRegion("back"));
        final Button back = new ImageButton(backButtonStyle);
        back.setPosition(460, 130);
        stage.addActor(back);
        shopButtons.add(back);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                shopMenu(stage);
            }
        });
    }
    private void infoOfAllUsersButton()
    {
        final Skin skin = new Skin(Gdx.files.internal("assets/default.json"));

        final Button allUsersButton = new TextButton("Users", skin);
        allUsersButton.setPosition(590, 20);
        allUsersButton.setColor(Color.OLIVE);

        stage.addActor(allUsersButton);
        showInfoButtons.add(allUsersButton);

        allUsersButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String out = "";
                for(Empire empire : Game.getEmpires())
                {
                    out += "ID -> " + empire.getEmpireId() + " Username -> " + empire.getUser().getUsername();
                    if(GameMenuController.getCurrentEmpire() == empire)
                        out += " Your turn";
                    out += "\n";
                }

                Dialog dialog = new Dialog("", skin);
                dialog.text(out);
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);
            }
        });
    }
    public void infoMenu(final Stage stage) {
        infoOfAllUsersButton();
        scribeReport();
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("faces.atlas"));
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));

        addTwoFacesImage(stage, textureAtlas, skin);
        String faceImageName;
        int population = GameMenuController.getCurrentEmpire().getEmployedPeople() + GameMenuController.getCurrentEmpire().getUnemployedPeople();


        final Button showTreasury = new TextButton("Treasury", skin);
        showTreasury.setPosition(590, -6);
        showTreasury.setColor(Color.OLIVE);

        stage.addActor(showTreasury);
        tradeMenuButtons.add(showTreasury);

        showTreasury.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                showTreasuryAction(stage);
            }
        });

        addLabel(stage, "Population: " + population, 450, 150, 1.8f, com.badlogic.gdx.graphics.Color.BLUE, skin);
        addLabel(stage, "Food", 660, 140, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);
        int foodRate = GameMenuController.getCurrentEmpire().getFoodPopularity();
        faceImageName = getFaceImageName(foodRate);
        addLabel(stage, String.valueOf(foodRate), 740, 140, 1.5f, com.badlogic.gdx.graphics.Color.GREEN, skin);
        addButtonImage(stage, textureAtlas, faceImageName, 780, 133);
        addLabel(stage, "Tax", 660, 100, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);
        int taxRate = GameMenuController.getCurrentEmpire().getTaxPopularity();
        faceImageName = getFaceImageName(taxRate);

        addLabel(stage, String.valueOf(taxRate), 740, 100, 1.5f, com.badlogic.gdx.graphics.Color.GREEN, skin);
        addButtonImage(stage, textureAtlas, faceImageName, 780, 93);
        addLabel(stage, "Religion", 660, 60, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);
        int religionRate = GameMenuController.getCurrentEmpire().getReligionPopularity();
        faceImageName = getFaceImageName(religionRate);

        addLabel(stage, String.valueOf(religionRate), 740, 60, 1.5f, com.badlogic.gdx.graphics.Color.GREEN, skin);
        addButtonImage(stage, textureAtlas, faceImageName, 780, 53);
        addLabel(stage, "Ale Coverage", 850, 140, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);
        int aleCoverageRate = GameMenuController.getCurrentEmpire().getAleCoverage();
        faceImageName = getFaceImageName(aleCoverageRate);

        addLabel(stage, String.valueOf(aleCoverageRate), 1000, 140, 1.5f, com.badlogic.gdx.graphics.Color.GREEN, skin);
        addButtonImage(stage, textureAtlas, faceImageName, 1050, 133);
        addLabel(stage, "Fear Factor", 850, 100, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);
        int fearFactorRate = GameMenuController.getCurrentEmpire().getFearPopularity();
        faceImageName = getFaceImageName(fearFactorRate);


        addLabel(stage, String.valueOf(fearFactorRate), 1000, 100, 1.5f, com.badlogic.gdx.graphics.Color.GREEN, skin);
        addButtonImage(stage, textureAtlas, faceImageName, 1050, 93);

        int poisonFactorRate = GameMenuController.getCurrentEmpire().getDisaffectionFromPoisonedCells();
        addLabelForPoison(stage, String.valueOf(poisonFactorRate), 950, 60, 1.5f, com.badlogic.gdx.graphics.Color.GREEN, skin);
        addLabel(stage, "Poison", 850, 60, 1.5f, Color.BLACK, skin);
        faceImageName = getFaceImageNameForPoison(poisonFactorRate);
        addButtonImage(stage, textureAtlas, faceImageName, 1000, 60);


        addLabel(stage, "In the coming month", 700, 20, 1.8f, com.badlogic.gdx.graphics.Color.BLACK, skin);
        int comingMonthRate = foodRate + taxRate + religionRate + aleCoverageRate + fearFactorRate - poisonFactorRate;
        faceImageName = getFaceImageName(comingMonthRate);

        addLabel(stage, String.valueOf(comingMonthRate), 965, 20, 1.8f, com.badlogic.gdx.graphics.Color.GREEN, skin);
        addButtonImage(stage, textureAtlas, faceImageName, 1030, 15);
    }

    private String getFaceImageNameForPoison(int rate) {
        if (rate > 2) {
            return "redFace";
        } else if (rate == 1 || rate == 2) {
            return "yellowFace";
        } else {
            return "greenFace";
        }
    }


    public void showTreasuryAction(final Stage stage) {
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));

        String[] objects = {"meat", "apple", "bread", "cheese", "wheat", "flour", "hop", "ale", "stone", "iron", "wood", "pitch", "bow", "crossbow", "spear", "pike", "mace", "sword", "leatherArmor", "metalArmor"};

        for (int i = 0; i < 10; i++) {
            addObjectsAndLabelsForTreasury(objects[i], 440 + 60 * i, 120, skin);
        }

        for (int i = 10; i < objects.length; i++) {
            addObjectsAndLabelsForTreasury(objects[i], 440 + 60 * (i - 10), 45, skin);
        }


        Button backButton = new TextButton("Back", skin);
        backButton.setPosition(720, -10);
        backButton.setColor(Color.OLIVE);

        stage.addActor(backButton);
        objectsForTradeRequest.add(backButton);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                infoMenu(stage);
            }
        });

    }

    public void addObjectsAndLabelsForTreasury(final String nameOfObject, int x, int y, Skin skin) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("tradeObjects.atlas"));

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion(nameOfObject));
        ImageButton button = new ImageButton(imageButtonStyle);
        button.setPosition(x, y);
        stage.addActor(button);
        objectsForTradeRequest.add(button);

        boolean isResourceType = new Resources().isResourceType(nameOfObject);
        boolean isFoodStuck = new FoodStock().isFoodType(nameOfObject);
        boolean isArmoury = new Armoury().isArmouryType(nameOfObject);
        String value = "";


        if (isResourceType) {
            switch (nameOfObject) {
                case "wheat":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getResources().getWheat());
                    break;
                case "flour":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getResources().getFlour());
                    break;
                case "hop":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getResources().getHop());
                    break;
                case "ale":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getResources().getAle());
                    break;
                case "stone":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getResources().getStone());
                    break;
                case "iron":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getResources().getIron());
                    break;
                case "wood":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getResources().getWood());
                    break;
                case "pitch":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getResources().getPitch());
                    break;
            }
        } else if (isArmoury) {
            switch (nameOfObject) {
                case "bow":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getBow());
                    break;
                case "crossbow":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getCrossbow());
                    break;
                case "spear":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getSpear());
                    break;
                case "pike":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getPike());
                    break;
                case "mace":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getMace());
                    break;
                case "sword":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getSword());
                    break;
                case "leatherArmor":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getLeatherArmor());
                    break;
                case "metalArmor":
                    value = String.valueOf(GameMenuController.getCurrentEmpire().getArmoury().getMetalArmor());
                    break;
            }
        } else {
            switch (nameOfObject) {
                case "meat":
                    value = String.valueOf((int) GameMenuController.getCurrentEmpire().getFoodStock().getMeat());
                    break;
                case "apple":
                    value = String.valueOf((int) GameMenuController.getCurrentEmpire().getFoodStock().getApple());
                    break;
                case "cheese":
                    value = String.valueOf((int) GameMenuController.getCurrentEmpire().getFoodStock().getCheese());
                    break;
                case "bread":
                    value = String.valueOf((int) GameMenuController.getCurrentEmpire().getFoodStock().getBread());
                    break;
            }
        }
        addLabelWithoutColorsForRates(stage, value, x + 13, y - 20, 1.2f, Color.BLACK, skin);
    }


    private String getFaceImageName(int rate) {
        if (rate > 0) {
            return "greenFace";
        } else if (rate < 0) {
            return "redFace";
        } else {
            return "yellowFace";
        }
    }

    private void addTwoFacesImage(final Stage stage, TextureAtlas textureAtlas, final Skin skin) {
        ImageButton.ImageButtonStyle twoFacesStyle = new ImageButton.ImageButtonStyle();
        twoFacesStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("2Face"));
        Button twoFacesButton = new ImageButton(twoFacesStyle);
        twoFacesButton.setPosition(470, 0);
        stage.addActor(twoFacesButton);
        showInfoButtons.add(twoFacesButton);

        final Array<Integer> foodRate = new Array<Integer>();
        foodRate.add(-2);
        foodRate.add(-1);
        foodRate.add(0);
        foodRate.add(1);
        foodRate.add(2);

        final Array<Integer> taxRate = new Array<Integer>();
        taxRate.add(-3);
        taxRate.add(-2);
        taxRate.add(-1);
        taxRate.add(0);
        taxRate.add(1);
        taxRate.add(2);
        taxRate.add(3);
        taxRate.add(4);
        taxRate.add(5);

        final SelectBox<Integer> selectBoxForFoodRate = new SelectBox<Integer>(skin);
        selectBoxForFoodRate.setItems(foodRate);
        selectBoxForFoodRate.setSelectedIndex(GameMenuController.getCurrentEmpire().getFoodRate() + 2);

        final SelectBox<Integer> selectBoxForTaxRate = new SelectBox<Integer>(skin);
        selectBoxForTaxRate.setItems(taxRate);
        selectBoxForTaxRate.setSelectedIndex(GameMenuController.getCurrentEmpire().getTaxRate() + 3);

        final Slider fearRateBar = new Slider(-5, 5, 1, false, skin);
        fearRateBar.setValue(GameMenuController.getCurrentEmpire().getFearRate());

        // Create a label to display the current value of the slider
        final Label fearRateValueLabel = new Label(fearRateBar.getValue() + "", skin);
        fearRateValueLabel.setPosition(1100, 130);

        final Button okButton = new TextButton("OK", skin);
        okButton.setPosition(640, 50);
        okButton.setColor(Color.OLIVE);

        final Button cancelButton = new TextButton("Cancel", skin);
        cancelButton.setPosition(740, 50);
        cancelButton.setColor(Color.OLIVE);

        twoFacesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                stage.addActor(selectBoxForFoodRate);
                addLabel(stage, "Food Rate: ", 460, 130, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);
                selectBoxForFoodRate.setPosition(580, 120);
                selectBoxForFoodRate.setColor(Color.GRAY);
                chooseRateSelectBoxes.add(selectBoxForFoodRate);

                stage.addActor(selectBoxForTaxRate);
                addLabel(stage, "Tax Rate: ", 640, 130, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);
                selectBoxForTaxRate.setPosition(760, 120);
                selectBoxForTaxRate.setColor(Color.GRAY);
                chooseRateSelectBoxes.add(selectBoxForTaxRate);

                addLabel(stage, "Fear Rate: ", 820, 130, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);
                fearRateBar.setPosition(960, 130);
                fearRateBar.setColor(Color.RED);
                stage.addActor(fearRateBar);
                chooseRateSliders.add(fearRateBar);

                stage.addActor(fearRateValueLabel);

                showInfoLabels.add(fearRateValueLabel);
                chooseRateButtons.add(okButton);
                chooseRateButtons.add(cancelButton);

                stage.addActor(cancelButton);
                cancelButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        clearMenu();
                        infoMenu(stage);
                    }
                });

                stage.addActor(okButton);
                okButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Perform action when OK button is clicked
                        GameMenuController.getCurrentEmpire().setFoodRate(selectBoxForFoodRate.getSelectedIndex() - 2);
                        GameMenuController.getCurrentEmpire().setTaxRate(selectBoxForTaxRate.getSelectedIndex() - 3);
                        GameMenuController.getCurrentEmpire().setFearRate((int) fearRateBar.getValue());

                        EmpireMenuController.calculatePopularityFactors();
                        clearMenu();
                        infoMenu(stage);
                    }
                });

                // Add a listener to the slider that updates the label's text
                fearRateBar.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        int value = (int) fearRateBar.getValue();
                        fearRateValueLabel.setText(value + "");
                    }
                });
            }
        });
    }

    private void addLabel(Stage stage, String text, float x, float y, float scale, Color color, Skin skin) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");
        if (text.matches("^-\\d+$") || text.matches("^\\d+$")) {
            if (Integer.parseInt(text) > 0) {
                labelStyle.fontColor = Color.GREEN;
                text = "+" + text;
            } else if (Integer.parseInt(text) < 0) {
                labelStyle.fontColor = Color.RED;
            } else
                labelStyle.fontColor = Color.BLACK;
        } else
            labelStyle.fontColor = color;
        Label label = new Label(text, labelStyle);
        label.setFontScale(scale);
        label.setPosition(x, y);
        stage.addActor(label);
        showInfoLabels.add(label);
    }

    private void addLabelForPoison(Stage stage, String text, float x, float y, float scale, Color color, Skin skin) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");
        if (text.matches("^-\\d+$") || text.matches("^\\d+$")) {
            if (Integer.parseInt(text) == 0) {
                labelStyle.fontColor = Color.GREEN;
            } else if (Integer.parseInt(text) == 1 || Integer.parseInt(text) == 2) {
                labelStyle.fontColor = Color.BLACK;
            } else
                labelStyle.fontColor = Color.RED;
        } else
            labelStyle.fontColor = color;
        Label label = new Label(text, labelStyle);
        label.setFontScale(scale);
        label.setPosition(x, y);
        stage.addActor(label);
        showInfoLabels.add(label);
    }


    private void addLabelWithoutColorsForRates(Stage stage, String text, float x, float y, float scale, Color color, Skin skin) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");

        labelStyle.fontColor = color;
        Label label = new Label(text, labelStyle);
        label.setFontScale(scale);
        label.setPosition(x, y);
        stage.addActor(label);
        showInfoLabels.add(label);
    }

    private void addButtonImage(Stage stage, TextureAtlas textureAtlas, String imageName, float x, float y) {
        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion(imageName));
        ImageButton imageButton = new ImageButton(imageButtonStyle);
        imageButton.setPosition(x, y);
        stage.addActor(imageButton);
        showInfoButtons.add(imageButton);
    }

    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////

    /********************* TRADE MENU ***************************/
    public void tradeMenu(final Stage stage) {
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));

        Button goToNewTradeRequestMenuButton = new TextButton("Request a new trade", skin);
        goToNewTradeRequestMenuButton.setPosition(530, 60);
        goToNewTradeRequestMenuButton.setColor(Color.OLIVE);
        goToNewTradeRequestMenuButton.setSize(200, 50);

        stage.addActor(goToNewTradeRequestMenuButton);
        tradeMenuButtons.add(goToNewTradeRequestMenuButton);


        Button goToRecievedTradesMenuButton = new TextButton("Received trade requests", skin);
        goToRecievedTradesMenuButton.setPosition(730, 60);
        goToRecievedTradesMenuButton.setColor(Color.OLIVE);
        goToRecievedTradesMenuButton.setSize(200, 50);

        stage.addActor(goToRecievedTradesMenuButton);
        tradeMenuButtons.add(goToRecievedTradesMenuButton);


        Button goToTradesHistoryMenuButton = new TextButton("Trades history", skin);
        goToTradesHistoryMenuButton.setPosition(930, 60);
        goToTradesHistoryMenuButton.setColor(Color.OLIVE);
        goToTradesHistoryMenuButton.setSize(150, 50);

        stage.addActor(goToTradesHistoryMenuButton);
        tradeMenuButtons.add(goToTradesHistoryMenuButton);


        goToNewTradeRequestMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                createNewTradeRequest(stage);
            }
        });


        goToRecievedTradesMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                showTradesSentToUser(stage);
            }
        });


        goToTradesHistoryMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                showTradeOffersSentByUser(stage);
            }
        });

    }

    ////////////////////////////////////////////////////////
    public void createNewTradeRequest(final Stage stage) {
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));

        final Array<Integer> playersInTheGame = new Array<Integer>();

        //TODO : if necessary check that is an empire alive or not
        for (Empire empire : Game.getEmpires()) {
            if (empire != GameMenuController.getCurrentEmpire())
                playersInTheGame.add(empire.getEmpireId());
        }

        final SelectBox<Integer> selectBoxForChoosingUser = new SelectBox<Integer>(skin);
        selectBoxForChoosingUser.setItems(playersInTheGame);
        selectBoxForChoosingUser.setSelectedIndex(0);

        stage.addActor(selectBoxForChoosingUser);
        addLabel(stage, "Choose the ID of user you want to trade with: ", 420, 130, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);
        selectBoxForChoosingUser.setPosition(1000, 120);
        selectBoxForChoosingUser.setColor(Color.GRAY);

        tradeMenuSelectBoxes.add(selectBoxForChoosingUser);

        addLabel(stage, "Your message: ", 420, 100, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);

        // Add a new TextField for the user's message
        final TextField messageField = new TextField("", skin);
        stage.addActor(messageField);
        messageField.setText("");
        messageField.setPosition(600, 96);
        messageField.setWidth(400);
        messageField.setColor(Color.ROYAL);

        tradeMenuTextFields.add(messageField);

        Button nextButton = new TextButton("Next", skin);
        nextButton.setPosition(640, 40);
        nextButton.setColor(Color.OLIVE);

        stage.addActor(nextButton);
        tradeMenuButtons.add(nextButton);

        final Button backButton = new TextButton("Back", skin);
        backButton.setPosition(750, 40);
        backButton.setColor(Color.OLIVE);

        stage.addActor(backButton);
        tradeMenuButtons.add(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                tradeMenu(stage);
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                chooseItemsToGiveMenu(stage, selectBoxForChoosingUser.getSelected(), messageField.getText());
            }
        });

    }

    public void chooseItemsToGiveMenu(final Stage stage, int empireID, String message) {
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));

        String[] objects = {"meat", "apple", "bread", "cheese", "wheat", "flour", "hop",
                "ale", "stone", "iron", "wood", "pitch", "bow", "crossbow", "spear", "pike",
                "mace", "sword", "leatherArmor", "metalArmor"};

        for (int i = 0; i < 10; i++) {
            addObjects(objects[i], 440 + 60 * i, 120, empireID, message);
        }

        for (int i = 10; i < objects.length; i++) {
            addObjects(objects[i], 440 + 60 * (i - 10), 45, empireID, message);
        }


        Button cancelButton = new TextButton("Cancel", skin);
        cancelButton.setPosition(720, 1);
        cancelButton.setColor(Color.OLIVE);

        stage.addActor(cancelButton);
        objectsForTradeRequest.add(cancelButton);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                createNewTradeRequest(stage);
            }
        });


    }

    public void addObjects(final String nameOfObject, int x, int y, final int empireId, final String message) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("tradeObjects.atlas"));

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion(nameOfObject));
        ImageButton button = new ImageButton(imageButtonStyle);
        button.setPosition(x, y);
        stage.addActor(button);
        objectsForTradeRequest.add(button);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                setNumberOfGivenObject(stage, nameOfObject, empireId, message);
            }
        });

    }

    public void setNumberOfGivenObject(final Stage stage, final String nameOfObject, final int empireID, final String message) {
        final Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("signs.atlas"));
        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("plus"));
        final Button plusButton = new ImageButton(imageButtonStyle);
        plusButton.setPosition(720, 110);
        stage.addActor(plusButton);

        ImageButton.ImageButtonStyle imageButtonStyle1 = new ImageButton.ImageButtonStyle();
        imageButtonStyle1.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("minus"));
        final Button minusButton = new ImageButton(imageButtonStyle1);
        minusButton.setPosition(770, 110);
        stage.addActor(minusButton);


        addLabel(stage, "Number: ", 480, 120, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");
        labelStyle.fontColor = Color.BLACK;
        final Label numberValue = new Label("0", labelStyle);
        numberValue.setFontScale(1.5f);
        numberValue.setPosition(620, 120);
        stage.addActor(numberValue);
        showInfoLabels.add(numberValue);

        tradeMenuButtons.add(plusButton);
        tradeMenuButtons.add(minusButton);

        plusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int currentValue = Integer.parseInt(numberValue.getText().toString());
                int newValue = currentValue + 5;
                numberValue.setText(String.valueOf(newValue));
            }
        });

        minusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int currentValue = Integer.parseInt(numberValue.getText().toString());
                int newValue = Math.max(currentValue - 5, 0);
                numberValue.setText(String.valueOf(newValue));
            }
        });


        Button nextButton = new TextButton("Next", skin);
        nextButton.setPosition(750, 10);
        nextButton.setColor(Color.OLIVE);

        stage.addActor(nextButton);
        objectsForTradeRequest.add(nextButton);


        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                chooseTheObjectToGetMenu(stage, empireID, message, nameOfObject, Integer.parseInt(numberValue.getText().toString()));
            }
        });


        Button cancelButton = new TextButton("Cancel", skin);
        cancelButton.setPosition(630, 10);
        cancelButton.setColor(Color.OLIVE);

        stage.addActor(cancelButton);
        objectsForTradeRequest.add(cancelButton);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                createNewTradeRequest(stage);
            }
        });

    }

    public void chooseTheObjectToGetMenu(final Stage stage, int empireID, String message, String nameOfGivenObject, int numberOfGivenObject) {
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));

        String[] objects = {"meat", "apple", "bread", "cheese", "wheat", "flour", "hop", "ale", "stone", "iron", "wood", "pitch", "bow", "crossbow", "spear", "pike", "mace", "sword", "leatherArmor", "metalArmor"};

        for (int i = 0; i < 10; i++) {
            addObjects1(nameOfGivenObject, objects[i], 440 + 60 * i, 120, empireID, message, numberOfGivenObject);
        }

        for (int i = 10; i < objects.length; i++) {
            addObjects1(nameOfGivenObject, objects[i], 440 + 60 * (i - 10), 45, empireID, message, numberOfGivenObject);
        }


        Button cancelButton = new TextButton("Cancel", skin);
        cancelButton.setPosition(720, 1);
        cancelButton.setColor(Color.OLIVE);

        stage.addActor(cancelButton);
        objectsForTradeRequest.add(cancelButton);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                createNewTradeRequest(stage);
            }
        });


    }


    public void addObjects1(final String nameOfGivenObject, final String nameOfWantedObject, int x, int y, final int empireId, final String message, final int numberOfGivenObject) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("tradeObjects.atlas"));

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion(nameOfWantedObject));
        ImageButton button = new ImageButton(imageButtonStyle);
        button.setPosition(x, y);
        stage.addActor(button);
        objectsForTradeRequest.add(button);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                setPriceWeWantForTheObject(stage, nameOfWantedObject, nameOfGivenObject, empireId, message, numberOfGivenObject);
            }
        });

    }

    public void setPriceWeWantForTheObject(final Stage stage, final String nameOfWantedObject, final String nameOfGivenObject, final int empireId, final String message, final int numberOfGivenObject) {
        final Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
        addLabel(stage, "Price: ", 480, 84, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);

        final Slider priceBar = new Slider(0, 1000, 5, false, skin);
        priceBar.setValue(0);
        priceBar.setPosition(600, 80);
        priceBar.setWidth(400);
        stage.addActor(priceBar);

        chooseRateSliders.add(priceBar);

        final Label priceValueLabel = new Label(priceBar.getValue() + "", skin);
        priceValueLabel.setPosition(1020, 84);
        stage.addActor(priceValueLabel);

        showInfoLabels.add(priceValueLabel);

        priceBar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float newValue = priceBar.getValue();
                priceValueLabel.setText(String.valueOf(newValue));
            }
        });


        Button finalizeButton = new TextButton("Send request", skin);
        finalizeButton.setPosition(730, 10);
        finalizeButton.setColor(Color.OLIVE);

        stage.addActor(finalizeButton);
        objectsForTradeRequest.add(finalizeButton);

        Button cancelButton = new TextButton("Cancel", skin);
        cancelButton.setPosition(630, 10);
        cancelButton.setColor(Color.OLIVE);

        stage.addActor(cancelButton);
        objectsForTradeRequest.add(cancelButton);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                createNewTradeRequest(stage);
            }
        });

        finalizeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                String out = TradeMenuController.trade(message, nameOfWantedObject, nameOfGivenObject, (int) priceBar.getValue(), numberOfGivenObject, String.valueOf(empireId));

                if (out.equals("success"))
                    out += "\nYour message:\n\"" + message + "\"";

                Dialog dialog = new Dialog("", skin);
                dialog.text(out);
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);

            }
        });
    }


/////////////////////////////////////////////////////////////

    public void showTradesSentToUser(final Stage stage) {
        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));

        // Create a scroll pane to display the trade history
        final ScrollPane scrollPane = new ScrollPane(null, skin);
        scrollPane.setPosition(450, 70);
        scrollPane.setSize(650, 100);


        StringBuilder sb = new StringBuilder();
        sb.append(TradeMenuController.listOfTradesThatHasBeenSentToUser());

        // Create a label to display the trade history
        final Label tradeHistoryLabel = new Label(sb.toString(), skin);
        tradeHistoryLabel.setWrap(true);

        // Set the label as the contents of the scroll pane
        scrollPane.setWidget(tradeHistoryLabel);
        scrollPanes.add(scrollPane);

        // Create a button to go back to the trade menu
        Button backButton = new TextButton("Back", skin);
        backButton.setPosition(870, 10);
        backButton.setSize(100, 50);
        backButton.setColor(Color.OLIVE);

        showInfoButtons.add(backButton);

        Button checkingRequestsButton = new TextButton("Check a trade request", skin);
        checkingRequestsButton.setPosition(670, 10);
        checkingRequestsButton.setSize(200, 50);
        checkingRequestsButton.setColor(Color.OLIVE);


        if (!tradeHistoryLabel.getText().toString().matches("^$")) {
            stage.addActor(checkingRequestsButton);
            tradeMenuButtons.add(checkingRequestsButton);
        }
        stage.addActor(scrollPane);
        stage.addActor(backButton);

        checkingRequestsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                checkRequestMenu(stage);
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                tradeMenu(stage);
            }
        });
    }

    public void checkRequestMenu(final Stage stage) {
        final Skin skin = new Skin(Gdx.files.internal("assets/default.json"));

        Array<Integer> uncheckedRequestIDs = new Array<>();
        for (int i = 0; i < GameMenuController.getGame().getAllTrades().size(); i++) {
            if (!GameMenuController.getGame().getAllTrades().get(i).isCheckedByGetterEmpire() && GameMenuController.getGame().getAllTrades().get(i).getGetterEmpire() == GameMenuController.getCurrentEmpire())
                uncheckedRequestIDs.add(i);
        }


        final SelectBox<Integer> selectIdOfTrade = new SelectBox<Integer>(skin);
        selectIdOfTrade.setItems(uncheckedRequestIDs);

        tradeMenuSelectBoxes.add(selectIdOfTrade);


        stage.addActor(selectIdOfTrade);
        addLabel(stage, "Select trade ID: ", 460, 130, 1.5f, com.badlogic.gdx.graphics.Color.BLACK, skin);
        selectIdOfTrade.setPosition(690, 120);
        selectIdOfTrade.setColor(Color.GRAY);

        Button acceptButton = new TextButton("Accept this trade", skin);
        acceptButton.setPosition(520, 10);
        acceptButton.setSize(200, 50);
        acceptButton.setColor(Color.OLIVE);

        Button rejectButton = new TextButton("Reject this trade", skin);
        rejectButton.setPosition(740, 10);
        rejectButton.setSize(200, 50);
        rejectButton.setColor(Color.OLIVE);


        Button backButton = new TextButton("Back", skin);
        backButton.setPosition(960, 10);
        backButton.setSize(80, 50);
        backButton.setColor(Color.OLIVE);

        stage.addActor(acceptButton);
        stage.addActor(rejectButton);
        stage.addActor(backButton);

        tradeMenuButtons.add(backButton);
        tradeMenuButtons.add(rejectButton);
        tradeMenuButtons.add(acceptButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                showTradesSentToUser(stage);
            }
        });

        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                String out = TradeMenuController.tradeAccept(selectIdOfTrade.getSelected());

                Dialog dialog = new Dialog("", skin);
                dialog.text(out);
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);

                showTradesSentToUser(stage);
            }
        });


        rejectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                String out = TradeMenuController.tradeReject(selectIdOfTrade.getSelected());

                Dialog dialog = new Dialog("", skin);
                dialog.text(out);
                GameMenuGDX.stage1.addActor(dialog);
                TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                dialog.show(GameMenuGDX.stage1);
                dialog.setPosition(0, 0);

                showTradesSentToUser(stage);
            }
        });


    }

/////////////////////////////////////////////////////////////

    public void showTradeOffersSentByUser(final Stage stage) {

        Skin skin = new Skin(Gdx.files.internal("assets/default.json"));

        // Create a scroll pane to display the trade history
        final ScrollPane scrollPane = new ScrollPane(null, skin);
        scrollPane.setPosition(450, 70);
        scrollPane.setSize(650, 100);
        scrollPanes.add(scrollPane);


        StringBuilder sb = new StringBuilder();
        sb.append(TradeMenuController.listOfTradesThatUserHasSent());

        // Create a label to display the trade history
        final Label tradeHistoryLabel = new Label(sb.toString(), skin);
        tradeHistoryLabel.setWrap(true);

        // Set the label as the contents of the scroll pane
        scrollPane.setWidget(tradeHistoryLabel);

        // Create a button to go back to the trade menu
        Button backButton = new TextButton("Back", skin);
        backButton.setPosition(750, 10);
        backButton.setSize(100, 50);
        backButton.setColor(Color.OLIVE);

        showInfoButtons.add(backButton);


        stage.addActor(scrollPane);
        stage.addActor(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMenu();
                tradeMenu(stage);
            }
        });
    }
}