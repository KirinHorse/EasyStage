package com.ayocrazy.easystage.ui;

import com.ayocrazy.easystage.command.Command;
import com.ayocrazy.easystage.command.SetValueCommand;
import com.ayocrazy.easystage.uimeta.MetaMethod;
import com.ayocrazy.easystage.uimeta.MetaText;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;

import net.mwplay.nativefont.NativeTextField;

import java.util.HashMap;

/**
 * Created by 26286 on 2017/1/14.
 */

public class EasyTextField extends NativeTextField implements EasyUI {
    private static HashMap<MetaText.Filter, TextFieldFilter> filters = new HashMap<>();
    private boolean inputError, isFocus;
    private int objId;
    private String fieldName, methodName, lastValue;
    private MetaText.Filter filter;

    public EasyTextField(Skin skin, int objId, String fieldName, MetaText metaText) {
        this(skin, objId, fieldName, metaText, null);
    }

    public EasyTextField(Skin skin, int objId, String fieldName, MetaText metaText, MetaMethod metaMethod) {
        super("", skin.get(TextFieldStyle.class));
        this.objId = objId;
        this.fieldName = fieldName;
        this.filter = metaText.filter();
        if (metaMethod != null) methodName = metaMethod.name();
        setMaxLength(metaText.maxLength());
        if (metaText.editable()) {
            setTextFieldFilter(filters.get(filter));
            initListener();
        } else {
            setDisabled(true);
        }
        setAlignment(Align.center);
    }

    @Override
    public void updateValue(Object value) {
        if (getStage().getKeyboardFocus() == this) return;
        setText(value.toString());
    }

    private void initListener() {
        addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                isFocus = focused;
                if (!isFocus) {
                    if (inputError) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                getStage().setKeyboardFocus(EasyTextField.this);
                            }
                        });
                        return;
                    } else if (!getText().equals(lastValue))
                        Command.doCmd(new SetValueCommand(objId, fieldName, methodName, lastValue, getText()));
                } else if (!inputError) {
                    lastValue = getText();
                }
            }
        });
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isFocus) return;
                boolean valid = checkInput(filter, getText());
                if (valid) {
                    if (inputError) setColor(Color.WHITE);
                } else {
                    if (!inputError) setColor(Color.RED);
                }
                inputError = !valid;
            }
        });
        setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\r' || c == '\n') {
                    getStage().setKeyboardFocus(null);
                }
            }
        });
    }


    static {
        filters.put(MetaText.Filter.PosINT, new TextFieldFilter.DigitsOnlyFilter());
        filters.put(MetaText.Filter.FLOAT, new TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                if (Character.isDigit(c)) return true;
                if (c == '.') return true;
                if (c == '-') return true;
                return false;
            }
        });
        filters.put(MetaText.Filter.INT, new TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                if (Character.isDigit(c)) return true;
                if (c == '-') return true;
                return false;
            }
        });
        filters.put(MetaText.Filter.PosFLOAT, new TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                if (Character.isDigit(c)) return true;
                if (c == '.') return true;
                return false;
            }
        });
    }

    public static boolean checkInput(MetaText.Filter filter, String text) {
        try {
            if (filter == MetaText.Filter.PosINT) {
                int result = Integer.parseInt(text);
                if (result < 0) return false;
            } else if (filter == MetaText.Filter.INT) {
                Integer.parseInt(text);
            } else if (filter == MetaText.Filter.FLOAT) {
                Float.parseFloat(text);
            } else if (filter == MetaText.Filter.PosFLOAT) {
                float result = Float.parseFloat(text);
                if (result < 0) return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static final TextFieldFilter getFilter(MetaText.Filter filter) {
        return filters.get(filter);
    }
}
