package ru.artorium.configs.serialization.minecraft;

import ru.artorium.configs.serialization.Serializer;
import ru.artorium.configs.serialization.TypeReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            final List<Component> lore = ((Collection<?>) map.get("lore"))
                .stream()
                .map(component -> (Component) Serializer.deserialize(Component.class, component))
                .toList();

            itemMeta.lore(lore);
        }

        if (map.containsKey("customModelData")) {
            itemMeta.setCustomModelData(((Number) map.get("customModelData")).intValue());
        }

        if (map.containsKey("enchantments")) {
            final Map<?, ?> enchantments = (Map<?, ?>) map.get("enchantments");
            enchantments.forEach((key, level) -> {
                final Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft((String) key));
                final int enchantmentLevel = ((Number) level).intValue();

                if (enchantment == null) {
                    throw new IllegalArgumentException("Failed to deserialize enchantment: " + key + " as it does not exist");
                }

                itemStack.addEnchantment(enchantment, enchantmentLevel);
            });
        }

        if (map.containsKey("itemFlags")) {
            final Collection<?> itemFlags = (Collection) map.get("itemFlags");
            itemFlags.forEach(key -> {
                final ItemFlag itemFlag = ItemFlag.valueOf((String) key);
                itemMeta.addItemFlags(itemFlag);
            });
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
            final List<String> lore = new ArrayList<>();
            item.lore().forEach(component -> lore.add((String) Serializer.serialize(Component.class, component)));
            map.put("lore", lore);
        }

        if (item.getItemMeta().hasCustomModelData()) {
            map.put("customModelData", item.getItemMeta().getCustomModelData());
        }

        if (!item.getEnchantments().isEmpty()) {
            final Map<String, Integer> enchantments = new HashMap<>();
            item.getEnchantments().forEach((enchantment, integer) -> enchantments.put(enchantment.getName(), integer));
            map.put("enchantments", enchantments);
        }

        if (!item.getItemFlags().isEmpty()) {
            final List<String> itemFlags = new ArrayList<>();
            item.getItemFlags().forEach(itemFlag -> itemFlags.add(itemFlag.name()));
            map.put("itemFlags", itemFlags);
        }

        return map;
    }

}
