package com.group1.taskmanagement.service;

import java.lang.reflect.Field;

public class ObjectUpdater {

    public static void updateObject(Object target, Object source) throws IllegalAccessException {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object sourceValue = getFieldIfExists(source, field.getName());
            if (sourceValue != null) {
                field.set(target, sourceValue);
            }
        }
    }

    private static Object getFieldIfExists(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            // Ignore fields that don't exist or cannot be accessed
        }
        return null;
    }
}