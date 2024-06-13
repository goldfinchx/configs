package com.goldfinch.configs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ConfigsPlugin extends JavaPlugin {
    private static ConfigsPlugin instance;

    @SuppressWarnings("UnstableApiUsage")
    static Logger getBukkitLogger(){
        if(instance == null){
            return Bukkit.getLogger();
        }
        return instance.getLogger();
    }

    @Override
    public void onEnable() {
        instance = this;
        this.getLogger().info("Configs has been enabled!");
    }

}