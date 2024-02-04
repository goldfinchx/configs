package ru.artorium.configs;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import net.kyori.adventure.text.Component;
import ru.artorium.configs.serialization.GenericSerializer;
import ru.artorium.configs.serialization.SerializerType;
import ru.artorium.configs.serialization.SpecificSerializer;

public class Utils {

    public static Object serialize(Field field, Object object) {
        if (isGeneric(field.getType())) {
            return serializeGeneric(field, field.getType(), object);
        } else {
            return serializeSpecific(field.getType(), object);
        }
    }
    public static Object deserialize(Field field, Object object) {
        if (isGeneric(field.getType())) {
            return deserializeGeneric(field, field.getType(), object);
        } else {
            return deserializeSpecific(field.getType(), object);
        }
    }

    public static Object serializeSpecific(Class<?> clazz, Object object) {
        return ((SpecificSerializer) SerializerType.getByClass(clazz).getSerializer()).serialize(object);
    }
    public static Object deserializeSpecific(Class<?> clazz, Object object) {
        return ((SpecificSerializer) SerializerType.getByClass(clazz).getSerializer()).deserialize(clazz, object);
    }

    private static boolean isGeneric(Class<?> fieldClass) {
        return SerializerType.getByClass(fieldClass).getSerializer() instanceof GenericSerializer<?,?>;
    }

    public static Object deserializeGeneric(Field field, Class<?> targetClass, Object object) {
        return ((GenericSerializer) SerializerType.getByClass(targetClass).getSerializer()).deserialize(field, object);
    }
    public static Object serializeGeneric(Field field, Class<?> targetClass, Object object) {
        return ((GenericSerializer) SerializerType.getByClass(targetClass).getSerializer()).serialize(field, object);
    }

    public static Class<?> getActualClass(Class<?> fieldClass) {

        if (fieldClass.isEnum()) {
            return Enum.class;
        }

        if (Collection.class.isAssignableFrom(fieldClass)) {
            return Collection.class;
        }

        if (Map.class.isAssignableFrom(fieldClass)) {
            return Map.class;
        }

        if (Component.class.isAssignableFrom(fieldClass)) {
            fieldClass = Component.class;
        }

        if (fieldClass.isArray()) {
            return Object[].class;
        }

        if (Number.class.isAssignableFrom(fieldClass)) {
            if (fieldClass == Integer.class) {
                return int.class;
            } else if (fieldClass == Double.class) {
                return double.class;
            } else if (fieldClass == Long.class) {
                return long.class;
            } else if (fieldClass == Float.class) {
                return float.class;
            }
        }

        return fieldClass;
    }
}
