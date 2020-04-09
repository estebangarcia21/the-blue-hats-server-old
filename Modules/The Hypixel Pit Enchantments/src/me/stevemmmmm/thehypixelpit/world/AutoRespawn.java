package me.stevemmmmm.thehypixelpit.world;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class AutoRespawn implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");

        triggerRespawnSequence(event.getEntity());
    }

    public static void triggerRespawnSequence(Player player) {
        player.setHealth(player.getMaxHealth());

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.INSTANCE, () -> {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            player.setFoodLevel(19);
            player.setHealth(player.getMaxHealth());

            player.teleport(new Location(player.getWorld(),0.5, 86.5, 11.5, -180, 0));
            player.setVelocity(new Vector(0, 0, 0));
            player.setFireTicks(0);
        }, 1);
    }
}
