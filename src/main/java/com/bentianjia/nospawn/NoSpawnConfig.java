package com.bentianjia.nospawn;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NoSpawnConfig {
    private final JavaPlugin plugin;
    private final Set<String> disabledTypes = new HashSet<>();

    public NoSpawnConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public synchronized void load() {
        disabledTypes.clear();
        plugin.reloadConfig();
        FileConfiguration cfg = plugin.getConfig();

        List<String> disabled = cfg.getStringList("disabled");
        if (disabled == null) {
            return;
        }

        for (String entry : disabled) {
            String name = normalizeEntityName(entry);
            if (name == null) {
                plugin.getLogger().warning("Unknown entity type in config: " + entry);
            } else {
                disabledTypes.add(name);
            }
        }
    }

    public synchronized void save() {
        FileConfiguration cfg = plugin.getConfig();
        List<String> serialized = new ArrayList<>();
        for (String name : disabledTypes) {
            serialized.add("minecraft:" + name);
        }
        cfg.set("disabled", serialized);
        plugin.saveConfig();
    }

    public synchronized boolean add(String typeName) {
        boolean changed = disabledTypes.add(typeName);
        if (changed) {
            save();
        }
        return changed;
    }

    public synchronized boolean remove(String typeName) {
        boolean changed = disabledTypes.remove(typeName);
        if (changed) {
            save();
        }
        return changed;
    }

    public synchronized List<String> disabledTypeNames() {
        return Collections.unmodifiableList(new ArrayList<>(disabledTypes));
    }

    public synchronized boolean isDisabled(EntityType type) {
        String name = type.name().toLowerCase(Locale.ROOT);
        return disabledTypes.contains(name);
    }

    /**
     * Normalizes entity type input to a consistent format.
     * Accepts: "creeper", "minecraft:creeper", "CREEPER"
     * Returns the normalized entity name (lowercase, namespace stripped).
     */
    public static String normalizeEntityName(String input) {
        if (input == null) {
            return null;
        }
        String name = input.toLowerCase(Locale.ROOT).trim();
        if (name.isEmpty()) {
            return null;
        }
        // Strip "minecraft:" prefix if present
        if (name.contains(":")) {
            String[] parts = name.split(":", 2);
            if (!parts[0].equals("minecraft")) {
                return null; // Only minecraft namespace is supported
            }
            name = parts[1];
        }
        // Validate it's a real entity type
        try {
            EntityType type = EntityType.valueOf(name.toUpperCase(Locale.ROOT));
            return type.name().toLowerCase(Locale.ROOT);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
