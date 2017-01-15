package com.ayocrazy.easystage.ui;

import com.ayocrazy.easystage.bean.BaseBean;
import com.ayocrazy.easystage.bean.UserBean;
import com.ayocrazy.easystage.uimeta.MetaCheckBox;
import com.ayocrazy.easystage.uimeta.MetaConvertor;
import com.ayocrazy.easystage.uimeta.MetaMethod;
import com.ayocrazy.easystage.uimeta.MetaSelectBox;
import com.ayocrazy.easystage.uimeta.MetaSlider;
import com.ayocrazy.easystage.uimeta.MetaTable;
import com.ayocrazy.easystage.uimeta.MetaText;
import com.ayocrazy.easystage.uimeta.MetaVector;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import net.mwplay.nativefont.NativeLabel;

import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by 26286 on 2017/1/14.
 */

public class UICreator extends Table {
    private BaseBean bean;
    private Class beanClass;
    private HashMap<String, EasyUI> widgets = new HashMap<>();
    private HashMap<String, UICreator> tables = new HashMap<>();
    private Array<Field> updateFields = new Array<>();

    public UICreator(Skin skin) {
        super(skin);
        setBackground(skin.getDrawable("default-rect"));
    }

    public void create(BaseBean bean) {
        this.bean = bean;
        createUser(bean.getUser());
        beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                MetaMethod metaMethod = field.getAnnotation(MetaMethod.class);
                Annotation[] annotations = field.getDeclaredAnnotations();
                for (Annotation anno : annotations) {
                    if (anno instanceof MetaMethod) continue;
                    else if (anno instanceof MetaTable) {
                        field.setAccessible(true);
                        table(field.getName(), (BaseBean) field.get(bean));
                        updateFields.add(field);
                        break;
                    } else {
                        if (addWidget(field.getName(), anno, metaMethod)) {
                            field.setAccessible(true);
                            updateFields.add(field);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void update(BaseBean bean) {
        if (bean.getClass() != beanClass) {
            reset();
            create(bean);
        }
        this.bean = bean;
        updateUser();
        for (Field field : updateFields) {
            try {
                Object value = field.get(bean);
                if (value instanceof BaseBean) {
                    tables.get(field.getName()).update((BaseBean) value);
                } else
                    widgets.get(field.getName()).updateValue(value);
            } catch (Exception e) {
            }
        }
    }

    public void reset() {
        clear();
        getCells().clear();
        updateFields.clear();
    }

    private void createUser(UserBean userbean) {
        if (userbean == null) return;
        String[] names = userbean.getFieldNames();
        if (names == null || names.length < 1) return;
        String[] metas = userbean.getMetas();
        String[] methods = userbean.getMethodNames();
        for (int i = 0; i < names.length; i++) {
            try {
                Annotation meta = MetaConvertor.getMeta(metas[i]);
                if (meta instanceof MetaTable) {
                    table(names[i], (BaseBean) userbean.getValues()[i]);
                } else {
                    addWidget(names[i], meta, (MetaMethod) MetaConvertor.getMeta(methods[i]));
                }
            } catch (Exception e) {
                e.printStackTrace(new PrintStream(System.err));
            }
        }
        separator();
    }

    private void updateUser() {
        UserBean userBean = bean.getUser();
        if (userBean == null) return;
        String[] names = userBean.getFieldNames();
        if (names.length < 1) return;
        Object[] values = userBean.getValues();
        for (int i = 0; i < names.length; i++) {
            try {
                if (values[i] instanceof BaseBean) {
                    tables.get(names[i]).update((BaseBean) values[i]);
                } else
                    widgets.get(names[i]).updateValue(values[i]);
            } catch (Exception e) {
            }
        }
    }

    private boolean addWidget(String name, Annotation meta, MetaMethod metaMethod) {
        addName(name);
        Actor widget;
        if (meta instanceof MetaText) {
            widget = new EasyTextField(getSkin(), bean.getId(), name, (MetaText) meta, metaMethod);
        } else if (meta instanceof MetaVector) {
            widget = new EasyVector(getSkin(), bean.getId(), name, (MetaVector) meta, metaMethod);
        } else if (meta instanceof MetaSlider) {
            widget = new EasySlider(getSkin(), bean.getId(), name, (MetaSlider) meta, metaMethod);
        } else if (meta instanceof MetaSelectBox) {
            widget = new EasySelectBox(getSkin(), bean.getId(), name, (MetaSelectBox) meta, metaMethod);
        } else if (meta instanceof MetaCheckBox) {
            widget = new EasyCheckBox(getSkin(), bean.getId(), name, (MetaCheckBox) meta, metaMethod);
        } else return false;
        add(widget).left().pad(2, 0, 2, 2).row();
        widgets.put(name, (EasyUI) widget);
        return true;
    }

    private Cell addName(String name) {
        NativeLabel lab = new NativeLabel(name, getSkin().get(Label.LabelStyle.class));
        return add(lab).right().pad(2, 2, 2, 10).fillY();
    }

    private void table(String name, BaseBean tableBean) {
        addName(name).pad(0).center();
        UICreator creator = new UICreator(getSkin());
        creator.create(tableBean);
        add(creator).left().pad(2, 0, 2, 2).row();
        tables.put(name, creator);
    }

    private void separator() {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGB888);
        p.setColor(Color.WHITE);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        Image img = new Image(t);
        img.setColor(Color.GRAY);
        img.setHeight(10);
        add(img).pad(5, 5, 5, 5).colspan(2).fillX().row();
    }
}
