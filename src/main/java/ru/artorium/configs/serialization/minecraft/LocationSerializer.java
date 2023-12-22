package ru.artorium.configs.serialization.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.simple.JSONObject;
import ru.artorium.configs.serialization.Serializer;

public class LocationSerializer implements Serializer<Location, JSONObject> {

    @Override
    public Location deserialize(Class fieldClass, Object object) {
        final JSONObject json = (JSONObject) object;
        final Location location = new Location(null, 0, 0, 0);

        location.setWorld(Bukkit.getWorld((String) json.get("world")));
        location.setX((double) json.get("x"));
        location.setY((double) json.get("y"));
        location.setZ((double) json.get("z"));

        if (json.containsKey("yaw")) {
            location.setYaw((float) (double) json.get("yaw"));
        }
        if (json.containsKey("pitch")) {
            location.setPitch((float) (double) json.get("pitch"));
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
