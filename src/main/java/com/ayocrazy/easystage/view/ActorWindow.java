package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.ui.UICreator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by 26286 on 2017/1/16.
 */

public class ActorWindow extends EasyWindow {
    private ActorBean bean;
    private UICreator creator;

    public ActorWindow(String title, Skin skin) {
        super(title, skin);
        creator = new UICreator(getSkin());
        creator.pad(0, 15, 0, 15);
        creator.left().top();
        getScrollPane().setWidget(creator);
    }

    public void setBean(ActorBean bean) {
        if (bean == null) return;
        this.bean = bean;
        getTitleLabel().setText(bean.getName());
        creator.update(bean);
    }
}
