package ru.artorium.configs.core.serialization.primitives;

import ru.artorium.configs.core.serialization.Serializer;

public class FloatSerializer implements Serializer<Float, Object> {

    @Override
    public Float deserialize(Class fieldClass, Object object) {
        return Float.parseFloat((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
