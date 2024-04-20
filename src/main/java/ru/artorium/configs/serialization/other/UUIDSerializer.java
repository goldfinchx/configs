package ru.artorium.configs.serialization.other;


import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;
import java.util.UUID;

public class UUIDSerializer implements Serializer<UUID, String> {

    @Override
    public UUID deserialize(TypeReference type, String serialized) {
        try {
            return java.util.UUID.fromString(serialized);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to deserialize UUID: " + serialized + " as it is not a valid UUID");
        }
    }

    @Override
    public String serialize(TypeReference type, UUID uuid) {
        return uuid.toString();
    }
}
