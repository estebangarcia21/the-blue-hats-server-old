package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.CalculationMode;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
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
    private LevelVariable<Double> damageIncrease = new LevelVariable<>(.33, .66, 1D);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        //TODO Gold system

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            tryExecutingEnchant(((Player) event.getDamager()).getItemInHand(), event.getDamager(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damager = (Player) args[0];

        DamageManager.getInstance().addDamage((EntityDamageByEntityEvent) args[1], damageIncrease.at(level), CalculationMode.MULTIPLICATIVE);
        damager.playSound(damager.getLocation(), Sound.ORB_PICKUP, 1, 0.1f);
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
        return new double[] { .33, .66, 1};
    }

    @Override
    public CalculationMode getDamageCalculationMode() {
        return CalculationMode.MULTIPLICATIVE;
    }
}
