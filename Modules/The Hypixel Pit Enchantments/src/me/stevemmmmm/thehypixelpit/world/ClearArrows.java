package me.stevemmmmm.thehypixelpit.world;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ClearArrows implements Listener {

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.INSTANCE, () -> {
            if (projectile != null) {
                projectile.remove();
            }
        }, 100L);
    }
}
