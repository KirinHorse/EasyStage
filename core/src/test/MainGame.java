package test;

import com.ayocrazy.easystage.Easy;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MainGame extends ApplicationAdapter {
    TestStage stage;

    @Override
    public void create() {
        stage = (TestStage) Easy.newStage(TestStage.class);
        Image img = new Image(new Texture("badlogic.jpg"));
        stage.addActor(img);
        stage.setName("AyoCrazy");
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
    }
}
