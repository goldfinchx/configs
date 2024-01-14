package ru.artorium.configs.serialization;

import java.lang.reflect.Field;

public interface GenericSerializer<T, R> extends Serializer {

    T deserialize(Field field, Object object);

    R serialize(Field field, Object object);


}
