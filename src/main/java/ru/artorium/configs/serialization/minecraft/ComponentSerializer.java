package ru.artorium.configs.serialization.minecraft;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ru.artorium.configs.serialization.SimpleSerializer;

public class ComponentSerializer implements SimpleSerializer<Component, String> {

    @Override
    public Component deserialize(Object object) {
        return MiniMessage.miniMessage().deserialize((String) object);
    }

    @Override
    public String serialize(Object object) {
        return MiniMessage.miniMessage().serialize((Component) object);
    }

}
