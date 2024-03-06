package ru.artorium.configs.serialization.minecraft;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import ru.artorium.configs.serialization.TypeReference;
import ru.artorium.configs.serialization.Serializer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ItemStackSerializer implements Serializer<ItemStack, Map<String, Object>> {

    @Override
    public ItemStack deserialize(TypeReference typeReference, Map<String, Object> map) {
        final ItemStack itemStack;

        try {
            itemStack = new ItemStack(Material.valueOf((String) map.get("type")), ((Number) map.get("amount")).intValue());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack type: " + map.get("type") + " as it does not exist");
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (map.containsKey("displayName")) {
            itemMeta.displayName(Component.text((String) map.get("displayName")));
        }

        if (map.containsKey("lore")) {
            final JSONArray lore = (JSONArray) map.get("lore");
            itemMeta.lore(lore.stream().map(component -> Component.text((String) component)).toList());
        }

        if (map.containsKey("customModelData")) {
            itemMeta.setCustomModelData(((Number) map.get("customModelData")).intValue());
        }

        if (map.containsKey("enchantments")) {
            final JSONObject enchantments = (JSONObject) map.get("enchantments");
            enchantments.forEach((enchantment, level) -> itemStack.addUnsafeEnchantment(
                Objects.requireNonNull(Enchantment.getByName((String) enchantment)),
                ((Number) level).intValue())
            );
        }

        if (map.containsKey("itemFlags")) {
            final JSONArray itemFlags = (JSONArray) map.get("itemFlags");
            itemFlags.forEach(itemFlag -> itemMeta.addItemFlags(ItemFlag.valueOf((String) itemFlag)));
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public Map<String, Object> serialize(TypeReference typeReference, ItemStack object) {
        final Map<String, Object> map = new HashMap<>();

        map.put("type", object.getType().name());
        map.put("amount", object.getAmount());

        if (object.getItemMeta().hasDisplayName()) {
            map.put("displayName", PlainTextComponentSerializer.plainText().serialize(object.getItemMeta().displayName()));
        }

        if (object.getItemMeta().hasLore()) {
            final JSONArray lore = new JSONArray();
            Objects.requireNonNull(object.lore()).forEach(component -> lore.add(PlainTextComponentSerializer.plainText().serialize(component)));
            map.put("lore", lore);
        }

        if (object.getItemMeta().hasCustomModelData()) {
            map.put("customModelData", object.getItemMeta().getCustomModelData());
        }

        if (!object.getEnchantments().isEmpty()) {
            final JSONObject enchantments = new JSONObject();
            object.getEnchantments().forEach((enchantment, integer) -> enchantments.put(enchantment.getName(), integer));
            map.put("enchantments", enchantments);
        }

        if (!object.getItemFlags().isEmpty()) {
            final JSONArray itemFlags = new JSONArray();
            object.getItemFlags().forEach(itemFlag -> itemFlags.add(itemFlag.name()));
            map.put("itemFlags", itemFlags);
        }

        return map;
    }

}
