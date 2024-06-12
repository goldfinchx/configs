package com.goldfinch.configs.serialization.collections;

import com.goldfinch.configs.serialization.Serializer;
import com.goldfinch.configs.serialization.TypeReference;
import org.json.simple.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArraySerializer implements Serializer<Object, Collection<?>> {

    @Override
    public Collection<?> serialize(TypeReference typeReference, Object array) {
        final List<Object> list = new ArrayList<>();
        if (array.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(array); i++) {
                list.add(Array.get(array, i));
            }
        }

        final Class<?> targetClass = typeReference.clazz().getComponentType();
        final JSONArray json = new JSONArray();

        //noinspection unchecked
        list.forEach(value -> json.add(Serializer.serialize(targetClass, value)));
        return json;
    }

    @Override
    public Object deserialize(TypeReference typeReference, Collection<?> serialized) {
        final Class<?> targetClass = typeReference.clazz().getComponentType();
        List<Object> objectList = serialized.stream().map(value -> Serializer.deserialize(targetClass, value)).toList();
        Object resultArray = Array.newInstance(targetClass, objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            Array.set(resultArray, i, objectList.get(i));
        }
        return resultArray;
    }

    @Override
    public boolean isCompatibleWith(Class<?> clazz) {
        return clazz.isArray();
    }
}
