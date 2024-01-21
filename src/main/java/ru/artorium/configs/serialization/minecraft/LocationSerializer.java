package ru.artorium.configs.serialization.minecraft;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.simple.JSONObject;
import ru.artorium.configs.serialization.SpecificSerializer;

public class LocationSerializer implements SpecificSerializer<Location, JSONObject> {

    @Override
    public Location deserialize(Class<?> fieldClass, Object object) {
        final Map<String, Object> map = (Map) object;
        final Location location = new Location(null, 0, 0, 0);

        location.setWorld(Bukkit.getWorld((String) map.get("world")));
        location.setX(((Number) map.get("x")).floatValue());
        location.setY(((Number) map.get("y")).floatValue());
        location.setZ(((Number) map.get("z")).floatValue());

        if (map.containsKey("yaw")) {
            location.setYaw(((Number) map.get("yaw")).floatValue());
        }
        if (map.containsKey("pitch")) {
            location.setPitch(((Number) map.get("pitch")).floatValue());
        }

        return location;
    }

    @Override
    public JSONObject serialize(Object object) {
        final JSONObject json = new JSONObject();
        final Location location = (Location) object;

        json.put("world", location.getWorld().getName());
        json.put("x", location.getX());
        json.put("y", location.getY());
        json.put("z", location.getZ());

        if (location.getYaw() != 0) {
            json.put("yaw", location.getYaw());
        }
        if (location.getPitch() != 0) {
            json.put("pitch", location.getPitch());
        }

        return json;
    }

}
