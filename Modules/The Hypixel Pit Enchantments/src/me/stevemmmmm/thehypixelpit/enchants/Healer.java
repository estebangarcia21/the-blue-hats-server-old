package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.enchants.EnvironmentalEnchant;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Healer extends EnvironmentalEnchant {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            triggerEnchant(((Player) event.getDamager()).getItemInHand(), event);
        }
    }

    @Override
    public void triggerEnchant(ItemStack sender, Object... args) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[0];

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        if (itemHasEnchant(sender, 1, this)) {
            damager.setHealth(Math.min(damager.getHealth() + 2, damager.getMaxHealth()));
            damaged.setHealth(Math.min(damager.getHealth() + 2, damaged.getMaxHealth()));
        }

        if (itemHasEnchant(sender, 2, this)) {
            damager.setHealth(Math.min(damager.getHealth() + 4, damager.getMaxHealth()));
            damaged.setHealth(Math.min(damager.getHealth() + 4, damaged.getMaxHealth()));
        }

        if (itemHasEnchant(sender, 3, this)) {
            damager.setHealth(Math.min(damager.getHealth() + 6, damager.getMaxHealth()));
            damaged.setHealth(Math.min(damager.getHealth() + 6, damaged.getMaxHealth()));
        }
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
