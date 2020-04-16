package me.stevemmmmm.thehypixelpit.game;

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.world.AutoRespawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class PlayableArea implements Listener {
    private final HashMap<UUID, Integer> tasks = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        tasks.put(event.getPlayer().getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
            if (!RegionManager.getInstance().playerIsInRegion(event.getPlayer(), RegionManager.RegionType.PLAYABLEAREA)) {
                if (event.getPlayer().getLocation().getY() < 0) {
                    Main.INSTANCE.getServer().getPluginManager().callEvent(new PlayerDeathEvent(event.getPlayer(), new ArrayList<>(), 0, ""));
                } else {
                    AutoRespawn.triggerRespawnSequence(event.getPlayer());
                }

                event.getPlayer().sendMessage(ChatColor.RED + "Congratulations! You went out of the map!");
            }
        }, 0L, 1L));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getServer().getScheduler().cancelTask(tasks.get(event.getPlayer().getUniqueId()));
        tasks.remove(event.getPlayer().getUniqueId());
    }
}
