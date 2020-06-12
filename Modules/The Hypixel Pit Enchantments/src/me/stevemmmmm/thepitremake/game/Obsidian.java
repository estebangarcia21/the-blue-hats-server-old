package me.stevemmmmm.thepitremake.game;

import me.stevemmmmm.configapi.sqlmanagement.databases.PlayerRanksDatabase;
import me.stevemmmmm.configapi.sqlmanagement.managers.SQLManager;
import me.stevemmmmm.permissions.core.Rank;
import me.stevemmmmm.permissions.ranks.RankManager;
import me.stevemmmmm.thepitremake.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.sql.ResultSet;
import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

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

        try {
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
        } catch (NullPointerException ignored) {

        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        try {
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
        } catch (NullPointerException ignored) {

        }
    }

    public void removeObsidian() {
        try {
            for (Block block : obsidianToRemovalTasks.keySet()) {
                block.setType(Material.AIR);
            }
        } catch (NullPointerException ignored) {

        }
    }
}
