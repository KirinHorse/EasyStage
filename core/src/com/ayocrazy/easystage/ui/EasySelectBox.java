package com.ayocrazy.easystage.ui;

import com.ayocrazy.easystage.command.Command;
import com.ayocrazy.easystage.command.SetValueCommand;
import com.ayocrazy.easystage.uimeta.MetaMethod;
import com.ayocrazy.easystage.uimeta.MetaSelectBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.mwplay.nativefont.NativeFont;

/**
 * Created by 26286 on 2017/1/14.
 */

public class EasySelectBox extends SelectBox<String> implements EasyUI {
    private int objId;
    private String fieldName, methodName;
    private String lastValue;

    public EasySelectBox(Skin skin, int objId, String fieldName, MetaSelectBox metaSelectBox, MetaMethod metaMethod) {
        super(skin);
        this.objId = objId;
        this.fieldName = fieldName;
        if (metaMethod != null) methodName = metaMethod.name();
        if (metaSelectBox.items().length > 0) {
            setItems(metaSelectBox.items());
        } else if (metaSelectBox.enumClass().isEnum()) {
            Object[] enums = metaSelectBox.enumClass().getEnumConstants();
            String items[] = new String[enums.length];
            for (int i = 0; i < items.length; i++) {
                items[i] = enums[i].toString();
                ((NativeFont) getStyle().font).appendText(items[i]);
            }
            setItems(items);
        }
        initListener();
    }

    private void initListener() {
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Command.doCmd(new SetValueCommand(objId, fieldName, methodName, lastValue, getSelected()));
            }
        });
    }

    @Override
    protected void onShow(Actor selectBoxList, boolean below) {
        super.onShow(selectBoxList, below);
        lastValue = getSelected();
    }

    @Override
    public void updateValue(Object value) {
        setSelected((String) value);
    }
}
