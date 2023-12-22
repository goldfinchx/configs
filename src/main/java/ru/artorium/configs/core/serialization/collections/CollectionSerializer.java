package ru.artorium.configs.core.serialization.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.json.simple.JSONArray;
import ru.artorium.configs.core.serialization.Serializer;
import ru.artorium.configs.utils.Utils;

public class CollectionSerializer implements Serializer<Collection, JSONArray> {

    @Override
    public Collection deserialize(Class fieldClass, Object object) {
        final JSONArray json = (JSONArray) object;
        final List<?> list = json.stream().map(val -> Utils.deserialize(fieldClass, val)).toList();
        return new ArrayList(Collections.checkedList(list, fieldClass));
    }

    @Override
    public JSONArray serialize(Object object) {
        final JSONArray json = new JSONArray();
        final Collection collection = (Collection) object;

        collection.forEach(value -> json.add(Utils.serialize(value)));
        return json;
    }
}
