package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.SimpleSerializer;

public class DoubleSerializer implements SimpleSerializer<Double, Object> {

    @Override
    public Double deserialize(Object object) {
        return Double.parseDouble((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }
}
