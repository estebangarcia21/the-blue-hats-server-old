package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.other.DamageEnchant;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Billionaire extends CustomEnchant implements DamageEnchant {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        //TODO Gold system

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            triggerEnchant(((Player) event.getDamager()).getItemInHand(), event);
        }
    }

    public void triggerEnchant(ItemStack sender, Object... args) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[0];

        if (itemHasEnchant(sender, 1, this)) {
            event.setDamage(event.getDamage() * 1.33f);
            ((Player) event.getDamager()).playSound(event.getDamager().getLocation(), Sound.ORB_PICKUP, 1, 0.1f);
        }

        if (itemHasEnchant(sender, 2, this)) {
            event.setDamage(event.getDamage() * 1.67f);
            ((Player) event.getDamager()).playSound(event.getDamager().getLocation(), Sound.ORB_PICKUP, 1, 0.1f);
        }

        if (itemHasEnchant(sender, 3, this)) {
            event.setDamage(event.getDamage() * 2f);
            ((Player) event.getDamager()).playSound(event.getDamager().getLocation(), Sound.ORB_PICKUP, 1, 0.1f);
        }
    }

    @Override
    public String getName() {
        return "Billionaire";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Billionaire";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String dmgAmount = level == 1 ? "1.33" : level == 2 ? "1.67" : level == 3 ? "2" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Hits with this sword deals " + ChatColor.RED + dmgAmount + "x");
            add(ChatColor.RED + "damage " + ChatColor.GRAY + "but costs " + ChatColor.GOLD + "gold");
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

    @Override
    public double[] getPercentDamageIncreasePerLevel() {
        return new double[] { };
    }
}
