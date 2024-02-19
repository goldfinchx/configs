package ru.artorium.configs.serialization.minecraft;

import java.util.Map;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.artorium.configs.serialization.SpecificSerializer;

public class ItemStackSerializer implements SpecificSerializer<ItemStack, JSONObject> {

    @Override
    public ItemStack deserialize(Class<?> fieldClass, Object object) {
        final Map<String, Object> map = (Map) object;
        final ItemStack itemStack = new ItemStack(Material.valueOf((String) map.get("type")), ((Number) map.get("amount")).intValue());
        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (map.containsKey("displayName")) {
            itemMeta.setDisplayName((String) map.get("displayName"));
        }

        if (map.containsKey("lore")) {
            final JSONArray lore = (JSONArray) map.get("lore");
            itemMeta.setLore(lore.stream().map(String.class::cast).toList());
        }

        if (map.containsKey("customModelData")) {
            itemMeta.setCustomModelData(((Number) map.get("customModelData")).intValue());
        }

        if (map.containsKey("enchantments")) {
            final JSONObject enchantments = (JSONObject) map.get("enchantments");
            enchantments.forEach((enchantment, level) -> itemStack.addUnsafeEnchantment(
                Enchantment.getByName((String) enchantment),
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
    public JSONObject serialize(Object object) {
        final JSONObject json = new JSONObject();
        final ItemStack itemStack = (ItemStack) object;

        json.put("type", itemStack.getType().name());
        json.put("amount", itemStack.getAmount());

        if (itemStack.getItemMeta().hasDisplayName()) {
            json.put("displayName", itemStack.getItemMeta().getDisplayName());
        }
        if (itemStack.getItemMeta().hasLore()) {
            final JSONArray lore = new JSONArray();
            lore.addAll(Objects.requireNonNull(itemStack.getItemMeta().getLore()));
            json.put("lore", lore);
        }
        if (itemStack.getItemMeta().hasCustomModelData()) {
            json.put("customModelData", itemStack.getItemMeta().getCustomModelData());
        }
        if (!itemStack.getEnchantments().isEmpty()) {
            final JSONObject enchantments = new JSONObject();
            itemStack.getEnchantments().forEach((enchantment, integer) -> enchantments.put(enchantment.getName(), integer));
            json.put("enchantments", enchantments);
        }


        return json;
    }

}
