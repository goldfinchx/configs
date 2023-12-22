### Introduction
This is a simple library for creating config files for your plugins. It uses reflection to get and
set values from the config file. You can also create your custom serializers for your custom classes.

### Default Serializable Types
- Primitives
- Enums
- Map
- Collection
- ItemStack
- Location


### Creating your own config class
Its super easy to create a new config class, just extend the Config class and add your fields
and provide a default constructor with config template. That's it.
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

    @Range(min = 0, max = 100)
    private int rangedValue = 10;

    public TestConfig(JavaPlugin plugin) {
        super("fileName", plugin.getDataFolder().getPath());

        // get updated values from the file and use this in you reload command
        this.reload();     
    }

    // provide config with default values
    @Override
    Config getTemplate() {
        return new TestConfig();
    }
}
```
