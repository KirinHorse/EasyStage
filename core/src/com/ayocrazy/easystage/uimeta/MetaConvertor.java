package com.ayocrazy.easystage.uimeta;

import java.lang.annotation.Annotation;

/**
 * Created by ayo on 2017/1/12.
 */

public class MetaConvertor {
    public static final String getString(Annotation meta) {
        if (meta instanceof MetaText) {
            MetaText mt = (MetaText) meta;
            StringBuilder sb = new StringBuilder("MetaText,");
            sb.append(mt.editable());
            sb.append(',');
            sb.append(mt.filter().name());
            sb.append(',');
            sb.append(mt.maxLength());
            sb.append(',');
            sb.append(mt.arraySize());
            sb.append(',');
            sb.append(mt.prefix());
            return sb.toString();
        } else {
            return meta.getClass().getName();
        }
    }

    public static final Annotation getMeta(String metaString) {
        final String[] params = metaString.split(",");
        return new MetaText() {
            @Override
            public boolean editable() {
                return Boolean.parseBoolean(params[1]);
            }

            @Override
            public Filter filter() {
                return Filter.valueOf(params[2]);
            }

            @Override
            public int maxLength() {
                return Integer.parseInt(params[3]);
            }

            @Override
            public int arraySize() {
                return Integer.parseInt(params[4]);
            }

            @Override
            public char[] prefix() {
                return params[5].toCharArray();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return MetaText.class;
            }
        };
    }
}
