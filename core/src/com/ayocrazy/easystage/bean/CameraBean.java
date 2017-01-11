package com.ayocrazy.easystage.bean;

import com.ayocrazy.easystage.uimeta.MetaText;

import java.io.Serializable;

/**
 * Created by ayo on 2017/1/11.
 */

public class CameraBean implements Serializable {
    @MetaText(editable = true, filter = MetaText.Filter.FLOAT)
    private float posX, posY, posZ, drcX, drcY, drcZ, upX, upY, upZ, near, far, zoom;

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPosZ() {
        return posZ;
    }

    public void setPosZ(float posZ) {
        this.posZ = posZ;
    }

    public float getDrcX() {
        return drcX;
    }

    public void setDrcX(float drcX) {
        this.drcX = drcX;
    }

    public float getDrcY() {
        return drcY;
    }

    public void setDrcY(float drcY) {
        this.drcY = drcY;
    }

    public float getDrcZ() {
        return drcZ;
    }

    public void setDrcZ(float drcZ) {
        this.drcZ = drcZ;
    }

    public float getUpX() {
        return upX;
    }

    public void setUpX(float upX) {
        this.upX = upX;
    }

    public float getUpY() {
        return upY;
    }

    public void setUpY(float upY) {
        this.upY = upY;
    }

    public float getUpZ() {
        return upZ;
    }

    public void setUpZ(float upZ) {
        this.upZ = upZ;
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
