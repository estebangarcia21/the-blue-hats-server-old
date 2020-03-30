package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Explosive extends CustomEnchant {
    private LevelVariable<Double> explosionRange = new LevelVariable<>(1D, 2.5D, 6D);

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (event.getEntity().getShooter() instanceof Player) {
                tryExecutingEnchant(((Player) event.getEntity().getShooter()).getInventory().getItemInHand(), event.getEntity());
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Arrow arrow = (Arrow) args[0];

        for (Entity entity : arrow.getNearbyEntities(explosionRange.at(level), explosionRange.at(level), explosionRange.at(level))) {
            if (entity instanceof Player) {
                Player player = (Player) entity;

                Vector force = player.getLocation().toVector().subtract(arrow.getLocation().toVector()).normalize().multiply(1.25);
                player.setVelocity(force);
            }
        }
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
