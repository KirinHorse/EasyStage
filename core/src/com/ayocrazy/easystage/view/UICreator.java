package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.bean.UserBean;
import com.ayocrazy.easystage.uimeta.MetaCheckBox;
import com.ayocrazy.easystage.uimeta.MetaConvertor;
import com.ayocrazy.easystage.uimeta.MetaSelectBox;
import com.ayocrazy.easystage.uimeta.MetaSlider;
import com.ayocrazy.easystage.uimeta.MetaTable;
import com.ayocrazy.easystage.uimeta.MetaText;
import com.ayocrazy.easystage.uimeta.MetaVector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;
import net.mwplay.nativefont.NativeTextField;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * Created by ayo on 2017/1/11.
 */

public class UICreator extends Table {
    private static TextField.TextFieldFilter floatFilter, intFilter;
    private HashMap<String, Actor> widgets = new HashMap();
    private Class<? extends Serializable> claz;
    private UICreator userCreator;

    public UICreator(Skin skin) {
        super(skin);
        setBackground(skin.getDrawable("default-rect"));
//        setDebugAll(true);
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
                        text(f.getName(), (MetaText) anno);
                    } else if (anno instanceof MetaVector) {
                        vector(f.getName(), (MetaVector) anno);
                    } else if (anno instanceof MetaSlider) {
                        slider(f.getName(), (MetaSlider) anno);
                    } else if (anno instanceof MetaSelectBox) {
                        selectBox(f.getName(), (MetaSelectBox) anno);
                    } else if (anno instanceof MetaCheckBox) {
                        checkBox(f.getName(), (MetaCheckBox) anno);
                    } else if (anno instanceof MetaTable) {
                        table(f.getName(), (Class<? extends Serializable>) f.getType());
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
        setSize(getPrefWidth(), getPrefHeight());
    }

    private void text(String name, MetaText meta) {
        addName(name);
        NativeTextField tf = new NativeTextField("", getSkin().get(TextField.TextFieldStyle.class));
        tf.setDisabled(!meta.editable());
        tf.setTextFieldFilter(getFilter(meta.filter()));
        tf.setMaxLength(meta.maxLength());
        tf.setAlignment(Align.center);
        tf.setProgrammaticChangeEvents(false);
        widgets.put(name, tf);
        add(tf).left().pad(2, 0, 2, 0).row();
    }

    private void vector(String name, MetaVector meta) {
        addName(name);
        Table table = new Table(getSkin());
        int size = meta.size();
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
            tf.setAlignment(Align.center);
            widgets.put(name + "@" + i, tf);
            table.add(tf).width(80).padRight(5);
        }
        widgets.put(name, table);
        add(table).pad(2, 0, 2, 0).left().row();
    }

    private void slider(String name, MetaSlider meta) {
        addName(name);
        NativeLabel label = new NativeLabel("", getSkin().get(Label.LabelStyle.class));
        Slider slider = new Slider(meta.minValue(), meta.maxValue(), meta.step(), false, getSkin());
        slider.setUserObject(label);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                NativeLabel lab = (NativeLabel) actor.getUserObject();
                lab.setText(((Slider) actor).getValue() + "");
            }
        });
        widgets.put(name, slider);
        Table tab = new Table(getSkin());
        tab.add(slider).expand();
        tab.add(label).pad(0, 5, 0, 2);
        add(tab).pad(2, 0, 2, 0).left().row();
    }

    private void selectBox(String name, MetaSelectBox meta) {
        addName(name);
        SelectBox<String> sb = new SelectBox<String>(getSkin());
        Array<String> items = new Array<String>();
        if (meta.items().length > 0) {
            for (String item : meta.items()) {
                items.add(item);
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
        widgets.put(name, sb);
        add(sb).pad(2, 0, 2, 0).left().row();
    }

    private void checkBox(String name, MetaCheckBox meta) {
        CheckBox cb = new CheckBox("", getSkin());
        ((NativeFont) cb.getStyle().font).appendText(name);
        cb.setText(name);
        cb.getCells().get(0).padRight(3);
        widgets.put(name, cb);
        add(cb).pad(2, 0, 2, 0).left().colspan(2).row();
    }

    private void table(String name, Class<? extends Serializable> claz) {
        addName(name).center();
        UICreator creator = new UICreator(getSkin());
        creator.create(claz);
        widgets.put(name, creator);
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
                setValue(f.getName(), f.get(bean));
            } catch (Exception e) {
                e.printStackTrace();
                EasyLog.log(EasyLog.Tag.warn, "did not find the widget for " + f.getName());
            }
        }
    }

    private void setValue(String name, Object value) {
        Actor widget = widgets.get(name);
        if (widget == null) return;
        if (widget instanceof TextField) {
            ((TextField) widget).setText(value.toString());
        } else if (widget instanceof Slider) {
            ((Slider) widget).setValue((Float) value);
        } else if (widget instanceof CheckBox) {
            ((CheckBox) widget).setChecked((Boolean) value);
        } else if (widget instanceof SelectBox) {
            ((SelectBox) widget).setSelected(value);
        } else if (widget instanceof UICreator) {
            ((UICreator) widget).setBean((Serializable) value);
        } else if (widget instanceof Table) {
            int i = 0;
            while (true) {
                try {
                    TextField tf = (TextField) widgets.get(name + "@" + i);
                    tf.setText(java.lang.reflect.Array.get(value, i++).toString());
                } catch (Exception e) {
                    break;
                }
            }
        }
    }

    public void createUserUI(UserBean bean) {
        String[] names = bean.getFieldNames();
        if (names == null || names.length < 1) return;
        String[] metas = bean.getMetas();
        Object[] values = bean.getValues();
        addName("user").center();
        userCreator = new UICreator(getSkin());
        for (int i = 0; i < names.length; i++) {
            Annotation anno = MetaConvertor.getMeta(metas[i]);
            if (anno instanceof MetaText) {
                userCreator.text(names[i], (MetaText) anno);
            } else if (anno instanceof MetaVector) {
                userCreator.vector(names[i], (MetaVector) anno);
            } else if (anno instanceof MetaSlider) {
                userCreator.slider(names[i], (MetaSlider) anno);
            } else if (anno instanceof MetaSelectBox) {
                userCreator.selectBox(names[i], (MetaSelectBox) anno);
            } else if (anno instanceof MetaCheckBox) {
                userCreator.checkBox(names[i], (MetaCheckBox) anno);
            } else if (anno instanceof MetaTable) {
                userCreator.table(names[i], (Class<? extends Serializable>) values[i].getClass());
            } else {
                continue;
            }
        }
        add(userCreator).pad(2, 0, 2, 0).left().row();
    }

    public void setUserBean(UserBean bean) {
        if (userCreator == null) createUserUI(bean);
        if (userCreator == null) return;
        String[] names = bean.getFieldNames();
        Object[] values = bean.getValues();
        for (int i = 0; i < names.length; i++) {
            userCreator.setValue(names[i], values[i]);
        }
    }
}
