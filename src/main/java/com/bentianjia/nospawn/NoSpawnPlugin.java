package com.bentianjia.nospawn;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public final class NoSpawnPlugin extends JavaPlugin {
    private NoSpawnConfig config;

    @Override
    public void onEnable() {
        config = new NoSpawnConfig(this);
        config.load();

        getServer().getPluginManager().registerEvents(new SpawnListener(config), this);

        NoSpawnCommand command = new NoSpawnCommand(config);
        getCommand("nospawn").setExecutor(command);
        getCommand("nospawn").setTabCompleter(command);

        getLogger().info("NoSpawn enabled. Configure with /ns or /nospawn.");
    }

    @Override
    public void onDisable() {
        config.save();
        getLogger().info("NoSpawn disabled.");
    }
}
