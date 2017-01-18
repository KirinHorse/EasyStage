package com.ayocrazy.easystage.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import net.mwplay.nativefont.NativeLabel;

import java.util.Calendar;

/**
 * Created by ayo on 2017/1/11.
 */

public class EasyLog extends EasyWindow {
    private int maxLines = 50;
    private static EasyLog instance;
    private VerticalGroup vg;

    public enum Tag {
        warn(new Color(0xccbb22ff)), error(new Color(0xee2222ff)), info(new Color(0xccccccff));
        private Color color;

        Tag(Color c) {
            this.color = c;
        }
    }

    public EasyLog(String title, Skin skin) {
        super(title, skin);
        vg = new VerticalGroup();
        vg.pad(2, 2, 20, 20);
        vg.align(Align.topLeft);
        vg.columnLeft();
        getScrollPane().setWidget(vg);
        instance = this;
    }

    public static void log(Tag tag, String msg) {
        if (instance == null) {
            System.err.println("EasyStage " + tag.name() + ":" + msg);
        }
        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        int s = c.get(Calendar.SECOND);
        String time = h + ":" + m + ":" + s + "  ";
        if (instance.vg.getChildren().size > instance.maxLines) {
            instance.vg.getChildren().first().remove();
        }
        NativeLabel lab = new NativeLabel(time + tag.name() + ": " + msg, instance.getSkin().get(Label.LabelStyle.class));
        lab.setColor(tag.color);
        instance.vg.addActor(lab);
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                instance.getScrollPane().setScrollPercentY(1f);
            }
        });
    }
}
