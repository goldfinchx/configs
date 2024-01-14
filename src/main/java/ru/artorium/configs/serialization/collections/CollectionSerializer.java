package ru.artorium.configs.serialization.collections;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import org.json.simple.JSONArray;
import ru.artorium.configs.Utils;
import ru.artorium.configs.serialization.GenericSerializer;

public class CollectionSerializer implements GenericSerializer<Collection, JSONArray> {

    @Override
    public JSONArray serialize(Field field, Object object) {
        final JSONArray array = new JSONArray();
        final Collection collection = (Collection) object;
        final Class<?> genericClass = this.getGenericType(field);

        collection.forEach(value -> array.add(Utils.serializeSpecific(genericClass, value)));
        return array;
    }


    @Override
    public Collection deserialize(Field field, Object object) {
        final JSONArray array = (JSONArray) object;
        final Class<?> genericClass = this.getGenericType(field);

        return new ArrayList(array.stream().map(val -> Utils.deserializeSpecific(genericClass, val)).toList());
    }

    private Class<?> getGenericType(Field field) {
        final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

}
