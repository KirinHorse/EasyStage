package com.ayocrazy.easystage.view;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;


/**
 * Created by ayo on 2017/1/10.
 */

public class WindowLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setInitialBackgroundColor(Color.CLEAR);
        config.setWindowSizeLimits(640, 360, -1, -1);
        config.setMaximized(true);
        config.setTitle("EasyStage v" + System.getProperty("Manifest-Version"));
        new Lwjgl3Application(new EasyGame(), config);
    }
}
