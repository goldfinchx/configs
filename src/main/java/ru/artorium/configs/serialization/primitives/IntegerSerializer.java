package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.SpecificSerializer;

public class IntegerSerializer implements SpecificSerializer<Integer, Object> {

    @Override
    public Integer deserialize(Object object) {
        return Integer.parseInt((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
