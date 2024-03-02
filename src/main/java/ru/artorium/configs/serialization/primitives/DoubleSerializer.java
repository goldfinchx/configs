package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.Serializer;

public class DoubleSerializer implements Serializer.Specific<Double, Double> {

    @Override
    public Double deserialize(Class fieldClass, Double serialized) {
        return serialized;
    }

    @Override
    public Double serialize(Double object) {
        return object;
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.equals(Double.class) || clazz.equals(double.class);
    }
}
