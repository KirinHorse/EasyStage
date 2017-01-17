package test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;

/**
 * Created by ayo on 2017/1/10.
 */

public class TestStage extends Stage {
    private TestImage img;

    public TestStage() {
        img = new TestImage(new Texture("game/badlogic.jpg"));
        img.setPosition(getWidth() * 0.5f, getHeight() * 0.5f, Align.center);
        img.setOrigin(Align.center);
        addActor(img);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
