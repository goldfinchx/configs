package ru.artorium.configs.serialization.collections;

import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;
import ru.artorium.configs.Utils;
import ru.artorium.configs.serialization.SpecificSerializer;

public class ArraySerializer implements SpecificSerializer<Object[], JSONArray> {

    @Override
    public Object[] deserialize(Class<?> fieldClass, Object object) {
        final JSONArray array = (JSONArray) object;
        final Class<?> genericClass = object.getClass().getComponentType();

        return array.stream().map(value -> Utils.deserializeSpecific(genericClass, value)).toArray();
    }

    @Override
    public JSONArray serialize(Object object) {
        final JSONArray json = new JSONArray();
        final List<?> list = Arrays.asList((Object[]) object);
        final Class<?> genericClass = object.getClass().getComponentType();

        list.forEach(value -> json.add(Utils.deserializeSpecific(genericClass, value)));
        return json;
    }

}
