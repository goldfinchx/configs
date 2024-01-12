package ru.artorium.configs.serialization.collections;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;
import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.utils.Utils;

public class ArraySerializer implements Serializer<Object[], JSONArray> {

    @Override
    public Object[] deserialize(Class<?> fieldClass, Object object) {
        final JSONArray array = (JSONArray) object;
        final Class<?> genericClass = this.getArrayType(fieldClass);

        return array.stream().map(o -> Utils.deserialize(genericClass, o)).toArray();
    }

    @Override
    public JSONArray serialize(Class<?> fieldClass, Object object) {
        final JSONArray json = new JSONArray();
        final List<?> list = Arrays.asList((Object[]) object);
        final Class<?> genericClass = this.getArrayType(fieldClass);

        list.forEach(value -> json.add(Utils.serialize(genericClass, value)));
        return json;
    }

    private Class<?> getArrayType(Class<?> fieldClass) {
        final ParameterizedType parameterizedType = (ParameterizedType) fieldClass.getGenericSuperclass();
        return  (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

}
