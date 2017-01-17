package com.ayocrazy.easystage.ui;

import com.ayocrazy.easystage.command.Command;
import com.ayocrazy.easystage.command.SetValueCommand;
import com.ayocrazy.easystage.uimeta.MetaMethod;
import com.ayocrazy.easystage.uimeta.MetaText;
import com.ayocrazy.easystage.uimeta.MetaVector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;

import net.mwplay.nativefont.NativeLabel;
import net.mwplay.nativefont.NativeTextField;

import java.lang.reflect.Array;

/**
 * Created by 26286 on 2017/1/14.
 */

public class EasyVector extends Table implements EasyUI {
    private int objId;
    private String fieldName, methodName;
    private boolean isFocus, inputError;
    private Object[] values, lastValues;
    private NativeLabel[] labs;
    private NativeTextField[] tfs;
    private MetaText.Filter filter;

    public EasyVector(Skin skin, int objId, String fieldName, MetaVector metaVector, MetaMethod metaMethod) {
        super(skin);
        this.objId = objId;
        this.fieldName = fieldName;
        if (metaMethod != null) methodName = metaMethod.name();
        this.filter = metaVector.filter();
        int size = metaVector.size();
        char[] prefix = metaVector.prefix();
        values = new Object[size];
        lastValues = new Object[size];
        labs = new NativeLabel[size];
        tfs = new NativeTextField[size];
        initListener();
        for (int i = 0; i < size; i++) {
            String name = i < prefix.length ? Character.toString(prefix[i]) : Integer.toString(i);
            labs[i] = new NativeLabel(name, skin.get(Label.LabelStyle.class));
            tfs[i] = new NativeTextField("", skin.get(TextField.TextFieldStyle.class));
            tfs[i].setMaxLength(metaVector.maxLength());
            if (metaVector.editable()) {
                tfs[i].setTextFieldFilter(EasyTextField.getFilter(filter));
                tfs[i].addListener(focusListener);
                tfs[i].addListener(changeListener);
                tfs[i].addListener(enterListener);
            } else {
                tfs[i].setDisabled(true);
            }
            tfs[i].setAlignment(Align.center);
            add(labs[i]).padRight(2);
            add(tfs[i]).width(70).padRight(8);
        }
    }

    private Object[] getValues() {
        int size = tfs.length;
        for (int i = 0; i < size; i++) {
            values[i] = tfs[i].getText();
        }
        return values;
    }

    private FocusListener focusListener;
    private ChangeListener changeListener;
    private InputListener enterListener;

    private void initListener() {
        focusListener = new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, final Actor actor, boolean focused) {
                isFocus = focused;
                if (!isFocus) {
                    if (inputError) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                getStage().setKeyboardFocus(actor);
                            }
                        });
                        return;
                    }
                    Command.doCmd(new SetValueCommand(objId, fieldName, methodName, lastValues, getValues()));
                } else if (!inputError) {
                    lastValues = getValues();
                }
            }
        };
        changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isFocus) return;
                NativeTextField tf = (NativeTextField) actor;
                boolean valid = EasyTextField.checkInput(filter, tf.getText());
                if (valid) {
                    if (inputError) tf.setColor(Color.WHITE);
                } else {
                    if (!inputError) tf.setColor(Color.RED);
                }
                inputError = !valid;
            }
        };
        enterListener = new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.CENTER || keycode == Input.Keys.ENTER) {
                    getStage().setKeyboardFocus(null);
                }
                return true;
            }
        };
    }

    @Override
    public void updateValue(Object value) {
        if (isFocus) return;
        if (value.getClass().isArray()) {
            for (int i = 0; i < tfs.length; i++) {
                try {
                    tfs[i].setText(Array.get(value, i).toString());
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void setId(int id) {
        objId = id;
    }
}
