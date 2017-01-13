package com.ayocrazy.easystage.bean;

import com.ayocrazy.easystage.uimeta.MetaSelectBox;
import com.ayocrazy.easystage.uimeta.MetaText;
import com.ayocrazy.easystage.uimeta.MetaVector;

import java.io.Serializable;

/**
 * Created by ayo on 2017/1/11.
 */

public class ViewportBean implements Serializable {
    @MetaSelectBox(items = {"ScreenViewport", "ScalingViewport"})
    private String type;
    @MetaSelectBox(items = {"fit", "fill", "fillX", "fillY", "stretch", "stretchX", "stretchY", "none"})
    private String scalling;
    @MetaVector(editable = true, filter = MetaText.Filter.FLOAT, prefix = {'w', 'h'})
    private float[] worldSize;
    @MetaVector(editable = true, filter = MetaText.Filter.INT)
    private int[] screenPos;
    @MetaVector(prefix = {'w', 'h'}, filter = MetaText.Filter.INT)
    private int[] screenSize;

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
