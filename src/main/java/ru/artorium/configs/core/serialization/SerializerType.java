package ru.artorium.configs.core.serialization;

import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.artorium.configs.core.serialization.collections.ArraySerializer;
import ru.artorium.configs.core.serialization.collections.CollectionSerializer;
import ru.artorium.configs.core.serialization.collections.MapSerializer;
import ru.artorium.configs.core.serialization.minecraft.ItemStackSerializer;
import ru.artorium.configs.core.serialization.minecraft.LocationSerializer;
import ru.artorium.configs.core.serialization.primitives.DoubleSerializer;
import ru.artorium.configs.core.serialization.primitives.EnumSerializer;
import ru.artorium.configs.core.serialization.primitives.FloatSerializer;
import ru.artorium.configs.core.serialization.primitives.IntegerSerializer;
import ru.artorium.configs.core.serialization.primitives.LongSerializer;
import ru.artorium.configs.core.serialization.primitives.StringSerializer;

@Getter
@AllArgsConstructor
public enum SerializerType {
    STRING(String.class, String.class, new StringSerializer()),
    INTEGER(Integer.class, String.class, new IntegerSerializer()),
    DOUBLE(Double.class, String.class, new DoubleSerializer()),
    LONG(Long.class, String.class, new LongSerializer()),
    FLOAT(Float.class, String.class, new FloatSerializer()),
    ENUM(Enum.class, String.class, new EnumSerializer()),
    ITEMSTACK(ItemStack.class, JSONObject.class, new ItemStackSerializer(), true),
    LOCATION(Location.class, JSONObject.class, new LocationSerializer(), true),
    MAP(Map.class, JSONObject.class, new MapSerializer()),
    COLLECTION(Collection.class, JSONArray.class, new CollectionSerializer()),
    ARRAY(Object[].class, JSONArray.class, new ArraySerializer());

    private final Class<?> from;
    private final Class<?> to;
    private final Serializer<?, ?> serializer;
    private final boolean requireTypification;

    SerializerType(Class<?> from, Class<?> to, Serializer<?, ?> serializer) {
        this.from = from;
        this.to = to;
        this.serializer = serializer;
        this.requireTypification = false;
    }

}