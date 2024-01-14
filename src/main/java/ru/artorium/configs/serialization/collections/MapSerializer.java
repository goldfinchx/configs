package ru.artorium.configs.serialization.collections;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import oshi.util.tuples.Pair;
import ru.artorium.configs.Utils;
import ru.artorium.configs.serialization.GenericSerializer;

public class MapSerializer implements GenericSerializer<Map, JSONObject> {

    @Override
    public JSONObject serialize(Field field, Object object) {
        final JSONObject json = new JSONObject();
        final Pair<Class<?>, Class<?>> genericTypes = this.getGenericTypes(field);
        final Class<?> keyType = genericTypes.getA();
        final Class<?> valueType = genericTypes.getB();
        final Map map = (Map) object;

        map.forEach(((k, v) -> json.put(Utils.serializeSpecific(keyType, k), Utils.serializeSpecific(valueType, v))));
        return json;
    }

    @Override
    public Map deserialize(Field field, Object object) {
        final JSONObject json = (JSONObject) object;
        final Pair<Class<?>, Class<?>> genericTypes = this.getGenericTypes(field);
        final Class<?> keyType = genericTypes.getA();
        final Class<?> valueType = genericTypes.getB();
        final Map map = new HashMap<>();

        json.forEach((k, v) -> map.put(Utils.serializeSpecific(keyType, k), Utils.serializeSpecific(valueType, v)));
        return new HashMap(Collections.checkedMap(map, keyType, valueType));
    }

    private Pair<Class<?>, Class<?>> getGenericTypes(Field field) {
        final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        final Class<?> keyType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        final Class<?> valueType = (Class<?>) parameterizedType.getActualTypeArguments()[1];
        return new Pair(keyType, valueType);
    }

}
