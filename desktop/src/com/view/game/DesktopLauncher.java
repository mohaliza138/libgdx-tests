package com.view.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import model.Block;
import view.game.GameMenuGDX;

import java.awt.*;
import java.util.Random;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("game-gdx");
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        float screenWidth = gd.getDisplayMode().getWidth();
        float screenHeight = gd.getDisplayMode().getHeight();
        config.setWindowedMode((int) screenWidth, (int) (screenHeight - 150));
        Block[][] blocks = new Block[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                blocks[i][j] = new Block(i, j, (i > 20 && i < 26) ? "river" : (j > 81 && j < 89)
                        ? "burnt" : "ground");
            }
        }
        new Lwjgl3Application(new GameMenuGDX(blocks), config);
    }
}