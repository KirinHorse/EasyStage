package com.ayocrazy.easystage.ui;

import com.ayocrazy.easystage.bean.BaseBean;
import com.ayocrazy.easystage.junit.LibgdxRunner;
import com.ayocrazy.easystage.junit.NeedGL;
import com.ayocrazy.easystage.uimeta.MetaCheckBox;
import com.ayocrazy.easystage.uimeta.MetaText;
import com.ayocrazy.easystage.uimeta.MetaVector;
import com.ayocrazy.easystage.util.SkinHelper;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
@NeedGL
public class UICreatorTest {
    private UICreator uiCreator;
    private HashMap<String, EasyUI> widgets;

    @Before
    public void setUp() throws Exception {
        Skin skin = SkinHelper.prepareSkin();
        uiCreator = new UICreator(skin);
        widgets = (HashMap<String, EasyUI>) Whitebox.getInternalState(uiCreator, "widgets");
    }

    @Test
    public void shouldCreateText() throws Exception {
        uiCreator.create(new MetaTextBean());
        assertThat(widgets.containsKey("text"), is(true));
        assertThat(widgets.get("text"), instanceOf(EasyTextField.class));
    }

    @Test
    public void shouldCreateCheckBox() throws Exception {
        uiCreator.create(new MetaCheckBoxBean());
        assertThat(widgets.containsKey("trueOrFalse"), is(true));
        assertThat(widgets.get("trueOrFalse"), instanceOf(EasyCheckBox.class));
    }

    @Test
    public void shouldCheckVector() throws Exception {
        uiCreator.create(new MetaVectorBean());
        assertThat(widgets.containsKey("vectors"), is(true));
        assertThat(widgets.get("vectors"), instanceOf(EasyVector.class));
    }

    class MetaTextBean extends BaseBean {
        @MetaText
        private String text;
    }

    class MetaCheckBoxBean extends BaseBean {
        @MetaCheckBox
        private Boolean trueOrFalse;
    }

    class MetaVectorBean extends BaseBean{
        @MetaVector
        private float[] vectors;
    }

}