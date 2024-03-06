package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.TypeReference;
import ru.artorium.configs.serialization.Serializer;

public class LongSerializer implements Serializer<Long, Long> {

    @Override
    public Long deserialize(TypeReference typeReference, Long serialized) {
        return serialized;
    }

    @Override
    public Long serialize(TypeReference typeReference, Long object) {
        return object;
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.equals(Long.class) || clazz.equals(long.class);
    }
}