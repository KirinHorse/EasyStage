package com.ayocrazy.easystage.server;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.BeanGenerator;
import com.ayocrazy.easystage.bean.StageBean;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Created by ayo on 2017/1/10.
 */

public class StageServer extends UnicastRemoteObject implements Server {
    private Stage stage;
    private StageBean stageBean;
    private HashMap<String, ActorBean> actorBeans = new HashMap<String, ActorBean>();

    public StageServer(Stage stage) throws RemoteException {
        this.stage = stage;
        stageBean = BeanGenerator.genStage(stage);
    }

    @Override
    public StageBean getStage() throws RemoteException {
        return stageBean;
    }

    public void setStage(Stage stage) throws RemoteException {
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
