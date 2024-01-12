package ru.artorium.configs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import ru.artorium.configs.utils.Utils;

@Getter
@NoArgsConstructor
public abstract class Config {

    private JSONObject json;
    private File file;

    public Config(String fileName, String path) {
        this.file = new File(path, fileName + ".json");
        this.json = Utils.convertFile(this.file);
        this.reload();
    }

    public void reload() {
        if (!this.file.exists()) {
            this.createFile();
        }

        this.fillMissingFields();

        Utils.getFields(this).forEach(field -> {
            try {
                field.set(this, Utils.deserialize(field.getType(), this.json.get(field.getName())));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
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
        final Config defaultConfig = this.getTemplate();

        Utils.getFields(this).forEach(field -> {
            this.json.computeIfAbsent(field.getName(), $ -> {
                try {
                    return Utils.serialize(field.getType(), field.get(defaultConfig));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });

        });

        Utils.writeFile(this.file, this.json);
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
