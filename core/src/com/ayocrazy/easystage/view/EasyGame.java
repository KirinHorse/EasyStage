package com.ayocrazy.easystage.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by ayo on 2017/1/10.
 */

public class EasyGame extends ApplicationAdapter {
    private MainStage mainStage;

    @Override
    public void create() {
        mainStage = new MainStage();
        Gdx.input.setInputProcessor(mainStage);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.act();
        mainStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainStage.resize(width, height);
    }

    @Override
    public void dispose() {
        mainStage.dispose();
    }
}
