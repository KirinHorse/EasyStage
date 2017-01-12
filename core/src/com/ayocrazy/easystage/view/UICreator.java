package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.uimeta.MetaCheckBox;
import com.ayocrazy.easystage.uimeta.MetaSelectBox;
import com.ayocrazy.easystage.uimeta.MetaSlider;
import com.ayocrazy.easystage.uimeta.MetaTable;
import com.ayocrazy.easystage.uimeta.MetaText;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import net.mwplay.nativefont.NativeLabel;
import net.mwplay.nativefont.NativeTextField;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayo on 2017/1/11.
 */

public class UICreator extends Table {
    private static TextField.TextFieldFilter floatFilter, intFilter;
    private static final int lineHeight = 25;

    public UICreator(Skin skin) {
        super(skin);
        setBackground(skin.getDrawable("default-rect"));
//        setDebug(true);
    }

    public void create(Serializable bean) {
        Class claz = bean.getClass();
        try {
            Field[] fields = claz.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                Object obj = f.get(bean);
                if (obj == null) obj = "";
                Annotation[] annotations = f.getDeclaredAnnotations();
                for (Annotation anno : annotations) {
                    if (anno instanceof MetaText) {
                        if (f.getType().isArray()) {
                            if (obj == null) obj = new float[2];
                            vector(f, obj);
                        } else {
                            text(f, obj.toString());
                        }
                    } else if (anno instanceof MetaSlider) {
                        slider(f, (Float) obj);
                    } else if (anno instanceof MetaSelectBox) {
                        selectBox(f, obj.toString());
                    } else if (anno instanceof MetaCheckBox) {
                        checkBox(f, (Boolean) obj);
                    } else if (anno instanceof MetaTable) {
                        table(f, (Serializable) obj);
                    } else {
                        continue;
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MainStage.get().log.log(e.getMessage(), e.getLocalizedMessage());
        }
        setSize(getPrefWidth(), getPrefHeight());
    }

    private void text(Field field, String value) {
        addName(field.getName());
        MetaText meta = field.getAnnotation(MetaText.class);
        NativeTextField tf = new NativeTextField(value, getSkin().get(TextField.TextFieldStyle.class));
        tf.setDisabled(!meta.editable());
        tf.setTextFieldFilter(getFilter(meta.filter()));
        tf.setMaxLength(meta.maxLength());
        tf.setProgrammaticChangeEvents(false);
        add(tf).height(lineHeight).left().row();
    }

    private void vector(Field field, Object valueArray) {
        List list = new ArrayList();
        int index = 0;
        while (true) {
            try {
                list.add(java.lang.reflect.Array.get(valueArray, index++));
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
        addName(field.getName());
        MetaText meta = field.getAnnotation(MetaText.class);
        Table table = new Table(getSkin());
        char[] prefix = meta.prefix();
        boolean useNum = prefix.length < list.size();
        for (int i = 0; i < list.size(); i++) {
            NativeLabel lab = new NativeLabel(useNum ? (i + 1 + "") : (prefix[i] + ""),
                    getSkin().get(Label.LabelStyle.class));
            table.add(lab).height(lineHeight);
            NativeTextField tf = new NativeTextField(list.get(i) + "", getSkin().get(TextField.TextFieldStyle.class));
            tf.setTextFieldFilter(getFilter(meta.filter()));
            tf.setProgrammaticChangeEvents(false);
            tf.setMaxLength(meta.maxLength());
            table.add(tf).width(80).height(lineHeight).padRight(5);
        }
        add(table).height(lineHeight).left().row();
    }

    private void slider(Field field, float value) {
        addName(field.getName());
        MetaSlider meta = field.getAnnotation(MetaSlider.class);
        Slider slider = new Slider(meta.minValue(), meta.maxValue(), meta.step(), false, getSkin());
        add(slider).height(lineHeight).left().row();
    }

    private void selectBox(Field field, String value) {
        addName(field.getName());
        SelectBox<String> sb = new SelectBox<String>(getSkin());
        MetaSelectBox meta = field.getAnnotation(MetaSelectBox.class);
        Array<String> items = new Array<String>();
        if (meta.items().length > 0) {
            items.addAll(meta.items());
        } else if (meta.enumClass().isEnum()) {
            Object[] enums = meta.enumClass().getEnumConstants();
            for (Object obj : enums) {
                items.add(obj.toString());
            }
        }
        sb.setItems(items);
        sb.setSelected(value);
        add(sb).height(lineHeight).left().row();
    }

    private void checkBox(Field field, boolean value) {
        CheckBox cb = new CheckBox(field.getName(), getSkin());
        cb.setChecked(value);
        add(cb).height(lineHeight).left().colspan(2).row();
    }

    private void table(Field field, Serializable value) {
        addName(field.getName()).fillY().expandY().center();
        UICreator table = new UICreator(getSkin());
        table.create(value);
        add(table).left().row();
    }


    private Cell addName(String name) {
        NativeLabel lab = new NativeLabel(name, getSkin().get(Label.LabelStyle.class));
        lab.setAlignment(Align.center, Align.center);
        return add(lab).right().height(lineHeight).padRight(10);
    }

    private static TextField.TextFieldFilter getFilter(MetaText.Filter filter) {
        switch (filter) {
            case INT:
                if (intFilter == null) {
                    intFilter = new TextField.TextFieldFilter.DigitsOnlyFilter();
                }
                return intFilter;
            case FLOAT:
                if (floatFilter == null) {
                    floatFilter = new TextField.TextFieldFilter() {
                        @Override
                        public boolean acceptChar(TextField textField, char c) {
                            System.out.println(c + "");
                            if (Character.isDigit(c)) return true;
                            if (c == '.' || c == '-') return true;
                            return false;
                        }
                    };
                }
                return floatFilter;
            default:
                return null;
        }
    }
}
