package ru.artorium.configs.core.serialization.primitives;

import ru.artorium.configs.core.serialization.Serializer;

public class StringSerializer implements Serializer<String, Object> {

    @Override
    public String deserialize(Class fieldClass, Object object) {
        return ((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
