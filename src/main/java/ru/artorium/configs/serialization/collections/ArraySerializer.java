package ru.artorium.configs.serialization.collections;

import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.Serializer.Specific;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.json.simple.JSONArray;

public class ArraySerializer implements Specific<Object[], Collection<?>> {

    @Override
    public Collection<?> serialize(Object[] object) {
        final List<?> list = Arrays.asList(object);
        final Class<?> genericClass = object.getClass().getComponentType();
        final JSONArray json = new JSONArray();

        list.forEach(value -> json.add(Serializer.serialize(genericClass, value)));
        return json;
    }

    @Override
    public Object[] deserialize(Class<?> fieldClass, Collection<?> serialized) {
        return serialized.stream().map(value -> Serializer.deserialize(fieldClass.getComponentType(), value)).toArray();
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.isArray();
    }
}
