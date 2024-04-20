package ru.artorium.configs.serialization.primitives;


import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;

public class StringSerializer implements Serializer<String, String> {

    @Override
    public String deserialize(TypeReference type, String serialized) {
        return serialized;
    }

    @Override
    public String serialize(TypeReference type, String object) {
        return object;
    }
}
