package com.goldfinch.configs.serialization.primitives;


import com.goldfinch.configs.serialization.Serializer;
import com.goldfinch.configs.serialization.TypeReference;

public class StringSerializer implements Serializer<String, String> {

    /**
     * Replaces the & character with §.
     * This allows Minecraft clients to interpret
     * color codes for colored text.
     * <a href="https://minecraft.fandom.com/wiki/Formatting_codes">See on wiki</a>
     */
    public static boolean REPLACE_AMPERSANDS = true;

    @Override
    public String deserialize(TypeReference type, String serialized) {
        if (REPLACE_AMPERSANDS) {
            return serialized.replace("&", "§");
        }
        return serialized;
    }

    @Override
    public String serialize(TypeReference type, String object) {
        if (REPLACE_AMPERSANDS) {
            return object.replace("§", "&");
        }
        return object;
    }
}
