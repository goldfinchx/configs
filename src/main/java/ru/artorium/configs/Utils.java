package ru.artorium.configs;

import java.lang.reflect.Field;
import ru.artorium.configs.serialization.GenericSerializer;
import ru.artorium.configs.serialization.SerializerType;
import ru.artorium.configs.serialization.SimpleSerializer;

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
        return ((SimpleSerializer) SerializerType.getByClass(clazz).getSerializer()).serialize(object);
    }
    public static Object deserializeSpecific(Class<?> clazz, Object object) {
        return ((SimpleSerializer) SerializerType.getByClass(clazz).getSerializer()).deserialize(object);
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

}
