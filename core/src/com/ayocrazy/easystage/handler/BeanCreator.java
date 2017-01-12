package com.ayocrazy.easystage.handler;

import com.ayocrazy.easystage.bean.UserBean;

import net.sf.cglib.beans.BeanGenerator;

/**
 * Created by ayo on 2017/1/12.
 */

public class BeanCreator {
    public BeanCreator() {
        BeanGenerator generator = new BeanGenerator();
        generator.addProperty("speed", float.class);
    }
}
