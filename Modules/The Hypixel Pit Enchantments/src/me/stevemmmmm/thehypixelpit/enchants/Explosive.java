package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Explosive extends CustomEnchant {

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (event.getEntity().getShooter() instanceof Player) {
                executeEnchant(((Player) event.getEntity().getShooter()).getInventory().getItemInHand(), event);
            }
        }
    }

    @Override
    public boolean executeEnchant(ItemStack sender, Object executedEvent) {
        ProjectileHitEvent event = (ProjectileHitEvent) executedEvent;

        if (itemHasEnchant(sender, 1, this)) {
            for (Entity entity : event.getEntity().getNearbyEntities(1, 1, 1)) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;

                    Vector force = player.getLocation().toVector().subtract(event.getEntity().getLocation().toVector()).normalize().multiply(1.25);
                    player.setVelocity(force);
                }
            }

            return true;
        }

        if (itemHasEnchant(sender, 2, this)) {
            for (Entity entity : event.getEntity().getNearbyEntities(2.5, 2.5, 2.5)) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;

                    Vector force = player.getLocation().toVector().subtract(event.getEntity().getLocation().toVector()).normalize().multiply(1.25);
                    player.setVelocity(force);
                }
            }

            return true;
        }

        if (itemHasEnchant(sender, 3, this)) {
            for (Entity entity : event.getEntity().getNearbyEntities(6, 6, 6)) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;

                    Vector force = player.getLocation().toVector().subtract(event.getEntity().getLocation().toVector()).normalize().multiply(1.25);
                    player.setVelocity(force);
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public String getName() {
        return "Explosive";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Explosive";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new ArrayList<String>() {{

        }};
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }
}
