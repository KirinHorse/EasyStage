package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.BeanCreator;
import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.bean.UserBean;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.io.Serializable;
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
