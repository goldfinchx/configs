package ru.artorium.configs.serialization.primitives;

import ru.artorium.configs.serialization.Serializer;

public class EnumSerializer implements Serializer<Enum, String> {

    @Override
    public Enum deserialize(Class<?> fieldClass, Object object) {
        final String name = (String) object;
        return Enum.valueOf((Class<Enum>) fieldClass, name);
    }

    @Override
    public String serialize(Class<?> fieldClass, Object object) {
        final Enum<?> value = (Enum<?>) object;
        return value.name();
    }

}
