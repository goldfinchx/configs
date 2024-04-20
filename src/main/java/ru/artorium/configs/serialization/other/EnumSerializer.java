package ru.artorium.configs.serialization.other;

import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;
import java.util.Arrays;

public class EnumSerializer implements Serializer<Enum, String> {

    @Override
    public Enum deserialize(TypeReference typeReference, String serialized) {
        try {
            return Enum.valueOf((Class<Enum>) typeReference.clazz(), serialized);
        } catch (IllegalArgumentException ignore) {
            final String[] existingValues = Arrays.stream(typeReference.clazz().getEnumConstants())
                .map(en -> ((Enum) en).name())
                .toList()
                .toArray(new String[0]);

            throw new IllegalArgumentException("Failed to deserialize Enum value: " + serialized + ", as it does not exist, available values: " + String.join(", ", existingValues));
        }
    }

    @Override
    public String serialize(TypeReference typeReference, Enum object) {
        return object.name();
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.isEnum();
    }
}
