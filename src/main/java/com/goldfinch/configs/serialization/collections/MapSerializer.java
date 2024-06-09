package com.goldfinch.configs.serialization.collections;

import com.goldfinch.configs.serialization.Serializer;
import com.goldfinch.configs.serialization.TypeReference;
import java.util.HashMap;
import java.util.Map;

public class MapSerializer implements Serializer<Map<?, ?>, Map<?, ?>> {

    @Override
    public Map<?, ?> deserialize(TypeReference typeReference, Map<?, ?> serialized) {
        final Class<?>[] types = this.getGenericTypes(typeReference);
        return serialized.entrySet()
            .stream()
            .collect(
                HashMap::new,
                (map, entry) -> map.put(
                    Serializer.deserialize(types[0], entry.getKey()),
                    Serializer.deserialize(types[1], entry.getValue())
                ),
                HashMap::putAll
            );
    }

    @Override
    public Map<?, ?> serialize(TypeReference typeReference, Map<?, ?> map) {
        final Class<?>[] types = this.getGenericTypes(typeReference);
        final Map<Object, Object> serialized = new HashMap<>();

        map.forEach(((k, v) -> serialized.put(
            Serializer.serialize(types[0], k),
            Serializer.serialize(types[1], v)))
        );

        return serialized;
    }

    @Override
    public boolean isCompatibleWith(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }
}
