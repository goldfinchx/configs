package ru.artorium.configs.serialization.minecraft;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ru.artorium.configs.serialization.SpecificSerializer;

public class ComponentSerializer implements SpecificSerializer<Component, String> {

    @Override
    public Component deserialize(Object object) {
        return MiniMessage.miniMessage().deserialize((String) object);
    }

    @Override
    public String serialize(Object object) {
        return MiniMessage.miniMessage().serialize((Component) object);
    }

}
