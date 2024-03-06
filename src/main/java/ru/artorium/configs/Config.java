package ru.artorium.configs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.artorium.configs.annotations.Ignore;
import ru.artorium.configs.formats.Format;
import ru.artorium.configs.formats.JSON;
import ru.artorium.configs.formats.YAML;
import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;

@Getter
@NoArgsConstructor
public abstract class Config {

    @Ignore
    private File file;
    @Ignore
    private Format format;
    @Ignore
    private LinkedHashMap<String, Object> map;
    @Ignore
    private TypeReference typeReference;

    public Config(String fileName, String path) {
        this.file = new File(path, fileName);
        this.format = fileName.endsWith(".yaml") || fileName.endsWith(".yml") ? new YAML() : new JSON();
        this.map = new LinkedHashMap<>(this.format.readFile(this.file));
        this.typeReference = new TypeReference(this.getClass());
        this.reload();
    }

    public void reload() {
        if (!this.file.exists()) {
            this.createFile();
        }

        this.fillMissingValues();
        this.updateValues();
    }

    private void fillMissingValues() {
        final Map<String, Object> serialized = (Map<String, Object>) Serializer.serialize(this, this.typeReference, this.getTemplate());
        serialized.forEach((key, value) -> this.map.putIfAbsent(key, value));
        this.format.writeFile(this.file, this.map);
    }

    private Config getTemplate() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            System.err.println("Cannot create an instance of " + this.getClass().getName() + " class, please check if it has a default no args constructor");
            throw new RuntimeException(e);
        }
    }

    private void updateValues() {
        final Object deserialized = Serializer.deserialize(this, this.typeReference, this.map);
        this.getFields().forEach(field -> {
            try {
                field.set(this, field.get(deserialized));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<Field> getFields() {
        return Arrays.stream(FieldUtils.getAllFields(this.getClass()))
            .filter(field -> !field.isAnnotationPresent(Ignore.class))
            .peek(field -> field.setAccessible(true))
            .collect(Collectors.toList());
    }

    private void createFile() {
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
        }

        try {
            this.file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
