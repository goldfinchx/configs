package com.goldfinch.configs.serialization.other;

import com.goldfinch.configs.Utils;
import com.goldfinch.configs.serialization.Serializer;
import com.goldfinch.configs.serialization.TypeReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectSerializer implements Serializer<Object, Map<String, Object>> {

    @Override
    public Object deserialize(TypeReference typeReference, Map<String, Object> serialized) {
        final Object instance;

        try {
            final Constructor c = typeReference.clazz().getConstructor();
            c.setAccessible(true);
            instance = c.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to create instance for: " + typeReference.clazz().getName() + ", make sure you provided no-args constructor for this class", e);
        }

        Utils.getSerializableFields(instance).forEach(field -> {
            final String key = field.getName().replaceAll("(?=[A-Z0-9])", "-").toLowerCase();
            final Object value = serialized.get(key);

            try {
                field.setAccessible(true);
                field.set(instance, Serializer.deserialize(field, value));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to deserialize field: " + field.getName() + " for object: " + instance.getClass().getSimpleName(), e);
            } finally {
                field.setAccessible(false);
            }
        });

        return instance;
    }

    @Override
    public LinkedHashMap<String, Object> serialize(TypeReference typeReference, Object object) {
        final LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        Utils.getSerializableFields(object).forEach(field -> {
            final String key = field.getName().replaceAll("(?=[A-Z0-9])", "-").toLowerCase();
            final Object value;

            try {
                field.setAccessible(true);
                value = Serializer.serialize(field, field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to serialize field: " + field.getName() + " for object: " + object.getClass().getSimpleName(), e);
            } finally {
                field.setAccessible(false);
            }

            map.put(key, value);
        });

        return map;
    }


    @Override
    public boolean isCompatibleWith(Class clazz) {
        return false;
    }

}
