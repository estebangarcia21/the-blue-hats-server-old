package me.stevemmmmm.perworldinventories.core;

import me.stevemmmmm.thepitremake.events.PlayerPreWorldSelect;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class InventoryManager implements Listener {
    private static InventoryManager instance;

    private HashMap<World, PlayerInventoryData> playerInventories = new HashMap<>();

    public static InventoryManager getInstance() {
        if (instance == null) instance = new InventoryManager();

        return instance;
    }

    @EventHandler
    public void onWorldPreSelect(PlayerPreWorldSelect event) {
        
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
//        ConfigAPI.InventorySerializer.serializePlayerInventory(Main.instance, event.getFrom(), event.getPlayer());
//
//        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, () -> ConfigAPI.InventorySerializer.loadPlayerInventory(Main.instance, event.getPlayer().getWorld(), event.getPlayer()), 1L);
    }
}
