package ru.artorium.configs.serialization.collections;

import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;
import ru.artorium.configs.serialization.Serializer;

public class ArraySerializer implements Serializer<Object[], JSONArray> {

    @Override
    public Object[] deserialize(Class<?> fieldClass, Object object) {
        final JSONArray array = (JSONArray) object;
        final Class<?> genericClass = fieldClass.getComponentType();

        return array.stream().map(o -> Serializer.deserialize(genericClass, genericClass, o)).toArray();
    }

    @Override
    public JSONArray serialize(Class<?> fieldClass, Object object) {
        final JSONArray json = new JSONArray();
        final List<?> list = Arrays.asList((Object[]) object);
        final Class<?> genericClass = object.getClass().getComponentType();

        list.forEach(value -> json.add(Serializer.serialize(genericClass, genericClass, value)));
        return json;
    }

}
