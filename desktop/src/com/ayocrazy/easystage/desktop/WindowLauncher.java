package com.ayocrazy.easystage.desktop;

import com.ayocrazy.easystage.view.EasyGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


/**
 * Created by ayo on 2017/1/10.
 */

public class WindowLauncher {
    public static void main(String... args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 800;
        config.title = "EasyStage v" + System.getProperty("Manifest-Version");
        new LwjglApplication(new EasyGame(), config);
    }
}
