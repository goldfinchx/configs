package com.goldfinch.configs;

import com.goldfinch.configs.formats.Format;
import com.goldfinch.configs.serialization.Serializer;
import com.goldfinch.configs.serialization.SerializerFactory;
import com.goldfinch.configs.serialization.TypeReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Map;
import java.util.logging.Level;


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
        this.validateConfigClass();
        this.file = new File(path, fileName);
        this.format = Format.factory().getFormatFromFileName(fileName);
        if (this.format == null){
            throw new NullPointerException("File format " + fileName + " not found. Maybe you forgot to specify the dependency in plugin.yml?");
        }
        this.reload();
    }

    public void reload() {
        this.createFile();

        this.setMissingValues();
        this.updateValues();
    }

    private void validateConfigClass(){
        try {
            this.getClass().getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Constructor without arguments not found.");
        }
    }

    /**
     * Save non-existent values into the config file
     */
    private void setMissingValues() {
        // read the file
        this.fileMap = this.format.readFile(this.file);
        // serialize config class
        final Map<String, Object> serialized = SerializerFactory.OBJECT.serialize(new TypeReference(this), this.getTemplate());
        // remember the initial number of keys
        int keys = this.fileMap.keySet().size();
        // insert non-existent values
        serialized.forEach(this.fileMap::putIfAbsent);
        // check for map changes
        if(keys != this.fileMap.keySet().size()){ // prevent the load on the drive
            // save into file
            this.format.writeFile(this.file, this.fileMap);
        }
    }

    private Config getTemplate() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            ConfigsPlugin.getBukkitLogger().log(Level.SEVERE, "Failed to create a new instance of the config class", e);
            ConfigsPlugin.getBukkitLogger().log(Level.SEVERE, "Please make sure that the config class has a default no-args constructor");
            throw new RuntimeException(e);
        }
    }

    private void updateValues() {
        Utils.getSerializableFields(this).forEach(field -> {
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
        try {
            if (this.file.getParentFile().isFile() || this.file.isDirectory()){
                throw new IllegalStateException("Invalid config path.");
            }
            if (!this.file.getParentFile().isDirectory()) {
                Files.createDirectories(this.file.getParentFile().toPath());
            }
            if (!this.file.isFile()) {
                Files.createFile(this.file.toPath());
            }
        } catch (IOException exception){
            throw new RuntimeException("Failed initialize " + this.getClass().getName() + "'s file system.", exception);
        }
    }

}
