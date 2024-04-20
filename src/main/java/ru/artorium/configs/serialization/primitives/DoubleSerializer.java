package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;

public class DoubleSerializer implements Serializer<Double, Double> {

    @Override
    public Double deserialize(TypeReference typeReference, Double serialized) {
        return serialized;
    }

    @Override
    public Double serialize(TypeReference typeReference, Double object) {
        return object;
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.equals(Double.class) || clazz.equals(double.class);
    }
}
