package ru.artorium.configs;

import ru.artorium.configs.formats.Format;
import ru.artorium.configs.formats.JSON;
import ru.artorium.configs.formats.YAML;
import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * A class that represents a configuration file.
 * Remember:
 * <br> 1. Your class MUST have a default no-args constructor.
 * <br> 2. You MUST initialize your fields with default values.
 * <br> 3. You must call reload() method in the constructor of your class, otherwise it won't load file values.
 * <br> 4. Use keywords: transient or final for fields that you don't want to be serialized.
 * <br>
 * <br> Serializable types:
 * <br> Primitives: Integer, Double, Boolean, String, Character
 * <br> Collections: List, Map
 * <br> Minecraft: ItemStack, Location, World, Kyori's Components
 * <br> Other: UUIDs, Arrays, Custom classes (require no-args constructor), Enums
 * <br>
 * <br> Example:
 * <pre>
 * {@code
 * @NoArgsConstructor
 * public class MyConfig extends Config {
 *     private String myField;
 *     private final int myTransientField = 10;
 *
 *     public MyConfig(JavaPlugin plugin) {
 *         super("my-config.yml", plugin.getDataFolder().getPath());
 *         this.reload();
 *     }
 * }
 * </pre>
 */
@Getter
@NoArgsConstructor
public abstract class Config {

    private transient File file;
    private transient Format format;
    private transient Map<String, Object> fileMap;

    public Config(String fileName, JavaPlugin plugin) {
        this(fileName, plugin.getDataFolder().getPath());
    }

    public Config(String fileName, String path) {
        this.file = new File(path, fileName);
        this.format = fileName.endsWith(".yaml") || fileName.endsWith(".yml") ? new YAML() : new JSON();
        this.fileMap = this.format.readFile(this.file);
        this.reload();
    }

    public void reload() {
        if (!this.file.exists()) {
            this.createFile();
        }

        this.setMissingValues();
        this.updateValues();
    }

    private void setMissingValues() {
        this.fileMap = this.format.readFile(this.file);
        final Map<String, Object> serialized = Serializer.OBJECT.serialize(new TypeReference(this), this.getTemplate());
        serialized.forEach((key, value) -> this.fileMap.putIfAbsent(key, value));
        this.format.writeFile(this.file, this.fileMap);
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
        Utils.getFields(this).forEach(field -> {
            final String key = field.getName().replaceAll("(?=[A-Z0-9])", "-").toLowerCase();

            try {
                field.setAccessible(true);
                final Object deserializedValue = Serializer.deserialize(field, this.fileMap.get(key));
                field.set(this, deserializedValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update the value of the field " + field.getName(), e);
            } finally {
                field.setAccessible(false);
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
