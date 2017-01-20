package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.command.Command;
import com.ayocrazy.easystage.command.SetValueCommand;
import com.ayocrazy.easystage.rmi.Client;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.IntMap;

import net.mwplay.nativefont.NativeLabel;

/**
 * Created by ayo on 2017/1/10.
 */

public class ActorTree extends Tree {
    private Skin skin;
    private IntMap<ActorBean> beans = new IntMap<>();
    private Node last;

    public ActorTree(Skin skin) {
        super(skin);
        this.skin = skin;
        initListener();
    }

    private void initListener() {
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                last = getSelection().getLastSelected();
                if (last == null) return;
                ActorBean bean = (ActorBean) last.getObject();
                Client.get().setCurrentActor(bean);
            }
        });
    }

    public void addNodes(Node parent, ActorBean actor) {
        Node node = new Node(new NativeLabel(actor.getName(), skin.get(Label.LabelStyle.class)));
        node.setObject(actor);
        if (parent != null) {
            parent.add(node);
        } else {
            ((NativeLabel) node.getActor()).setText("root");
            add(node);
        }
        int children[] = actor.getChildren();
        for (int i = 0; i < children.length; i++) {
            ActorBean bean = beans.get(children[i]);
            addNodes(node, bean);
        }
    }

    public void setActors(final StageBean stage, ActorBean[] actors) {
        beans.clear();
        for (ActorBean actor : actors) {
            beans.put(actor.getId(), actor);
        }
        clearChildren();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                addNodes(null, beans.get(stage.getRoot()));
                if (last == null) return;
                try {
                    Node node = findNode(last.getObject());
                    node.expandTo();
                } catch (Exception e) {
                }
            }
        });
    }
}
