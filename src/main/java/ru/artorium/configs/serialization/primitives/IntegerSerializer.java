package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.Serializer;

public class IntegerSerializer implements Serializer.Specific<Integer, Integer> {

    @Override
    public Integer deserialize(Class fieldClass, Integer serialized) {
        return serialized;
    }

    @Override
    public Integer serialize(Integer object) {
        return object;
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.equals(Integer.class) || clazz.equals(int.class);
    }

}
