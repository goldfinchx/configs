package ru.artorium.configs.utils;

import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.artorium.configs.annotations.Ignore;
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

    public static Object serialize(Class<?> fieldClass, Object object) {
        return getSerializerType(fieldClass).getSerializer().serialize(fieldClass, object);
    }

    public static Object deserialize(Class<?> fieldClass, Object object) {
        return getSerializerType(fieldClass).getSerializer().deserialize(fieldClass, object);
    }

    private static SerializerType getSerializerType(Class<?> fieldClass) {
        if (Collection.class.isAssignableFrom(fieldClass)) {
            fieldClass = Collection.class;
        }

        final Class<?> finalFieldClass = fieldClass;
        return Arrays.stream(SerializerType.values())
            .filter(type -> type.getFrom().equals(finalFieldClass))
            .findFirst()
            .orElse(SerializerType.OBJECT);
    }

    public static List<Field> getFields(Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
            .filter(field -> !field.isAnnotationPresent(Ignore.class))
            .peek(field -> field.setAccessible(true))
            .toList();
    }
}
