package com.view.game;

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

public class GameMenuGDX extends ApplicationAdapter implements InputProcessor {
    private AssetManager textureAssets;
    private TextureAtlas textureAtlas;
    private AssetManager towerAssets;
    private TextureAtlas towerAtlas;
    private AssetManager map2dAssets;
    private TextureAtlas map2dAtlas;
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
    private Sprite[][] sprites2d;
    private ArrayList<Sprite> towers;
    private final float screenWidth;
    private final float screenHeight;
    private int currentTilej;
    private int currentTileI;
    private float hoverTime;
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
        screenHeight = gd.getDisplayMode().getHeight() - 150;
        currentTilej = currentTileI = -1;
        hoverTime = 0;
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
        
        map2dAssets = new AssetManager();
        map2dAssets.load("miniMap.atlas", TextureAtlas.class);
        map2dAssets.finishLoading();
        
        map2dAtlas = map2dAssets.get("miniMap.atlas");
        
        sprites = new Sprite[mapSize][mapSize];
        sprites2d = new Sprite[mapSize][mapSize];
//        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
//        Gdx.graphics.setFullscreenMode(displayMode);
        
        camera = new OrthographicCamera();
        camera.zoom = 1.0f;
        camera.setToOrtho(false, screenWidth, screenHeight);
        camera.position.set(0, 0, 0);
        Random random = new Random();
        for (int i = 0; i < mapSize; i++)
            for (int j = 0; j < mapSize; j++) {
                sprites[i][j] = new Sprite(textureAtlas.findRegion((i > 20 && i < 26) ? "river" + random.nextInt(3) :
                        (j > 81 && j < 89) ? "burnt" + random.nextInt(3) : "ground" + random.nextInt(3)));
                sprites[i][j].setPosition(getXFromIAndJ(i, j), getYFromIAndJ(i, j));
                sprites2d[i][j] = new Sprite(map2dAtlas.findRegion((i > 20 && i < 26) ? "river" : (j > 81 && j < 89)
                        ? "burnt" : "wheat"));
            }
        sprites[10][15] = new Sprite(towerAtlas.findRegion("tower"));
        sprites[10][15].setPosition(getXFromIAndJ(10, 15), getYFromIAndJ(10, 15));
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
//        System.out.println(Gdx.input.getX() + " : " + Gdx.input.getY());
//        System.out.println(getCursorX() + " : " + getCursorY() + "+");
        if (currentTilej == getPositionJ(getCursorX(), getCursorY()) && currentTileI == getPositionI(getCursorX(),
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
            currentTilej = getPositionJ(getCursorX(), getCursorY());
            currentTileI = getPositionI(getCursorX(), getCursorY());
            if (currentTilej > 99 || currentTilej < 0 || currentTileI > 99 || currentTileI < 0)
                currentTilej = currentTileI = -1;
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
    public void dispose () {
        spriteBatch.dispose();
    }
    
    public int getPositionJ (float x, float y) {
        double atan = Math.atan(y / (x + MAP_WIDTH / 2));
        return (int) (((Math.cos(Math.atan((float) 8 / 15) - atan) - Math.sin(Math.atan((float) 8 / 15) - atan) / Math.tan(2 * Math.atan((float) 8 / 15))) * Math.sqrt((x + MAP_WIDTH / 2) * (x + MAP_WIDTH / 2) + y * y)) / Math.sqrt(289));
    }
    
    public int getPositionI (float x, float y) {
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
            camera.position.y =
                    -windowWithTopRightCornerCloseButton.getHeight() - (MAP_HEIGHT / 2 - camera.viewportHeight * camera.zoom / 2);
    }
    
    private void handleInput () {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float cameraSpeed = CAMERA_SPEED * deltaTime;
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && camera.position.y - getCursorY() < MAP_HEIGHT / 4 - windowWithTopRightCornerCloseButton.getHeight()) {
            float mouseX = -Gdx.input.getDeltaX() * camera.zoom;
            float mouseY = Gdx.input.getDeltaY() * camera.zoom;
            camera.translate(mouseX * cameraSpeed, mouseY * cameraSpeed);
            fitMap();
        }
    }
    
    public float getCursorX () {
        return camera.zoom * (Gdx.input.getX() - screenWidth / 2) + camera.position.x;
    }
    
    public float getCursorY () {
        return camera.zoom * (screenHeight / 2 - Gdx.input.getY()) + camera.position.y;
    }
    
    @Override
    public boolean keyDown (int keycode) {
        if (keycode == Input.Keys.NUM_2 && camera.zoom < 2) {
            camera.zoom += 0.1;
        }
        if (keycode == Input.Keys.NUM_3 && camera.zoom > 0.1) {
            camera.zoom -= 0.1;
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
    public boolean keyUp (int i) {
        return false;
    }
    
    @Override
    public boolean keyTyped (char c) {
        return false;
    }
    
    @Override
    public boolean touchDown (int i, int i1, int i2, int i3) {
        return false;
    }
    
    @Override
    public boolean touchUp (int i, int i1, int i2, int i3) {
        return false;
    }
    
    @Override
    public boolean touchDragged (int i, int i1, int i2) {
        return false;
    }
    
    @Override
    public boolean mouseMoved (int i, int i1) {
        return false;
    }
    
    @Override
    public boolean scrolled (float v, float v1) {
        return false;
    }
}
