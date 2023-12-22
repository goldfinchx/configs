package ru.artorium.configs.core.serialization.primitives;

import ru.artorium.configs.core.serialization.Serializer;

public class LongSerializer implements Serializer<Long, Object> {

    @Override
    public Long deserialize(Class fieldClass, Object object) {
        return Long.parseLong((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
