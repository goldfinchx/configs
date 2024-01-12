package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.Serializer;

public class FloatSerializer implements Serializer<Float, Object> {

    @Override
    public Float deserialize(Class<?> fieldClass, Object object) {
        return Float.parseFloat((String) object);
    }

    @Override
    public String serialize(Class<?> fieldClass, Object object) {
        return object.toString();
    }

}
