package me.stevemmmmm.thehypixelpit.game;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class MysticWell implements Listener {
    private HashMap<UUID, Inventory> playerGui = new HashMap<>();
    private HashMap<UUID, Animation> activeAnimations = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!playerGui.containsKey(event.getPlayer().getUniqueId())) playerGui.put(event.getPlayer().getUniqueId(), createMysticWell());
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            event.setCancelled(true);
            event.getPlayer().openInventory(playerGui.get(event.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(ChatColor.GRAY + "Mystic Well")) {
            event.setCancelled(true);
        }
    }

    private void setMysticWellAnimation(Player player, Animation animation) {

    }

    private Inventory createMysticWell() {
        Inventory gui = Bukkit.createInventory(null, 45, ChatColor.GRAY + "Mystic Well");

        ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);

        gui.setItem(10, grayGlassPane);
        gui.setItem(11, grayGlassPane);
        gui.setItem(12, grayGlassPane);

        gui.setItem(19, grayGlassPane);
        gui.setItem(21, grayGlassPane);

        gui.setItem(28, grayGlassPane);
        gui.setItem(29, grayGlassPane);
        gui.setItem(30, grayGlassPane);

        return gui;
    }

    enum Animation {
        IDLE, ENCHANTING
    }
}
