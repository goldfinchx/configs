package com.goldfinch.configs.serialization.collections;

import com.goldfinch.configs.serialization.SerializeType;
import com.goldfinch.configs.serialization.Serializer;
import com.goldfinch.configs.serialization.TypeReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.simple.JSONArray;

@SuppressWarnings("unchecked")
public class CollectionSerializer<T> implements Serializer<Collection<T>, Collection<T>> {

    @Override
    public Collection<T> serialize(TypeReference typeReference, Collection<T> collection) {
        final SerializeType type = this.getSerializeType(typeReference).getSubTypes().get(0);
        final JSONArray array = new JSONArray();
        collection.forEach(value -> array.add(Serializer.serializeReference(type, value)));
        return array;
    }

    @Override
    public Collection<T> deserialize(TypeReference typeReference, Collection<T> serialized) {
        final SerializeType type = this.getSerializeType(typeReference).getSubTypes().get(0);
        List<T> list = serialized.stream().map(val -> (T) Serializer.deserializeReference(type, val)).toList();
        if(typeReference.clazz().isAssignableFrom(ArrayList.class)){ // if class can be ArrayList
            return new ArrayList<>(list);
        }
        // else. f.e.: java.util.LinkedList<E>
        try {
            Constructor<? extends Collection<T>> constructor = (Constructor<? extends Collection<T>>) typeReference.clazz().getConstructor();
            constructor.setAccessible(true);
            Collection<T> collection = constructor.newInstance();
            collection.addAll(list);
            return collection;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean isCompatibleWith(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }
}
