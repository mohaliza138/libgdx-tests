package view.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.awt.*;

/**
 * Window which features the close button in top right corner (button moved outside of the window bounds).
 *
 * @author serhiy
 */
public class WindowWithTopRightCornerCloseButton extends Window {
    
    private static final WindowStyle windowStyle;
    private static final ImageButtonStyle closeButtonStyle;
    
    static {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("windows.pack"));
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("click.pack"));
        windowStyle = new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(textureAtlas.findRegion("window")));
        closeButtonStyle = new ImageButtonStyle();
        closeButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas1.findRegion("ST80_Siege_Tent.tgx"));
    }
    
    /**
     * Default constructor.
     */
    public WindowWithTopRightCornerCloseButton() {
        super("", windowStyle);
        final Button closeButton = new ImageButton(closeButtonStyle);
        getTitleTable().add(closeButton).size(30, 30).padRight(200).padTop(50);
        //   closeButton.addListener(new )
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                float screenWidth = gd.getDisplayMode().getWidth();
                float screenHeight = gd.getDisplayMode().getHeight();
                if (Gdx.input.isKeyPressed(Input.Keys.B)) {
                    System.out.println((closeButton.getX() - screenWidth / 2) * GameMenuGDX.camera.zoom + " : " + GameMenuGDX.camera.zoom * ((closeButton.getY() - ((screenHeight - 150) / 2) + 300)));
                    System.out.println("camera position: " + GameMenuGDX.camera.position.x + " : " + GameMenuGDX.camera.position.y);
                    System.out.println((closeButton.getX() - screenWidth / 2) * GameMenuGDX.camera.zoom + GameMenuGDX.camera.position.x + " : " + (GameMenuGDX.camera.zoom * ((closeButton.getY() - ((screenHeight - 150) / 2) + 300)) + GameMenuGDX.camera.position.y));
                    int j = GameMenuGDX.getPositionJ((closeButton.getX() - screenWidth / 2) * GameMenuGDX.camera.zoom + GameMenuGDX.camera.position.x , (GameMenuGDX.camera.zoom * ((closeButton.getY() - ((screenHeight - 150) / 2) + 300)) + GameMenuGDX.camera.position.y));
                    int i = GameMenuGDX.getPositionI((closeButton.getX() - screenWidth / 2) * GameMenuGDX.camera.zoom + GameMenuGDX.camera.position.x , (GameMenuGDX.camera.zoom * ((closeButton.getY() - ((screenHeight - 150) / 2) + 300)) + GameMenuGDX.camera.position.y));
                    System.out.println(i + " : " + j);
                    AssetManager textureAssets;
                    textureAssets = new AssetManager();
                    textureAssets.load("Tiles.atlas", TextureAtlas.class);
                    textureAssets.finishLoading();
                    TextureAtlas textureAtlas;
                    textureAtlas = textureAssets.get("Tiles.atlas");
                    Sprite sprite = new Sprite(textureAtlas.findRegion("river0"));
                    sprite.setPosition(((j + i) * 15) - (GameMenuGDX.MAP_WIDTH / 2), (j - i) * 8 - 8);
                    GameMenuGDX.sprites[i][j] = sprite;
                    closeButton.setX(500);
                    closeButton.setY(10);
                }
            }
        });
        closeButton.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                closeButton.moveBy(x - closeButton.getWidth() / 2, y - closeButton.getHeight() / 2);
            }
        });
        setClip(false);
        setTransform(true);
    }
}