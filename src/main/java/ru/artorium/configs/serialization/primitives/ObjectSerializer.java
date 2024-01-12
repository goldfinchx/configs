package ru.artorium.configs.serialization.primitives;

import java.lang.reflect.InvocationTargetException;
import org.json.simple.JSONObject;
import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.utils.Utils;

public class ObjectSerializer implements Serializer<Object, JSONObject> {

    @Override
    public Object deserialize(Class<?> fieldClass, Object object) {
        final JSONObject jsonObject = (JSONObject) object;
        final Object instance;

        try {
            instance = fieldClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }



        Utils.getFields(instance).forEach(objectField -> {
            try {
                objectField.set(instance, Utils.deserialize(objectField.getType(), jsonObject.get(objectField.getName())));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return instance;
    }

    @Override
    public JSONObject serialize(Class<?> fieldClass, Object object) {
        final JSONObject jsonObject = new JSONObject();

        Utils.getFields(object).forEach(objectField -> {
            try {
                jsonObject.put(objectField.getName(), Utils.serialize(objectField.getType(), objectField.get(object)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return jsonObject;
    }

}
