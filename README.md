
### Introduction
This is a simple library for creating config files for your plugins. It uses reflection to get and
set values from the config file. You can also create your own serializers for your classes and add your own file formats.

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
    compileOnly 'com.github.goldfinchx:configs:1.1.4'
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
```
```xml
    <dependencies>
        <dependency>
            <groupId>com.github.goldfinchx</groupId>
            <artifactId>configs</artifactId>
            <version>1.1.4</version>
        </dependency>
    </dependencies>
```


---

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

### Registering your file format
#### Create format class
```java
public class MyFormat implements Format {  

	@Override  
    public void writeFile(File file, Map<String, Object> map) {  
	    // write the map into 'file'      
    }  
  
    @Override  
    public Map<String, Object> readFile(File file) {  
		// Please note that you should not deserialize 
		// values from a file (using serializers). You 
		// just have to return a map of the original 
		// values from the file
        Map<String, Object> result = new HashMap<>();
        return result;  
    }  
}
```
#### Register format
[*MIME types*](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types)
- Find out the MIME type of your format (or make one up if it's your own)
  (f.e.: for `JSON` is `application/json`, for `YAML` is `application/yaml`)
- Prepare a list of file formats of your format
  (f.e.: for `JSON` is only `.json`, for `YAML` is `.yaml` and `.yml`)
  **Please note! The type must be registered before using configs, otherwise it will not be applied to them.**
```java
    @Override  
    public void onEnable() {  
        this.getLogger().info("Plugin has been enabled!");
        // The number of file types is unlimited (1-∞) 
        // You can specify file formats with or without a dot at the beginning - it doesn’t matter
        FormatFactory.getFactory().register(MyFormat.class, "mime", "fileFormat1", "fileFormat2" /* ... */);  
        
        // initialize configs
    }
```

---

### Registering your serializer
Let's imagine that you need to store one number in a YAML config file. So you have `TestConfig.java`:
```java
public class TestConfig extends Config {

    private MyStoreValue myValue = new MyStoreValue(3);

    public TestConfig(JavaPlugin plugin) {
        super("fileName.yml", plugin.getDataFolder().getPath());
    }
}
```
And `MyStoreValue.java`:
```java
public class MyStoreValue {
	public int myInt = 4;
	public MyStoreValue() {} // it is important
	public MyStoreValue(int myInt){
		this.myInt = myInt;
	}
}
```
When we run our plugin, we will see the following in the config:
```yaml
---
myValue:
  myInt: 3

```
But why create an extra line for `myInt`?
Let's create a serializer:
```java
class MyValueSerializer 
    /* 
      Serializer<
           The class we want to process, 
           The type that will be stored in the config
      > 
    */
	implements Serializer<MyStoreValue, Integer> {
    @Override  
	public MyStoreValue deserialize(TypeReference type, Integer serialized) {  
        return new MyStoreValue(serialized); // create our value
    }  
  
    @Override  
    public Integer serialize(TypeReference type, MyStoreValue object) {  
        return object.myInt; // get and return value 
    }  
}
```
Now we need to register, but! We must do this before using the config.
This is what our main plugin class will look like:
```java
public class MyPlugin extends JavaPlugin {  
    @Override  
  public void onEnable() {  
        this.getLogger().info("Plugin has been enabled!");  
        SerializerFactory.getFactory().register(new MyValueSerializer()); 
		
		TestConfig config = new TestConfig(this);
    }  
  
}
```
Now, when we run our plugin, we will see the following in the config:
```yaml
---
myValue: 3

```
