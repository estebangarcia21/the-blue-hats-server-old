package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.CalculationMode;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.DescriptionBuilder;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Billionaire extends CustomEnchant {
    private LevelVariable<Double> damageIncrease = new LevelVariable<>(.33, .67, 1D);

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
        return new DescriptionBuilder()
                .addVariable("1.33", "1.67", "2")
                .setColor(ChatColor.GRAY)
                .write("Hits with this sword deals ").setColor(ChatColor.RED).writeVariable(0, level).write("x").nextLine()
                .setColor(ChatColor.RED).write("damage ").setColor(ChatColor.GRAY).write("but costs ").setColor(ChatColor.GOLD).write("gold")
                .build();
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
