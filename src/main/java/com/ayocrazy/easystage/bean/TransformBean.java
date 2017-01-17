package com.ayocrazy.easystage.bean;

import com.ayocrazy.easystage.uimeta.MetaMethod;
import com.ayocrazy.easystage.uimeta.MetaText;
import com.ayocrazy.easystage.uimeta.MetaVector;

import java.io.Serializable;

/**
 * Created by ayo on 2017/1/11.
 */

public class TransformBean extends BaseBean {
    @MetaVector(editable = true)
    private float[] position, size, origion, scale;
    @MetaText(editable = true, filter = MetaText.Filter.FLOAT, maxLength = 10)
    private float rotation;

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getSize() {
        return size;
    }

    public void setSize(float[] size) {
        this.size = size;
    }

    public float[] getOrigion() {
        return origion;
    }

    public void setOrigion(float[] origion) {
        this.origion = origion;
    }

    public float[] getScale() {
        return scale;
    }

    public void setScale(float[] scale) {
        this.scale = scale;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
