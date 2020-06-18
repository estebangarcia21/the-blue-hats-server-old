package me.stevemmmmm.thepitremake.world;

import me.stevemmmmm.thepitremake.core.Main;
import me.stevemmmmm.thepitremake.game.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class AutoRespawn implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");

        triggerRespawnSequence(event.getEntity());
    }

    public static void triggerRespawnSequence(Player player) {
        player.setHealth(player.getMaxHealth());
        player.teleport(RegionManager.getInstance().getSpawnLocation(player));

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.INSTANCE, () -> {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            player.setFoodLevel(19);

            player.setHealth(player.getMaxHealth());
            ((CraftPlayer) player).getHandle().setAbsorptionHearts(0);

            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));

            player.setVelocity(new Vector(0, 0, 0));
            player.setFireTicks(0);
        }, 1);
    }
}
