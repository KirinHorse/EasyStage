package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.ui.UICreator;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Created by ayo on 2017/1/17.
 */

public class TreeWindow extends EasyWindow {
    public TreeWindow(String title, Skin skin, Tree tree) {
        super(title, skin);
        tree.setPadding(5);
        getScrollPane().setWidget(tree);
    }
}
