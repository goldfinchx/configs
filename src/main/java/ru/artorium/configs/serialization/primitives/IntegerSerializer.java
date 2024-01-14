package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.SimpleSerializer;

public class IntegerSerializer implements SimpleSerializer<Integer, Object> {

    @Override
    public Integer deserialize(Object object) {
        return Integer.parseInt((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
