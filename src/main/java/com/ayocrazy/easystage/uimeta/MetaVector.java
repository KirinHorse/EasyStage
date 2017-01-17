package com.ayocrazy.easystage.uimeta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ayo on 2017/1/11.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MetaVector {
    boolean editable() default false;

    MetaText.Filter filter() default MetaText.Filter.FLOAT;

    int size() default 2;

    int maxLength() default 10;

    char[] prefix() default {'x', 'y', 'z', 'p', 'q', 'r', 'u', 'v', 'w', 'm', 'n', 'k'};
}
