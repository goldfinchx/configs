package com.goldfinch.configs.serialization;

import com.goldfinch.configs.serialization.minecraft.WorldSerializer;
import com.goldfinch.configs.serialization.other.EnumSerializer;
import com.goldfinch.configs.serialization.collections.CollectionSerializer;
import com.goldfinch.configs.serialization.collections.MapSerializer;
import com.goldfinch.configs.serialization.minecraft.ComponentSerializer;
import com.goldfinch.configs.serialization.minecraft.ItemStackSerializer;
import com.goldfinch.configs.serialization.minecraft.LocationSerializer;
import com.goldfinch.configs.serialization.other.ArraySerializer;
import com.goldfinch.configs.serialization.other.ObjectSerializer;
import com.goldfinch.configs.serialization.other.UUIDSerializer;
import com.goldfinch.configs.serialization.primitives.BooleanSerializer;
import com.goldfinch.configs.serialization.primitives.CharacterSerializer;
import com.goldfinch.configs.serialization.primitives.DoubleSerializer;
import com.goldfinch.configs.serialization.primitives.IntegerSerializer;
import com.goldfinch.configs.serialization.primitives.StringSerializer;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Set;

public interface Serializer<T, R> extends Serializable {

    StringSerializer STRING = new StringSerializer();
    IntegerSerializer INTEGER = new IntegerSerializer();
    CharacterSerializer CHARACTER = new CharacterSerializer();
    DoubleSerializer DOUBLE = new DoubleSerializer();
    BooleanSerializer BOOLEAN = new BooleanSerializer();
    EnumSerializer ENUM = new EnumSerializer();
    WorldSerializer WORLD = new WorldSerializer();
    ItemStackSerializer ITEMSTACK = new ItemStackSerializer();
    LocationSerializer LOCATION = new LocationSerializer();
    CollectionSerializer COLLECTION = new CollectionSerializer();
    ComponentSerializer COMPONENT = new ComponentSerializer();
    MapSerializer MAP = new MapSerializer();
    ArraySerializer ARRAY = new ArraySerializer();
    ObjectSerializer OBJECT = new ObjectSerializer();
    UUIDSerializer UUID = new UUIDSerializer();
    Set<Serializer> ALL = Set.of(
        STRING, INTEGER, DOUBLE, BOOLEAN, CHARACTER,
        ENUM, WORLD, ITEMSTACK, LOCATION, COMPONENT,
        COLLECTION, MAP, ARRAY, OBJECT, UUID
    );

    static Serializer getByClass(Class<?> clazz) {
        return ALL.stream()
            .filter(type -> type.isCompatibleWith(clazz))
            .findFirst()
            .orElse(OBJECT);
    }

    static Object deserialize(Field field, Object serialized) {
        final Serializer serializer = getByClass(field.getType());

        try {
            return serializer.deserialize(new TypeReference(field), serialized);
        } catch (Exception exception) {
            throw new RuntimeException("Error while deserializing object of type " + serialized.getClass().getSimpleName() + " with contents: " + serialized,
                exception);
        }
    }

    static Object serialize(Field field, Object serialized) {
        final Serializer serializer = getByClass(field.getType());

        try {
            return serializer.serialize(new TypeReference(field), serialized);
        } catch (Exception exception) {
            throw new RuntimeException("Error while serializing object of type " + serialized.getClass().getSimpleName() + " with contents: " + serialized,
                exception);
        }
    }

    static Object deserialize(Class<?> targetClass, Object serialized) {
        final Serializer serializer = getByClass(targetClass);

        try {
            return serializer.deserialize(new TypeReference(targetClass), serialized);
        } catch (Exception exception) {
            throw new RuntimeException("Error while deserializing object of type " + serialized.getClass().getSimpleName() + " with contents: " + serialized,
                exception);
        }
    }

    static Object serialize(Class<?> targetClass, Object serialized) {
        final Serializer serializer = getByClass(targetClass);

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
