package ru.artorium.configs.serialization.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.World;
import ru.artorium.configs.serialization.SpecificSerializer;

public class WorldSerializer implements SpecificSerializer<World, String> {

    @Override
    public World deserialize(Class<?> fieldClass, Object object) {
        return Bukkit.getWorld((String) object);
    }

    @Override
    public String serialize(Object object) {
        return ((World) object).getName();
    }

}
