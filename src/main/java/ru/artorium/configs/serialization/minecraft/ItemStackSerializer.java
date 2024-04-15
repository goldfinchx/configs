package ru.artorium.configs.serialization.minecraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;

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

        if (map.containsKey("name")) {
            final Component name = (Component) Serializer.deserialize(Component.class, map.get("name"));
            itemMeta.displayName(name);
        }

        if (map.containsKey("lore")) {
            final JSONArray lore = (JSONArray) map.get("lore");
            itemMeta.lore(lore.stream().map(component -> Serializer.deserialize(Component.class, component)).toList());
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
    public Map<String, Object> serialize(TypeReference typeReference, ItemStack item) {
        final Map<String, Object> map = new HashMap<>();

        map.put("type", item.getType().name());
        map.put("amount", item.getAmount());

        if (item.getItemMeta().hasDisplayName()) {
            map.put("name", Serializer.serialize(Component.class, item.getItemMeta().displayName()));
        }

        if (item.getItemMeta().hasLore()) {
            final JSONArray lore = new JSONArray();
            item.lore().forEach(component -> lore.add(Serializer.serialize(Component.class, component)));
            map.put("lore", lore);
        }

        if (item.getItemMeta().hasCustomModelData()) {
            map.put("customModelData", item.getItemMeta().getCustomModelData());
        }

        if (!item.getEnchantments().isEmpty()) {
            final JSONObject enchantments = new JSONObject();
            item.getEnchantments().forEach((enchantment, integer) -> enchantments.put(enchantment.getName(), integer));
            map.put("enchantments", enchantments);
        }

        if (!item.getItemFlags().isEmpty()) {
            final JSONArray itemFlags = new JSONArray();
            item.getItemFlags().forEach(itemFlag -> itemFlags.add(itemFlag.name()));
            map.put("itemFlags", itemFlags);
        }

        return map;
    }

}
