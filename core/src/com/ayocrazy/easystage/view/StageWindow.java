package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.bean.UserBean;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Created by ayo on 2017/1/11.
 */

public class StageWindow extends Window {
    StageBean stageBean;
    UserBean userBean;
    private ScrollPane sp;
    private UICreator creator;

    public StageWindow(String title, Skin skin) {
        super(title, skin);
        creator = new UICreator(skin);
        creator.pad(0, 15, 0, 15);
        creator.left().top();
        sp = new ScrollPane(creator, skin);
        sp.setupOverscroll(20, 20, 80);
        sp.setFlingTime(0.6f);
        add(sp).pad(3).expand().fill();
        setResizable(true);
        setResizeBorder(10);
        creator.create(StageBean.class);
    }

    public void setStageBean(StageBean bean) {
        if (stageBean == null || stageBean.getId() != bean.getId()) {
            getTitleLabel().setText(bean.getName());
        }
        this.stageBean = bean;
        creator.setBean(bean);
    }

    public void setUserBean(UserBean bean) {
        if (userBean == null || userBean.getId() != bean.getId()) {
            creator.clear();
            creator.getCells().clear();
            creator.createUserUI(bean);
            creator.create(StageBean.class);
        }
        userBean = bean;
        creator.setUserBean(bean);
    }
}
