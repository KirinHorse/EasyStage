package com.ayocrazy.easystage.uimeta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ayo on 2017/1/11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MetaText {
    public enum Filter {
        INT, FLOAT, STRING
    }

    boolean editable() default false;

    Filter filter() default Filter.STRING;

    int maxLength() default 8;
}
