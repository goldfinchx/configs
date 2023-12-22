package ru.artorium.configs.core.serialization.primitives;

import ru.artorium.configs.core.serialization.Serializer;

public class DoubleSerializer implements Serializer<Double, Object> {

    @Override
    public Double deserialize(Class fieldClass, Object object) {
        return Double.parseDouble((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }
}
