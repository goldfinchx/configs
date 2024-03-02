package ru.artorium.configs;

import ru.artorium.configs.annotations.Ignore;
import ru.artorium.configs.formats.Format;
import ru.artorium.configs.formats.JSON;
import ru.artorium.configs.formats.YAML;
import ru.artorium.configs.serialization.Serializer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class Config {

    @Ignore private File file;
    @Ignore private Format format;
    @Ignore private LinkedHashMap<String, Object> map;

    public Config(String fileName, String path) {
        this.file = new File(path, fileName);
        this.format = fileName.endsWith(".yaml") || fileName.endsWith(".yml") ? new YAML() : new JSON();
        this.map = new LinkedHashMap<>(this.format.readFile(this.file));
        
        this.reload();
    }

    public void reload() {
        if (!this.file.exists()) {
            this.createFile();
        }

        this.fillMissingFields();
        Serializer.OBJECT.deserialize(this.getClass(), this.map);
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

    private void fillMissingFields() {
        final Map<String, Object> serialized = (LinkedHashMap<String, Object>) Serializer.OBJECT.serialize(this.getTemplate());
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
}
