package ru.artorium.configs;

import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.artorium.configs.annotations.Ignore;

@Data
@NoArgsConstructor
public class TestConfig extends Config {

    @Ignore
    private JavaPlugin plugin;

    private Map<Integer, Material> map = Map.of(
        1, Material.STONE
    );
    private ItemStack item = new ItemStack(Material.STONE, 1);
    private int x = 10;

    public TestConfig(JavaPlugin plugin) {
        super("test", plugin.getDataFolder().getPath());
        this.reload();
    }


}
