package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.ui.UICreator;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Created by ayo on 2017/1/17.
 */

public class TreeWindow extends Window {
    public TreeWindow(String title, Skin skin, Tree tree) {
        super(title, skin);
        tree.setPadding(5);
        ScrollPane sp = new ScrollPane(tree, skin);
        sp.setupOverscroll(20, 20, 80);
        sp.setFlickScroll(false);
        sp.setFlingTime(0.6f);
        add(sp).pad(3).expand().fill();
        setResizable(true);
        setResizeBorder(10);
    }
}
