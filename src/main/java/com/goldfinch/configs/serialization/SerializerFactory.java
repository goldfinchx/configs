package com.goldfinch.configs.serialization;

import com.goldfinch.configs.Config;
import com.goldfinch.configs.serialization.collections.ArraySerializer;
import com.goldfinch.configs.serialization.collections.CollectionSerializer;
import com.goldfinch.configs.serialization.collections.MapSerializer;
import com.goldfinch.configs.serialization.minecraft.ComponentSerializer;
import com.goldfinch.configs.serialization.minecraft.ItemStackSerializer;
import com.goldfinch.configs.serialization.minecraft.LocationSerializer;
import com.goldfinch.configs.serialization.minecraft.WorldSerializer;
import com.goldfinch.configs.serialization.other.EnumSerializer;
import com.goldfinch.configs.serialization.other.ObjectSerializer;
import com.goldfinch.configs.serialization.other.UUIDSerializer;
import com.goldfinch.configs.serialization.primitives.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class SerializerFactory {

    public static StringSerializer STRING;
    public static IntegerSerializer INTEGER;
    public static CharacterSerializer CHARACTER;
    public static DoubleSerializer DOUBLE;
    public static BooleanSerializer BOOLEAN;
    public static EnumSerializer ENUM;
    public static WorldSerializer WORLD;
    public static ItemStackSerializer ITEMSTACK;
    public static LocationSerializer LOCATION;
    public static CollectionSerializer COLLECTION;
    public static ComponentSerializer COMPONENT;
    public static MapSerializer MAP;
    public static ArraySerializer ARRAY;
    public static ObjectSerializer OBJECT;
    public static UUIDSerializer UUID;

    @Getter
    private static SerializerFactory factory;

    static {
        factory = new SerializerFactory();

        /* Register build-in serializers */
        factory.register(STRING = new StringSerializer());
        factory.register(INTEGER = new IntegerSerializer());
        factory.register(CHARACTER = new CharacterSerializer());
        factory.register(DOUBLE = new DoubleSerializer());
        factory.register(BOOLEAN = new BooleanSerializer());
        factory.register(ENUM = new EnumSerializer());
        factory.register(WORLD = new WorldSerializer());
        factory.register(ITEMSTACK = new ItemStackSerializer());
        factory.register(LOCATION = new LocationSerializer());
        factory.register(COLLECTION = new CollectionSerializer());
        factory.register(COMPONENT = new ComponentSerializer());
        factory.register(MAP = new MapSerializer());
        factory.register(ARRAY = new ArraySerializer());
        factory.register(OBJECT = new ObjectSerializer());
        factory.register(UUID = new UUIDSerializer());
    }

    private final List<Serializer<?, ?>> serializers;

    private SerializerFactory() {
        serializers = new ArrayList<>();
    }

    public <O, I> void register(Serializer<O, I> serializer){
        this.serializers.add(serializer);
    }

    public Serializer<?, ?> get(Class<?> original){
        for (Serializer<?, ?> serializer : serializers) {
            if (serializer.isCompatibleWith(original)) {
                return serializer;
            }
        }
        return OBJECT;
    }

}
