package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.Serializer;

public class StringSerializer implements Serializer<String, Object> {

    @Override
    public String deserialize(Class<?> fieldClass, Object object) {
        return ((String) object);
    }

    @Override
    public String serialize(Class<?> fieldClass, Object object) {
        return object.toString();
    }

}
