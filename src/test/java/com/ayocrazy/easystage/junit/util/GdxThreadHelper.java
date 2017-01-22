package com.ayocrazy.easystage.junit.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jglfw.JglfwApplication;

public class GdxThreadHelper {
    public static void waitRunnableFinish() {
        final JglfwApplication app = (JglfwApplication) Gdx.app;
        ConditionWaiter.wait(new Condition() {
            @Override
            public Boolean check() {
                return app.executeRunnables();
            }
        }, "libGdx still running in background", 10);
    }
}
