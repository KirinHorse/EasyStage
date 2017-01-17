package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.TransformBean;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.IntMap;

/**
 * Created by 26286 on 2017/1/16.
 */

public class ActorGetter {
    private IntMap<Object> objects;
    private Actor actor;
    private ActorBean actorBean;

    public ActorGetter(Actor actor, StageGetter stageGetter) {
        this.objects = stageGetter.objects;
        this.actor = actor;
        objects.put(actor.hashCode(), actor);
        actorBean = new ActorBean();
        actorBean.setId(actor.hashCode());
        actorBean.setName(EasyReflect.getClaz(actor).getSimpleName());
        actorBean.setUser(StageGetter.createUser(actor));
        actorBean.setTransform(createTransform());
    }

    private TransformBean createTransform() {
        TransformBean bean = new TransformBean();
        bean.setPosition(new float[2]);
        bean.setOrigion(new float[2]);
        bean.setScale(new float[2]);
        bean.setSize(new float[2]);
        return bean;
    }

    public ActorBean refreshActor() {
        actorBean.setChildren(getChildren(actor));
        actorBean.setTouchable(actor.getTouchable().name());
        actorBean.setColor((float[]) EasyReflect.getValue("color", actor));
        actorBean.setDebug((boolean) EasyReflect.getValue("debug", actor));
        actorBean.setVisible((boolean) EasyReflect.getValue("visible", actor));
        actorBean.setzIndex(actor.getZIndex());
        refreshTransform();
        StageGetter.refreshUser(actorBean.getUser(), actor);
        return actorBean;
    }

    private void refreshTransform() {
        TransformBean bean = actorBean.getTransform();
        bean.setId(actor.hashCode());
        bean.getPosition()[0] = actor.getX();
        bean.getPosition()[1] = actor.getY();
        bean.getOrigion()[0] = actor.getOriginX();
        bean.getOrigion()[1] = actor.getOriginY();
        bean.getScale()[0] = actor.getScaleX();
        bean.getScale()[1] = actor.getScaleY();
        bean.getSize()[0] = actor.getWidth();
        bean.getSize()[1] = actor.getHeight();
        bean.setRotation(actor.getRotation());
    }

    private int[] getChildren(Actor actor) {
        if (actor instanceof Group) {
            int[] children = actorBean.getChildren();
            if (children == null || children.length != ((Group) actor).getChildren().size) {
                children = new int[((Group) actor).getChildren().size];
            }
            for (int i = 0; i < children.length; i++) {
                children[i] = ((Group) actor).getChildren().get(i).hashCode();
            }
            return children;
        } else {
            return noChild;
        }
    }

    private static final int[] noChild = new int[0];
}
