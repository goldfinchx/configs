package ru.artorium.configs.serialization.primitives;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import org.json.simple.JSONObject;
import ru.artorium.configs.Utils;
import ru.artorium.configs.annotations.Ignore;
import ru.artorium.configs.serialization.GenericSerializer;

public class ObjectSerializer implements GenericSerializer<Object, JSONObject> {

    @Override
    public Object deserialize(Field field, Object object) {
        final JSONObject json;

        if (object instanceof LinkedHashMap<?,?>) {
            json = new JSONObject((LinkedHashMap<?,?>) object);
        } else {
            json = (JSONObject) object;
        }

        final Object instance;

        try {
            instance = field.getType().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        this.getFields(instance).forEach(objectField -> {
            try {
                objectField.set(instance, Utils.deserialize(objectField, json.get(objectField.getName())));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return instance;
    }

    @Override
    public JSONObject serialize(Field field, Object object) {
        final JSONObject jsonObject = new JSONObject();

        this.getFields(object).forEach(objectField -> {
            try {
                jsonObject.put(objectField.getName(), Utils.serializeSpecific(objectField.getType(), objectField.get(object)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return jsonObject;
    }

    private List<Field> getFields(Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
            .filter(field -> !field.isAnnotationPresent(Ignore.class))
            .peek(field -> field.setAccessible(true))
            .toList();
    }

}
