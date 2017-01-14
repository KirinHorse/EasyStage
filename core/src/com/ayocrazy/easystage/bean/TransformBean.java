package com.ayocrazy.easystage.bean;

import com.ayocrazy.easystage.uimeta.MetaText;
import com.ayocrazy.easystage.uimeta.MetaVector;

import java.io.Serializable;

/**
 * Created by ayo on 2017/1/11.
 */

public class TransformBean extends BaseBean {
    @MetaVector(editable = true)
    private float[] position, size, origion, scale;
    @MetaText(editable = true, filter = MetaText.Filter.FLOAT, maxLength = 10)
    private float rotation;
}
