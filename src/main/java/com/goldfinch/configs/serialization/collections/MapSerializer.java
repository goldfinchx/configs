package com.goldfinch.configs.serialization.collections;

import com.goldfinch.configs.serialization.SerializeType;
import com.goldfinch.configs.serialization.Serializer;
import com.goldfinch.configs.serialization.TypeReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapSerializer implements Serializer<Map<?, ?>, Map<?, ?>> {

    @Override
    public Map<?, ?> deserialize(TypeReference typeReference, Map<?, ?> serialized) {
        final List<SerializeType> types = this.getSerializeType(typeReference).getSubTypes();
        return serialized.entrySet()
            .stream()
            .collect(
                HashMap::new,
                (map, entry) -> map.put(
                    Serializer.deserializeReference(types.get(0), entry.getKey()),
                    Serializer.deserializeReference(types.get(1), entry.getValue())
                ),
                HashMap::putAll
            );
    }

    @Override
    public Map<?, ?> serialize(TypeReference typeReference, Map<?, ?> map) {
        final List<SerializeType> types = this.getSerializeType(typeReference).getSubTypes();
        final Map<Object, Object> serialized = new HashMap<>();

        map.forEach(((k, v) -> serialized.put(
            Serializer.serializeReference(types.get(0), k),
            Serializer.serializeReference(types.get(1), v)))
        );

        return serialized;
    }

    @Override
    public boolean isCompatibleWith(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }
}
