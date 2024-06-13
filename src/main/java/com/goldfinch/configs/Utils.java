package com.goldfinch.configs;

import com.goldfinch.configs.annotations.Ignore;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;

public class Utils {

    public static List<Field> getSerializableFields(Object object) {
        return Arrays.stream(FieldUtils.getAllFields(object.getClass()))
            .filter(field -> !Modifier.isStatic(field.getModifiers()))
            .filter(field -> !Modifier.isFinal(field.getModifiers()))
            .filter(field -> !Modifier.isTransient(field.getModifiers()))
            .filter(field -> !field.isAnnotationPresent(Ignore.class))
            .toList();
    }

    public static boolean isServerUsesComponents() {
        try {
            Class.forName("net.kyori.adventure.text.Component");
        } catch (ClassNotFoundException e) {
            return false;
        }

        return true;
    }


}
