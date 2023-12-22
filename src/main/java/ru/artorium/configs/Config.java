package ru.artorium.configs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import ru.artorium.configs.annotations.Ignore;
import ru.artorium.configs.utils.Utils;

@Getter
@NoArgsConstructor
public abstract class Config {

    private String fileName;
    private File file;

    public Config(String fileName, String path) {
        this.fileName = fileName;
        this.file = new File(path, this.fileName + ".json");
        this.load();
    }

    public void reload() {
        final JSONObject json = Utils.convertFile(this.file);
        this.fillDefaults();

        for (final Field field : this.getFields()) {
            field.setAccessible(true);
            try {
                field.set(this, Utils.deserialize(field.getType(), json.get(field.getName())));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void load() {
        if (!this.file.exists()) {
            this.create();
        }

        if (this.file.length() == 0) {
            this.fillDefaults();
        }

        this.reload();
    }

    private void create() {
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
        }

        try {
            this.file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Field> getFields() {
        return Arrays.stream(this.getClass().getDeclaredFields())
            .filter(field -> !field.isAnnotationPresent(Ignore.class))
            .toList();
    }

    private void fillDefaults() {
        final JSONObject json = Utils.convertFile(this.file);
        final Config defaultConfig;

        try {
            defaultConfig = this.getClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Check if you added @NoArgsConstructor to your config class", e);
        }

        for (final Field field : this.getFields()) {
            field.setAccessible(true);

            if (json.containsKey(field.getName())) {
                return;
            }

            try {
                json.put(field.getName(), Utils.serialize(field.get(defaultConfig)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        this.save(json);
    }

    public void save(JSONObject json) {
        Utils.writeFile(this.file, json);
    }

}
