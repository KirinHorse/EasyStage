package com.ayocrazy.easystage;

import com.ayocrazy.easystage.handler.StageHandler;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by ayo on 2017/1/10.
 */

public class Easy {
    public static final Stage newStage(Class<? extends Stage> clazz, Object... args) {
        if (EasyConfig.disable || Gdx.app.getType() != Application.ApplicationType.Desktop) {
            Class argTypes[] = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            try {
                return clazz.getConstructor(argTypes).newInstance(args);
            } catch (Exception e) {
                throw new GdxRuntimeException(e);
            }
        }
        StageHandler handler = new StageHandler();
        return handler.newStage(clazz, args);
    }
}
