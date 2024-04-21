package com.goldfinch.configs.serialization.primitives;


import com.goldfinch.configs.serialization.Serializer;
import com.goldfinch.configs.serialization.TypeReference;

public class BooleanSerializer implements Serializer<Boolean, Boolean> {

    @Override
    public Boolean deserialize(TypeReference typeReference, Boolean serialized) {
        return serialized;
    }

    @Override
    public Boolean serialize(TypeReference typeReference, Boolean object) {
        return object;
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.equals(Boolean.class) || clazz.equals(boolean.class);
    }
}

