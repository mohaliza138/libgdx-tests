package view.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import view.game.Controller.GameMenuController;
import view.game.Controller.MapMenuController;
import view.game.Enums.BuildingType;
import view.game.Model.Building;
import view.game.Model.Cell;
import view.game.Model.Unit;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GameMenuGDX extends ApplicationAdapter implements InputProcessor {
    public static float MAP_WIDTH;
    public static float MAP_HEIGHT;
    public static OrthographicCamera camera;
    public static Sprite[][] sprites;
    public static Stage stage;
    public static Stage stage1;
    public static ArrayList<Sprite> units;
    public static ArrayList<Sprite> poison;
    public static ArrayList<int[]> poisonCoordiantes;
    public static ArrayList<int[]> fireCoordinates;
    public static ArrayList<Sprite> fire;
    private static float screenWidth;
    private static float screenHeight;
    private final float CAMERA_SPEED;
    private final float ZOOM_SPEED;
    private final float maxZoom;
    private final float minZoom;
    private final int mapSize;
    private AssetManager textureAssets;
    private TextureAtlas textureAtlas;
    private AssetManager towerAssets;
    private TextureAtlas towerAtlas;
    private AssetManager map2dAssets;
    private TextureAtlas map2dAtlas;
    private SpriteBatch spriteBatch;
    private Sprite[][] sprites2d;
    private int currentTileJ;
    private int currentTileI;
    private float hoverTime;
    private float deleteTime;
    private Skin skin;
    private Dialog dialog;
    private Menu menu;
    private int draggingTileI;
    private int draggingTileJ;
    private ArrayList<int[]> selectedCells;
    private ArrayList<Building> clipboard;
    private int[] lastCoordinate;
    private Cell lastCell;


    public GameMenuGDX(int mapSize) throws Exception {
        clipboard = new ArrayList<>();
        selectedCells = new ArrayList<>();
        poisonCoordiantes = new ArrayList<>();
        fireCoordinates = new ArrayList<>();
        fire = new ArrayList<>();
        this.mapSize = mapSize;
        MAP_HEIGHT = 16 * mapSize;
        MAP_WIDTH = 30 * mapSize;
        CAMERA_SPEED = 70f;
        ZOOM_SPEED = 0.1f;
        maxZoom = 1.0f;
        minZoom = 0.1f;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        screenWidth = gd.getDisplayMode().getWidth();
        screenHeight = gd.getDisplayMode().getHeight() - 150;
        draggingTileI = draggingTileJ = currentTileJ = currentTileI = -1;
        hoverTime = 0;
        deleteTime = 0;
        units = new ArrayList<>();
        poison = new ArrayList<>();
    }

    public static int getPositionJ(float x, float y) {
        double atan = Math.atan(y / (x + MAP_WIDTH / 2));
        return (int) (((Math.cos(Math.atan((float) 8 / 15) - atan) - Math.sin(Math.atan((float) 8 / 15) - atan) / Math.tan(2 * Math.atan((float) 8 / 15))) * Math.sqrt((x + MAP_WIDTH / 2) * (x + MAP_WIDTH / 2) + y * y)) / Math.sqrt(289));
    }

    public static int getPositionI(float x, float y) {
        return (int) ((Math.sin(Math.atan((float) 8 / 15) - Math.atan(y / (x + MAP_WIDTH / 2))) * Math.sqrt((x + MAP_WIDTH / 2) * (x + MAP_WIDTH / 2) + y * y)) / (Math.sqrt(289) * Math.sin(2 * Math.atan((float) 8 / 15))));
    }

    public static float getCursorX() {
        return camera.zoom * (Gdx.input.getX() - screenWidth / 2) + camera.position.x;
    }

    public static float getCursorY() {
        return camera.zoom * (screenHeight / 2 - Gdx.input.getY()) + camera.position.y;
    }

    public static float getXFromIAndJ(int i, int j) {
        return ((j + i) * 15) - (MAP_WIDTH / 2);
    }

    public static float getYFromIAndJ(int i, int j) {
        return (j - i - 1) * 8;
    }

    @Override
    public void create() {
        stage = new Stage();
        stage1 = new Stage();
        menu = new Menu();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        spriteBatch = new SpriteBatch();


        skin = new Skin(Gdx.files.internal("assets/default.json"));
        dialog = new Dialog("Warning", skin);

        textureAssets = new AssetManager();
        textureAssets.load("Tiles.atlas", TextureAtlas.class);
        textureAssets.finishLoading();

        textureAtlas = textureAssets.get("Tiles.atlas");

        towerAssets = new AssetManager();
        towerAssets.load("Buildings.atlas", TextureAtlas.class);
        towerAssets.finishLoading();

        towerAtlas = towerAssets.get("Buildings.atlas");

        map2dAssets = new AssetManager();
        map2dAssets.load("miniMap.atlas", TextureAtlas.class);
        map2dAssets.finishLoading();

        map2dAtlas = map2dAssets.get("miniMap.atlas");


        sprites = new Sprite[mapSize][mapSize];
        sprites2d = new Sprite[mapSize][mapSize];

        camera = new OrthographicCamera();
        camera.zoom = 1.0f;
        camera.setToOrtho(false, screenWidth, screenHeight);
        camera.position.set(0, 0, 0);

        createMap(textureAtlas, map2dAtlas);
        GameMenuController gameMenuController = new GameMenuController();
        try {
            gameMenuController.startANewGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Menu.scribeReport();
    }

    @Override
    public void render() {
        if (currentTileJ == getPositionJ(getCursorX(), getCursorY()) && currentTileI == getPositionI(getCursorX(),
                getCursorY())) {
            if (hoverTime <= 1f && hoverTime >= 0f) {
                hoverTime += Gdx.graphics.getDeltaTime();
            } else if (hoverTime > 1f) {
                hoverTime = -1f;
                displayDialog();
            }
        } else {
            hoverTime = 0f;
            currentTileJ = getPositionJ(getCursorX(), getCursorY());
            currentTileI = getPositionI(getCursorX(), getCursorY());
            if (currentTileJ > 99 || currentTileJ < 0 || currentTileI > 99 || currentTileI < 0)
                currentTileJ = currentTileI = -1;
            if (!stage1.getActors().isEmpty()) {
                for (int i = 0; i < stage1.getActors().size; i++) {
                    if (stage1.getActors().get(i).getX() > 0) {
                        stage1.getActors().get(i).remove();
                    }
                }
            }
        }
        if (deleteTime < 2f) {
            deleteTime += Gdx.graphics.getDeltaTime();
        } else {
            deleteTime = 0;
            for (int i = 0; i < stage1.getActors().size; i++) {
                if (stage1.getActors().get(i).getX() == 0) {
                    stage1.getActors().get(i).remove();
                }
            }
        }
        handleInput();
        Gdx.gl.glClearColor(0.5f, 0.8f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (int i = 0; i < mapSize; i++) {
            for (int j = mapSize - 1; j >= 0; j--) {
                sprites[i][j].draw(spriteBatch);
            }
        }
        for (Sprite tower : units) {
            tower.draw(spriteBatch);
        }
        for (Sprite tower : poison) {
            tower.draw(spriteBatch);
        }
        for (Sprite tower : fire) {
            tower.draw(spriteBatch);
        }
        spriteBatch.end();


        stage.act();
        stage.draw();

        stage1.act(Gdx.graphics.getDeltaTime());
        stage1.draw();
        spriteBatch.begin();
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                sprites2d[i][j].setSize(1.8F * camera.zoom, 1.8F * camera.zoom);
                sprites2d[i][j].setPosition(camera.position.x + (3 * screenWidth / 8 + j * 1.8F) * camera.zoom,
                        camera.position.y + (i * 1.8F - screenHeight / 2) * camera.zoom);
                sprites2d[i][j].draw(spriteBatch);
            }
        }
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }

    public void fitMap() {
        if (camera.position.x > (MAP_WIDTH / 2 - camera.viewportWidth * camera.zoom / 2))
            camera.position.x = MAP_WIDTH / 2 - camera.viewportWidth * camera.zoom / 2;
        else if (camera.position.x < -(MAP_WIDTH / 2 - camera.viewportWidth * camera.zoom / 2))
            camera.position.x = -(MAP_WIDTH / 2 - camera.viewportWidth * camera.zoom / 2);
        if (camera.position.y > MAP_HEIGHT / 2 - camera.viewportHeight * camera.zoom / 2)
            camera.position.y = MAP_HEIGHT / 2 - camera.viewportHeight * camera.zoom / 2;
        else if (camera.position.y < -300 - (MAP_HEIGHT / 2 - camera.viewportHeight * camera.zoom / 2))
            camera.position.y =
                    -300 - (MAP_HEIGHT / 2 - camera.viewportHeight * camera.zoom / 2);
    }

    private void handleInput() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float cameraSpeed = CAMERA_SPEED * deltaTime;
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && camera.position.y - getCursorY() < MAP_HEIGHT / 4 - 300) {
            float mouseX = -Gdx.input.getDeltaX() * camera.zoom;
            float mouseY = Gdx.input.getDeltaY() * camera.zoom;
            camera.translate(mouseX * cameraSpeed, mouseY * cameraSpeed);
            fitMap();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.NUM_2 && camera.zoom < 5) {
            camera.zoom += 0.1;
        }
        if (keycode == Input.Keys.NUM_3 && camera.zoom > 0.1) {
            camera.zoom -= 0.1;
        }
        if (keycode == Input.Keys.NUM_4) {
            GameMenuController.setUnitMode("defensive");
        }
        if (keycode == Input.Keys.NUM_5) {
            GameMenuController.setUnitMode("standing");
        }
        if (keycode == Input.Keys.NUM_6) {
            GameMenuController.setUnitMode("offensive");
        }
        if (keycode == Input.Keys.I) {
            int unitsNumber = 0;
            double rates = 0;
            double minRate = 0;
            double maxRate = 0;
            for (int[] coordinate : selectedCells) {
                Cell cell = GameMenuController.getMap().getMap()[coordinate[0]][coordinate[1]];
                unitsNumber += cell.getUnits().size();
                if (cell.getBuilding() != null) {
                    if (minRate > cell.getBuilding().getRate()) minRate = cell.getBuilding().getRate();
                    if (maxRate < cell.getBuilding().getRate()) maxRate = cell.getBuilding().getRate();
                    rates += cell.getBuilding().getRate();
                }
            }
            rates = rates / selectedCells.size();
            Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
            Dialog dialog = new Dialog("", skin);
            if (selectedCells.size() == 0) dialog.text("select some cells");
            else
                dialog.text("units number : " + unitsNumber + "\naverage rate : " + rates + "\nmin rate : " + minRate + "\nmax rate : " + maxRate);
            GameMenuGDX.stage1.addActor(dialog);
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
            dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
            dialog.show(GameMenuGDX.stage1);
            dialog.setPosition(0, 0);
        }
        if (keycode == Input.Keys.C) {
            String copy;
            if (selectedCells.size() == 0) copy = "select a cell";
            else {
                Cell cell = GameMenuController.getMap().getMap()[selectedCells.get(0)[0]][selectedCells.get(0)[1]];
                if (selectedCells.size() != 1) copy = "only select one cell";
                else if (cell.getBuilding() == null) copy = "select a building";
                else {
                    clipboard.add(cell.getBuilding());
                    copy = "Success";
                }
            }
            Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
            Dialog dialog = new Dialog("", skin);
            dialog.text(copy);
            GameMenuGDX.stage1.addActor(dialog);
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
            dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
            dialog.show(GameMenuGDX.stage1);
            dialog.setPosition(0, 0);
        }
        if (keycode == Input.Keys.A) {
            int k = getPositionI(getCursorX(), getCursorY());
            int j = getPositionJ(getCursorX(), getCursorY());
            String out = GameMenuController.attackEnemy(k, j);
            Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
            Dialog dialog = new Dialog("", skin);
            dialog.text(out);
            GameMenuGDX.stage1.addActor(dialog);
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
            dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
            dialog.show(GameMenuGDX.stage1);
            dialog.setPosition(0, 0);
        }
        if (keycode == Input.Keys.V) {
            String paste;
            if (selectedCells.size() == 0) paste = "select a cell";
            else if (selectedCells.size() != 1) paste = "only select one cell";
            else if (GameMenuController.getMap().getMap()[selectedCells.get(0)[0]][selectedCells.get(0)[1]].getBuilding() != null)
                paste = "select an empty cell";
            else if (clipboard.size() == 0) paste = "clipboard is empty";
            else {
                paste = BuildBuildings(clipboard.get(clipboard.size() - 1).getBuildingType().getName());
                if (paste.equals("success")) {
                    lastCell = GameMenuController.getMap().getMap()[selectedCells.get(0)[0]][selectedCells.get(0)[1]];
                    lastCoordinate = new int[]{selectedCells.get(0)[0], selectedCells.get(0)[1]};
                    for (int[] coordinate : selectedCells) {
                        if (coordinate[0] == selectedCells.get(0)[0] && coordinate[1] == selectedCells.get(0)[1]) {
                            selectedCells.remove(coordinate);
                            break;
                        }
                    }
                }
            }
            Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
            Dialog dialog = new Dialog("", skin);
            dialog.text(paste);
            GameMenuGDX.stage1.addActor(dialog);
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
            dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
            dialog.show(GameMenuGDX.stage1);
            dialog.setPosition(0, 0);
        }
        if (keycode == Input.Keys.X) {
            String clipboard = "";
            if (this.clipboard.size() == 0) clipboard = "clipboard is empty";
            else {
                for (int i = this.clipboard.size() - 1; i > this.clipboard.size() - 7; i--) {
                    clipboard = clipboard + this.clipboard.get(i).getBuildingType().getName() + "\n";
                    if (i == 0) break;
                }
            }
            Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
            Dialog dialog = new Dialog("", skin);
            dialog.text(clipboard);
            GameMenuGDX.stage1.addActor(dialog);
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
            dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
            dialog.show(GameMenuGDX.stage1);
            dialog.setPosition(0, 0);
        }
        if (keycode == Input.Keys.Z) {
            String undo;
            if (lastCell == null) undo = "you changed nothing recently";
            else {
                undo = "success";
                GameMenuController.getCurrentEmpire().getBuildings().remove(GameMenuController.getMap().getMap()[lastCoordinate[0]][lastCoordinate[1]].getBuilding());
                GameMenuController.getMap().getMap()[lastCoordinate[0]][lastCoordinate[1]].setBuilding(null);
                //System.out.println(GameMenuController.getMap().getMap()[lastCoordinate[0]][lastCoordinate[1]].getType());
                sprites[lastCoordinate[0]][lastCoordinate[1]].setPosition(getXFromIAndJ(lastCoordinate[0], lastCoordinate[1]), getYFromIAndJ(lastCoordinate[0], lastCoordinate[1]));
                if (GameMenuController.getMap().getMap()[lastCoordinate[0]][lastCoordinate[1]].getType().equals("earth")) {
                    sprites[lastCoordinate[0]][lastCoordinate[1]] = new Sprite(textureAtlas.findRegion("ground0"));
                    sprites[lastCoordinate[0]][lastCoordinate[1]].setPosition(getXFromIAndJ(lastCoordinate[0], lastCoordinate[1]), getYFromIAndJ(lastCoordinate[0], lastCoordinate[1]));
                } else if (GameMenuController.getMap().getMap()[lastCoordinate[0]][lastCoordinate[1]].getType().equals("oil")) {
                    sprites[lastCoordinate[0]][lastCoordinate[1]] = new Sprite(textureAtlas.findRegion("oil0"));
                    sprites[lastCoordinate[0]][lastCoordinate[1]].setPosition(getXFromIAndJ(lastCoordinate[0], lastCoordinate[1]), getYFromIAndJ(lastCoordinate[0], lastCoordinate[1]));
                } else if (GameMenuController.getMap().getMap()[lastCoordinate[0]][lastCoordinate[1]].getType().equals("thickGrass")) {
                    sprites[lastCoordinate[0]][lastCoordinate[1]] = new Sprite(textureAtlas.findRegion("grass0"));
                    sprites[lastCoordinate[0]][lastCoordinate[1]].setPosition(getXFromIAndJ(lastCoordinate[0], lastCoordinate[1]), getYFromIAndJ(lastCoordinate[0], lastCoordinate[1]));
                } else if (GameMenuController.getMap().getMap()[lastCoordinate[0]][lastCoordinate[1]].getType().equals("boulder")) {
                    sprites[lastCoordinate[0]][lastCoordinate[1]] = new Sprite(textureAtlas.findRegion("boulder0"));
                    sprites[lastCoordinate[0]][lastCoordinate[1]].setPosition(getXFromIAndJ(lastCoordinate[0], lastCoordinate[1]), getYFromIAndJ(lastCoordinate[0], lastCoordinate[1]));
                } else if (GameMenuController.getMap().getMap()[lastCoordinate[0]][lastCoordinate[1]].getType().equals("ironTexture")) {
                    sprites[lastCoordinate[0]][lastCoordinate[1]] = new Sprite(textureAtlas.findRegion("ironTexture0"));
                    sprites[lastCoordinate[0]][lastCoordinate[1]].setPosition(getXFromIAndJ(lastCoordinate[0], lastCoordinate[1]), getYFromIAndJ(lastCoordinate[0], lastCoordinate[1]));
                } else if (GameMenuController.getMap().getMap()[lastCoordinate[0]][lastCoordinate[1]].getType().equals("sea")) {
                    sprites[lastCoordinate[0]][lastCoordinate[1]].setPosition(getXFromIAndJ(lastCoordinate[0], lastCoordinate[1]), getYFromIAndJ(lastCoordinate[0], lastCoordinate[1]));
                    sprites[lastCoordinate[0]][lastCoordinate[1]] = new Sprite(textureAtlas.findRegion("sea0"));
                } else if (GameMenuController.getMap().getMap()[lastCoordinate[0]][lastCoordinate[1]].getType().equals("oasisGrass")) {
                    sprites[lastCoordinate[0]][lastCoordinate[1]].setPosition(getXFromIAndJ(lastCoordinate[0], lastCoordinate[1]), getYFromIAndJ(lastCoordinate[0], lastCoordinate[1]));
                    sprites[lastCoordinate[0]][lastCoordinate[1]] = new Sprite(textureAtlas.findRegion("wheat0"));
                }
            }
            Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
            Dialog dialog = new Dialog("", skin);
            dialog.text(undo);
            GameMenuGDX.stage1.addActor(dialog);
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
            dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
            dialog.show(GameMenuGDX.stage1);
            dialog.setPosition(0, 0);

        }
        if (keycode == Input.Keys.UP) {
            moveUp();
        }
        if (keycode == Input.Keys.LEFT) {
            moveLeft();
        }
        if (keycode == Input.Keys.RIGHT) {
            moveRight();
        }
        if (keycode == Input.Keys.DOWN) {
            moveDown();
        }
        if (keycode == Input.Keys.R) {
            String repair;
            if (selectedCells.size() == 0) repair = "select a cell";
            else if (selectedCells.size() != 1) repair = "only select one cell";
            else if (GameMenuController.getMap().getMap()[selectedCells.get(0)[0]][selectedCells.get(0)[1]].getBuilding() == null)
                repair = "select a building";
            else {
                GameMenuController.setSelectedBuilding(GameMenuController.getMap().getMap()[selectedCells.get(0)[0]][selectedCells.get(0)[1]].getBuilding());
                GameMenuController.getSelectedCoordinates().put("building", selectedCells.get(0));
                repair = GameMenuController.repair();
            }
            Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
            Dialog dialog = new Dialog("", skin);
            dialog.text(repair);
            GameMenuGDX.stage1.addActor(dialog);
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
            dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
            dialog.show(GameMenuGDX.stage1);
            dialog.setPosition(0, 0);
        }
        return false;
    }

    private void moveUp() {
        for (int k = 0; k < GameMenuController.selectedUnits.size(); k++) {
            Unit unit = GameMenuController.selectedUnits.get(k);
            int i = unit.getI();
            int j = unit.getJ();
            if (GameMenuController.isPassable(i, j + 1)) {
                GameMenuController.map.getMap()[i][j].getUnits().remove(unit);
                GameMenuController.map.getMap()[i][j + 1].getUnits().add(unit);
                unit.setJ(j + 1);
                unit.getSprite().setPosition(getXFromIAndJ(i, j + 1), getYFromIAndJ(i, j + 1));
            }
        }
        GameMenuController.selectedUnits.clear();
    }

    private void moveRight() {
        for (int k = 0; k < GameMenuController.selectedUnits.size(); k++) {
            Unit unit = GameMenuController.selectedUnits.get(k);
            int i = unit.getI();
            int j = unit.getJ();
            if (GameMenuController.isPassable(i + 1, j)) {
                GameMenuController.map.getMap()[i][j].getUnits().remove(unit);
                GameMenuController.map.getMap()[i + 1][j].getUnits().add(unit);
                unit.setI(i + 1);
                unit.getSprite().setPosition(getXFromIAndJ(i + 1, j), getYFromIAndJ(i + 1, j));
            }
        }
        GameMenuController.selectedUnits.clear();
    }

    private void moveLeft() {
        for (int k = 0; k < GameMenuController.selectedUnits.size(); k++) {
            Unit unit = GameMenuController.selectedUnits.get(k);
            int i = unit.getI();
            int j = unit.getJ();
            if (GameMenuController.isPassable(i - 1, j)) {
                GameMenuController.map.getMap()[i][j].getUnits().remove(unit);
                GameMenuController.map.getMap()[i - 1][j].getUnits().add(unit);
                unit.setI(i - 1);
                unit.getSprite().setPosition(getXFromIAndJ(i - 1, j), getYFromIAndJ(i - 1, j));
            }
        }
        GameMenuController.selectedUnits.clear();
    }

    private void moveDown() {
        for (int k = 0; k < GameMenuController.selectedUnits.size(); k++) {
            Unit unit = GameMenuController.selectedUnits.get(k);
            int i = unit.getI();
            int j = unit.getJ();
            if (GameMenuController.isPassable(i, j - 1)) {
                GameMenuController.map.getMap()[i][j].getUnits().remove(unit);
                GameMenuController.map.getMap()[i][j - 1].getUnits().add(unit);
                unit.setJ(j - 1);
                unit.getSprite().setPosition(getXFromIAndJ(i, j - 1), getYFromIAndJ(i, j - 1));
            }
        }
        GameMenuController.selectedUnits.clear();
    }

    public void displayDialog() {
        if (Gdx.input.getY() > 730) {
            return;
        }
        dialog = new Dialog("", skin);
        int j = getPositionJ((getCursorX()), (((getCursorY()))));
        int i = getPositionI((getCursorX()), (((getCursorY()))));
        dialog.text(MapMenuController.showDetail(i, j));
        stage1.addActor(dialog);
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
        dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
        dialog.show(stage1);
        dialog.setPosition(Gdx.input.getX() - dialog.getWidth() / 2, screenHeight - Gdx.input.getY() + 10);
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            selectedCells.clear();
        }
        if (i3 == 0 && Gdx.input.isKeyPressed(Input.Keys.S)) {
            int k = getPositionI(getCursorX(), getCursorY());
            int j = getPositionJ(getCursorX(), getCursorY());

            if (sprites[k][j].getColor().b == 0.5 && sprites[k][j].getColor().g == 0.5 && sprites[k][j].getColor().r == 0.5) {
                sprites[k][j].setColor(1, 1, 1, 1);
                for (int[] coordinate : selectedCells) {
                    if (coordinate[0] == k && coordinate[1] == j) {
                        selectedCells.remove(coordinate);
                        break;
                    }
                }
                GameMenuController.removeSelect(k, j);
            } else {
                if (GameMenuController.map.getMap()[k][j].getBuilding() != null && GameMenuController.map.getMap()[k][j].getBuilding().getBuildingType().equals(BuildingType.MERCENARY_POST)) {
                    menu.MercenaryPostMenu();
                } else if (GameMenuController.map.getMap()[k][j].getBuilding() != null && GameMenuController.map.getMap()[k][j].getBuilding().getBuildingType().equals(BuildingType.BARRACK)) {
                    menu.BarrackMenu();
                } else if (GameMenuController.map.getMap()[k][j].getBuilding() != null && GameMenuController.map.getMap()[k][j].getBuilding().getBuildingType().equals(BuildingType.ENGINEER_GUILD)) {
                    menu.EngineerGuildMenu();
                }
                selectedCells.add(new int[]{k, j});
                if (GameMenuController.map.getMap()[k][j].getUnits().size() > 0) {
                    GameMenuController.selectUnit(k, j);
                    Skin skin = new Skin(Gdx.files.internal("assets/default.json"));
                    Dialog dialog = new Dialog("", skin);
                    dialog.text("press 4 -> defencive\npress 5 ->standing\npress 6-> offensive");
                    GameMenuGDX.stage1.addActor(dialog);
                    TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("hoverBackGround.pack"));
                    dialog.setBackground(new TextureRegionDrawable(textureAtlas.findRegion("background")));
                    dialog.show(GameMenuGDX.stage1);
                    dialog.setPosition(0, 0);
                }
                sprites[k][j].setColor(0.5f, 0.5f, 0.5f, 1);
            }
            return true;
        }
        return false;
    }


    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        if (i2 == 0 && Gdx.input.isKeyPressed(Input.Keys.S)) {
            int x1 = Gdx.input.getX();
            int y1 = Gdx.input.getY();
            Vector3 input = new Vector3(x1, y1, 0);
            int iOfTile = getPositionI(getCursorX(), getCursorY());
            int jOfTile = getPositionJ(getCursorX(), getCursorY());
            camera.unproject(input);
            Sprite sprite = sprites[iOfTile][jOfTile];
            if (iOfTile != draggingTileI || jOfTile != draggingTileJ) {
                if (sprite.getColor().b == 0.5 && sprite.getColor().g == 0.5 && sprite.getColor().r == 0.5)
                    sprite.setColor(1, 1, 1, 1);
                else sprite.setColor(0.5f, 0.5f, 0.5f, 1);
            }
            draggingTileI = iOfTile;
            draggingTileJ = jOfTile;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    public void createMap(TextureAtlas textureAtlas, TextureAtlas map2dAtlas) {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("earth");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("ground0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("ground"));
            }
        }
        for (int i = 45; i < 55; i++) {
            for (int j = 45; j < 55; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("oil");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("oil0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("oil"));
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 15; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("thickGrass");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("grass0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("grass"));
            }
        }
        for (int i = 97; i < 100; i++) {
            for (int j = 0; j < 15; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("thickGrass");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("grass0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("grass"));
            }
        }
        for (int i = 97; i < 100; i++) {
            for (int j = 85; j < 100; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("thickGrass");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("grass0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("grass"));
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 85; j < 100; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("thickGrass");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("grass0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("grass"));
            }
        }

        for (int i = 92; i < 95; i++) {
            for (int j = 92; j < 100; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("boulder");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("boulder0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("boulder"));
            }
        }
        for (int i = 92; i < 95; i++) {
            for (int j = 0; j < 8; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("boulder");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("boulder0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("boulder"));
            }
        }
        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("boulder");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("boulder0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("boulder"));
            }
        }
        for (int i = 5; i < 8; i++) {
            for (int j = 92; j < 100; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("boulder");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("boulder0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("boulder"));
            }
        }
        for (int i = 97; i < 100; i++) {
            for (int j = 76; j < 78; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("ironTexture");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("iron0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("iron"));
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 76; j < 78; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("ironTexture");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("iron0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("iron"));
            }
        }
        for (int i = 97; i < 100; i++) {
            for (int j = 20; j < 22; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("ironTexture");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("iron0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("iron"));
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 20; j < 22; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("ironTexture");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("iron0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("iron"));
            }
        }
        for (int i = 30; i < 32; i++) {
            for (int j = 20; j < 50; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("sea");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("sea0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("sea"));
            }
        }
        for (int i = 40; i < 60; i++) {
            for (int j = 80; j < 82; j++) {
                GameMenuController.map.getMap()[i][j] = new Cell("oasisGrass");
                sprites[i][j] = new Sprite(textureAtlas.findRegion("wheat0"));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion("wheat"));
            }
        }
    }

    private String BuildBuildings(String name) {
        int j = GameMenuGDX.getPositionJ((GameMenuGDX.getCursorX()), (((GameMenuGDX.getCursorY()))));
        int i = GameMenuGDX.getPositionI((GameMenuGDX.getCursorX()), (((GameMenuGDX.getCursorY()))));
        String out = GameMenuController.dropBuilding(i, j, name);
        if (out.equals("success")) {
            TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("Buildings.atlas"));
            Sprite sprite = new Sprite(textureAtlas.findRegion(name));
            sprite.setPosition(((j + i) * 15) - (GameMenuGDX.MAP_WIDTH / 2), (j - i) * 8 - 8);
            GameMenuGDX.sprites[i][j] = sprite;
        }
        return out;
    }
}