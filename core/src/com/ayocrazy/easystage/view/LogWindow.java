package com.ayocrazy.easystage.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import net.mwplay.nativefont.NativeLabel;

import java.util.Calendar;

/**
 * Created by ayo on 2017/1/11.
 */

public class LogWindow extends Window {
    private ScrollPane sp;
    private VerticalGroup vg;

    public LogWindow(String title, Skin skin) {
        super(title, skin);
        vg = new VerticalGroup();
        sp = new ScrollPane(vg, skin);
        setResizeBorder(20);
        setResizable(true);
    }

    public void log(String tag, String msg) {
        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        int s = c.get(Calendar.SECOND);
        String time = h + ":" + m + ":" + s + "  ";
        vg.addActor(new NativeLabel(time + tag + ":" + msg, getSkin().get(Label.LabelStyle.class)));
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (sp != null)
            sp.setSize(getWidth(), getHeight());
    }
}
