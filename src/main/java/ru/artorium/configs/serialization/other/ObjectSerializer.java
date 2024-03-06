package ru.artorium.configs.serialization.other;

import ru.artorium.configs.serialization.TypeReference;
import ru.artorium.configs.annotations.Ignore;
import ru.artorium.configs.serialization.Serializer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ObjectSerializer implements Serializer<Object, Map<String, Object>> {

    @Override
    public Object deserialize(TypeReference typeReference, Map<String, Object> serialized) {
        final Object instance;

        try {
            instance = typeReference.clazz().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to create instance for a field: " + typeReference.clazz().getName() +
                                       ", make sure you provided no-args constructor for this class", e);
        }

        this.getFields(instance).forEach(field -> {
            final String key = field.getName().replaceAll("(?=[A-Z0-9])", "-").toLowerCase();
            final Object value = serialized.get(key);

            try {
                field.set(instance, Serializer.deserialize(this, new TypeReference(field), value));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to deserialize field: " + field.getName(), e);
            }
        });

        return instance;
    }

    @Override
    public LinkedHashMap<String, Object> serialize(TypeReference typeReference, Object object) {
        final LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        this.getFields(object).forEach(objectField -> {
            final String key = objectField.getName().replaceAll("(?=[A-Z0-9])", "-").toLowerCase();
            final Object value;

            try {
                value = Serializer.serialize(this, new TypeReference(objectField), objectField.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to serialize field: " + objectField.getName(), e);
            }

            map.put(key, value);
        });

        return map;
    }


    private List<Field> getFields(Object object) {
        return FieldUtils.getAllFieldsList(object.getClass())
            .stream()
            .filter(field -> !field.isAnnotationPresent(Ignore.class))
            .peek(field -> field.setAccessible(true))
            .toList();
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return false;
    }

}
