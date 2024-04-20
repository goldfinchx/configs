package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;

public class IntegerSerializer implements Serializer<Integer, Integer> {

    @Override
    public Integer deserialize(TypeReference typeReference, Integer serialized) {
        return serialized;
    }

    @Override
    public Integer serialize(TypeReference typeReference, Integer object) {
        return object;
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.equals(Integer.class) || clazz.equals(int.class);
    }

}
