package ru.artorium.configs.serialization.primitives;


import ru.artorium.configs.serialization.Serializer;

public class StringSerializer implements Serializer.Specific<String, String> {

    @Override
    public String deserialize(Class<?> fieldClass, String serialized) {
        return serialized;
    }

    @Override
    public String serialize(String object) {
        return object;
    }
}
