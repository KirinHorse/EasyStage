package com.ayocrazy.easystage.cover;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.BufferUtils;

import net.mwplay.nativefont.NativeLabel;

import java.nio.ByteBuffer;

/**
 * Created by ayo on 2017/1/19.
 */

public class CursorInfo extends Group {
    private Image bg, imgColor;
    private Label lab;
    private Stage gameStage;
    private Vector2 gamePos = new Vector2();
    private Vector2 tempPos = new Vector2();
    private Color color = new Color();
    private Type type = Type.pos;

    enum Type {
        color, pos
    }

    public CursorInfo(Skin skin, Stage gameStage) {
        this.gameStage = gameStage;
        bg = new Image(skin.getDrawable("textfield"));
        bg.setColor(1, 1, 1, 0.65f);
        imgColor = getImage(15, 10, Color.WHITE);
        lab = new NativeLabel("", skin.get(Label.LabelStyle.class));
        lab.setColor(1, 1, 1, 0.85f);
        addActor(bg);
        addActor(lab);
    }

    private Image getImage(float w, float h, Color color) {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(Color.WHITE);
        p.fill();
        Image img = new Image(new Texture(p));
        img.setSize(w, h);
        p.dispose();
        return img;
    }

    @Override
    public void act(float delta) {
        toFront();
        if (type == Type.pos) updatePos();
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (type == Type.color) updateColor();
        super.draw(batch, parentAlpha);
    }

    private static final String X = "X:", Y = " Y:", HASH = "#";

    private void updatePos() {
        tempPos.set(Gdx.input.getX(), Gdx.input.getY());
        gameStage.screenToStageCoordinates(gamePos.set(tempPos));
        gamePos.set(Math.round(gamePos.x * 10) / 10f, Math.round(gamePos.y * 10) / 10f);
        lab.setText(X + gamePos.x + Y + gamePos.y);
        lab.setSize(lab.getPrefWidth(), lab.getPrefHeight());
        setSize(lab.getWidth(), lab.getHeight());
        bg.setSize(getWidth(), getHeight());
        setPos();
    }

    private ByteBuffer buffer = BufferUtils.newByteBuffer(4);

    private void updateColor() {
        tempPos.set(Gdx.input.getX(), Gdx.input.getY());
        Gdx.gl.glReadPixels(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 1, 1, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, buffer);
        color.set(Byte.toUnsignedInt(buffer.get(0)) / 255f, Byte.toUnsignedInt(buffer.get(1)) / 255f,
                Byte.toUnsignedInt(buffer.get(2)) / 255f, Byte.toUnsignedInt(buffer.get(3)) / 255f);
        imgColor.setColor(color);
        lab.setText(HASH + Integer.toHexString(color.toIntBits()).toUpperCase());
        setPos();
    }

    private void setPos() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            copy();
        }
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

    public void setType(Type type) {
        if (this.type == type) return;
        this.type = type;
        if (type == Type.color) {
            addActor(imgColor);
            lab.setText("#00000000");
            lab.setSize(lab.getPrefWidth(), lab.getPrefHeight());
            imgColor.setWidth(lab.getWidth() - 10);
            imgColor.setPosition(lab.getX(Align.center), lab.getTop() + 5, Align.bottom);
            setSize(lab.getWidth(), lab.getHeight() + 10 + imgColor.getHeight());
            bg.setSize(getWidth(), getHeight());
        } else if (type == Type.pos) {
            imgColor.remove();
        }
    }

    private void copy() {
        if (type == Type.color) {
            Gdx.app.getClipboard().setContents(Integer.toHexString(color.toIntBits()));
        } else if (type == Type.pos) {
            Gdx.app.getClipboard().setContents(gamePos.x + "," + gamePos.y);
        }
    }
}
