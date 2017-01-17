package com.ayocrazy.easystage.view;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;


/**
 * Created by ayo on 2017/1/10.
 */

public class WindowLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.initialBackgroundColor = Color.CLEAR;
        config.title = "EasyStage v" + System.getProperty("Manifest-Version");
        LwjglApplication app = new LwjglApplication(new EasyGame(), config);
    }
}
