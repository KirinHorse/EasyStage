package com.ayocrazy.easystage.rmi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by ayo on 2017/1/15.
 */

public class EasyReflect {
    static Object getValue(String fieldName, Object obj) {
        try {
            Field field = getField(obj.getClass(), fieldName);
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value instanceof Vector2) {
                return new float[]{((Vector2) value).x, ((Vector2) value).y};
            } else if (value instanceof Vector3) {
                return new float[]{((Vector3) value).x, ((Vector3) value).y, ((Vector3) value).z};
            } else if (value instanceof Color) {
                return new float[]{((Color) value).r, ((Color) value).g, ((Color) value).b, ((Color) value).a};
            } else if (field.getType().isEnum()) {
                return field.toString();
            } else return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean setValue(String fieldName, Object obj, Object value) {
        Field field = getField(obj.getClass(), fieldName);
        if (field == null) return false;
        field.setAccessible(true);
        try {
            if (typeFit(value, field.getType())) {
                field.set(obj, value);
            } else {
                value = typeCast(field, obj, value);
                if (value == null) return true;
                field.set(obj, value);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static Object typeCast(Field field, Object obj, Object value) throws Exception {
        return typeCast(field, obj, value, true);
    }

    private static Object typeCast(Field field, Object obj, Object value, boolean setValue) throws Exception {
        Class type = field.getType();
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value.toString());
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(value.toString());
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value.toString());
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value.toString());
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value.toString());
        } else if (type == char.class || type == Character.class) {
            return value.toString().charAt(0);
        } else if (type == Vector2.class) {
            Vector2 vec2 = (Vector2) field.get(obj);
            if (vec2 == null || !setValue)
                return new Vector2(toFloat(value, 0), toFloat(value, 1));
            else {
                vec2.set(toFloat(value, 0), toFloat(value, 1));
                return null;
            }
        } else if (type == Vector3.class) {
            Vector3 vec3 = (Vector3) field.get(obj);
            if (vec3 == null || !setValue)
                return new Vector3(toFloat(value, 0), toFloat(value, 1), toFloat(value, 2));
            else {
                vec3.set(toFloat(value, 0), toFloat(value, 1), toFloat(value, 2));
                return null;
            }
        } else if (type == Color.class) {
            Color color = (Color) field.get(obj);
            if (color == null || !setValue)
                return new Color(toFloat(value, 0), toFloat(value, 1), toFloat(value, 2), toFloat(value, 3));
            else {
                color.set(toFloat(value, 0), toFloat(value, 1), toFloat(value, 2), toFloat(value, 3));
                return null;
            }
        } else if (type.isEnum()) {
            return Enum.valueOf(type, value.toString());
        } else return null;
    }

    static float toFloat(Object arr, int index) {
        return Float.parseFloat(Array.get(arr, index).toString());
    }

    static int toInt(Object arr, int index) {
        return Integer.parseInt(Array.get(arr, index).toString());
    }

    static boolean typeFit(Object value, Class type) {
        if (value == null) return true;
        Class claz = value.getClass();
        while (claz != Object.class) {
            if (claz == type) return true;
            claz = claz.getSuperclass();
        }
        return false;
    }

    static boolean invoke(String fieldName, String methodName, Object obj, Object value) {
        Field field = getField(obj.getClass(), fieldName);
        Method method = getMethod(obj.getClass(), fieldName, methodName);
        if (field == null || method == null) return false;
        try {
            field.setAccessible(true);
            method.invoke(obj, typeCast(field, obj, value, false));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    static boolean isValid(Class type) {
        return type == int.class ||
                type == boolean.class ||
                type == float.class ||
                type == String.class ||
                type == Vector2.class ||
                type == Vector3.class ||
                type == Color.class ||
                type == double.class ||
                type == long.class ||
                type == char.class ||
                type == short.class ||
                type.isEnum() ||
                type == Integer.class ||
                type == Boolean.class ||
                type == Float.class ||
                type == Double.class ||
                type == Long.class ||
                type == Short.class ||
                type == Character.class ||
                (type.isArray() && isValid(type.getComponentType()));
    }

    static Field getField(Class claz, String fieldName) {
        claz = getClaz(claz);
        while (claz != Object.class) {
            try {
                return claz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
            claz = claz.getSuperclass();
        }
        return null;
    }

    static Method getMethod(Class claz, String fieldName, String methodName) {
        if (methodName.equals("default")) {
            methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }
        claz = getClaz(claz);
        Field field = getField(claz, fieldName);
        if (field == null) return null;
        while (claz != Object.class) {
            try {
                return claz.getDeclaredMethod(methodName, field.getType());
            } catch (Exception e) {
            }
            claz = claz.getSuperclass();
        }
        return null;
    }

    static Class getClaz(Class claz) {
        while (claz.getSimpleName().contains("EnhancerByCGLIB")) {
            claz = claz.getSuperclass();
        }
        return claz;
    }

    static Class getClaz(Object obj) {
        return getClaz(obj.getClass());
    }
}
