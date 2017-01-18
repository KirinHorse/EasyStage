package com.ayocrazy.easystage.cover;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Queue;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;

/**
 * Created by ayo on 2017/1/18.
 */

public class Info extends Group {
    private Image bg;
    private NativeLabel lab;
    private Queue<Runnable> queue = new Queue<>();

    Info(TextureAtlas atlas, NativeFont font) {
        NinePatchDrawable drawable = new NinePatchDrawable(
                new NinePatch(atlas.findRegion("textfield"), 3, 3, 3, 3));
        bg = new Image(drawable);
        bg.setColor(1, 1, 1, 0.7f);
        lab = new NativeLabel("", font);
        lab.setColor(Color.LIGHT_GRAY);
        setTouchable(Touchable.disabled);
        addActor(bg);
        addActor(lab);
    }

    void show(String text, Stage stage) {
        Runnable run = showRun(text, stage);
        if (queue.size == 0) {
            queue.addLast(run);
            Gdx.app.postRunnable(run);
        } else {
            queue.addLast(showRun(text, stage));
        }

    }

    private Runnable showRun(final String text, final Stage stage) {
        return new Runnable() {
            @Override
            public void run() {
                stage.addActor(Info.this);
                toFront();
                setSize(stage.getWidth(), 35);
                bg.setSize(getWidth(), getHeight());
                setPosition(0, stage.getHeight());
                lab.setText(text);
                lab.setSize(lab.getPrefWidth(), lab.getPrefHeight());
                lab.setPosition(getWidth() * 0.5f, getHeight() * 0.5f, Align.center);
                addAction(Actions.sequence(
                        Actions.moveToAligned(0, stage.getHeight(), Align.topLeft, 0.5f, Interpolation.circleOut),
                        Actions.delay(2f),
                        Actions.moveTo(0, stage.getHeight(), 0.5f, Interpolation.circleIn), Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                queue.removeFirst();
                                if (queue.size > 0) Gdx.app.postRunnable(queue.first());
                                else remove();
                            }
                        })));
            }
        };
    }

}
