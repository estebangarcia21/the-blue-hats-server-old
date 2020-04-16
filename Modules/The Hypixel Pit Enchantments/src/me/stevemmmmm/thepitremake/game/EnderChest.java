package me.stevemmmmm.thepitremake.game;

import me.stevemmmmm.configapi.core.ConfigAPI;
import me.stevemmmmm.thepitremake.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class EnderChest implements Listener {
    private static EnderChest instance;

    private final HashMap<UUID, Inventory> playerEnderChestsToxicWorld = new HashMap<>();
    private final HashMap<UUID, Inventory> playerEnderChestsNonToxicWorld = new HashMap<>();

    public static EnderChest getInstance() {
        if (instance == null) instance = new EnderChest();

        return instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!playerEnderChestsToxicWorld.containsKey(event.getPlayer().getUniqueId())) {
            playerEnderChestsToxicWorld.put(event.getPlayer().getUniqueId(), Bukkit.createInventory(null, 54, ChatColor.GRAY + "Ender Chest"));

            ConfigAPI.InventorySerializer.loadInventory(Main.INSTANCE, event.getPlayer(), "ToxicWorld", playerEnderChestsToxicWorld.get(event.getPlayer().getUniqueId()));
        }

        if (!playerEnderChestsNonToxicWorld.containsKey(event.getPlayer().getUniqueId())) {
            playerEnderChestsNonToxicWorld.put(event.getPlayer().getUniqueId(), Bukkit.createInventory(null, 54, ChatColor.GRAY + "Ender Chest"));

            ConfigAPI.InventorySerializer.loadInventory(Main.INSTANCE, event.getPlayer(), "NonToxicWorld", playerEnderChestsNonToxicWorld.get(event.getPlayer().getUniqueId()));
        }

        for (int i = 0; i < event.getPlayer().getEnderChest().getSize(); i++) {
            if (event.getPlayer().getEnderChest().getItem(i) == null) continue;
            if (event.getPlayer().getEnderChest().getItem(i).getType() == Material.AIR) continue;

            playerEnderChestsToxicWorld.get(event.getPlayer().getUniqueId()).setItem(i, event.getPlayer().getEnderChest().getItem(i));
        }

        event.getPlayer().getEnderChest().clear();
    }

    @EventHandler
    public void onEnderChestOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            event.setCancelled(true);

            if (event.getPlayer().getWorld().getName().equals("world")) {
                event.getPlayer().openInventory(playerEnderChestsToxicWorld.get(event.getPlayer().getUniqueId()));
            } else if (event.getPlayer().getWorld().getName().equals("ThePit_0")) {
                event.getPlayer().openInventory(playerEnderChestsNonToxicWorld.get(event.getPlayer().getUniqueId()));
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            storeEnderChests();
        }
    }

    public void storeEnderChests() {
        for (Map.Entry<UUID, Inventory> entry : playerEnderChestsToxicWorld.entrySet()) {
            ConfigAPI.InventorySerializer.serializeInventory(Main.INSTANCE, entry.getKey(), "ToxicWorld", entry.getValue());
        }

        for (Map.Entry<UUID, Inventory> entry : playerEnderChestsNonToxicWorld.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());

            ConfigAPI.InventorySerializer.serializeInventory(Main.INSTANCE, entry.getKey(), "NonToxicWorld", entry.getValue());
        }
    }
}
