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
    private boolean isShow;

    public EasySelectBox(Skin skin, int objId, String fieldName, MetaSelectBox metaSelectBox, MetaMethod metaMethod) {
        super(skin);
        this.objId = objId;
        this.fieldName = fieldName;
        if (metaMethod != null) methodName = metaMethod.name();
        String items[];
        String metaItems[] = metaSelectBox.items();
        if (metaItems.length > 0) {
            items = new String[metaItems.length + 1];
            items[0] = "";
            System.arraycopy(metaSelectBox.items(), 0, items, 1, metaItems.length);
            setItems(items);
        } else if (metaSelectBox.enumClass().isEnum()) {
            Object[] enums = metaSelectBox.enumClass().getEnumConstants();
            items = new String[enums.length + 1];
            items[0] = "";
            for (int i = 1; i < items.length; i++) {
                items[i] = enums[i - 1].toString();
                ((NativeFont) getStyle().font).appendText(items[i]);
            }
        } else return;
        setItems(items);
        initListener();
    }

    private void initListener() {
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isShow) return;
                if (!getSelected().equals(lastValue))
                    Command.doCmd(new SetValueCommand(objId, fieldName, methodName, lastValue, getSelected()));
            }
        });
    }

    @Override
    protected void onShow(Actor selectBoxList, boolean below) {
        super.onShow(selectBoxList, below);
        lastValue = getSelected();
        isShow = true;
    }

    @Override
    protected void onHide(Actor selectBoxList) {
        super.onHide(selectBoxList);
        isShow = false;
    }

    @Override
    public void updateValue(Object value) {
        if (isShow) return;
        setSelected((String) value);
    }

    @Override
    public void setId(int id) {
        objId = id;
    }
}
