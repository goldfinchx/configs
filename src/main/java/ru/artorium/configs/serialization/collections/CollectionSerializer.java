package ru.artorium.configs.serialization.collections;

import java.util.ArrayList;
import java.util.Collection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.SerializerType;

public class CollectionSerializer implements Serializer<Collection, JSONObject> {

    @Override
    public JSONObject serialize(Class<?> fieldClass, Object object) {
        final JSONObject json = new JSONObject();
        final Collection collection = (Collection) object;
        final Class<?> genericClass = ((Collection<?>) object).stream().findFirst().get().getClass();
        final JSONArray array = new JSONArray();

        collection.forEach(value -> array.add(Serializer.serialize(fieldClass, genericClass, value)));
        json.put("_type", genericClass.getTypeName());
        json.put("values", array);
        return json;
    }


    @Override
    public Collection deserialize(Class<?> fieldClass, Object object) {
        final JSONObject json = (JSONObject) object;
        final Class<?> genericClass;
        try {
            genericClass = Class.forName((String) json.get("_type"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        final JSONArray array = (JSONArray) json.get("values");
        return new ArrayList(array.stream().map(val -> Serializer.deserialize(fieldClass, genericClass, val)).toList());
    }

}
