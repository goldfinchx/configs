package ru.artorium.configs.serialization.collections;

import ru.artorium.configs.serialization.TypeReference;
import ru.artorium.configs.serialization.Serializer;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

public class MapSerializer implements Serializer<Map<?, ?>, Map<?, ?>> {

    @Override
    public Map deserialize(TypeReference typeReference, Map<?, ?> serialized) {
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
        final JSONObject json = new JSONObject();
        map.forEach(((k, v) -> json.put(
            Serializer.serialize(types[0], k),
            Serializer.serialize(types[1], v)))
        );

        return json;
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return Map.class.isAssignableFrom(clazz);
    }
}
