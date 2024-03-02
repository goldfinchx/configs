package ru.artorium.configs.serialization;

import ru.artorium.configs.serialization.collections.ArraySerializer;
import ru.artorium.configs.serialization.collections.CollectionSerializer;
import ru.artorium.configs.serialization.collections.MapSerializer;
import ru.artorium.configs.serialization.minecraft.ItemStackSerializer;
import ru.artorium.configs.serialization.minecraft.LocationSerializer;
import ru.artorium.configs.serialization.minecraft.WorldSerializer;
import ru.artorium.configs.serialization.other.EnumSerializer;
import ru.artorium.configs.serialization.other.ObjectSerializer;
import ru.artorium.configs.serialization.primitives.BooleanSerializer;
import ru.artorium.configs.serialization.primitives.DoubleSerializer;
import ru.artorium.configs.serialization.primitives.IntegerSerializer;
import ru.artorium.configs.serialization.primitives.LongSerializer;
import ru.artorium.configs.serialization.primitives.StringSerializer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

public interface Serializer<T, R> {

    Specific STRING = new StringSerializer();
    Specific INTEGER = new IntegerSerializer();
    Specific DOUBLE = new DoubleSerializer();
    Specific LONG = new LongSerializer();
    Specific BOOLEAN = new BooleanSerializer();
    Specific ENUM = new EnumSerializer();
    Specific WORLD = new WorldSerializer();
    Specific ITEMSTACK = new ItemStackSerializer();
    Specific LOCATION = new LocationSerializer();
    Generic COLLECTION = new CollectionSerializer();
    Generic MAP = new MapSerializer();
    Specific ARRAY = new ArraySerializer();
    Specific OBJECT = new ObjectSerializer();


    Set<Serializer> ALL = Set.of(
        STRING, INTEGER, DOUBLE, LONG, BOOLEAN,
        ENUM, WORLD, ITEMSTACK, LOCATION,
        COLLECTION, MAP, ARRAY, OBJECT
    );

    static Serializer getByClass(Class<?> targetClass) {
        return ALL.stream()
            .filter(type -> type.isCompatibleWith(targetClass))
            .findFirst()
            .orElse(OBJECT);
    }


    /**
     * Serializes the given object. Handles generic types.
     *
     * @param field  The field of the object that needs to be serialized.
     * @param object The object that needs to be serialized.
     * @return The serialized object.
     */
    static Object serialize(Field field, Object object) {
        final Serializer serializer = Serializer.getByClass(field.getType());
        final boolean isGeneric = serializer instanceof Serializer.Generic;

        if (isGeneric) {
            return ((Generic) serializer).serialize(field, object);
        } else {
            return ((Specific) serializer).serialize(object);
        }
    }

    /**
     * Deserializes the given object. Handles generic types.
     *
     * @param field  The field of the object that needs to be deserialized.
     * @param object The object that needs to be deserialized.
     * @return The deserialized object.
     */
    static Object deserialize(Field field, Object object) {
        final Serializer serializer = Serializer.getByClass(field.getType());
        final boolean isGeneric = serializer instanceof Serializer.Generic;

        if (isGeneric) {
            return ((Generic) serializer).deserialize(field, object);
        } else {
            return ((Specific) serializer).deserialize(field.getType(), object);
        }
    }

    /**
     * Serializes the given object. IMPORTANT: Doesn't work with generic objects
     *
     * @param clazz  The class of the object that needs to be serialized.
     * @param object The object that needs to be serialized.
     * @return The serialized object.
     */
    static Object serialize(Class<?> clazz, Object object) {
        return ((Specific) Serializer.getByClass(clazz)).serialize(object);
    }

    /**
     * Deserializes the given object. IMPORTANT: Doesn't work with generic objects
     *
     * @param clazz  The class of the object that needs to be deserialized.
     * @param object The object that needs to be deserialized.
     * @return The deserialized object.
     */
    static Object deserialize(Class<?> clazz, Object object) {
        return ((Specific) Serializer.getByClass(clazz)).deserialize(clazz, object);
    }

    default boolean isCompatibleWith(Class<?> clazz) {
        final ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericInterfaces()[0];
        final Class<?> targetClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        return targetClass.equals(clazz);
    }

    interface Generic<T, R> extends Serializer {
        T deserialize(Field field, R serialized);
        R serialize(Field field, T object);
    }

    interface Specific<T, R> extends Serializer {
        T deserialize(Class<?> fieldClass, R serialized);
        R serialize(T object);
    }

}
