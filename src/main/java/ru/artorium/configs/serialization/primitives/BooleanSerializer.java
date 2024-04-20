package ru.artorium.configs.serialization.primitives;


import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;

public class BooleanSerializer implements Serializer<Boolean, Boolean> {

    @Override
    public Boolean deserialize(TypeReference typeReference, Boolean serialized) {
        return serialized;
    }

    @Override
    public Boolean serialize(TypeReference typeReference, Boolean object) {
        return object;
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.equals(Boolean.class) || clazz.equals(boolean.class);
    }
}

