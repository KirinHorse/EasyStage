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
    @MetaText(editable = true, filter = MetaText.Filter.FLOAT, arraySize = 2)
    private float[] worldSize;
    @MetaText(editable = true, filter = MetaText.Filter.INT, arraySize = 2)
    private int[] screenPos, screenSize;

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

    public float[] getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(float[] worldSize) {
        this.worldSize = worldSize;
    }

    public int[] getScreenPos() {
        return screenPos;
    }

    public void setScreenPos(int[] screenPos) {
        this.screenPos = screenPos;
    }

    public int[] getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(int[] screenSize) {
        this.screenSize = screenSize;
    }
}
