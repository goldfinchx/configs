package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.SpecificSerializer;

public class DoubleSerializer implements SpecificSerializer<Double, Object> {

    @Override
    public Double deserialize(Class<?> fieldClass, Object object) {
        return Double.parseDouble((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }
}
