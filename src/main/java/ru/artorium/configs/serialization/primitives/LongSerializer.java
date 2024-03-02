package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.Serializer.Specific;

public class LongSerializer implements Specific<Long, Long> {

    @Override
    public Long deserialize(Class fieldClass, Long serialized) {
        return serialized;
    }

    @Override
    public Long serialize(Long object) {
        return object;
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.equals(Long.class) || clazz.equals(long.class);
    }
}