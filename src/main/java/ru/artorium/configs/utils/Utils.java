package ru.artorium.configs.utils;

import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.artorium.configs.serialization.SerializerType;

public class Utils {

    public static JSONObject convertFile(File file) {
        final JSONParser parser = new JSONParser();

        if (file.length() == 0) {
            return new JSONObject();
        }

        try {
            return (JSONObject) parser.parse(new FileReader(file, StandardCharsets.UTF_8));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeFile(File file, JSONObject json) {
        final String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(json);

        try (final FileWriter writer = new FileWriter(file)) {
            writer.write(jsonString);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Object serialize(Object object) {
        final Class<?> objectClass = getObjectType(object.getClass(), object);
        final SerializerType serializerType = getSerializerType(objectClass);
        final Object serializedObject = serializerType.getSerializer().serialize(object);

        if (serializerType.isRequireTypification()) {
            ((JSONObject) serializedObject).put("_type", object.getClass().getName());
        }

        return serializedObject;
    }

    public static Object deserialize(Class fieldClass, Object object) {
        final Class objectClass = getObjectType(fieldClass, object);
        final SerializerType serializerType = getSerializerType(objectClass);

        return serializerType.getSerializer().deserialize(objectClass, object);
    }

    private static SerializerType getSerializerType(Class objectClass) {
        return Arrays.stream(SerializerType.values())
            .filter(type -> type.getFrom().equals(objectClass))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Unknown type: " + objectClass));
    }

    private static Class getObjectType(Class fieldClass, Object object) {
        if (object instanceof Number || (object instanceof String && ClassUtils.isNumber((String) object))) {
            if (fieldClass.equals(Integer.class) || fieldClass.equals(int.class)) {
                return Integer.class;
            } else if (fieldClass.equals(Double.class) || fieldClass.equals(double.class))  {
                return Double.class;
            } else if (fieldClass.equals(Long.class) || fieldClass.equals(long.class)) {
                return Long.class;
            } else if (fieldClass.equals(Float.class) || fieldClass.equals(float.class)) {
                return Float.class;
            } else if (fieldClass.equals(Byte.class) || fieldClass.equals(byte.class)) {
                return Byte.class;
            } else if (fieldClass.equals(Short.class) || fieldClass.equals(short.class)) {
                return Short.class;
            }
        }

        if (object instanceof JSONObject jsonObject && jsonObject.containsKey("_type")) {
            try {
                return Class.forName((String) jsonObject.get("_type"));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if (fieldClass.isEnum()) {
            return Enum.class;
        }

        if (object instanceof String && fieldClass.equals(String.class)) {
            return String.class;
        }

        if (ClassUtils.getCollectionType(object) != null) {
            return ClassUtils.getCollectionType(object);
        }

        return object.getClass();
    }

}
