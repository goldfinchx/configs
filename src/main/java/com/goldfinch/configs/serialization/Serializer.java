package com.goldfinch.configs.serialization;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

/**
 *
 * @param <T> Original type (inside a config class)
 * @param <R> Serialized type (inside a config file) [Maps, Strings, Numbers, Booleans, Collections]
 */
public interface Serializer<T, R> extends Serializable {

    static Object deserialize(Field field, Object serialized) {
        final Serializer serializer = SerializerFactory.getFactory().get(field.getType());

        try {
            return serializer.deserialize(new TypeReference(field), serialized);
        } catch (Exception exception) {
            throw new RuntimeException("Error while deserializing object of type " + serialized.getClass().getSimpleName() + " with contents: " + serialized,
                exception);
        }
    }

    static Object serialize(Field field, Object serialized) {
        final Serializer serializer = SerializerFactory.getFactory().get(field.getType());

        try {
            return serializer.serialize(new TypeReference(field), serialized);
        } catch (Exception exception) {
            throw new RuntimeException("Error while serializing object of type " + serialized.getClass().getSimpleName() + " with contents: " + serialized,
                exception);
        }
    }

    static Object deserialize(Class<?> targetClass, Object serialized) {
        final Serializer serializer = SerializerFactory.getFactory().get(targetClass);

        try {
            return serializer.deserialize(new TypeReference(targetClass), serialized);
        } catch (Exception exception) {
            throw new RuntimeException("Error while deserializing object of type " + serialized.getClass().getSimpleName() + " with contents: " + serialized,
                exception);
        }
    }

    static Object serialize(Class<?> targetClass, Object serialized) {
        final Serializer serializer = SerializerFactory.getFactory().get(targetClass);

        try {
            return serializer.serialize(new TypeReference(targetClass), serialized);
        } catch (Exception exception) {
            throw new RuntimeException("Error while serializing object of type " + serialized.getClass().getSimpleName() + " with contents: " + serialized, exception);
        }
    }

    default boolean isCompatibleWith(Class<?> clazz) {
        final ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericInterfaces()[0];
        final Class<?> targetClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        return targetClass.equals(clazz);
    }

    T deserialize(TypeReference type, R serialized);

    R serialize(TypeReference type, T object);

    default Class<?>[] getGenericTypes(TypeReference typeReference) {
        if (typeReference.field() == null) {
            return new Class<?>[0];
        }

        final ParameterizedType parameterizedType = (ParameterizedType) typeReference.field().getGenericType();
        return Arrays.stream(parameterizedType.getActualTypeArguments())
            .map(type -> (Class<?>) type)
            .toArray(Class<?>[]::new);
    }

}
