package com.ayocrazy.easystage.bean;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.reflect.Field;

/**
 * Created by ayo on 2017/1/11.
 */

public class BeanGenerator {
    public static final StageBean genStage(Stage stage) {
        StageBean bean = new StageBean();
        bean.setName(getName(stage));
        bean.setId(getId(stage));
        bean.setRoot(getName(stage.getRoot()) + "@" + getId(stage.getRoot()));
        bean.setChildrenSize(getActorsCount(stage.getRoot()));
        bean.setDebug((Boolean) reflectValue(Stage.class, "debug", stage));
        bean.setCamera(genCamera(stage.getCamera()));
        bean.setViewport(genViewport(stage.getViewport()));
        return bean;
    }

    public static final ViewportBean genViewport(Viewport vp) {
        ViewportBean bean = new ViewportBean();
        bean.setType(getName(vp));
        if (vp instanceof ScalingViewport) {
            bean.setScalling(((ScalingViewport) vp).getScaling().name());
        }
        bean.setWorldSize(new float[]{vp.getWorldWidth(), vp.getWorldHeight()});
        bean.setScreenPos(new int[]{vp.getScreenX(), vp.getScreenY()});
        bean.setScreenSize(new int[]{vp.getScreenWidth(), vp.getScreenHeight()});
        return bean;
    }

    public static final CameraBean genCamera(Camera camera) {
        CameraBean bean = new CameraBean();
        bean.setPosition(new float[]{camera.position.x, camera.position.y, camera.position.z});
        bean.setDirection(new float[]{camera.direction.x, camera.direction.y, camera.direction.z});
        bean.setUp(new float[]{camera.up.x, camera.up.y, camera.up.z});
        bean.setFar(camera.far);
        bean.setNear(camera.near);
        if (camera instanceof OrthographicCamera) {
            bean.setZoom(((OrthographicCamera) camera).zoom);
        }
        return bean;
    }

    private static int getActorsCount(Group group) {
        int count = 0;
        for (Actor actor : group.getChildren()) {
            if (actor instanceof Group) {
                count += getActorsCount((Group) actor);
            }
        }
        count += group.getChildren().size;
        return count;
    }

    private static String getId(Object obj) {
        return Integer.toHexString(obj.hashCode());
    }

    private static String getName(Object obj) {
        return obj.getClass().getSimpleName().split("\\$")[0];
    }

    private static final Object reflectValue(Class claz, String fieldName, Object obj) {
        try {
            Field field = claz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
