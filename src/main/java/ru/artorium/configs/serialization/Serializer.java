package ru.artorium.configs.serialization;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Set;
import org.apache.commons.lang.SerializationException;
import ru.artorium.configs.serialization.collections.ArraySerializer;
import ru.artorium.configs.serialization.collections.CollectionSerializer;
import ru.artorium.configs.serialization.collections.MapSerializer;
import ru.artorium.configs.serialization.minecraft.ComponentSerializer;
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

public interface Serializer<T, R> extends Serializable {

    StringSerializer STRING = new StringSerializer();
    IntegerSerializer INTEGER = new IntegerSerializer();
    DoubleSerializer DOUBLE = new DoubleSerializer();
    LongSerializer LONG = new LongSerializer();
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
    Set<Serializer> ALL = Set.of(
        STRING, INTEGER, DOUBLE, LONG, BOOLEAN,
        ENUM, WORLD, ITEMSTACK, LOCATION, COMPONENT,
        COLLECTION, MAP, ARRAY, OBJECT
    );


    static Serializer getByClass(Class<?> clazz) {
        return ALL.stream()
            .filter(type -> type.isCompatibleWith(clazz))
            .findFirst()
            .orElse(OBJECT);
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
        return (Class<?>[]) parameterizedType.getActualTypeArguments();
    }

}
