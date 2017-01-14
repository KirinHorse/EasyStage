package com.ayocrazy.easystage.bean;

import com.ayocrazy.easystage.uimeta.MetaCheckBox;
import com.ayocrazy.easystage.uimeta.MetaTable;
import com.ayocrazy.easystage.uimeta.MetaText;
import com.ayocrazy.easystage.uimeta.MetaVector;

import java.io.Serializable;

/**
 * Created by ayo on 2017/1/11.
 */

public class ActorBean extends BaseBean {
    @MetaText
    private int childrenSize;
    @MetaText
    private String name, parentName;
    @MetaVector(editable = true, size = 4, prefix = {'r', 'g', 'b', 'a'})
    private float[] color;
    @MetaCheckBox
    private boolean visible, debug;
    @MetaTable
    private TransformBean transform;

    public int getChildrenSize() {
        return childrenSize;
    }

    public void setChildrenSize(int childrenSize) {
        this.childrenSize = childrenSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public TransformBean getTransform() {
        return transform;
    }

    public void setTransform(TransformBean transform) {
        this.transform = transform;
    }
}
