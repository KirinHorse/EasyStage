package com.ayocrazy.easystage.ui;


import com.ayocrazy.easystage.command.Command;
import com.ayocrazy.easystage.command.SetValueCommand;
import com.ayocrazy.easystage.uimeta.MetaCheckBox;
import com.ayocrazy.easystage.uimeta.MetaMethod;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by 26286 on 2017/1/14.
 */

public class EasyCheckBox extends CheckBox implements EasyUI {
    private int objId;
    private String fieldName, methodName;

    public EasyCheckBox(Skin skin, int objId, String fieldName, MetaCheckBox metaCheckBox, MetaMethod metaMethod) {
        super("", skin);
        this.objId = objId;
        this.fieldName = fieldName;
        if (metaMethod != null) methodName = metaMethod.name();
        initListener();
    }

    private void initListener() {
        setProgrammaticChangeEvents(false);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Command.doCmd(new SetValueCommand(objId, fieldName, methodName, !isChecked(), isChecked()));
            }
        });
    }

    @Override
    public void updateValue(Object value) {
        setChecked((boolean) value);
    }

    @Override
    public void setId(int id) {
        objId = id;
    }
}
