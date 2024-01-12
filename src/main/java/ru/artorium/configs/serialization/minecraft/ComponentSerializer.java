package ru.artorium.configs.serialization.minecraft;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ru.artorium.configs.serialization.Serializer;

public class ComponentSerializer implements Serializer<Component, String> {

    @Override
    public Component deserialize(Class<?> fieldClass, Object object) {
        return MiniMessage.miniMessage().deserialize((String) object);
    }

    @Override
    public String serialize(Class<?> fieldClass, Object object) {
        return MiniMessage.miniMessage().serialize((Component) object);
    }

}
