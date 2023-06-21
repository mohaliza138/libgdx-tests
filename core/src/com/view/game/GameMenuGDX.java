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

public class GameMenuGDX extends ApplicationAdapter {
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
        windowWithTopRightCornerCloseButton.setSize(Gdx.graphics.getWidth(), 120);
        windowWithTopRightCornerCloseButton.setModal(true);
        windowWithTopRightCornerCloseButton.setVisible(true);
        windowWithTopRightCornerCloseButton.setMovable(true);
        windowWithTopRightCornerCloseButton.setPosition(0, 0);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(windowWithTopRightCornerCloseButton);
        
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
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);
        
        camera = new OrthographicCamera();
        camera.zoom = 1.0f;
//		camera.setToOrtho(false, displayMode.width, displayMode.height);
        camera.setToOrtho(false, MAP_WIDTH / 2, MAP_HEIGHT / 2);
        camera.position.set(0, 0, 0);
        Random random = new Random();
        for (int i = 0; i < mapSize; i++)
            for (int j = 0; j < mapSize; j++) {
                Sprite sprite = new Sprite(textureAtlas.findRegion((i > 20 && i < 26) ? "river" + random.nextInt(3) :
                        (j > 81 && j < 89) ? "burnt" + random.nextInt(3) : "wheat" + random.nextInt(3)));
                sprite.setPosition(((j + i) * 15) - (MAP_WIDTH / 2), (j - i) * 8 - 8);
                sprites[i][j] = sprite;
            }
        
        towers = new ArrayList<>();
        
        for (int i = 0; i < mapSize; i++)
            for (int j = 0; j < mapSize; j++) {
                if ((i + j) % 99 == 4) {
                    Sprite sprite = new Sprite(towerAtlas.findRegion("tower"));
                    sprite.setPosition(((j + i) * 15) - (MAP_WIDTH / 2), (j - i) * 8 - 8);
                    towers.add(sprite);
                }
            }
        
    }
    
    @Override
    public void render () {
//        System.out.println("cam psition: " + camera.position.x + " : " + camera.position.y + "\nsize(wh): " +
//        screenWidth + " : " + screenHeight);
////        System.out.println(Gdx.input.getX() + " : " + Gdx.input.getY());
//        System.out.println("cursor relative pos(xy): " + (Gdx.input.getX() - screenWidth / 2) + " : " +
//        (screenHeight / 2 - Gdx.input.getY()));
//        System.out.println(getPositionI((MAP_WIDTH * (Gdx.input.getX() - screenWidth / 2) / (2 * screenWidth) +
//        camera.position.x), (MAP_HEIGHT * (screenHeight / 2 - Gdx.input.getY()) / (2 * screenHeight) + camera
//        .position.y)) + " : " + getPositionJ((MAP_WIDTH * (Gdx.input.getX() - screenWidth / 2) / (2 * screenWidth)
//        + camera.position.x), (MAP_HEIGHT * (screenHeight / 2 - Gdx.input.getY()) / (2 * screenHeight) + camera
//        .position.y)));
//        System.out.println(getPositionI((Gdx.input.getX() - screenWidth / 2) + camera.position.x, (screenHeight / 2
//        - Gdx.input.getY()) + camera.position.y) + " : " + getPositionJ((Gdx.input.getX() - camera.viewportWidth /
//        2) + camera.position.x, (screenHeight / 2 - Gdx.input.getY()) + camera.position.y));
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
        }
//        System.out.println(hoverTime);
//        System.out.println(currentTileI + " : " + currentTileJ);
        handleInput();
        Gdx.gl.glClearColor(0.5f, 0.8f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (int i = 0; i < mapSize; i++) {
            for (int j = mapSize - 1; j >= 0; j--) {
//				int numberX;
//				int numberY;
//				numberX = -47 * (j % 2 == 0 ? 0 : 1);
//				numberY = 25 * j;
                sprites[i][j].draw(spriteBatch);
//				spriteBatch.draw(sprites[i][j]);
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
        for (int i = 0; i < mapSize; i++)
            for (int j = 0; j < mapSize; j++) ;
//				sprites[i][j]();
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
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && camera.position.y - getCursorY() < MAP_HEIGHT / 4 - windowWithTopRightCornerCloseButton.getHeight()) {
            float mouseX = -Gdx.input.getDeltaX() * camera.zoom * horizontalCameraOnScreenRatio;
            float mouseY = Gdx.input.getDeltaY() * camera.zoom * verticalCameraOnScreenRatio;
            camera.translate(mouseX * cameraSpeed, mouseY * cameraSpeed);
            fitMap();
        }
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled (float x, float y) {
                float zoomFactor = y * ZOOM_SPEED;
                camera.zoom += zoomFactor;
                if (camera.zoom < minZoom) camera.zoom = minZoom;
                if (camera.zoom > maxZoom) camera.zoom = maxZoom;
                fitMap();
                return true;
            }
            
            @Override
            public boolean keyUp (int keycode) {
                return super.keyUp(keycode);
            }
        });
    }
    
    public float getCursorX () {
        return MAP_WIDTH * camera.zoom * (Gdx.input.getX() - screenWidth / 2) / (2 * screenWidth) + camera.position.x;
    }
    
    public float getCursorY () {
        return MAP_HEIGHT * camera.zoom * (screenHeight / 2 - Gdx.input.getY()) / (2 * screenHeight) + camera.position.y;
    }
//	SpriteBatch batch;
//	Texture img;
//
//	@Override
//	public void create () {
//		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
//	}
//
//	@Override
//	public void render () {
//		ScreenUtils.clear(1, 0, 0, 1);
//		batch.begin();
//		batch.draw(img, 100, 0);
//		batch.end();
//	}
//
//	@Override
//	public void dispose () {
//		batch.dispose();
//		img.dispose();
//	}
}
