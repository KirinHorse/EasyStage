package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.bean.ViewportBean;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * Created by ayo on 2017/1/13.
 */

public class ValueSpecial {

    public static int specialStage(Stage stage, StageBean stageBean, String fieldName, Object value) {
        if (fieldName.startsWith("viewport:")) {
            return specialViewport(stage, stageBean, fieldName, value);
        }
        return 0;
    }

    public static int specialViewport(Stage stage, StageBean stageBean, String fieldName, Object value) {
        try {
            if (fieldName.equals("viewport:type")) {
                ViewportBean vb = stageBean.getViewport();
                Viewport newVp = null;
                if (value.equals("ScalingViewport")) {
                    newVp = new ScalingViewport(Scaling.valueOf(vb.getScalling()), vb.getWorldSize()[0], vb.getWorldSize()[1], stage.getCamera());
                } else if (value.equals("ScreenViewport")) {
                    newVp = new ScreenViewport(stage.getCamera());
                }
                newVp.update(vb.getScreenSize()[0], vb.getScreenSize()[1]);
                stage.setViewport(newVp);
            } else if (fieldName.equals("viewport:scalling")) {
                if (stage.getViewport() instanceof ScalingViewport) {
                    ((ScalingViewport) stage.getViewport()).setScaling(Scaling.valueOf(value.toString()));
                } else return -1;
            } else if (fieldName.equals("viewport:wordSize")) {
                stage.getViewport().setWorldSize(Array.getFloat(value, 0), Array.getFloat(value, 1));
            } else if (fieldName.equals("viewport:screenPos")) {
                stage.getViewport().setScreenPosition(Array.getInt(value, 0), Array.getInt(value, 1));
            } else if (fieldName.equals("viewport:screenSize")) {
                stage.getViewport().setScreenSize(Array.getInt(value, 0), Array.getInt(value, 1));
            } else return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public static int specialField(Object owner, Field field, Object value) {
        try {
            Class type = field.getType();
            field.setAccessible(true);
            if (type == Vector2.class) {
                field.set(owner, new Vector2(Array.getFloat(value, 0), Array.getFloat(value, 1)));
            } else if (type == Vector3.class) {
                field.set(owner, new Vector3(Array.getFloat(value, 0), Array.getFloat(value, 1), Array.getFloat(value, 2)));
            } else if (type == Color.class) {
                field.set(owner, new Color(Array.getFloat(value, 0), Array.getFloat(value, 1), Array.getFloat(value, 2), Array.getFloat(value, 3)));
            } else if (type.isEnum()) {
                field.set(owner, Enum.valueOf(type, value.toString()));
            } else return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }
}
