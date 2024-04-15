package ru.artorium.configs;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.artorium.configs.formats.Format;
import ru.artorium.configs.formats.JSON;
import ru.artorium.configs.formats.YAML;
import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;

@Getter
@NoArgsConstructor
public abstract class Config implements Serializable {

    private transient File file;
    private transient Format format;
    private transient Map<String, Object> map;

    public Config(String fileName, String path) {
        this.file = new File(path, fileName);
        this.format = fileName.endsWith(".yaml") || fileName.endsWith(".yml") ? new YAML() : new JSON();
        this.map = this.format.readFile(this.file);
        this.reload();
    }

    public Config(String fileName, JavaPlugin plugin) {
        this(fileName, plugin.getDataFolder().getPath());
    }

    public void reload() {
        if (!this.file.exists()) {
            this.createFile();
        }

        this.setMissingValues();
        this.updateValues();
    }

    private void setMissingValues() {
        this.map = this.format.readFile(this.file);
        final Map<String, Object> serialized = Serializer.OBJECT.serialize(new TypeReference(this), this.getTemplate());
        serialized.forEach((key, value) -> this.map.putIfAbsent(key, value));
        this.format.writeFile(this.file, this.map);
    }

    private Config getTemplate() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to create a new instance of the config class", e);
            Bukkit.getLogger().log(Level.SEVERE, "Please make sure that the config class has a default no-args constructor");
            throw new RuntimeException(e);
        }
    }

    private void updateValues() {
        Utils.getFields(this.getClass()).forEach(field -> {
            try {
                field.set(this, this.map.get(field.getName()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update the value of the field " + field.getName(), e);
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

}
