package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.enchants.EnvironmentalEnchant;
import me.stevemmmmm.thehypixelpit.managers.other.DamageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Lifesteal extends EnvironmentalEnchant {
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

        if (itemHasEnchant(sender, 1, this)) {
            damager.setHealth(Math.min(damager.getHealth() + event.getDamage() * 0.04f, 20));
        }

        if (itemHasEnchant(sender, 2, this)) {
            damager.setHealth(Math.min(damager.getHealth() + event.getDamage() * 0.08f, 20));
        }

        if (itemHasEnchant(sender, 3, this)) {
            damager.setHealth(Math.min(damager.getHealth() + DamageManager.getInstance().calculateDamage(event, sender) * 0.13f, 20));
            event.getDamager().sendMessage(String.valueOf(DamageManager.getInstance().calculateDamage(event, sender) * 0.13f));
        }
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
