package com.ayocrazy.easystage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

/**
 * Created by ayo on 2017/1/10.
 */

public class TestStage extends Stage {
    private TestImage img;
    private Group group1, group2;

    public TestStage() {
        img = new TestImage(new Texture("game/badlogic.jpg"));
        img.setPosition(getWidth() * 0.5f, getHeight() * 0.5f, Align.center);
        img.setOrigin(Align.center);
        addActor(img);
        group1 = new Group();
        group2 = new Group();
        group2.setPosition(getWidth(), 0);
        group1.addActor(new Image(new Texture("game/badlogic.jpg")));
        group2.addActor(new Image(new Texture("game/badlogic.jpg")));
        group1.addActor(group2);
        addActor(group1);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
