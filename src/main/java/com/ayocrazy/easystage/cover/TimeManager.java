package com.ayocrazy.easystage.cover;

/**
 * Created by ayo on 2017/1/19.
 */

public class TimeManager {
    private long actTotal, drawTotal;
    private long actStart, drawStart;
    private int index = 0;
    float actTime, drawTime;

    void actStart() {
        actStart = System.nanoTime();
    }

    void actEnd() {
        actTotal += System.nanoTime() - actStart;
        if (++index >= 60) {
            actTime = actTotal / index / 10000 / 100f;
        }
    }

    void drawStart() {
        drawStart = System.nanoTime();
    }

    void drawEnd() {
        drawTotal += System.nanoTime() - drawStart;
        if (index >= 60) {
            drawTime = drawTotal / index / 10000 / 100f;
            index = 0;
        }
    }
}
