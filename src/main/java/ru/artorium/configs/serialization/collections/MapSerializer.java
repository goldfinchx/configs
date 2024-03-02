package ru.artorium.configs.serialization.collections;

import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.Serializer.Generic;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

public class MapSerializer implements Generic<Map<?, ?>, Map<?, ?>> {

    @Override
    public Map deserialize(Field field, Map<?, ?> serialized) {
        final Map.Entry<Class<?>, Class<?>> genericTypes = this.getGenericTypes(field);
        final Class<?> keyType = genericTypes.getKey();
        final Class<?> valueType = genericTypes.getValue();

        return serialized.entrySet()
            .stream()
            .collect(
                HashMap::new,
                (map, entry) -> map.put(
                    Serializer.deserialize(keyType, entry.getKey()),
                    Serializer.deserialize(valueType, entry.getValue())
                ),
                HashMap::putAll
            );
    }

    @Override
    public Map<?, ?> serialize(Field field, Map<?, ?> map) {
        final Map.Entry<Class<?>, Class<?>> genericTypes = this.getGenericTypes(field);
        final Class<?> keyType = genericTypes.getKey();
        final Class<?> valueType = genericTypes.getValue();

        final JSONObject json = new JSONObject();
        map.forEach(((k, v) -> json.put(Serializer.serialize(keyType, k), Serializer.serialize(valueType, v))));

        return json;
    }

    private Map.Entry<Class<?>, Class<?>> getGenericTypes(Field field) {
        final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        final Class<?> keyType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        final Class<?> valueType = (Class<?>) parameterizedType.getActualTypeArguments()[1];
        return Map.entry(keyType, valueType);
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return Map.class.isAssignableFrom(clazz);
    }
}
