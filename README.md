### Introduction
This is a simple library for creating config files for your plugins. It uses reflection to get and
set values from the config file. You can also create custom serializers for your custom classes.

### Default Serializable Types
- Primitives
- Objects
- Enums
- Maps
- Collections
- ItemStacks
- Locations
- Adventure Text Components


### Creating your config class
It is super easy to create a new config class, just extend the Config class, add your fields, and provide a default
no-args constructor. That's it.
```java
@Getter
@NoArgsConstructor
public class TestConfig extends Config {

    private String value = "This is String value!";
    private Material enumValue = Material.DIAMOND;
    private ItemStack itemStackValue = new ItemStack(Material.STONE, 1);
    private Map<Integer, Material> mapValue = Map.of(1, Material.STONE);

    @Ignore
    private final String ignoredValue;

    private int rangedValue = 10;

    public TestConfig(JavaPlugin plugin) {
        // Formats available: JSON and YAML (change the type of the file to x.json or x.yaml/x.yml)
        super("fileName.yml", plugin.getDataFolder().getPath());

        // get updated values from the file and use this in your reload command
        this.reload();     
    }
}
```
