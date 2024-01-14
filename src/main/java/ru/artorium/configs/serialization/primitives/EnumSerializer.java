package ru.artorium.configs.serialization.primitives;

import java.lang.reflect.Field;
import ru.artorium.configs.serialization.GenericSerializer;

public class EnumSerializer implements GenericSerializer<Enum, String> {

    @Override
    public Enum deserialize(Field field, Object object) {
        final String name = (String) object;
        return Enum.valueOf((Class<Enum>) field.getType(), name);
    }

    @Override
    public String serialize(Field field, Object object) {
        final Enum<?> value = (Enum<?>) object;
        return value.name();
    }

}
