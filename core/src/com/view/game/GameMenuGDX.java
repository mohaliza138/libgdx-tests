package com.view.game;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameMenuGDX extends ApplicationAdapter implements InputProcessor{
    private AssetManager textureAssets;
    private TextureAtlas textureAtlas;
    private AssetManager towerAssets;
    private TextureAtlas towerAtlas;
    private final float MAP_WIDTH;
    private final float MAP_HEIGHT;
    private final float CAMERA_SPEED;
    private final float ZOOM_SPEED;
    private final float maxZoom;
    private final float minZoom;
    private final int mapSize;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private Sprite[][] sprites;
    private ArrayList<Sprite> towers;
    private final float screenWidth;
    private final float screenHeight;
    private int currentTileI;
    private int currentTileJ;
    private float hoverTime;
    private final float verticalCameraOnScreenRatio;
    private final float horizontalCameraOnScreenRatio;
    private Stage stage;
    private WindowWithTopRightCornerCloseButton windowWithTopRightCornerCloseButton;
    
    
    public GameMenuGDX (int mapSize) {
        this.mapSize = mapSize;
        MAP_HEIGHT = 16 * mapSize;
        MAP_WIDTH = 30 * mapSize;
        CAMERA_SPEED = 70f;
        ZOOM_SPEED = 0.1f;
        maxZoom = 1.0f;
        minZoom = 0.1f;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        screenWidth = gd.getDisplayMode().getWidth();
        screenHeight = gd.getDisplayMode().getHeight();
        currentTileI = currentTileJ = -1;
        hoverTime = 0;
        verticalCameraOnScreenRatio = MAP_HEIGHT / (2 * screenHeight);
        horizontalCameraOnScreenRatio = MAP_WIDTH / (2 * screenWidth);
    }
    
    
    @Override
    public void create () {
        stage = new Stage();
        windowWithTopRightCornerCloseButton = new WindowWithTopRightCornerCloseButton();
        windowWithTopRightCornerCloseButton.setSize(Gdx.graphics.getWidth(), 300);
        windowWithTopRightCornerCloseButton.setModal(true);
        windowWithTopRightCornerCloseButton.setVisible(true);
        windowWithTopRightCornerCloseButton.setMovable(true);
        windowWithTopRightCornerCloseButton.setPosition(0, 0);
        stage.addActor(windowWithTopRightCornerCloseButton);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        spriteBatch = new SpriteBatch();



        textureAssets = new AssetManager();
        textureAssets.load("Tiles.atlas", TextureAtlas.class);
        textureAssets.finishLoading();
        
        textureAtlas = textureAssets.get("Tiles.atlas");
        
        towerAssets = new AssetManager();
        towerAssets.load("Buildings.atlas", TextureAtlas.class);
        towerAssets.finishLoading();
        
        towerAtlas = towerAssets.get("Buildings.atlas");
        
        sprites = new Sprite[mapSize][mapSize];
//        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
//        Gdx.graphics.setFullscreenMode(displayMode);
        
        camera = new OrthographicCamera();
        camera.zoom = 1.0f;
        camera.setToOrtho(false, MAP_WIDTH / 2, MAP_HEIGHT / 2);
        camera.position.set(0, 0, 0);
        Random random = new Random();
        for (int i = 0; i < mapSize; i++)
            for (int j = 0; j < mapSize; j++) {
                Sprite sprite = new Sprite(textureAtlas.findRegion((i > 20 && i < 26) ? "river" + random.nextInt(3) :
                        (j > 81 && j < 89) ? "burnt" + random.nextInt(3) : "wheat" + random.nextInt(3)));
                sprite.setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites[i][j] = sprite;
            }
        
        towers = new ArrayList<>();
        
        for (int i = 0; i < mapSize; i++)
            for (int j = 0; j < mapSize; j++) {
                if (i % 10 == 8 && (i + j) % 99 == 4) {
                    Sprite sprite = new Sprite(towerAtlas.findRegion("tower"));
                    sprite.setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                    towers.add(sprite);
                }
            }

    }
    
    @Override
    public void render () {
        if (currentTileI == getPositionI(getCursorX(), getCursorY()) && currentTileJ == getPositionJ(getCursorX(),
                getCursorY())) {
            if (hoverTime <= 1f && hoverTime >= 0f) {
                hoverTime += Gdx.graphics.getDeltaTime();
            } else if (hoverTime > 1f) {
                //TODO: implement hover function
                System.out.println(hoverTime);
                System.out.println("hover at i: " + getPositionI(getCursorX(), getCursorY()) + " j: " + getPositionJ(getCursorX(), getCursorY()));
                hoverTime = -1f;
            }
        } else {
            hoverTime = 0f;
            currentTileI = getPositionI(getCursorX(), getCursorY());
            currentTileJ = getPositionJ(getCursorX(), getCursorY());
            if (currentTileI > 99 || currentTileI < 0 || currentTileJ > 99 || currentTileJ < 0) currentTileI = currentTileJ = -1;
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
        for (Sprite tower : towers) {
            tower.draw(spriteBatch);
        }
        spriteBatch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
    
    @Override
    public void dispose () {
        spriteBatch.dispose();
    }
    
    public int getPositionI (float x, float y) {
        double atan = Math.atan(y / (x + MAP_WIDTH / 2));
        return (int) (((Math.cos(Math.atan((float) 8 / 15) - atan) - Math.sin(Math.atan((float) 8 / 15) - atan) / Math.tan(2 * Math.atan((float) 8 / 15))) * Math.sqrt((x + MAP_WIDTH / 2) * (x + MAP_WIDTH / 2) + y * y)) / Math.sqrt(289));
    }
    
    public int getPositionJ (float x, float y) {
        return (int) ((Math.sin(Math.atan((float) 8 / 15) - Math.atan(y / (x + MAP_WIDTH / 2))) * Math.sqrt((x + MAP_WIDTH / 2) * (x + MAP_WIDTH / 2) + y * y)) / (Math.sqrt(289) * Math.sin(2 * Math.atan((float) 8 / 15))));
    }
    
    public void fitMap () {
        if (camera.position.x > (MAP_WIDTH / 2 - camera.viewportWidth * camera.zoom / 2))
            camera.position.x = MAP_WIDTH / 2 - camera.viewportWidth * camera.zoom / 2;
        else if (camera.position.x < -(MAP_WIDTH / 2 - camera.viewportWidth * camera.zoom / 2))
            camera.position.x = -(MAP_WIDTH / 2 - camera.viewportWidth * camera.zoom / 2);
        if (camera.position.y > MAP_HEIGHT / 2 - camera.viewportHeight * camera.zoom / 2)
            camera.position.y = MAP_HEIGHT / 2 - camera.viewportHeight * camera.zoom / 2;
        else if (camera.position.y < -windowWithTopRightCornerCloseButton.getHeight() - (MAP_HEIGHT / 2 - camera.viewportHeight * camera.zoom / 2))
            camera.position.y = -windowWithTopRightCornerCloseButton.getHeight() - (MAP_HEIGHT / 2 - camera.viewportHeight * camera.zoom / 2);
    }
    
    private void handleInput () {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float cameraSpeed = CAMERA_SPEED * deltaTime;
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && camera.position.y - getCursorY() < MAP_HEIGHT / 4 - windowWithTopRightCornerCloseButton.getHeight()) {
            float mouseX = -Gdx.input.getDeltaX() * camera.zoom * horizontalCameraOnScreenRatio;
            float mouseY = Gdx.input.getDeltaY() * camera.zoom * verticalCameraOnScreenRatio;
            camera.translate(mouseX * cameraSpeed, mouseY * cameraSpeed);
            fitMap();
        }
    }
    
    public float getCursorX () {
        return MAP_WIDTH * camera.zoom * (Gdx.input.getX() - screenWidth / 2) / (2 * screenWidth) + camera.position.x;
    }
    
    public float getCursorY () {
        return MAP_HEIGHT * camera.zoom * (screenHeight / 2 - Gdx.input.getY()) / (2 * screenHeight) + camera.position.y;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.NUM_2 && camera.zoom < 5) {
            camera.zoom += 1;
        }
        if (keycode == Input.Keys.NUM_3 && camera.zoom > 1) {
            camera.zoom -= 1;
        }

        return false;
    }
    
    public float getXFromIAndJ (int i, int j) {
        return ((j + i) * 15) - (MAP_WIDTH / 2);
    }
    
    public float getYFromIAndJ (int i, int j) {
        return (j - i - 1) * 8;
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
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
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
}
