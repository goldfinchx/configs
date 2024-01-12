package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.Serializer;

public class IntegerSerializer implements Serializer<Integer, Object> {

    @Override
    public Integer deserialize(Class<?> fieldClass, Object object) {
        return Integer.parseInt((String) object);
    }

    @Override
    public String serialize(Class<?> fieldClass, Object object) {
        return object.toString();
    }

}
