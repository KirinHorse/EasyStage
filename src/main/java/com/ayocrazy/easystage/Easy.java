package com.ayocrazy.easystage;

import com.ayocrazy.easystage.handler.StageHandler;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by ayo on 2017/1/10.
 */

public class Easy {
    public static final Stage newStage(Class<? extends Stage> clazz, Object... args) {
        StageHandler handler = new StageHandler();
        return handler.newStage(clazz, args);
    }
}
