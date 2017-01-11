package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.bean.BeanGenerator;
import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.server.Server;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;

import java.rmi.Naming;

/**
 * Created by ayo on 2017/1/10.
 */

public class MainStage extends Stage {
    private static MainStage instance;
    private Skin skin;
    NativeFont font;
    ActorTree actorTree;
    LogWindow log;
    StageWindow stageWindow;

    public static MainStage get() {
        return instance;
    }

    public MainStage() {
        super(new ScreenViewport());
        instance = this;
//        setDebugAll(true);

        skin = new Skin(Gdx.files.internal("skin/skin.json"));
        font = new NativeFont();
        font.setSize(15);
        TextField.TextFieldStyle style = skin.get(TextField.TextFieldStyle.class);
        style.font = font;
        Label.LabelStyle labStyle = skin.get(Label.LabelStyle.class);
        labStyle.font = font;
        SelectBox.SelectBoxStyle sbStyle = skin.get(SelectBox.SelectBoxStyle.class);
        sbStyle.font = font;
        CheckBox.CheckBoxStyle cbStyle = skin.get(CheckBox.CheckBoxStyle.class);
        cbStyle.font = font;

        stageWindow = new StageWindow("", skin);
        actorTree = new ActorTree(skin);
        log = new LogWindow("log", skin);
        stageWindow.setSize(getWidth() * 0.3f, getHeight() * 0.6f);
        log.setSize(getWidth(), getHeight() * 0.2f);
        stageWindow.setPosition(getWidth(), getHeight(), Align.topRight);
        addActor(stageWindow);
        addActor(actorTree);
        addActor(log);
//        try {
//            Server server = (Server) Naming.lookup("rmi://localhost:9126/Server");
//            stageWindow.setStageBean(server.getStage());
//        } catch (Exception e) {
//            e.printStackTrace();
        stageWindow.setStageBean(BeanGenerator.genStage(this));
//        }
    }

    public void resize(int width, int height) {
        getViewport().update(width, height);
    }
}
