package ru.artorium.configs.serialization;

public interface SpecificSerializer<T, R> extends Serializer {

    T deserialize(Class<?> fieldClass, Object object);

    R serialize(Object object);

}
