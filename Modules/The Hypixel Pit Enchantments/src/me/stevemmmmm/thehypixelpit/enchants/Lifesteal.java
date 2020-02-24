package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Lifesteal extends CustomEnchant {
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            executeEnchant(((Player) event.getDamager()).getItemInHand(), event);
        }
    }

    @Override
    public boolean executeEnchant(ItemStack sender, Object executedEvent) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) executedEvent;

        Player damager = (Player) event.getDamager();

        if (itemHasEnchant(sender, 1, this)) {
            damager.setHealth(Math.min(damager.getHealth() + DamageManager.getInstance().calculateDamage(event, damager) * 0.04f, damager.getMaxHealth()));
            return true;
        }

        if (itemHasEnchant(sender, 2, this)) {
            damager.setHealth(Math.min(damager.getHealth() + DamageManager.getInstance().calculateDamage(event, damager) * 0.08f, damager.getMaxHealth()));
            return true;
        }

        if (itemHasEnchant(sender, 3, this)) {
            damager.setHealth(Math.min(damager.getHealth() + DamageManager.getInstance().calculateDamage(event, damager) * 0.13f, damager.getMaxHealth()));
            return true;
        }

        return false;
    }

    @Override
    public String getName() {
        return "Lifesteal";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Lifesteal";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String healAmount = level == 1 ? "4%" : level == 2 ? "8%" : level == 3 ? "13%" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Heal for " + ChatColor.RED + healAmount + ChatColor.GRAY + " of damage dealt");
        }};
    }

    @Override
    public boolean isTierTwoEnchant() {
        return true;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }
}
