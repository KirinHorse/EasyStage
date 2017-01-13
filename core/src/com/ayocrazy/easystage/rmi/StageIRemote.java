package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.BeanCreator;
import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.bean.UserBean;
import com.ayocrazy.easystage.bean.ViewportBean;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Created by ayo on 2017/1/10.
 */

public class StageIRemote extends UnicastRemoteObject implements IRemote {
    private Stage stage;
    private StageBean stageBean;
    private HashMap<Integer, Object> owners = new HashMap<Integer, Object>();
    private HashMap<Integer, UserBean> userBeans = new HashMap<Integer, UserBean>();
    private HashMap<String, ActorBean> actorBeans = new HashMap<String, ActorBean>();

    public StageIRemote(Stage stage) throws RemoteException {
        this.stage = stage;
        stageBean = BeanCreator.refreshStage(stage, null);
        owners.put(stage.hashCode(), stage);
        userBeans.put(stage.hashCode(), BeanCreator.refreshUserBean(stage, null));
    }

    @Override
    public StageBean getStage() throws RemoteException {
        BeanCreator.refreshStage(stage, stageBean);
        return stageBean;
    }

    @Override
    public UserBean getUser(int id) throws RemoteException {
        UserBean user = userBeans.get(id);
        if (user != null) BeanCreator.refreshUserBean(owners.get(id), user);
        return user;
    }

    @Override
    public boolean setValue(int id, String fieldName, Object value) throws RemoteException {
        if (value == null) return false;
        Object owner = owners.get(id);
        if (owner == null) return false;
        //whether is setting the stage special value
        if (owner instanceof Stage) {
            int result = ValueSpecial.specialStage(stage, stageBean, fieldName, value);
            if (result == 1) return true;
            else if (result == -1) return false;
        }
        Class claz = owner.getClass().getSuperclass();
        System.out.println(claz.getName());
        try {
            String names[] = fieldName.split(":");
            Object parent = owner;
            Field field;
            for (int i = 0; i < names.length; i++) {
                field = getField(claz, fieldName);
                System.out.println(field.getName());
                field.setAccessible(true);
                if (i == names.length - 1) {
                    int result = ValueSpecial.specialField(parent, field, value);
                    if (result == 1) return true;
                    else if (result == -1) return false;
                    return setValue(field, parent, value);
                } else {
                    parent = field.get(parent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static Field getField(Class claz, String fieldName) {
        while (claz != Object.class) {
            try {
                return claz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
            claz = claz.getSuperclass();
        }
        throw new GdxRuntimeException("Can not find field " + fieldName +
                " in class " + claz.getName() + " and its super classes");
    }

    private static Method getMethod(Class claz, Field field) {
        String fieldName = field.getName();
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Class paramType = field.getType();
        while (claz != Object.class) {
            try {
                return claz.getDeclaredMethod(methodName, paramType);
            } catch (NoSuchMethodException e) {
            }
            claz = claz.getSuperclass();
        }
        return null;
    }

    private boolean setValue(Field field, Object parent, Object value) {
        Method method = getMethod(parent.getClass(), field);
        try {
            if (method != null) {
                System.out.println(method.getName());
                method.setAccessible(true);
                method.invoke(parent, value);
            } else field.set(parent, value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public ActorBean getActor(String id) throws RemoteException {
        return null;
    }

    @Override
    public ActorBean[] getActors() throws RemoteException {
        return new ActorBean[0];
    }

    @Override
    public boolean childrenChanged() throws RemoteException {
        return false;
    }
}
