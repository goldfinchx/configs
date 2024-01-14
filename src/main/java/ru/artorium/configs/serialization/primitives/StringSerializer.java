package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.SpecificSerializer;

public class StringSerializer implements SpecificSerializer<String, Object> {

    @Override
    public String deserialize(Class<?> fieldClass, Object object) {
        return ((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
