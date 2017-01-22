package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.junit.LibgdxRunner;
import com.ayocrazy.easystage.junit.NeedGL;
import com.ayocrazy.easystage.util.SkinHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
@NeedGL
public class EasyLogTest {
    private EasyLog easyLog;
    private Color errorColor;

    @Before
    public void setUp() throws Exception {
        this.easyLog = new EasyLog("easyLog", SkinHelper.prepareSkin());
        errorColor = (Color) Whitebox.getInternalState(EasyLog.Tag.error, "color");
    }

    @Test
    public void shouldEnableToShowErrorMessage() throws Exception {
        EasyLog.log(EasyLog.Tag.error, "Error message");

        VerticalGroup verticalGroup = (VerticalGroup) easyLog.getScrollPane().getChildren().get(0);
        assertThat(verticalGroup.getChildren().size, is(1));
        Label label = (Label) verticalGroup.getChildren().get(0);
        assertThat(label.getText().toString(), containsString("Error message"));
        assertThat(label.getColor(), is(errorColor));
    }

    @Test
    public void shouldRemovePreviousMessageIfNewMessageIsTooLong() throws Exception {
        VerticalGroup verticalGroup = (VerticalGroup) easyLog.getScrollPane().getChildren().get(0);
        for (int i = 0; i < 50; i++) {
            EasyLog.log(EasyLog.Tag.info, "First message");
        }
        assertThat(verticalGroup.getChildren().size, is(50));
        EasyLog.log(EasyLog.Tag.info, "Second message");
        assertThat(verticalGroup.getChildren().size, is(51));
        EasyLog.log(EasyLog.Tag.info, "Last message");
        assertThat(verticalGroup.getChildren().size, is(51));
    }
}