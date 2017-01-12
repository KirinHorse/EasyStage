package test;

import com.ayocrazy.easystage.uimeta.MetaText;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

/**
 * Created by ayo on 2017/1/10.
 */

public class TestStage extends Stage {
    @MetaText(editable = true, filter = MetaText.Filter.FLOAT)
    private float speed = 100f;
    private Image img;
    @MetaText
    private int dirction = 1;

    public TestStage() {
        img = new Image(new Texture("game/badlogic.jpg"));
        img.setPosition(getWidth() * 0.5f, getHeight() * 0.5f, Align.center);
        addActor(img);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update(delta);
    }

    private void update(float delta) {
        img.moveBy(speed * dirction * delta, 0);
        if (img.getX() < 0) dirction = 1;
        else if (img.getX(Align.right) > getWidth()) dirction = -1;
    }
}
