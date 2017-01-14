package com.ayocrazy.easystage.bean;

import com.ayocrazy.easystage.uimeta.MetaCheckBox;
import com.ayocrazy.easystage.uimeta.MetaTable;
import com.ayocrazy.easystage.uimeta.MetaText;

import java.io.Serializable;

/**
 * Created by ayo on 2017/1/11.
 */

public class StageBean extends BaseBean {
    private String name;
    @MetaText
    private int children;
    @MetaCheckBox
    private boolean debugAll;
    @MetaTable
    private ViewportBean viewport;
    @MetaTable
    private CameraBean camera;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public boolean isDebugAll() {
        return debugAll;
    }

    public void setDebugAll(boolean debugAll) {
        this.debugAll = debugAll;
    }

    public ViewportBean getViewport() {
        return viewport;
    }

    public void setViewport(ViewportBean viewport) {
        this.viewport = viewport;
    }

    public CameraBean getCamera() {
        return camera;
    }

    public void setCamera(CameraBean camera) {
        this.camera = camera;
    }
}
