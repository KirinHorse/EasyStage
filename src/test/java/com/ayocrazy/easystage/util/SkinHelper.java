package com.ayocrazy.easystage.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeFontPaint;

public class SkinHelper {
    public static Skin prepareSkin() {
        Skin skin = new Skin(Gdx.files.internal("skin/skin.json"));
        NativeFont font = new NativeFont(new NativeFontPaint(15));
        skin.add("default", font, BitmapFont.class);
        skin.get(TextField.TextFieldStyle.class).font = font;
        skin.get(TextButton.TextButtonStyle.class).font = font;
        skin.get(Label.LabelStyle.class).font = font;
        skin.get(SelectBox.SelectBoxStyle.class).font = font;
        skin.get(CheckBox.CheckBoxStyle.class).font = font;
        return skin;
    }
}
