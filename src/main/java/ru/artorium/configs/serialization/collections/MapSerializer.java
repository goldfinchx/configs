package ru.artorium.configs.serialization.collections;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import ru.artorium.configs.serialization.Serializer;

public class MapSerializer implements Serializer<Map, JSONObject> {

    @Override
    public JSONObject serialize(Class<?> fieldClass, Object object) {
        final JSONObject json = new JSONObject();
        final Map map = (Map) object;
        final Map.Entry entry = (Map.Entry) map.entrySet().stream().findFirst().get();
        final Class<?> keyType = entry.getKey().getClass();
        final Class<?> valueType = entry.getValue().getClass();

        json.put("_keyType", keyType.getTypeName());
        json.put("_valueType", valueType.getTypeName());

        map.forEach(((k, v) -> json.put(Serializer.serialize(keyType, keyType, k), Serializer.serialize(valueType, valueType, v))));
        return json;
    }

    @Override
    public Map deserialize(Class<?> fieldClass, Object object) {
        final JSONObject json = (JSONObject) object;

        final Class<?> keyType;
        final Class<?> valueType;

        try {
            keyType = Class.forName((String) json.get("_keyType"));
            valueType = Class.forName((String) json.get("_valueType"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        final Map map = new HashMap<>();
        json.forEach((k, v) -> {
            if (k.equals("_keyType") || k.equals("_valueType")) {
                return;
            }

            map.put(Serializer.deserialize(keyType, keyType, k), Serializer.deserialize(valueType, valueType, v));
        });
        return new HashMap(Collections.checkedMap(map, keyType, valueType));
    }

}
