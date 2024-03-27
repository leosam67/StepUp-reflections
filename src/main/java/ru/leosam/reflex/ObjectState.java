package ru.leosam.reflex;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ObjectState {
    public static String calcState(Object obj) {
        Class clazz = obj.getClass();
        StringBuilder sb = new StringBuilder();
        for (Field field : clazz.getDeclaredFields()) {
            if(Modifier.isStatic(field.getModifiers()))
                continue;
            if (sb.length() > 0) {
                sb.append("; ");
            }
            field.setAccessible(true);
            try {
                sb.append(field.getName()).append("=").append(field.get(obj));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        sb.insert(0, '{');
        sb.append('}');
        sb.insert(0, clazz.getName());
        return sb.toString();
    }
}
