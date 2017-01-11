package com.ayocrazy.easystage.bean;

import com.ayocrazy.easystage.uimeta.MetaText;

import java.io.Serializable;

/**
 * Created by ayo on 2017/1/11.
 */

public class ColorBean implements Serializable {
    private float r, g, b, a;

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }
}
