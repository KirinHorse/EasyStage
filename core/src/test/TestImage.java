package test;

import com.ayocrazy.easystage.uimeta.MetaText;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

/**
 * Created by ayo on 2017/1/17.
 */

public class TestImage extends Image {
    @MetaText(editable = true, filter = MetaText.Filter.FLOAT)
    private float speed = 100f, rotateSpeed = 0f;
    @MetaText
    private int direction = 1;

    public TestImage(Texture texture) {
        super(texture);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update(delta);
    }

    private void update(float delta) {
        moveBy(speed * direction * delta, 0);
        if (getX(Align.center) < 0) direction = speed > 0 ? 1 : -1;
        else if (getX(Align.center) > getStage().getWidth()) direction = speed > 0 ? -1 : 1;
        rotateBy(rotateSpeed * delta);
    }
}
