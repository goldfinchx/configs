package ru.artorium.configs.serialization;

public interface Serializer<T, R> {

    T deserialize(Class<?> fieldClass, Object object);
    R serialize(Class<?> fieldClass, Object object);

    static Object serialize(Class<?> fieldClass, Class<?> genericClass, Object object) {
        return SerializerType.getByClass(fieldClass).getSerializer().serialize(genericClass, object);
    }

    static Object deserialize(Class<?> fieldClass, Class<?> genericClass, Object object) {
        return SerializerType.getByClass(fieldClass).getSerializer().deserialize(genericClass, object);
    }

}
