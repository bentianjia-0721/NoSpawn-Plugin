package com.bentianjia.nospawn;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SpawnListener implements Listener {
    private final NoSpawnConfig config;

    public SpawnListener(NoSpawnConfig config) {
        this.config = config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (config.isDisabled(event.getEntityType())) {
            event.setCancelled(true);
        }
    }
}
