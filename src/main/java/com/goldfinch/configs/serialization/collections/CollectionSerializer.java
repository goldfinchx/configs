package com.goldfinch.configs.serialization.collections;

import com.goldfinch.configs.serialization.Serializer;
import com.goldfinch.configs.serialization.TypeReference;
import java.util.ArrayList;
import java.util.Collection;
import org.json.simple.JSONArray;

public class CollectionSerializer implements Serializer<Collection<?>, Collection<?>> {

    @Override
    public Collection<?> serialize(TypeReference typeReference, Collection<?> collection) {
        final Class<?> type = this.getGenericTypes(typeReference)[0];
        final JSONArray array = new JSONArray();
        //noinspection unchecked
        collection.forEach(value -> array.add(Serializer.serialize(type, value)));
        return array;
    }

    @Override
    public Collection<?> deserialize(TypeReference typeReference, Collection<?> serialized) {
        final Class<?> type = this.getGenericTypes(typeReference)[0];
        return new ArrayList<>(serialized.stream().map(val -> Serializer.deserialize(type, val)).toList());
    }


    @Override
    public boolean isCompatibleWith(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }
}
