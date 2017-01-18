package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.EasyConfig;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 * Created by ayo on 2017/1/10.
 */

public class WindowLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        try {
            EasyConfig.port = Integer.parseInt(args[0]);
        } catch (Exception e) {
        }
        config.initialBackgroundColor = Color.CLEAR;
        config.width = 1280;
        config.height = 720;
        config.title = "EasyStage v" + System.getProperty("Manifest-Version");
        new LwjglApplication(new EasyGame(), config);
    }
}
