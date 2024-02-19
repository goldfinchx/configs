package ru.artorium.configs.replacer;

import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Replacer {

    final Map<String, Object> replacements;

    private Replacer(Object... replacements) {
        this.replacements = this.mapReplacements(replacements);
    }


    public static Replacer of(Object... replacements) {
        return new Replacer(replacements);
    }

    private Map<String, Object> mapReplacements(Object... replacements) {
        if (replacements.length % 2 != 0) {
            throw new IllegalArgumentException("Arguments count must be even");
        }

        final Map<String, Object> replacementsMap = new HashMap<>();

        for (int i = 0; i < replacements.length; i += 2) {
            final String key = (String) replacements[i];
            final Object value = replacements[i + 1];


            replacementsMap.put(key, value);
        }

        return replacementsMap;
    }


    public String replace(String string) {
        for (final Map.Entry<String, Object> entry : this.replacements.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();

            string = string.replace(key, String.valueOf(value));
        }

        return string;
    }

    public ItemStack replace(ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }

        if (itemMeta.hasDisplayName()) {
            itemMeta.setDisplayName(this.replace(itemMeta.getDisplayName()));
        }

        if (itemMeta.hasLore()) {
            itemMeta.setLore(this.replace(itemMeta.getLore()));
        }

        return itemStack;
    }

    public List<String> replace(List<String> strings) {
        strings.replaceAll(this::replace);
        return strings;
    }

    public List<String> replace(String... strings) {
        for (int i = 0; i < strings.length; i++) {
            strings[i] = this.replace(strings[i]);
        }

        return ImmutableList.copyOf(strings);
    }

    public static String replace(String string, String... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Arguments count must be even");
        }

        for (int i = 0; i < args.length; i += 2) {
            final String key = args[i];
            final String value = args[i + 1];

            string = string.replace(key, value);
        }

        return string;
    }

}
