package me.stevemmmmm.configapi.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class ConfigAPI extends JavaPlugin implements Listener {
    private static HashMap<JavaPlugin, HashMap<String, String>> dataCategories = new HashMap<>();

    private static ArrayList<ConfigWriter> configWriters = new ArrayList<>();
    private static ArrayList<ConfigReader> configReaders = new ArrayList<>();

    @Override
    public void onEnable() {
        Logger log = Bukkit.getLogger();
        log.info("------------------------------------------");
        log.info("ConfigAPI by Stevemmmmm");
        log.info("------------------------------------------");

        getServer().getPluginManager().registerEvents(this, this);

//        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
//            for (ConfigWriter writer : configWriters) {
//                writer.writeToConfig();
//            }
//        }, 36000L, 36000L);
    }

    @Override
    public void onDisable() {
        for (ConfigWriter writer : configWriters) {
            writer.writeToConfig();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (ConfigReader reader : configReaders) {
            reader.readConfig(event.getPlayer());
        }
    }

    public static void addPlugin(JavaPlugin javaPlugin, HashMap<String, String> locations) {
        dataCategories.put(javaPlugin, locations);
    }

    public static void registerConfigWriter(ConfigWriter writer) {
        configWriters.add(writer);
    }

    public static void registerConfigReader(ConfigReader reader) {
        configReaders.add(reader);
    }

    public static void write(JavaPlugin plugin, Player player, String path, String value) {
        String fullPath = dataCategories.get(plugin).get(path);

        plugin.getConfig().set(player.getUniqueId().toString() + "." + fullPath, value);
        plugin.saveConfig();
    }

    public static <V> void write(JavaPlugin plugin, String path, HashMap<UUID, V> entries) {
        String fullPath = dataCategories.get(plugin).get(path);

        for (Map.Entry<UUID, V> entry : entries.entrySet()) {
            plugin.getConfig().set(entry.getKey() + "." + fullPath, entry.getValue().toString());
            plugin.saveConfig();
        }
    }

    public static <T> T read(JavaPlugin plugin, Player player, String path, Class<T> type, T defaultValue) {
        if (plugin.getConfig().getString(player.getUniqueId().toString() + "." + dataCategories.get(plugin).get(path)) == null) {
            return defaultValue;
        }

        if (type == Integer.class) {
            return type.cast(Integer.parseInt(plugin.getConfig().getString(player.getUniqueId().toString() + "." + dataCategories.get(plugin).get(path))));
        }

        if (type == Double.class) {
            return type.cast(Double.parseDouble(plugin.getConfig().getString(player.getUniqueId().toString() + "." + dataCategories.get(plugin).get(path))));
        }

        if (type == Float.class) {
            return type.cast(Float.parseFloat(plugin.getConfig().getString(player.getUniqueId().toString() + "." + dataCategories.get(plugin).get(path))));
        }

        if (type == Long.class) {
            return type.cast(Long.parseLong(plugin.getConfig().getString(player.getUniqueId().toString() + "." + dataCategories.get(plugin).get(path))));
        }

        if (type == Short.class) {
            return type.cast(Short.parseShort(plugin.getConfig().getString(player.getUniqueId().toString() + "." + dataCategories.get(plugin).get(path))));
        }

        if (type == Byte.class) {
            return type.cast(Byte.parseByte(plugin.getConfig().getString(player.getUniqueId().toString() + "." + dataCategories.get(plugin).get(path))));
        }

        return type.cast(plugin.getConfig().getString(player.getUniqueId().toString() + "." + dataCategories.get(plugin).get(path)));
    }

    public static class InventorySerializer {
        public static void serializePlayerInventory(JavaPlugin plugin, Player player) {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) == null) continue;

                writeInventory(plugin, player, i, player.getInventory().getItem(i).serialize());
            }
        }

        public static void loadInventory(JavaPlugin plugin, Player player) {

        }

        private static void writeInventory(JavaPlugin plugin, Player player, int slot, Map<String, Object> object) {
            plugin.getConfig().set(player.getUniqueId().toString() + ".inventory.mainworld.slots." + slot, object);
            plugin.saveConfig();
        }
    }
}
