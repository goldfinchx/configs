package com.goldfinch.configs.serialization.minecraft;

import com.goldfinch.configs.Utils;
import com.goldfinch.configs.serialization.Serializer;
import com.goldfinch.configs.serialization.TypeReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackSerializer implements Serializer<ItemStack, Map<String, Object>> {


    @Override
    public ItemStack deserialize(TypeReference typeReference, Map<String, Object> map) {
        final boolean modern = Utils.isServerUsesComponents();

        if (modern) {
            return this.deserializeModern(map);
        } else {
            return this.deserializeLegacy(map);
        }
    }

    @Override
    public Map<String, Object> serialize(TypeReference typeReference, ItemStack item) {
        final boolean modern = Utils.isServerUsesComponents();

        if (modern) {
            return this.serializeModern(item);
        } else {
            return this.serializeLegacy(item);
        }
    }

    private Map<String, Object> serializeLegacy(ItemStack item) {
        final Map<String, Object> map = new HashMap<>();

        map.put("type", item.getType().name());
        map.put("amount", item.getAmount());

        if (item.hasItemMeta()) {
            final ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta.hasDisplayName()) {
                map.put("name", itemMeta.getDisplayName());
            }

            if (itemMeta.hasLore()) {
                map.put("lore", itemMeta.getLore());
            }

            if (!itemMeta.getEnchants().isEmpty()) {
                final Map<String, Integer> enchantments = new HashMap<>();
                itemMeta.getEnchants().forEach((enchantment, integer) -> enchantments.put(enchantment.getName(), integer));

                map.put("enchantments", enchantments);
            }

            if (!itemMeta.getItemFlags().isEmpty()) {
                final List<String> itemFlags = new ArrayList<>();
                itemMeta.getItemFlags().forEach(itemFlag -> itemFlags.add(itemFlag.name()));

                map.put("itemFlags", itemFlags);
            }
        }

        return map;
    }

    private Map<String, Object> serializeModern(ItemStack item) {
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

    private ItemStack deserializeLegacy(Map<String, Object> map) {
        final ItemStack itemStack;

        try {
            itemStack = new ItemStack(Material.valueOf((String) map.get("type")), ((Number) map.get("amount")).intValue());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack type: " + map.get("type") + " as it does not exist");
        }

        if (map.containsKey("name") || map.containsKey("lore") || map.containsKey("enchantments") || map.containsKey("itemFlags")) {
            final ItemMeta itemMeta = itemStack.getItemMeta();

            if (map.containsKey("name")) {
                itemMeta.setDisplayName((String) map.get("name"));
            }

            if (map.containsKey("lore")) {
                itemMeta.setLore((List<String>) map.get("lore"));
            }

            if (map.containsKey("enchantments")) {
                final Map<?, ?> enchantments = (Map<?, ?>) map.get("enchantments");
                enchantments.forEach((key, level) -> {
                    final Enchantment enchantment = Enchantment.getByName((String) key);
                    final int enchantmentLevel = ((Number) level).intValue();

                    if (enchantment == null) {
                        throw new IllegalArgumentException("Failed to deserialize enchantment: " + key + " as it does not exist");
                    }

                    itemMeta.addEnchant(enchantment, enchantmentLevel, true);
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
        }

        return itemStack;
    }

    private ItemStack deserializeModern(Map<String, Object> map) {
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
                final Enchantment enchantment = Enchantment.getByName((String) key);
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

}
