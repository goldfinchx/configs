package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.Serializer;

public class DoubleSerializer implements Serializer<Double, Object> {

    @Override
    public Double deserialize(Class<?> fieldClass, Object object) {
        return Double.parseDouble((String) object);
    }

    @Override
    public String serialize(Class<?> fieldClass, Object object) {
        return object.toString();
    }
}
