package com.goldfinch.configs;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigsPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getLogger().info("Configs has been enabled!");
    }

}