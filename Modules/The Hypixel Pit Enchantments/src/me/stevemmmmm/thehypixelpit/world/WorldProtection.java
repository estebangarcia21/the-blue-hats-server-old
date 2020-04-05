package me.stevemmmmm.thehypixelpit.world;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.game.RegionManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class WorldProtection implements Listener {

    @EventHandler
    public void onBlockBread(BlockBreakEvent event) {
        if (event.getPlayer().getName().equalsIgnoreCase("Stevemmmmmmm") || event.getPlayer().getName().equalsIgnoreCase("OVEREXERTED") || event.getPlayer().getName().equalsIgnoreCase("Sundews")) return;

        if (event.getBlock().getType() != Material.OBSIDIAN) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getName().equalsIgnoreCase("Stevemmmmmmm") || event.getPlayer().getName().equalsIgnoreCase("OVEREXERTED") || event.getPlayer().getName().equalsIgnoreCase("Sundews")) return;

        if (event.getBlockPlaced().getType() != Material.OBSIDIAN && event.getBlockPlaced().getType() != Material.COBBLESTONE) {
            event.setCancelled(true);
            return;
        }

        if (RegionManager.getInstance().locationIsInRegion(event.getBlockPlaced().getLocation(), RegionManager.RegionType.SPAWN)) {
            event.setCancelled(true);
        }
    }
}
