package ru.artorium.configs.serialization.collections;

import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.Serializer.Generic;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import org.json.simple.JSONArray;

public class CollectionSerializer implements Generic<Collection<?>, Collection<?>> {

    @Override
    public Collection<?> serialize(Field field, Collection<?> collection) {
        final Class<?> genericClass = this.getGenericType(field);
        final JSONArray array = new JSONArray();
        collection.forEach(value -> array.add(Serializer.serialize(genericClass, value)));
        return array;
    }

    @Override
    public Collection<?> deserialize(Field field, Collection<?> serialized) {
        final Class<?> genericClass = this.getGenericType(field);
        return new ArrayList(serialized.stream().map(val -> Serializer.deserialize(genericClass, val)).toList());
    }

    private Class<?> getGenericType(Field field) {
        final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }
}
