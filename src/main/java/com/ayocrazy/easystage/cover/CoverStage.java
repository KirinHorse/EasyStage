package com.ayocrazy.easystage.cover;

import com.ayocrazy.easystage.rmi.Server;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeFontPaint;

/**
 * Created by ayo on 2017/1/18.
 */

public class CoverStage extends Stage {
    private Stage gameStage;
    private TextureAtlas atlas;
    private Info info;
    private NativeFont font;
    private static Server server;

    public CoverStage(Stage gameStage) {
        super(new ScreenViewport());
        this.gameStage = gameStage;
        atlas = new TextureAtlas("skin/skin.atlas");
        font = new NativeFont(new NativeFontPaint(22));
        info = new Info(atlas, font);
    }

    public void startServer() {
        if (server == null) {
            showInfo("Server is starting");
            server = new Server(gameStage, this);
        } else {
            showInfo("Server is already started, press F5 to reopen the window of EasyStage");
            server.setStage(gameStage);
        }
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {
            startServer();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            server.reopen();
        }
        super.act(delta);
    }

    public void showInfo(String text) {
        info.show(text, this);
    }
}
