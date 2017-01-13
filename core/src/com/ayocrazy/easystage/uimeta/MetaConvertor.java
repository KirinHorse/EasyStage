package com.ayocrazy.easystage.uimeta;

import com.badlogic.gdx.utils.StringBuilder;

import java.io.PrintStream;
import java.lang.annotation.Annotation;

/**
 * Created by ayo on 2017/1/12.
 */

public class MetaConvertor {
    public static final String getString(Annotation meta) {
        StringBuilder sb = new StringBuilder();
        if (meta instanceof MetaText) {
            MetaText mt = (MetaText) meta;
            sb.append("MetaText,");
            sb.append(mt.editable()).append(',');
            sb.append(mt.filter().name()).append(',');
            sb.append(mt.maxLength());
        } else if (meta instanceof MetaVector) {
            MetaVector mv = (MetaVector) meta;
            sb.append("MetaVector,");
            sb.append(mv.editable()).append(',');
            sb.append(mv.filter().name()).append(',');
            sb.append(mv.size()).append(',');
            sb.append(mv.maxLength()).append(',');
            sb.append(mv.prefix());
        } else if (meta instanceof MetaSlider) {
            MetaSlider ms = (MetaSlider) meta;
            sb.append("MetaSlider,");
            sb.append(ms.minValue()).append(',');
            sb.append(ms.maxValue()).append(',');
            sb.append(ms.step());
        } else if (meta instanceof MetaSelectBox) {
            MetaSelectBox msb = (MetaSelectBox) meta;
            sb.append("MetaSelectBox,");
            String[] items = msb.items();
            for (int i = 0; i < items.length; i++) {
                String item = items[i];
                sb.append(item);
                if (i < items.length - 1) sb.append("@#");
            }
            sb.append(',');
            sb.append(msb.enumClass().getName());
        } else if (meta instanceof MetaCheckBox) {
            sb.append("MetaCheckBox");
        } else if (meta instanceof MetaTable) {
            sb.append("MetaTable");
        } else {
            sb.append(meta.getClass().getName());
        }
        return sb.toString();
    }

    public static final Annotation getMeta(String metaString) {
        final String[] params = metaString.split(",");
        String metaName = params[0];
        if (metaName.equals("MetaText")) {
            return text(params);
        } else if (metaName.equals("MetaVector")) {
            return vector(params);
        } else if (metaName.equals("MetaCheckBox")) {
            return checkBox();
        } else if (metaName.equals("MetaSlider")) {
            return slider(params);
        } else if (metaName.equals("MetaSelectBox")) {
            return selectBox(params);
        } else if (metaName.equals("MetaTable")) {
            return table();
        }
        return null;
    }

    private static final Annotation text(String[] params) {
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
            public Class<? extends Annotation> annotationType() {
                return MetaText.class;
            }
        };
    }

    private static final Annotation vector(String[] params) {
        return new MetaVector() {
            @Override
            public boolean editable() {
                return Boolean.parseBoolean(params[1]);
            }

            @Override
            public MetaText.Filter filter() {
                return MetaText.Filter.valueOf(params[2]);
            }

            @Override
            public int size() {
                return Integer.parseInt(params[3]);
            }

            @Override
            public int maxLength() {
                return Integer.parseInt(params[4]);
            }

            @Override
            public char[] prefix() {
                return params[5].toCharArray();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return MetaVector.class;
            }
        };
    }

    private static Annotation selectBox(String[] params) {
        return new MetaSelectBox() {
            @Override
            public String[] items() {
                if (params[1].equals("")) return new String[0];
                return params[1].split("@#");
            }

            @Override
            public Class enumClass() {
                try {
                    return Class.forName(params[2]);
                } catch (Exception e) {
                    return Object.class;
                }
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return MetaSelectBox.class;
            }
        };
    }

    private static Annotation slider(String[] params) {
        return new MetaSlider() {
            @Override
            public float minValue() {
                return Float.parseFloat(params[1]);
            }

            @Override
            public float maxValue() {
                return Float.parseFloat(params[2]);

            }

            @Override
            public float step() {
                return Float.parseFloat(params[3]);

            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return MetaSlider.class;
            }
        };
    }

    private static Annotation table() {
        return new MetaTable() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return MetaTable.class;
            }
        };
    }

    private static Annotation checkBox() {
        return new MetaCheckBox() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return MetaCheckBox.class;
            }
        };
    }
}
