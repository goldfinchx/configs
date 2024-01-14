package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.SpecificSerializer;

public class FloatSerializer implements SpecificSerializer<Float, Object> {

    @Override
    public Float deserialize(Class<?> fieldClass, Object object) {
        return Float.parseFloat((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
