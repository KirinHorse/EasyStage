package com.ayocrazy.easystage.ui;

import com.ayocrazy.easystage.command.Command;
import com.ayocrazy.easystage.command.SetValueCommand;
import com.ayocrazy.easystage.uimeta.MetaMethod;
import com.ayocrazy.easystage.uimeta.MetaSlider;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.mwplay.nativefont.NativeLabel;

/**
 * Created by 26286 on 2017/1/14.
 */

public class EasySlider extends Table implements EasyUI {
    private int objId;
    private String fieldName, methodName;
    private Slider slider;
    private NativeLabel label;
    private boolean isFocus;
    private float lastValue;

    public EasySlider(Skin skin, int objId, String fieldName, MetaSlider metaSlider) {
        this(skin, objId, fieldName, metaSlider, null);
    }

    public EasySlider(Skin skin, int objId, String fieldName, MetaSlider metaSlider, MetaMethod metaMethod) {
        super(skin);
        this.objId = objId;
        this.fieldName = fieldName;
        if (metaMethod != null) methodName = metaMethod.name();
        slider = new Slider(metaSlider.minValue(), metaSlider.maxValue(), metaSlider.step(), false, skin);
        label = new NativeLabel("", skin.get(Label.LabelStyle.class));
        add(slider).expandX().fillX().padRight(8);
        add(label).padRight(3);
        initListener();
    }

    private void initListener() {
        slider.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                lastValue = slider.getValue();
                getStage().setKeyboardFocus(slider);
                isFocus = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isFocus = false;
                Command.doCmd(new SetValueCommand(objId, fieldName, methodName, lastValue, slider.getValue()));
            }
        });
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                label.setText(Float.toString(slider.getValue()));
            }
        });
    }

    @Override
    public void updateValue(Object value) {
        if (isFocus) return;
        slider.setValue((float) value);
    }
}
