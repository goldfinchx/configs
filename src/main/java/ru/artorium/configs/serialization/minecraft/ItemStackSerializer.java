package ru.artorium.configs.serialization.minecraft;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.artorium.configs.serialization.Serializer;

public class ItemStackSerializer implements Serializer<ItemStack, JSONObject> {

    @Override
    public ItemStack deserialize(Class<?> fieldClass, Object object) {
        final JSONObject json = (JSONObject) object;
        final ItemStack itemStack = new ItemStack(Material.valueOf((String) json.get("type")), (int) (long) json.get("amount"));
        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (json.containsKey("displayName")) {
            itemMeta.displayName(Component.text((String) json.get("displayName")));
        }

        if (json.containsKey("lore")) {
            final JSONArray lore = (JSONArray) json.get("lore");
            itemMeta.lore(lore.stream().map(component -> Component.text((String) component)).toList());
        }

        if (json.containsKey("customModelData")) {
            itemMeta.setCustomModelData((int) (long) json.get("customModelData"));
        }

        if (json.containsKey("enchantments")) {
            final JSONObject enchantments = (JSONObject) json.get("enchantments");
            enchantments.forEach((enchantment, level) -> itemStack.addUnsafeEnchantment(Enchantment.getByName((String) enchantment), (int) (long) level));
        }

        if (json.containsKey("itemFlags")) {
            final JSONArray itemFlags = (JSONArray) json.get("itemFlags");
            itemFlags.forEach(itemFlag -> itemMeta.addItemFlags(ItemFlag.valueOf((String) itemFlag)));
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public JSONObject serialize(Class<?> fieldClass, Object object) {
        final JSONObject json = new JSONObject();
        final ItemStack itemStack = (ItemStack) object;

        json.put("type", itemStack.getType().name());
        json.put("amount", itemStack.getAmount());

        if (itemStack.getItemMeta().hasDisplayName()) {
            json.put("displayName", PlainTextComponentSerializer.plainText().serialize(itemStack.getItemMeta().displayName()));
        }
        if (itemStack.getItemMeta().hasLore()) {
            final JSONArray lore = new JSONArray();
            itemStack.lore().forEach(component -> lore.add(PlainTextComponentSerializer.plainText().serialize(component)));
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

        if (!itemStack.getItemFlags().isEmpty()) {
            final JSONArray itemFlags = new JSONArray();
            itemStack.getItemFlags().forEach(itemFlag -> itemFlags.add(itemFlag.name()));
            json.put("itemFlags", itemFlags);
        }

        return json;
    }

}
