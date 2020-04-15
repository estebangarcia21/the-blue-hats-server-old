package me.stevemmmmm.perworldinventories.core;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.configapi.core.ConfigAPI;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class SerializeInventories implements Listener {
    private static SerializeInventories instance;

    public static SerializeInventories getInstance() {
        if (instance == null) instance = new SerializeInventories();

        return instance;
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        ConfigAPI.InventorySerializer.serializePlayerInventory(Main.instance, event.getFrom(), event.getPlayer());

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, () -> ConfigAPI.InventorySerializer.loadPlayerInventory(Main.instance, event.getPlayer().getWorld(), event.getPlayer()), 1L);
    }
}
