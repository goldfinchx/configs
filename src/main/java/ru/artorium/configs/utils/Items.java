package ru.artorium.configs.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

public class Items {

    public static Builder create(Material material) {
        return new Builder(material);
    }

    public static Builder create(ItemStack itemStack) {
        return new Builder(itemStack);
    }

    public static Builder create(Material material, short data) {
        return new Builder(material, data);
    }

    public static class Builder {
        private ItemStack stack;

        public Builder(Material mat) {
            this.stack = new ItemStack(mat);
        }

        public Builder(Material mat, int data) {
            this.stack = new ItemStack(mat, 1, (short) data);
        }

        public Builder(ItemStack stack) {
            this.stack = stack;
        }

        public Builder armorColor(Color color) {
            final LeatherArmorMeta meta = (LeatherArmorMeta) this.stack.getItemMeta();
            meta.setColor(color);
            this.itemMeta(meta);
            return this;
        }

        public Builder itemMeta(ItemMeta meta) {
            this.stack.setItemMeta(meta);
            return this;
        }

        public Builder durability(int durability) {
            final ItemMeta meta = this.getItemMeta();
            ((Damageable) meta).setDamage(durability);
            this.itemMeta(meta);
            return this;
        }

        public ItemMeta getItemMeta() {
            return this.stack.getItemMeta();
        }

        public Builder data(BlockData data) {
            final ItemMeta meta = this.stack.getItemMeta();
            ((BlockDataMeta) meta).setBlockData(data);
            this.stack.setItemMeta(meta);
            return this;
        }

        public Builder breakables(List<Material> breakables) {
            final ItemMeta meta = this.getItemMeta();
            meta.setDestroyableKeys(breakables.stream().map(Material::getKey).collect(Collectors.toList()));
            this.itemMeta(meta);
            return this;
        }

        public Builder placeables(List<Material> placeables) {
            final ItemMeta meta = this.getItemMeta();
            meta.setPlaceableKeys(placeables.stream().map(Material::getKey).collect(Collectors.toList()));
            this.itemMeta(meta);
            return this;
        }

        public Builder glow(boolean glow) {
            if (glow) {
                this.enchant(Enchantment.KNOCKBACK, 1);
                this.itemFlag(ItemFlag.HIDE_ENCHANTS);
            } else {
                final ItemMeta meta = this.getItemMeta();
                final Iterator var3 = meta.getEnchants().keySet().iterator();

                while (var3.hasNext()) {
                    final Enchantment enchantment = (Enchantment) var3.next();
                    meta.removeEnchant(enchantment);
                }
            }

            return this;
        }

        public Builder enchant(Enchantment enchantment, int level) {
            if (enchantment == null) {
                return this;
            } else if (level == 0) {
                return this;
            } else {
                final ItemMeta meta = this.getItemMeta();
                meta.addEnchant(enchantment, level, true);
                this.itemMeta(meta);
                return this;
            }
        }

        public Builder itemFlag(ItemFlag flag) {
            final ItemMeta meta = this.getItemMeta();
            meta.addItemFlags(flag);
            this.itemMeta(meta);
            return this;
        }

        public Builder attribute(Attribute attribute, AttributeModifier modifier) {
            final ItemMeta meta = this.getItemMeta();
            meta.addAttributeModifier(attribute, modifier);
            this.itemMeta(meta);
            return this;
        }

        public <T, Z> Builder persistentData(NamespacedKey key, PersistentDataType<T, Z> dataType, Z value) {
            final ItemMeta meta = this.getItemMeta();
            meta.getPersistentDataContainer().set(key, dataType, value);
            this.itemMeta(meta);
            return this;
        }

        public Builder damage(int damage) {
            final AttributeModifier modifier = new AttributeModifier(
                UUID.randomUUID(), "generic.attackDamage", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            final ItemMeta itemMeta = this.getItemMeta();
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
            this.itemMeta(itemMeta);
            return this;
        }

        public Builder attackSpeed(double speed) {
            final AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", speed, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            final ItemMeta itemMeta = this.getItemMeta();
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
            this.itemMeta(itemMeta);
            return this;
        }

        public Builder hideAll() {
            this.itemFlag(ItemFlag.HIDE_ATTRIBUTES);
            this.itemFlag(ItemFlag.HIDE_DESTROYS);
            this.itemFlag(ItemFlag.HIDE_ENCHANTS);
            this.itemFlag(ItemFlag.HIDE_PLACED_ON);
            this.itemFlag(ItemFlag.HIDE_UNBREAKABLE);
            return this;
        }

        public Builder unbreakable(boolean unbreakable) {
            final ItemMeta meta = this.stack.getItemMeta();
            meta.setUnbreakable(unbreakable);
            this.stack.setItemMeta(meta);
            return this;
        }

        public Builder bannerColor(DyeColor color) {
            final BannerMeta meta = (BannerMeta) this.stack.getItemMeta();
            meta.setBaseColor(color);
            this.itemMeta(meta);
            return this;
        }

        public Builder amount(int amount) {
            this.stack.setAmount(amount);
            return this;
        }

        public Builder customModelData(int customModelData) {
            final ItemMeta meta = this.stack.getItemMeta();
            meta.setCustomModelData(customModelData);
            this.stack.setItemMeta(meta);
            return this;
        }

        public Builder headOwner(Player player) {
            final SkullMeta meta = (SkullMeta) this.stack.getItemMeta();
            meta.setOwningPlayer(player);
            this.itemMeta(meta);
            return this;
        }

        public Builder headOwner(OfflinePlayer player) {
            final SkullMeta meta = (SkullMeta) this.stack.getItemMeta();
            meta.setOwningPlayer(player);
            this.itemMeta(meta);
            return this;
        }

        public Builder displayName(Component displayName) {
            final ItemMeta meta = this.getItemMeta();
            meta.displayName(displayName);
            this.itemMeta(meta);
            return this;
        }

        public Builder setItemStack(ItemStack stack) {
            this.stack = stack;
            return this;
        }

        public Builder lore(List<Component> lore) {
            final ItemMeta meta = this.getItemMeta();
            meta.lore(lore);
            this.itemMeta(meta);
            return this;
        }

        public Builder lore(Component[] lore) {
            final ItemMeta meta = this.getItemMeta();
            meta.lore(Arrays.asList(lore));
            this.itemMeta(meta);
            return this;
        }

        public Builder loreLines(Component... lore) {
            final ItemMeta meta = this.getItemMeta();
            meta.lore(Arrays.asList(lore));
            this.itemMeta(meta);
            return this;
        }

        public Builder lore(Component lore) {
            final ArrayList<Component> loreList = new ArrayList();
            loreList.add(lore);
            final ItemMeta meta = this.getItemMeta();
            meta.lore(loreList);
            this.itemMeta(meta);
            return this;
        }

        public ItemStack build() {
            return this.stack;
        }
    }
}
