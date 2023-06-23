package view.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("joon");
            }
        });
        getTitleTable().add(closeButton).size(30, 30).padRight(450).padTop(50);

        setClip(false);
        setTransform(true);
    }
}