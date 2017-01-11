package com.ayocrazy.easystage.bean;

import com.ayocrazy.easystage.uimeta.MetaSelectBox;
import com.ayocrazy.easystage.uimeta.MetaText;

import java.io.Serializable;

/**
 * Created by ayo on 2017/1/11.
 */

public class ViewportBean implements Serializable {
    @MetaSelectBox(items = {"ScreenViewport", "ScalingViewport", "StretchViewport", "FitViewport", "ExtendViewport", "FillViewport"})
    private String type;
    @MetaSelectBox(items = {"fit", "fill", "fillX", "fillY", "stretch", "stretchX", "stretchY", "none"})
    private String scalling;
    @MetaText(editable = true, filter = MetaText.Filter.INT)
    private float worldWidth, worldHeight;
    @MetaText(editable = true, filter = MetaText.Filter.INT)
    private int screenX, screenY, screenWidth, screenHeight;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScalling() {
        return scalling;
    }

    public void setScalling(String scalling) {
        this.scalling = scalling;
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public void setWorldWidth(float worldWidth) {
        this.worldWidth = worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }

    public void setWorldHeight(float worldHeight) {
        this.worldHeight = worldHeight;
    }

    public int getScreenX() {
        return screenX;
    }

    public void setScreenX(int screenX) {
        this.screenX = screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public void setScreenY(int screenY) {
        this.screenY = screenY;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}
