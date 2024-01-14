package ru.artorium.configs.serialization;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.artorium.configs.serialization.collections.ArraySerializer;
import ru.artorium.configs.serialization.collections.CollectionSerializer;
import ru.artorium.configs.serialization.collections.MapSerializer;
import ru.artorium.configs.serialization.minecraft.ComponentSerializer;
import ru.artorium.configs.serialization.minecraft.ItemStackSerializer;
import ru.artorium.configs.serialization.minecraft.LocationSerializer;
import ru.artorium.configs.serialization.primitives.DoubleSerializer;
import ru.artorium.configs.serialization.primitives.EnumSerializer;
import ru.artorium.configs.serialization.primitives.FloatSerializer;
import ru.artorium.configs.serialization.primitives.IntegerSerializer;
import ru.artorium.configs.serialization.primitives.LongSerializer;
import ru.artorium.configs.serialization.primitives.ObjectSerializer;
import ru.artorium.configs.serialization.primitives.StringSerializer;

@Getter
@AllArgsConstructor
public enum SerializerType {
    STRING(String.class, String.class, new StringSerializer()),
    INTEGER(int.class, String.class, new IntegerSerializer()),
    DOUBLE(double.class, String.class, new DoubleSerializer()),
    LONG(long.class, String.class, new LongSerializer()),
    FLOAT(float.class, String.class, new FloatSerializer()),
    ENUM(Enum.class, String.class, new EnumSerializer(), true),
    ITEMSTACK(ItemStack.class, JSONObject.class, new ItemStackSerializer(), true),
    LOCATION(Location.class, JSONObject.class, new LocationSerializer(), true),
    COMPONENT(Component.class, String.class, new ComponentSerializer()),
    MAP(Map.class, JSONObject.class, new MapSerializer()),
    COLLECTION(Collection.class, JSONArray.class, new CollectionSerializer()),
    ARRAY(Object[].class, JSONArray.class, new ArraySerializer()),
    OBJECT(Object.class, JSONObject.class, new ObjectSerializer(), true);

    private final Class<?> from;
    private final Class<?> to;
    private final Serializer serializer;
    private final boolean requireTypification;

    SerializerType(Class<?> from, Class<?> to, Serializer serializer) {
        this.from = from;
        this.to = to;
        this.serializer = serializer;
        this.requireTypification = false;
    }

    public static SerializerType getByClass(Class<?> fieldClass) {
        if (fieldClass.isEnum()) {
            fieldClass = Enum.class;
        }

        if (Collection.class.isAssignableFrom(fieldClass)) {
            fieldClass = Collection.class;
        }

        if (Map.class.isAssignableFrom(fieldClass)) {
            fieldClass = Map.class;
        }

        if (Component.class.isAssignableFrom(fieldClass)) {
            fieldClass = Component.class;
        }

        if (fieldClass.isArray()) {
            fieldClass = Object[].class;
        }

        final Class<?> finalFieldClass = fieldClass;
        return Arrays.stream(SerializerType.values())
            .filter(type -> type.getFrom().equals(finalFieldClass))
            .findFirst()
            .orElse(SerializerType.OBJECT);
    }

}