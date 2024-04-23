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

### Adding to your project
#### Important
After you added the library as a dependency, you have 2 choices:
1. Add the library as a plugin to your server
```yaml
depends: [Configs]
```
2. Shade the library into your code

#### Gradle
```groovy
repositories {
    maven {
        url 'https://jitpack.io'
    }
}

dependencies {
    compileOnly 'com.github.goldfinchx:configs:1.1.3'
}
```

#### Maven
```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.goldfinchx</groupId>
            <artifactId>configs</artifactId>
            <version>1.1.3</version>
        </dependency>
    </dependencies>
```




### Creating your config class
It is super easy to create a new config class, just extend the Config class, add your fields, and provide a default
no-args constructor. That's it.
```java
@Getter
@NoArgsConstructor // you MUST add no-args constructor
public class TestConfig extends Config {

    private String value = "This is String value!";
    private Material enumValue = Material.DIAMOND;
    private ItemStack itemStackValue = new ItemStack(Material.STONE, 1);
    private Map<Integer, Material> mapValue = Map.of(1, Material.STONE);

    private final String ignoredValue;
    private transient int ignoredValue2;

    public TestConfig(JavaPlugin plugin) {
        // Formats available: JSON and YAML (change the type of the file to x.json or x.yaml/x.yml)
        super("fileName.yml", plugin.getDataFolder().getPath());

        // you MUST add this command to get updated values from the file and use this in your reload command
        this.reload();     
    }
}
```
