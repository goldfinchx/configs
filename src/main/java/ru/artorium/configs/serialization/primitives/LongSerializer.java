package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.SpecificSerializer;

public class LongSerializer implements SpecificSerializer<Long, Object> {

    @Override
    public Long deserialize(Object object) {
        return Long.parseLong((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
