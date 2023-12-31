package com.view.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import view.game.GameMenuGDX;

import java.awt.*;

public class DesktopLauncher {
    public static void main(String[] arg) throws Exception {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("game-gdx");
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        float screenWidth = gd.getDisplayMode().getWidth();
        float screenHeight = gd.getDisplayMode().getHeight();
        config.setWindowedMode((int) screenWidth, (int) (screenHeight - 150));
        new Lwjgl3Application(new GameMenuGDX(100), config);
    }
}