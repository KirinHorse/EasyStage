package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.bean.StageBean;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.reflect.Array;

/**
 * Created by ayo on 2017/1/15.
 */

public class StageSetter {
    private IntMap<Object> objects;
    private Stage stage;
    private StageBean stageBean;

    public StageSetter(StageGetter getter) {
        this.objects = getter.objects;
        this.stage = getter.stage;
        this.stageBean = getter.stageBean;
    }

    public boolean setValue(int id, String fieldName, String methodName, Object value) {
        Object obj = objects.get(id);
        if (obj == null) return false;
        System.out.println(obj.getClass().getSimpleName() + " " + fieldName + " " + methodName + " " + value);
        if (obj instanceof Viewport)
            return setViewport((Viewport) obj, fieldName, value);
        else if (obj instanceof Actor) {
            int result = setActor((Actor) obj, fieldName, value);
            if (result == 1) return true;
            else if (result == -1) return false;
        }
        if (methodName == null || methodName.equals("")) {
            return EasyReflect.setValue(fieldName, obj, value);
        } else {
            return EasyReflect.invoke(fieldName, methodName, obj, value);
        }
    }

    private boolean setViewport(Viewport vp, String fieldName, Object value) {
        try {
            int width = vp.getScreenWidth();
            int height = vp.getScreenHeight();
            if (fieldName.equals("type")) {
                if (value.equals("ScreenViewport")) {
                    vp = new ScreenViewport();
                    updateViewport(vp, width, height);
                    stage.setViewport(vp);
                } else if (value.equals("ScalingViewport")) {
                    String scallingStr = stageBean.getViewport().getScalling();
                    Scaling scaling = Scaling.stretch;
                    if (scallingStr != null) scaling = Scaling.valueOf(scallingStr);
                    vp = new ScalingViewport(scaling, vp.getWorldWidth(), vp.getWorldHeight());
                    updateViewport(vp, width, height);
                    stage.setViewport(vp);
                } else return false;
            } else if (fieldName.equals("scalling")) {
                if (vp instanceof ScalingViewport) {
                    Scaling scaling = Scaling.valueOf((String) value);
                    if (scaling == null) return false;
                    ((ScalingViewport) vp).setScaling(scaling);
                    updateViewport(vp, width, height);
                } else return false;
            } else if (fieldName.equals("worldSize")) {
                vp.setWorldSize(EasyReflect.toFloat(value, 0), EasyReflect.toFloat(value, 1));
                updateViewport(vp, width, height);
            } else if (fieldName.equals("screenPos")) {
                vp.setScreenPosition(EasyReflect.toInt(value, 0), EasyReflect.toInt(value, 1));
                updateViewport(vp, width, height);
            } else return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private int setActor(Actor actor, String fieldName, Object value) {
        try {
            if (fieldName.equals("position")) {
                actor.setPosition(EasyReflect.toFloat(value, 0), EasyReflect.toFloat(value, 1));
            } else if (fieldName.equals("size")) {
                actor.setSize(EasyReflect.toFloat(value, 0), EasyReflect.toFloat(value, 1));
            } else if (fieldName.equals("scale")) {
                actor.setScale(EasyReflect.toFloat(value, 0), EasyReflect.toFloat(value, 1));
            } else if (fieldName.equals("origion")) {
                actor.setOrigin(EasyReflect.toFloat(value, 0), EasyReflect.toFloat(value, 1));
            } else if (fieldName.equals("rotation")) {
                actor.setRotation(Float.parseFloat(value.toString()));
            } else {
                return 0;
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    private void updateViewport(final Viewport vp, final int width, final int height) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                vp.update(width, height, true);
            }
        });
    }
}
