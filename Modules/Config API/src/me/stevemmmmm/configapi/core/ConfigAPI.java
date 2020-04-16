package me.stevemmmmm.configapi.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class ConfigAPI extends JavaPlugin implements Listener {
    private static HashMap<JavaPlugin, HashMap<String, String>> dataCategories = new HashMap<>();

    private static ArrayList<ConfigWriter> configWriters = new ArrayList<>();
    private static ArrayList<ConfigReader> configReaders = new ArrayList<>();

    private ArrayList<UUID> readPlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        Logger log = Bukkit.getLogger();
        log.info("------------------------------------------");
        log.info("ConfigAPI by Stevemmmmm");
        log.info("------------------------------------------");

        getServer().getPluginManager().registerEvents(this, this);
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
            if (!readPlayers.contains(event.getPlayer().getUniqueId())) {
                reader.readConfig(event.getPlayer());
            }
        }

        readPlayers.add(event.getPlayer().getUniqueId());
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
        public static void serializeInventory(JavaPlugin plugin, UUID uuid, String name, Inventory inventory) {
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null){
                    plugin.getConfig().set("enderchests." + name + "." + uuid.toString() + ".slots." + i, new ItemStack(Material.AIR));
                    plugin.saveConfig();
                    continue;
                }

                plugin.getConfig().set("enderchests." + name + "." + uuid.toString() + ".slots." + i, inventory.getItem(i));
                plugin.saveConfig();
            }
        }

        public static void loadInventory(JavaPlugin plugin, Player player, String name, Inventory target) {
            if (plugin.getConfig().get("enderchests." + name + "." + player.getUniqueId().toString()) != null) {
                for (int i = 0; i < target.getSize(); i++) {
                    if (plugin.getConfig().get("enderchests." + name + "." + player.getUniqueId().toString() + ".slots." + i) != null) {
                        target.setItem(i, plugin.getConfig().getItemStack("enderchests." + name + "." + player.getUniqueId().toString() + ".slots." + i));
                    } else {
                        target.setItem(i, new ItemStack(Material.AIR));
                    }
                }
            }
        }

        public static void serializePlayerInventory(JavaPlugin plugin, World world, Player player) {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) == null) {
                    writeItemStack(plugin, world, player, i, new ItemStack(Material.AIR));
                    continue;
                }

                writeItemStack(plugin, world, player, i, player.getInventory().getItem(i));
            }

            if (player.getInventory().getBoots() != null) {
                writeItemStack(plugin, world, player, -1, player.getInventory().getBoots());
            } else {
                writeItemStack(plugin, world, player, -1, new ItemStack(Material.AIR));
            }

            if (player.getInventory().getLeggings() != null) {
                writeItemStack(plugin, world, player, -2, player.getInventory().getLeggings());
            } else {
                writeItemStack(plugin, world, player, -2, new ItemStack(Material.AIR));
            }

            if (player.getInventory().getChestplate() != null) {
                writeItemStack(plugin, world, player, -3, player.getInventory().getChestplate());
            } else {
                writeItemStack(plugin, world, player, -3, new ItemStack(Material.AIR));
            }

            if (player.getInventory().getHelmet() != null) {
                writeItemStack(plugin, world, player, -4, player.getInventory().getHelmet());
            } else {
                writeItemStack(plugin, world, player, -4, new ItemStack(Material.AIR));
            }
        }

        public static void loadPlayerInventory(JavaPlugin plugin, World world, Player player) {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (plugin.getConfig().get(world.getName() + "." + player.getUniqueId().toString() + ".inventory.slots." + i) != null) {
                    player.getInventory().setItem(i, plugin.getConfig().getItemStack(world.getName() + "." + player.getUniqueId().toString() + ".inventory.slots." + i));
                    player.updateInventory();
                } else {
                    player.getInventory().setItem(i, new ItemStack(Material.AIR));
                }
            }

            if (plugin.getConfig().get(world.getName() + "." + player.getUniqueId().toString() + ".inventory.slots.-1") != null) {
                player.getInventory().setBoots(plugin.getConfig().getItemStack(world.getName() + "." + player.getUniqueId().toString() + ".inventory.slots.-1"));
            } else {
                player.getInventory().setBoots(new ItemStack(Material.AIR));
            }

            if (plugin.getConfig().get(world.getName() + "." + player.getUniqueId().toString() + ".inventory.slots.-2") != null) {
                player.getInventory().setLeggings(plugin.getConfig().getItemStack(world.getName() + "." + player.getUniqueId().toString() + ".inventory.slots.-2"));
            } else {
                player.getInventory().setLeggings(new ItemStack(Material.AIR));
            }

            if (plugin.getConfig().get(world.getName() + "." + player.getUniqueId().toString() + ".inventory.slots.-3") != null) {
                player.getInventory().setChestplate(plugin.getConfig().getItemStack(world.getName() + "." + player.getUniqueId().toString() + ".inventory.slots.-3"));
            } else {
                player.getInventory().setChestplate(new ItemStack(Material.AIR));
            }

            if (plugin.getConfig().get(world.getName() + "." + player.getUniqueId().toString() + ".inventory.slots.-4") != null) {
                player.getInventory().setHelmet(plugin.getConfig().getItemStack(world.getName() + "." + player.getUniqueId().toString() + ".inventory.slots.-4"));
            } else {
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
            }
        }

        private static void writeItemStack(JavaPlugin plugin, World world, Player player, int slot, ItemStack object) {
            plugin.getConfig().set(world.getName() + "." + player.getUniqueId().toString() + ".inventory.slots." + slot, object);
            plugin.saveConfig();
        }
    }
}
