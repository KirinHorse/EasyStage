package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.bean.BeanCreator;
import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.rmi.Client;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeFontPaint;

/**
 * Created by ayo on 2017/1/10.
 */

public class MainStage extends Stage {
    private static MainStage instance;
    private Skin skin;
    NativeFont font;
    ActorTree actorTree;
    EasyLog log;
    StageWindow stageWindow;
    Client client;

    public static MainStage get() {
        return instance;
    }

    public MainStage() {
        super(new ScreenViewport());
        instance = this;
//        setDebugAll(true);
        initSkin();

        final TextButton stop = new TextButton("stop", skin);
        stop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (stop.isChecked()) {
                    client.stop();
                } else {
                    client.start(0);
                }
            }
        });
        stop.setPosition(5, getHeight(), Align.topLeft);
        addActor(stop);

        stageWindow = new StageWindow("", skin);
        actorTree = new ActorTree(skin);
        log = new EasyLog("log", skin);
        stageWindow.setSize(getWidth() * 0.4f, getHeight() * 0.6f);
        log.setSize(getWidth(), getHeight() * 0.2f);
        stageWindow.setPosition(getWidth(), getHeight(), Align.topRight);
        addActor(stageWindow);
        addActor(actorTree);
        addActor(log);
        client = new Client(stageWindow);
//        StageBean bean = BeanCreator.refreshStage(this, null);
//        stageWindow.setStageBean(bean);
    }

    private void initSkin() {
        skin = new Skin(Gdx.files.internal("skin/skin.json"));
        font = new NativeFont(new NativeFontPaint(15));
//        font.setSize(15);
        skin.add("default", font, BitmapFont.class);
        TextField.TextFieldStyle style = skin.get(TextField.TextFieldStyle.class);
        style.font = font;
        TextButton.TextButtonStyle tbStyle = skin.get(TextButton.TextButtonStyle.class);
        tbStyle.font = font;
        Label.LabelStyle labStyle = skin.get(Label.LabelStyle.class);
        labStyle.font = font;
        SelectBox.SelectBoxStyle sbStyle = skin.get(SelectBox.SelectBoxStyle.class);
        sbStyle.font = font;
        CheckBox.CheckBoxStyle cbStyle = skin.get(CheckBox.CheckBoxStyle.class);
        cbStyle.font = font;
    }

    public void resize(int width, int height) {
        getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
    }
}
