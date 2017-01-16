package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.bean.ActorBean;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;

/**
 * Created by ayo on 2017/1/10.
 */

public class ActorTree extends Tree {
    private Skin skin;

    public ActorTree(Skin skin) {
        super(skin);
        this.skin = skin;
    }

    public void addNode(ActorBean actor) {
        String name = actor.getName();
        if (name == null || name.length() == 0) {
            name = actor.getClass().getName();
        }
        Node node = new Node(new NativeLabel(name, skin.get(Label.LabelStyle.class)));
        node.setObject(actor.getId());
        Node parent = findNode(actor.getParentId());
        if (parent != null) {
            parent.add(node);
        } else {
            add(node);
        }
    }
}
