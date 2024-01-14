package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.SimpleSerializer;

public class StringSerializer implements SimpleSerializer<String, Object> {

    @Override
    public String deserialize(Object object) {
        return ((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
