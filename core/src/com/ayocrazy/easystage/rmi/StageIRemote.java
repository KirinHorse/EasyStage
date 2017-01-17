package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.BaseBean;
import com.ayocrazy.easystage.bean.StageBean;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Created by ayo on 2017/1/10.
 */

public class StageIRemote extends UnicastRemoteObject implements IRemote {
    private StageGetter stageGetter;
    private StageSetter stageSetter;
    private IntMap<ActorGetter> actorGetters = new IntMap<>();

    public StageIRemote(Stage stage) throws RemoteException {
        stageGetter = new StageGetter(stage);
        stageSetter = new StageSetter(stageGetter);
        actorGetters(stage.getRoot());
    }

    private void actorGetters(Group group) {
        actorGetters.put(group.hashCode(), new ActorGetter(group, stageGetter));
        System.out.print(group.hashCode() + "  ");
        for (Actor actor : group.getChildren()) {
            if (actor instanceof Group) {
                actorGetters((Group) actor);
            } else {
                actorGetters.put(actor.hashCode(), new ActorGetter(actor, stageGetter));
                System.out.print(actor.hashCode() + "  ");
            }
        }
        System.out.println();
    }

    @Override
    public StageBean getStage() throws RemoteException {
        return stageGetter.refreshStageBean();
    }

    @Override
    public boolean setValue(int id, String fieldName, String methodName, Object value) throws RemoteException {
        return stageSetter.setValue(id, fieldName, methodName, value);
    }


    public void setStage(Stage stage) {
    }

    @Override
    public ActorBean getActor(int id) throws RemoteException {
        return actorGetters.get(id).refreshActor();
    }

    @Override
    public ActorBean[] getActors() throws RemoteException {
        ActorBean[] beans = new ActorBean[actorGetters.size];
        Array<ActorGetter> getters = actorGetters.values().toArray();
        for (int i = 0; i < beans.length; i++) {
            beans[i] = getters.get(i).refreshActor();
        }
        return beans;
    }
}
