package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.SimpleSerializer;

public class FloatSerializer implements SimpleSerializer<Float, Object> {

    @Override
    public Float deserialize(Object object) {
        return Float.parseFloat((String) object);
    }

    @Override
    public String serialize(Object object) {
        return object.toString();
    }

}
