package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.StageBean;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by ayo on 2017/1/10.
 */

public class StageIRemote extends UnicastRemoteObject implements IRemote {
    private Stage stage;
    private StageGetter stageGetter;
    private StageSetter stageSetter;
    private IntMap<ActorGetter> actorGetters = new IntMap<>();

    public StageIRemote(Stage stage) throws RemoteException {
        this.stage = stage;
        stageGetter = new StageGetter(stage);
        stageSetter = new StageSetter(stageGetter);
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
        if (stageGetter != null && stageGetter.stage == stage) return;
        this.stage = stage;
        stageGetter = new StageGetter(stage);
        stageSetter = new StageSetter(stageGetter);
    }

    @Override
    public ActorBean getActor(int id) throws RemoteException {
        ActorGetter getter = actorGetters.get(id);
        if (getter != null)
            return getter.refreshActor();
        else return null;
    }

    @Override
    public ActorBean[] getActors() throws RemoteException {
        actorGetters.clear();
        actorGetters(stage.getRoot());
        ActorBean[] beans = new ActorBean[actorGetters.size];
        Array<ActorGetter> getters = actorGetters.values().toArray();
        for (int i = 0; i < beans.length; i++) {
            beans[i] = getters.get(i).refreshActor();
        }
        return beans;
    }


    private void actorGetters(Group group) {
        actorGetters.put(group.hashCode(), new ActorGetter(group, stageGetter));
        for (Actor actor : group.getChildren()) {
            if (actor instanceof Group) {
                actorGetters((Group) actor);
            } else {
                actorGetters.put(actor.hashCode(), new ActorGetter(actor, stageGetter));
            }
        }
    }
}
