package ru.artorium.configs.serialization.other;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.json.simple.JSONObject;
import ru.artorium.configs.Utils;
import ru.artorium.configs.annotations.Ignore;
import ru.artorium.configs.serialization.SpecificSerializer;

public class ObjectSerializer implements SpecificSerializer<Object, JSONObject> {

    @Override
    public Object deserialize(Class<?> classField, Object object) {
        final Map<String, Object> map = (Map) object;
        final Object instance;

        try {
            instance = classField.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        this.getFields(instance).forEach(objectField -> {
            try {
                objectField.set(instance, Utils.deserialize(objectField, map.get(objectField.getName())));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return instance;
    }

    @Override
    public JSONObject serialize(Object object) {
        final JSONObject jsonObject = new JSONObject();

        this.getFields(object).forEach(objectField -> {
            try {
                jsonObject.put(objectField.getName(), Utils.serialize(objectField, objectField.get(object)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return jsonObject;
    }

    private List<Field> getFields(Object object) {
        return FieldUtils.getAllFieldsList(object.getClass()).stream()
            .filter(field -> !field.isAnnotationPresent(Ignore.class))
            .peek(field -> field.setAccessible(true))
            .collect(Collectors.toList());

    }

}
