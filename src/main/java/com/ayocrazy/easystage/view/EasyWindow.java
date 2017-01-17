package com.ayocrazy.easystage.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Created by 26286 on 2017/1/17.
 */

public class EasyWindow extends Window {
    private ScrollPane scrollPane;

    public EasyWindow(String title, Skin skin) {
        super(title, skin);
        scrollPane = new ScrollPane(null, skin);
        scrollPane.setupOverscroll(20, 20, 80);
        scrollPane.setFlickScroll(false);
        add(scrollPane).pad(1).expand().fill();
        setResizable(true);
        setResizeBorder(5);
        initListener();
    }

    private void initListener() {
        addListener(new InputListener() {
            private Cursor.SystemCursor current;

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                if (y >= getHeight() - getTitleTable().getHeight()) {
                    if (current != Cursor.SystemCursor.Arrow)
                        Gdx.graphics.setSystemCursor(current = Cursor.SystemCursor.Arrow);
                } else if ((x <= 6 && x > 1) || x >= getWidth() - 5) {
                    if (current != Cursor.SystemCursor.HorizontalResize)
                        Gdx.graphics.setSystemCursor(current = Cursor.SystemCursor.HorizontalResize);
                } else if (y <= 5 && y > 0) {
                    if (current != Cursor.SystemCursor.VerticalResize)
                        Gdx.graphics.setSystemCursor(current = Cursor.SystemCursor.VerticalResize);
                } else {
                    if (current != Cursor.SystemCursor.Arrow)
                        Gdx.graphics.setSystemCursor(current = Cursor.SystemCursor.Arrow);
                }
                return true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (current != Cursor.SystemCursor.Arrow)
                    Gdx.graphics.setSystemCursor(current = Cursor.SystemCursor.Arrow);
            }
        });
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }
}
