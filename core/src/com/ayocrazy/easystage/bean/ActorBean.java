package com.ayocrazy.easystage.bean;

import java.io.Serializable;

/**
 * Created by ayo on 2017/1/11.
 */

public class ActorBean implements Serializable {
    private int childrenSize;
    private String name, id, parentName;
    private ColorBean color;
    private boolean visible, debug;
    private TransformBean transform;
}
