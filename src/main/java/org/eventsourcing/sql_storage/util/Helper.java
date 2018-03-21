package org.eventsourcing.sql_storage.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    private Helper() {
    }

    public static <T> List<T> valuesOfPublicStaticFinalFields(Class<T> fieldType, Class<?> classToScan) {
        List<T> values = new ArrayList<>();
        for (Field field : classToScan.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (!Modifier.isPublic(modifiers)
                    || !Modifier.isFinal(modifiers)
                    || !Modifier.isStatic(modifiers))
                continue;

            T value = getStaticField(fieldType, field);
            if (null == value)
                continue;

            values.add(value);
        }
        return values;
    }

    public static <T> T getStaticField(Class<T> castTo, Field field) {
        try {
            return cast(castTo, field.get(null));
        } catch (Throwable ex) {
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Class<T> castTo, Object value) {
        if (castTo.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

}
