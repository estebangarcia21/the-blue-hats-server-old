package me.stevemmmmm.thehypixelpit.game;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.commands.TogglePvPCommand;
import me.stevemmmmm.thehypixelpit.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;

public class Obsidian implements Listener {
    private static Obsidian instance;

    private final HashMap<Block, Integer> obsidianToRemovalTasks = new HashMap<>();
    private final HashMap<Block, Integer> removalTime = new HashMap<>();

    public static Obsidian getInstance() {
        if (instance == null) instance = new Obsidian();

        return instance;
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (RegionManager.getInstance().locationIsInRegion(event.getBlockPlaced().getLocation(), RegionManager.RegionType.SPAWN)) {
            return;
        }

        if (event.getBlockPlaced().getType() == Material.OBSIDIAN) {
            obsidianToRemovalTasks.put(event.getBlockPlaced(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
                removalTime.put(event.getBlockPlaced(), removalTime.getOrDefault(event.getBlockPlaced(), 120) - 1);

                if (removalTime.get(event.getBlockPlaced()) <= 0) {
                    event.getBlockPlaced().setType(Material.AIR);

                    Bukkit.getServer().getScheduler().cancelTask(obsidianToRemovalTasks.get(event.getBlockPlaced()));
                    obsidianToRemovalTasks.remove(event.getBlockPlaced());
                    removalTime.remove(event.getBlockPlaced());
                }
            }, 0L, 20L));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.OBSIDIAN) {
            for (Block obsidianBlocks : obsidianToRemovalTasks.keySet()) {
                if (obsidianBlocks.equals(event.getBlock())) {
                    Bukkit.getServer().getScheduler().cancelTask(obsidianToRemovalTasks.get(event.getBlock()));

                    obsidianToRemovalTasks.remove(event.getBlock());
                    removalTime.remove(event.getBlock());
                    break;
                }
            }
        }
    }

    public void removeObsidian() {
        for (Block block : obsidianToRemovalTasks.keySet()) {
            block.setType(Material.AIR);
        }
    }
}
