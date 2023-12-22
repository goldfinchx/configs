package ru.artorium.configs.core.serialization;

public interface Serializer<T, R> {

    T deserialize(Class fieldClass, Object object);
    R serialize(Object object);

}
