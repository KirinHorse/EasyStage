package com.ayocrazy.easystage.bean;

import com.ayocrazy.easystage.uimeta.MetaCheckBox;
import com.ayocrazy.easystage.uimeta.MetaMethod;
import com.ayocrazy.easystage.uimeta.MetaSelectBox;
import com.ayocrazy.easystage.uimeta.MetaTable;
import com.ayocrazy.easystage.uimeta.MetaText;
import com.ayocrazy.easystage.uimeta.MetaVector;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * Created by ayo on 2017/1/11.
 */

public class ActorBean extends BaseBean {
    private String name;
    @MetaText
    private int id, parentId;
    private int[] children;
    @MetaCheckBox
    @MetaMethod
    private boolean visible, debug;
    @MetaSelectBox(enumClass = Touchable.class)
    @MetaMethod
    private String touchable;
    @MetaVector(editable = true, size = 4, prefix = {'r', 'g', 'b', 'a'}, maxLength = 5)
    @MetaMethod
    private float[] color;
    @MetaText(editable = true, filter = MetaText.Filter.PosINT)
    @MetaMethod
    private int zIndex;
    @MetaTable
    private TransformBean transform;

    public int[] getChildren() {
        return children;
    }

    public void setChildren(int[] children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getTouchable() {
        return touchable;
    }

    public void setTouchable(String touchable) {
        this.touchable = touchable;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
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

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public int getzIndex() {
        return zIndex;
    }

    public TransformBean getTransform() {
        return transform;
    }

    public void setTransform(TransformBean transform) {
        this.transform = transform;
    }
}
