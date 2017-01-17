package com.ayocrazy.easystage.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

/**
 * Created by 26286 on 2017/1/16.
 */

public class EasyTab extends Table {
    private Array<Actor> actors;

    public EasyTab(Skin skin) {
        super(skin);

    }

    public void addItem(Actor actor) {
        actors.add(actor);
    }

    public void removeItem(Actor actor) {
        actors.removeValue(actor, true);
    }
}
