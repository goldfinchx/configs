package com.goldfinch.configs.serialization.primitives;


import com.goldfinch.configs.serialization.Serializer;
import com.goldfinch.configs.serialization.TypeReference;

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
