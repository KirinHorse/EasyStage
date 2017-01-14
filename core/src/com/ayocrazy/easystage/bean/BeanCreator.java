package com.ayocrazy.easystage.bean;

import com.ayocrazy.easystage.uimeta.MetaConvertor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by ayo on 2017/1/11.
 */

public class BeanCreator {
    public static final StageBean refreshStage(Stage stage, StageBean bean) {
        if (bean == null) bean = new StageBean();
        bean.setName(getName(stage));
        bean.setId(stage.hashCode());
        bean.setChildren(getActorsCount(stage.getRoot()));
        bean.setDebugAll((Boolean) reflectValue(Stage.class, "debugAll", stage));
        bean.setCamera(genCamera(stage.getCamera()));
        bean.setViewport(genViewport(stage.getViewport()));
        return bean;
    }

    public static final UserBean refreshUserBean(Object obj, UserBean bean) {
        if (bean == null) {
            bean = genUserBean(obj);
        }
        String[] names = bean.getFieldNames();
        Class claz = obj.getClass().getSuperclass();
        for (int i = 0; i < names.length; i++) {
            bean.getValues()[i] = reflectValue(claz, names[i], obj);
//            System.out.println(bean.getFieldNames()[i] + " : " + bean.getValues()[i]);
        }
        return bean;
    }


    public static final UserBean genUserBean(Object obj) {
        UserBean bean = new UserBean();
        Class claz = obj.getClass().getSuperclass();
        Field fields[] = claz.getDeclaredFields();
        Array<String> names = new Array();
        Array<String> metas = new Array();
        for (Field field : fields) {
            for (Annotation anno : field.getAnnotations()) {
                if (anno.annotationType().getSimpleName().startsWith("Meta")) {
                    names.add(field.getName());
                    metas.add(MetaConvertor.getString(anno));
                    break;
                }
            }
        }
        if (names == null || names.size < 1) return bean;
        bean.setFieldNames((String[]) names.toArray(String.class));
        bean.setMetas((String[]) metas.toArray(String.class));
        bean.setValues(new Object[names.size]);
        return bean;
    }

    public static final ViewportBean genViewport(Viewport vp) {
        ViewportBean bean = new ViewportBean();
        bean.setId(vp.hashCode());
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
        bean.setId(camera.hashCode());
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
