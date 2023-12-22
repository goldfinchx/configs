package ru.artorium.configs.serialization;

public interface Serializer<T, R> {

    T deserialize(Class fieldClass, Object object);
    R serialize(Object object);

}
