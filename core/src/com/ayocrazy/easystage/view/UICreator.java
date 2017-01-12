package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.uimeta.MetaCheckBox;
import com.ayocrazy.easystage.uimeta.MetaSelectBox;
import com.ayocrazy.easystage.uimeta.MetaSlider;
import com.ayocrazy.easystage.uimeta.MetaTable;
import com.ayocrazy.easystage.uimeta.MetaText;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;
import net.mwplay.nativefont.NativeTextField;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ayo on 2017/1/11.
 */

public class UICreator extends Table {
    private static TextField.TextFieldFilter floatFilter, intFilter;
    private HashMap<String, Actor> widgets = new HashMap<>();
    private Class<? extends Serializable> claz;

    public UICreator(Skin skin) {
        super(skin);
        setBackground(skin.getDrawable("default-rect"));
//        setDebug(true);
    }

    public void create(Class<? extends Serializable> claz) {
        this.claz = claz;
        Field[] fields = claz.getDeclaredFields();
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                Annotation[] annotations = f.getDeclaredAnnotations();
                for (Annotation anno : annotations) {
                    if (anno instanceof MetaText) {
                        if (f.getType().isArray()) {
                            vector(f);
                        } else {
                            text(f);
                        }
                    } else if (anno instanceof MetaSlider) {
                        slider(f);
                    } else if (anno instanceof MetaSelectBox) {
                        selectBox(f);
                    } else if (anno instanceof MetaCheckBox) {
                        checkBox(f);
                    } else if (anno instanceof MetaTable) {
                        table(f);
                    } else {
                        continue;
                    }
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                EasyLog.log(EasyLog.Tag.error, e.toString());
            }
        }
        System.out.println(widgets.size());
        setSize(getPrefWidth(), getPrefHeight());
    }

    private void text(Field field) {
        addName(field.getName());
        MetaText meta = field.getAnnotation(MetaText.class);
        NativeTextField tf = new NativeTextField("", getSkin().get(TextField.TextFieldStyle.class));
        tf.setDisabled(!meta.editable());
        tf.setTextFieldFilter(getFilter(meta.filter()));
        tf.setMaxLength(meta.maxLength());
        tf.setProgrammaticChangeEvents(false);
        widgets.put(field.getName(), tf);
        add(tf).left().pad(2, 0, 2, 0).row();
    }

    private void vector(Field field) {
        addName(field.getName());
        MetaText meta = field.getAnnotation(MetaText.class);
        Table table = new Table(getSkin());
        int size = meta.arraySize();
        char[] prefix = meta.prefix();
        boolean useNum = prefix.length < size;
        for (int i = 0; i < size; i++) {
            NativeLabel lab = new NativeLabel(useNum ? (i + 1 + "") : (prefix[i] + ""),
                    getSkin().get(Label.LabelStyle.class));
            table.add(lab);
            NativeTextField tf = new NativeTextField("", getSkin().get(TextField.TextFieldStyle.class));
            tf.setTextFieldFilter(getFilter(meta.filter()));
            tf.setProgrammaticChangeEvents(false);
            tf.setMaxLength(meta.maxLength());
            widgets.put(field.getName() + "@" + i, tf);
            table.add(tf).width(80).padRight(5);
        }
        widgets.put(field.getName(), table);
        add(table).pad(2, 0, 2, 0).left().row();
    }

    private void slider(Field field) {
        addName(field.getName());
        MetaSlider meta = field.getAnnotation(MetaSlider.class);
        Slider slider = new Slider(meta.minValue(), meta.maxValue(), meta.step(), false, getSkin());
        widgets.put(field.getName(), slider);
        add(slider).pad(2, 0, 2, 0).left().row();
    }

    private void selectBox(Field field) {
        addName(field.getName());
        SelectBox<String> sb = new SelectBox<String>(getSkin());
        MetaSelectBox meta = field.getAnnotation(MetaSelectBox.class);
        Array<String> items = new Array<String>();
        if (meta.items().length > 0) {
            items.addAll(meta.items());
            for (String item : meta.items()) {
                ((NativeFont) sb.getStyle().font).appendText(item);
            }
        } else if (meta.enumClass().isEnum()) {
            Object[] enums = meta.enumClass().getEnumConstants();
            for (Object obj : enums) {
                String item = obj.toString();
                items.add(item);
                ((NativeFont) sb.getStyle().font).appendText(item);
            }
        }
        sb.setItems(items);
        widgets.put(field.getName(), sb);
        add(sb).pad(2, 0, 2, 0).left().row();
    }

    private void checkBox(Field field) {
        CheckBox cb = new CheckBox("", getSkin());
        ((NativeFont) cb.getStyle().font).appendText(field.getName());
        cb.setText(field.getName());
        widgets.put(field.getName(), cb);
        add(cb).pad(2, 0, 2, 0).left().colspan(2).row();
    }

    private void table(Field field) {
        addName(field.getName()).center();
        UICreator creator = new UICreator(getSkin());
        creator.create((Class<? extends Serializable>) field.getType());
        widgets.put(field.getName(), creator);
        add(creator).pad(2, 0, 2, 0).left().row();
    }


    private Cell addName(String name) {
        NativeLabel lab = new NativeLabel(name, getSkin().get(Label.LabelStyle.class));
        return add(lab).right().padRight(10);
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

    public void setBean(Serializable bean) {
        if (bean.getClass() != claz) return;
        Field fields[] = claz.getDeclaredFields();
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                setValue(f, f.get(bean));
            } catch (Exception e) {
                e.printStackTrace();
                EasyLog.log(EasyLog.Tag.warn, "did not find the widget for " + f.getName());
            }
        }
    }

    private void setValue(Field field, Object value) {
        Actor widget = widgets.get(field.getName());
        if (widget == null) return;
        if (widget instanceof TextField) {
            ((TextField) widget).setText(value.toString());
        } else if (widget instanceof Slider) {
            ((Slider) widget).setValue((float) value);
        } else if (widget instanceof CheckBox) {
            ((CheckBox) widget).setChecked((boolean) value);
        } else if (widget instanceof SelectBox) {
            ((SelectBox) widget).setSelected(value);
        } else if (widget instanceof UICreator) {
            ((UICreator) widget).setBean((Serializable) value);
        } else if (widget instanceof Table) {
            MetaText meta = field.getAnnotation(MetaText.class);
            for (int i = 0; i < meta.arraySize(); i++) {
                TextField tf = (TextField) widgets.get(field.getName() + "@" + i);
                tf.setText(java.lang.reflect.Array.get(value, i).toString());
            }
        }
    }
}
