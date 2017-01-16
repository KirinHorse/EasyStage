package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.bean.CameraBean;
import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.bean.UserBean;
import com.ayocrazy.easystage.bean.ViewportBean;
import com.ayocrazy.easystage.uimeta.MetaConvertor;
import com.ayocrazy.easystage.uimeta.MetaMethod;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by ayo on 2017/1/15.
 */

public class StageGetter {
    IntMap<Object> objects = new IntMap<>();
    Stage stage;
    StageBean stageBean;
    private int children;

    public StageGetter(Stage stage) {
        this.stage = stage;
        objects.put(stage.hashCode(), stage);
        objects.put(stage.getViewport().hashCode(), stage.getViewport());
        objects.put(stage.getCamera().hashCode(), stage.getCamera());
        stageBean = new StageBean();
        stageBean.setViewport(new ViewportBean());
        stageBean.setCamera(new CameraBean());
        stageBean.setUser(createUser(stage));
        childrenChanged();
    }

    public StageBean refreshStageBean() {
        stageBean.setName(getName(stage));
        stageBean.setId(stage.hashCode());
        stageBean.setChildren(children);
        stageBean.setDebugAll((boolean) EasyReflect.getValue("debugAll", stage));
        refreshUser(stageBean.getUser(), stage);
        refreshViewport();
        refreshCamera();
        return stageBean;
    }

    public void childrenChanged() {
        children = stage.getActors().size;
    }

    private void refreshViewport() {
        ViewportBean bean = stageBean.getViewport();
        if (bean == null) return;
        Viewport vp = stage.getViewport();
        bean.setId(vp.hashCode());
        bean.setType(getName(vp));
        if (vp instanceof ScalingViewport) {
            bean.setScalling(((ScalingViewport) vp).getScaling().name());
        }
        bean.setWorldSize(new float[]{vp.getWorldWidth(), vp.getWorldHeight()});
        bean.setScreenPos(new int[]{vp.getScreenX(), vp.getScreenY()});
        bean.setScreenSize(new int[]{vp.getScreenWidth(), vp.getScreenHeight()});
    }

    private void refreshCamera() {
        CameraBean bean = stageBean.getCamera();
        if (bean == null) return;
        Camera camera = stage.getCamera();
        bean.setId(camera.hashCode());
        bean.setPosition(new float[]{camera.position.x, camera.position.y, camera.position.z});
        bean.setDirection(new float[]{camera.direction.x, camera.direction.y, camera.direction.z});
        bean.setUp(new float[]{camera.up.x, camera.up.y, camera.up.z});
        bean.setFar(camera.far);
        bean.setNear(camera.near);
        if (camera instanceof OrthographicCamera) {
            bean.setZoom(((OrthographicCamera) camera).zoom);
        }
    }

    static UserBean createUser(Object obj) {
        UserBean bean = new UserBean();
        Array<String> names = new Array<>();
        Array<String> metas = new Array<>();
        Array<String> methods = new Array<>();
        Array<Object> values = new Array<>();
        Class claz = EasyReflect.getClaz(obj);
        while (claz != Object.class) {
            Field fields[] = claz.getDeclaredFields();
            for (Field field : fields) {
                if (!EasyReflect.isValid(field.getType())) continue;
                for (Annotation anno : field.getAnnotations()) {
                    if (anno instanceof MetaMethod) {
                    } else if (anno.annotationType().getSimpleName().startsWith("Meta")) {
                        names.add(field.getName());
                        metas.add(MetaConvertor.getString(anno));
                        MetaMethod method = field.getAnnotation(MetaMethod.class);
                        if (method == null) methods.add("");
                        else methods.add(method.name());
                        try {
                            values.add(EasyReflect.getValue(field.getName(), obj));
                        } catch (Exception e) {
                        }
                        break;
                    }
                }
            }
            claz = claz.getSuperclass();
        }
        if (names.size < 1) return null;
        bean.setFieldNames((String[]) names.toArray(String.class));
        bean.setMetas((String[]) metas.toArray(String.class));
        bean.setMethodNames((String[]) methods.toArray(String.class));
        bean.setValues(values.toArray());
        return bean;
    }

    static void refreshUser(UserBean user, Object obj) {
        if (user == null || obj == null) return;
        String[] names = user.getFieldNames();
        if (names == null || names.length < 1) return;
        Object[] values = user.getValues();
        for (int i = 0; i < names.length; i++) {
            values[i] = EasyReflect.getValue(names[i], obj);
        }
    }

    static String getName(Object obj) {
        Class claz = EasyReflect.getClaz(obj);
        return claz.getSimpleName();
    }
}
