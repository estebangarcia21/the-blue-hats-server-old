package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.EnchantVariable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Healer extends CustomEnchant {
    private EnchantVariable<Integer> healAmount = new EnchantVariable<>(2, 4, 6);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            tryExecutingEnchant(((Player) event.getDamager()).getItemInHand(), event.getDamager(), event.getEntity());
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damager = (Player) args[0];
        Player damaged = (Player) args[1];

        damager.setHealth(Math.min(damager.getHealth() + healAmount.at(level), damager.getMaxHealth()));
        damaged.setHealth(Math.min(damager.getHealth() + healAmount.at(level), damaged.getMaxHealth()));
    }

    @Override
    public String getName() {
        return "Healer";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Healer";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String healAmount = level == 1 ? "1❤" : level == 2 ? "2❤" : level == 3 ? "3❤" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Hitting player " + ChatColor.GREEN + "heals" + ChatColor.GRAY + " both you and them for " + ChatColor.RED + healAmount);
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
