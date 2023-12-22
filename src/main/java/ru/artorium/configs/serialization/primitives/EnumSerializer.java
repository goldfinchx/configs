package ru.artorium.configs.serialization.primitives;

import org.json.simple.JSONObject;
import ru.artorium.configs.serialization.Serializer;

public class EnumSerializer implements Serializer<Enum, JSONObject> {

    @Override
    public Enum deserialize(Class fieldClass, Object object) {
        final JSONObject jsonObject = (JSONObject) object;

        final Enum<?> enumObject;
        try {
            enumObject = Enum.valueOf((Class<Enum>) Class.forName((String) jsonObject.get("_type")), (String) jsonObject.get("name"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return enumObject;
    }

    @Override
    public JSONObject serialize(Object object) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", ((Enum<?>) object).name());
        jsonObject.put("_type", object.getClass().getName());
        return jsonObject;
    }

}
