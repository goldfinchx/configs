package com.goldfinch.configs.serialization;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    static Object deserializeReference(TypeReference reference, Object serialized) {
        final Serializer serializer = SerializerFactory.getFactory().get(reference.clazz());

        try {
            return serializer.deserialize(reference, serialized);
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
    static Object serializeReference(TypeReference reference, Object serialized) {
        final Serializer serializer = SerializerFactory.getFactory().get(reference.clazz());

        try {
            return serializer.serialize(reference, serialized);
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

    private static SerializeType fromParameterizedTypeIntoSerialize(ParameterizedType parameterizedType){
        SerializeType stype = new SerializeType((Class<?>) parameterizedType.getRawType());
        List<SerializeType> subTypes = new ArrayList<>(Arrays.stream(parameterizedType.getActualTypeArguments())
                .map(type -> {
                    if (type instanceof ParameterizedType) {
                        return fromParameterizedTypeIntoSerialize((ParameterizedType) type);
                    } else {
                        return new SerializeType((Class<?>) type, new ArrayList<>());

                    }
                })
                .toList());
        stype.setSubTypes(subTypes);
        return stype;
    }

    default SerializeType getSerializeType(TypeReference typeReference){
        if (typeReference instanceof SerializeType) {
            return (SerializeType) typeReference;
        }

        if (typeReference.field() == null) {
            return null;
        }

        final ParameterizedType parameterizedType = (ParameterizedType) typeReference.field().getGenericType();
        return fromParameterizedTypeIntoSerialize(parameterizedType);

    }
}
