package test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by ayo on 2017/1/10.
 */

public class TestStage extends Stage {
    public TestStage() {
    }

    public void setName(String name) {
        System.out.println("set stage name:" + name);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            System.out.println("F5 just pressed");
        }
    }
}
