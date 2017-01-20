package com.ayocrazy.easystage;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by ayo on 2017/1/10.
 */

public class TestStage extends Stage {
    private TestImage img;
    private ClickListener listener;

    public TestStage() {
        initListener();
        img = new TestImage(new Texture("game/badlogic.jpg"));
        img.setPosition(getWidth() * 0.5f, getHeight() * 0.5f, Align.center);
        img.setOrigin(Align.center);
        img.addListener(listener);
        addActor(img);
    }

    private void initListener() {
        listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int key = event.getKeyCode();
                int button = event.getButton();
                Actor actor = event.getListenerActor();
                switch (key) {
                    case Input.Keys.CONTROL_LEFT:
                        add(actor, button);
                        break;
                    case Input.Keys.ALT_LEFT:
                        actor.remove();
                        break;
                }
            }
        };
    }

    private void add(Actor actor, int button) {
        Group parent = actor instanceof Group ? (Group) actor : actor.getParent();
        if (button == Input.Buttons.LEFT) {
            Image img = new TestImage(new Texture("game/badlogic.jpg"));
            img.addListener(listener);
            parent.addActor(img);
        } else if (button == Input.Buttons.RIGHT) {
            Group group = new Group();
            group.setSize(256, 256);
            group.addListener(listener);
            parent.addActor(group);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
