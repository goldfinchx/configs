package ru.artorium.configs;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.artorium.configs.annotations.Ignore;

public class Utils {

    public static List<Field> getFields(Object object) {
        return Arrays.stream(FieldUtils.getAllFields(object.getClass()))
            .filter(field -> !Modifier.isFinal(field.getModifiers()))
            .filter(field -> !Modifier.isTransient(field.getModifiers()))
            .filter(field -> !field.isAnnotationPresent(Ignore.class))
            .peek(field -> field.setAccessible(true))
            .collect(Collectors.toList());
    }

}
