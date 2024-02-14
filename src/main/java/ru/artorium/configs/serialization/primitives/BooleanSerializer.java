package ru.artorium.configs.serialization.primitives;


import ru.artorium.configs.serialization.SpecificSerializer;

public class BooleanSerializer implements SpecificSerializer<Boolean, Object> {

    @Override
    public Boolean deserialize(Class<?> fieldClass, Object object) {
        return Boolean.parseBoolean((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
