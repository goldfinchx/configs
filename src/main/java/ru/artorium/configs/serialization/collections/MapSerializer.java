package ru.artorium.configs.serialization.collections;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.utils.Utils;

public class MapSerializer implements Serializer<Map, JSONObject> {

    @Override
    public Map deserialize(Class fieldClass, Object object) {
        final JSONObject json = (JSONObject) object;
        final Class<?> keyType;
        final Class<?> valueType;

        final java.lang.reflect.Type superClassType = ((Class<? extends Map.Entry<?, ?>>) fieldClass).getGenericSuperclass();

        if (superClassType instanceof ParameterizedType parameterizedType) {
            final java.lang.reflect.Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (typeArguments.length < 1) {
                throw new ClassFormatError("You need to provide your Class<? extends Entry<?, ?>> in Class fieldClass parameter!");
            }

            keyType = (Class<?>) typeArguments[0];
            valueType = (Class<?>) typeArguments[1];
        } else {
            return new HashMap();
        }

        final Map map = new HashMap<>();
        json.forEach((k, v) -> map.put(Utils.deserialize(keyType, k), Utils.deserialize(valueType, v)));
        return new HashMap(Collections.checkedMap(map, keyType, valueType));
    }

    @Override
    public JSONObject serialize(Object object) {
        final JSONObject json = new JSONObject();
        final Map map = (Map) object;

        map.forEach(((k, v) -> json.put(Utils.serialize(k), Utils.serialize(v))));
        return json;
    }
}
