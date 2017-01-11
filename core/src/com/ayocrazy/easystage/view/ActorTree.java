package com.ayocrazy.easystage.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;

/**
 * Created by ayo on 2017/1/10.
 */

public class ActorTree extends Tree {
    private NativeFont font;

    public ActorTree(Skin skin) {
        super(skin);
        font = new NativeFont();
        font.setSize(16);
    }

    public void addNode(Actor actor) {
        String name = actor.getName();
        if (name == null || name.length() == 0) {
            name = actor.getClass().getName();
        }
        Node node = new Node(new NativeLabel(name, font));
        node.setObject(actor);
        Node parent = findNode(actor.getParent());
        if (parent != null) {
            parent.add(node);
        } else {
            add(node);
        }
    }
}
