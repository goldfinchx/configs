package ru.artorium.configs.serialization.collections;

import ru.artorium.configs.serialization.TypeReference;
import ru.artorium.configs.serialization.Serializer;
import java.util.ArrayList;
import java.util.Collection;
import org.json.simple.JSONArray;

public class CollectionSerializer implements Serializer<Collection<?>, Collection<?>> {

    @Override
    public Collection<?> serialize(TypeReference typeReference, Collection<?> collection) {
        final Class<?> type = this.getGenericTypes(typeReference)[0];
        final JSONArray array = new JSONArray();
        collection.forEach(value -> array.add(Serializer.serialize(type, value)));
        return array;
    }

    @Override
    public Collection<?> deserialize(TypeReference typeReference, Collection<?> serialized) {
        final Class<?> type = this.getGenericTypes(typeReference)[0];
        return new ArrayList(serialized.stream().map(val -> Serializer.deserialize(type, val)).toList());
    }


    @Override
    public boolean isCompatibleWith(Class clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }
}
