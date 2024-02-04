package ru.artorium.configs.serialization.other;

import ru.artorium.configs.serialization.SpecificSerializer;

public class EnumSerializer implements SpecificSerializer<Enum, String> {

    @Override
    public Enum deserialize(Class<?> fieldClass, Object object) {
        final String name = (String) object;
        return Enum.valueOf((Class<Enum>) fieldClass, name);
    }

    @Override
    public String serialize(Object object) {
        final Enum<?> value = (Enum<?>) object;
        return value.name();
    }

}
