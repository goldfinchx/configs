package ru.artorium.configs.serialization.primitives;


import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;

public class CharacterSerializer implements Serializer<Character, String> {

    @Override
    public Character deserialize(TypeReference type, String serialized) {
        return serialized.charAt(0);
    }

    @Override
    public String serialize(TypeReference type, Character character) {
        return character.toString();
    }

    @Override
    public boolean isCompatibleWith(Class clazz) {
        return clazz.equals(Character.class) || clazz.equals(char.class);
    }

}
