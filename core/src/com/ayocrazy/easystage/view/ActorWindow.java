package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.ui.UICreator;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Created by 26286 on 2017/1/16.
 */

public class ActorWindow extends Window {
    private ActorBean bean;
    private UICreator creator;
    private ScrollPane sp;

    public ActorWindow(String title, Skin skin) {
        super(title, skin);
        creator = new UICreator(getSkin());
        creator.pad(0, 15, 0, 15);
        creator.left().top();
        sp = new ScrollPane(creator, skin);
        sp.setupOverscroll(20, 20, 80);
        sp.setFlingTime(0.6f);
        add(sp).pad(3).expand().fill();
        setResizable(true);
        setResizeBorder(10);
    }

    public void setBean(ActorBean bean) {
        this.bean = bean;
        creator.update(bean);
    }
}
