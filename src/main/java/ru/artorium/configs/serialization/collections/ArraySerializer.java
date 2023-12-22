package ru.artorium.configs.serialization.collections;

import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;
import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.utils.Utils;

public class ArraySerializer implements Serializer<Object[], JSONArray> {

    @Override
    public Object[] deserialize(Class fieldClass, Object object) {
        final JSONArray array = (JSONArray) object;
        return array.stream().map(o -> Utils.deserialize(fieldClass, o)).toArray();
    }

    @Override
    public JSONArray serialize(Object object) {
        final JSONArray json = new JSONArray();
        final List list = Arrays.asList((Object[]) object);
        list.forEach(value -> json.add(Utils.serialize(value)));
        return json;
    }
}
