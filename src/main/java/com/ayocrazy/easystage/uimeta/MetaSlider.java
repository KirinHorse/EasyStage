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
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaSlider {

    float minValue() default 0f;

    float maxValue() default 1f;

    float step() default 0.1f;
}
