package com.ayocrazy.easystage.cover;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.mwplay.nativefont.NativeLabel;

/**
 * Created by ayo on 2017/1/19.
 */

public class CursorPos extends Group {
    private Image bg;
    private Label lab;
    private Stage gameStage;
    private Vector2 gamePos = new Vector2();
    private Vector2 tempPos = new Vector2();

    public CursorPos(Skin skin, Stage gameStage) {
        this.gameStage = gameStage;
        bg = new Image(skin.getDrawable("textfield"));
        bg.setColor(1, 1, 1, 0.6f);
        lab = new NativeLabel("", skin.get(Label.LabelStyle.class));
        lab.setColor(0.9f, 0.9f, 0.9f, 0.8f);
        addActor(bg);
        addActor(lab);
    }

    @Override
    public void act(float delta) {
        update();
        super.act(delta);
    }

    private void update() {
        toFront();
        tempPos.set(Gdx.input.getX(), Gdx.input.getY());
        gameStage.screenToStageCoordinates(gamePos.set(tempPos));
        lab.setText("X:" + Math.round(gamePos.x * 10) / 10f + " Y:" + Math.round(gamePos.y * 10) / 10f);
        lab.setSize(lab.getPrefWidth(), lab.getPrefHeight());
        setSize(lab.getWidth(), lab.getHeight());
        bg.setSize(getWidth(), getHeight());
        getStage().screenToStageCoordinates(tempPos);
        tempPos.add(-getWidth() * 0.5f, 5);
        if (tempPos.x < 0) tempPos.x = 0;
        else if (tempPos.x > getStage().getWidth() - getWidth()) {
            tempPos.x = getStage().getWidth() - getWidth();
        }
        if (tempPos.y < 0) tempPos.y = 0;
        else if (tempPos.y > getStage().getHeight() - getHeight()) {
            tempPos.y = getStage().getHeight() - getHeight();
        }
        setPosition(tempPos.x, tempPos.y);
    }
}
