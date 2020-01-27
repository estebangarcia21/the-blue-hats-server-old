package me.stevemmmmm.thehypixelpit.perks;

import me.stevemmmmm.thehypixelpit.managers.other.Perk;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Vampire extends Perk {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                Arrow arrow = (Arrow) event.getDamager();
                Player player = (Player) ((Arrow) event.getDamager()).getShooter();

                player.setHealth(Math.min(arrow.isCritical() ? player.getHealth() + 3 : player.getHealth() + 1, player.getMaxHealth()));
            }
        }

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getDamager();

            player.setHealth(Math.min((player.getHealth() + 1), player.getMaxHealth()));
        }
    }
}
