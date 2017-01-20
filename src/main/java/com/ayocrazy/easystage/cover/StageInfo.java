package com.ayocrazy.easystage.cover;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import net.mwplay.nativefont.NativeLabel;

/**
 * Created by ayo on 2017/1/19.
 */

public class StageInfo extends Window {
    private static String MB = " mb";
    private static String MS = " ms";
    private TimeManager timeManager;
    private Batch batch;
    private NativeLabel labFps, labDrawCall, labRendererTime, labActTime, labDrawTime, labJheap;

    public StageInfo(Skin skin, TimeManager timeManager, Batch batch) {
        super("Status", skin);
        this.timeManager = timeManager;
        this.batch = batch;
        labFps = createLab("FPS:");
        if (batch instanceof SpriteBatch || batch instanceof PolygonSpriteBatch) {
            labDrawCall = createLab("Draw Call:");
        }
        labRendererTime = createLab("Render Time:");
        labActTime = createLab("Act Time:");
        labDrawTime = createLab("Draw Time:");
        labJheap = createLab("Heap:");
        NativeLabel labGL = new NativeLabel(Gdx.graphics.getGLVersion().getDebugVersionString(), skin.get(Label.LabelStyle.class));
        add(labGL).padTop(8).colspan(2);
        setSize(getPrefWidth(), getPrefHeight());
        setColor(1, 1, 1, 0.7f);
    }

    private NativeLabel createLab(String text) {
        NativeLabel labelKey = new NativeLabel(text, getSkin().get(Label.LabelStyle.class));
        NativeLabel labelValue = new NativeLabel("", getSkin().get(Label.LabelStyle.class));
        add(labelKey).padRight(10).right();
        add(labelValue).expandX().fillX().row();
        return labelValue;
    }

    @Override
    public void act(float delta) {
        update();
        super.act(delta);
    }

    private void update() {
        labFps.setText(Integer.toString(Gdx.graphics.getFramesPerSecond()));
        updateRenderCalls(batch);
        labRendererTime.setText(Float.toString((int) (Gdx.graphics.getDeltaTime() * 100000) / 100f) + MS);
        labJheap.setText(Float.toString(Gdx.app.getJavaHeap() / 1024 / 1000f) + MB);
        labActTime.setText(Float.toString(timeManager.actTime) + MS);
        labDrawTime.setText(Float.toString(timeManager.drawTime) + MS);
    }

    private void updateRenderCalls(Batch batch) {
        int result;
        if (batch instanceof SpriteBatch) {
            result = ((SpriteBatch) batch).totalRenderCalls;
            ((SpriteBatch) batch).totalRenderCalls = 0;
        } else if (batch instanceof PolygonSpriteBatch) {
            result = ((PolygonSpriteBatch) batch).totalRenderCalls;
            ((PolygonSpriteBatch) batch).totalRenderCalls = 0;
        } else {
            return;
        }
        labDrawCall.setText(Integer.toString(result));
    }

}
