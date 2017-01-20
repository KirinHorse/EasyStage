package com.ayocrazy.easystage.cover;

import com.ayocrazy.easystage.rmi.Server;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeFontPaint;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Created by ayo on 2017/1/18.
 */

public class CoverStage extends Stage {
    private static Server server;
    private Stage gameStage;
    private Skin skin;
    private Message message;
    private StageInfo stageInfo;
    private CursorInfo cursorInfo;
    private TimeManager timeManager;
    private boolean paused;

    public CoverStage(Stage gameStage) {
        super(new ScreenViewport());
        this.gameStage = gameStage;
        skin = new Skin(Gdx.files.internal("skin/skin.json"));
        NativeFont font = new NativeFont(new NativeFontPaint(15));
        skin.add("default", font, BitmapFont.class);
        skin.get(Label.LabelStyle.class).font = font;
        skin.get(TextField.TextFieldStyle.class).font = font;
        skin.get(CheckBox.CheckBoxStyle.class).font = font;
        skin.get(SelectBox.SelectBoxStyle.class).font = font;

        timeManager = new TimeManager();
        stageInfo = new StageInfo(skin, timeManager, gameStage.getBatch());
        message = new Message(skin);
        cursorInfo = new CursorInfo(skin, gameStage);
        showMessage("Press F6 to start EasyStage tool");
    }

    public void startServer() {
        if (server == null) {
            showMessage("Server is starting");
            server = new Server(gameStage, this);
        } else {
            showMessage("Server already started, press F5 to reopen");
            server.setStage(gameStage);
        }
    }

    @Override
    public void act(float delta) {
        checkInput();
        checkSizeChanged();
        super.act(delta);
    }

    public void invokeAct(MethodProxy proxy, Object obj, Object[] args) throws Throwable {
        timeManager.actStart();
        if (!paused) {
            proxy.invokeSuper(obj, args);
        }
        timeManager.actEnd();
        act((float) args[0]);
    }

    public void invokeDraw(MethodProxy proxy, Object obj, Object[] args) throws Throwable {
        timeManager.drawStart();
        proxy.invokeSuper(obj, args);
        timeManager.drawEnd();
        draw();
    }

    private void checkInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {
            startServer();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            if (server != null) server.reopen();
            else showMessage("Server has not bean started, press F6 to start server");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            paused = !paused;
            stageInfo.getTitleLabel().setText(paused ? "status(paused)" : "status");
        } else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                if (stageInfo.getParent() != null) stageInfo.remove();
                else addActor(stageInfo);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                gameStage.setDebugAll(!gameStage.getRoot().getDebug());
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
            if (cursorInfo.getStage() == null) {
                addActor(cursorInfo);
                cursorInfo.setType(CursorInfo.Type.pos);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            if (cursorInfo.getStage() == null) {
                addActor(cursorInfo);
                cursorInfo.setType(CursorInfo.Type.color);
            }
        } else if (cursorInfo.getStage() != null) cursorInfo.remove();
    }

    private void checkSizeChanged() {
        if (Gdx.graphics.getWidth() != getWidth() || Gdx.graphics.getHeight() != getHeight()) {
            getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        }
    }

    public void showMessage(String text) {
        message.show(text, this);
    }
}
