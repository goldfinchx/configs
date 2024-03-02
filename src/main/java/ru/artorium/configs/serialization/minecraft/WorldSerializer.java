package ru.artorium.configs.serialization.minecraft;

import ru.artorium.configs.serialization.Serializer.Specific;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldSerializer implements Specific<World, String> {

    @Override
    public World deserialize(Class<?> fieldClass, String serialized) {
        final World world = Bukkit.getWorld(serialized);

        if (world == null) {
            throw new IllegalArgumentException("Failed to deserialize world with name " + serialized + " as it does not exist");
        }

        return world;
    }

    @Override
    public String serialize(World world) {
        return world.getName();
    }


}
