package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.BaseBean;
import com.ayocrazy.easystage.bean.StageBean;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by ayo on 2017/1/10.
 */

public class StageIRemote extends UnicastRemoteObject implements IRemote {
    private StageGetter stageGetter;
    private StageSetter stageSetter;

    public StageIRemote(Stage stage) throws RemoteException {
        stageGetter = new StageGetter(stage);
        stageSetter = new StageSetter(stageGetter);
    }

    @Override
    public StageBean getStage() throws RemoteException {
        return stageGetter.refreshStageBean();
    }

    @Override
    public BaseBean get(int id) throws RemoteException {
        return null;
    }

    @Override
    public boolean setValue(int id, String fieldName, String methodName, Object value) throws RemoteException {
        return stageSetter.setValue(id, fieldName, methodName, value);
    }


    public void setStage(Stage stage) {
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
