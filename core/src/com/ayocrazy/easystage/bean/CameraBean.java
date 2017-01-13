package com.ayocrazy.easystage.bean;

import com.ayocrazy.easystage.uimeta.MetaText;
import com.ayocrazy.easystage.uimeta.MetaVector;

import java.io.Serializable;

/**
 * Created by ayo on 2017/1/11.
 */

public class CameraBean implements Serializable {
    @MetaVector(editable = true, filter = MetaText.Filter.FLOAT, size = 3)
    private float[] position, direction, up;
    @MetaText(editable = true, filter = MetaText.Filter.FLOAT)
    private float near, far, zoom;

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getDirection() {
        return direction;
    }

    public void setDirection(float[] direction) {
        this.direction = direction;
    }

    public float[] getUp() {
        return up;
    }

    public void setUp(float[] up) {
        this.up = up;
    }

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        this.near = near;
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        this.far = far;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
}
