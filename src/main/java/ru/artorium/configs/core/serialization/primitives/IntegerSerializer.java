package ru.artorium.configs.core.serialization.primitives;

import ru.artorium.configs.core.serialization.Serializer;

public class IntegerSerializer implements Serializer<Integer, Object> {

    @Override
    public Integer deserialize(Class fieldClass, Object object) {
        return Integer.parseInt((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
