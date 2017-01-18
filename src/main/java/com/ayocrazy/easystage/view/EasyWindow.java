package com.ayocrazy.easystage.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglCursor;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import org.lwjgl.input.Mouse;

/**
 * Created by 26286 on 2017/1/17.
 */

public class EasyWindow extends Window {
    private ScrollPane scrollPane;
    private static Cursor topDown, leftRight, upleftrightdown, leftdownupright, all;

    public EasyWindow(String title, Skin skin) {
        super(title, skin);
        scrollPane = new ScrollPane(null, skin);
        scrollPane.setupOverscroll(20, 20, 80);
        scrollPane.setFlickScroll(false);
        add(scrollPane).pad(1).expand().fill();
        setResizable(true);
        setResizeBorder(5);
        initListener();
        if (topDown == null) {
            createCursor();
        }
    }

    int index = 0;

    private void initListener() {
        addListener(new InputListener() {
            private Cursor current;

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                if (y >= getHeight() - getTitleTable().getHeight()) {
                    if (current != all)
                        Gdx.graphics.setCursor(current = all);
                } else if ((x <= 6 && x > 1) || x >= getWidth() - 5) {
                    if (current != leftRight)
                        Gdx.graphics.setCursor(current = leftRight);
                } else if (y <= 5 && y > 0) {
                    if (current != topDown)
                        Gdx.graphics.setCursor(current = topDown);
                } else {
                    if (current != null) {
                        current = null;
                        try {
                            Mouse.setNativeCursor(null);
                        } catch (Exception e) {
                        }
                    }
                }
                return true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (current != null) {
                    current = null;
                    Gdx.graphics.setSystemCursor(null);
                }
            }
        });
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    private void createCursor() {
        Pixmap lr = new Pixmap(Gdx.files.internal("skin/cursor.png"));
        int width = lr.getWidth(), height = lr.getWidth();
        Pixmap td = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (int i = 0; i <= width; i++) {
            for (int j = 0; j <= height; j++) {
                td.drawPixel(i, j, lr.getPixel(j, i));
            }
        }
        Pixmap al = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        al.drawPixmap(lr, 0, 0);
        al.drawPixmap(td, 0, 0);
        leftRight = Gdx.graphics.newCursor(lr, width / 2, height / 2);
        topDown = Gdx.graphics.newCursor(td, width / 2, height / 2);
        all = Gdx.graphics.newCursor(al, width / 2, height / 2);
    }
}
