package ru.artorium.configs.serialization.collections;

import ru.artorium.configs.serialization.TypeReference;
import ru.artorium.configs.serialization.Serializer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.json.simple.JSONArray;

public class ArraySerializer implements Serializer<Object[], Collection<?>> {

    @Override
    public Collection<?> serialize(TypeReference typeReference, Object[] object) {
        final List<?> list = Arrays.asList(object);
        final Class<?> targetClass = typeReference.clazz().getComponentType();
        final JSONArray json = new JSONArray();

        list.forEach(value -> json.add(Serializer.serialize(targetClass, value)));
        return json;
    }

    @Override
    public Object[] deserialize(TypeReference typeReference, Collection<?> serialized) {
        final Class<?> targetClass = typeReference.clazz().getComponentType();
        return serialized.stream().map(value -> Serializer.deserialize(targetClass, value)).toArray();
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.isArray();
    }
}
