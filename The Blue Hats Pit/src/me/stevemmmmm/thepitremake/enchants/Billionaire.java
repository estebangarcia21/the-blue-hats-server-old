package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.*;
import me.stevemmmmm.thepitremake.managers.other.GrindingSystem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Billionaire extends CustomEnchant {
    private final EnchantProperty<Double> damageIncrease = new EnchantProperty<>(1.33D, 1.67D, 2D);
    private final EnchantProperty<Integer> goldNeeded = new EnchantProperty<>(100, 200, 350);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(((Player) event.getDamager()).getItemInHand(), event.getDamager(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damager = (Player) args[0];

        if (GrindingSystem.getInstance().getPlayerGold(damager) >= goldNeeded.getValueAtLevel(level)) {
            GrindingSystem.getInstance().setPlayerGold(damager,
                    GrindingSystem.getInstance().getPlayerGold(damager) - goldNeeded.getValueAtLevel(level));

            DamageManager.getInstance().addDamage((EntityDamageByEntityEvent) args[1],
                    damageIncrease.getValueAtLevel(level), CalculationMode.MULTIPLICATIVE);
            damager.playSound(damager.getLocation(), Sound.ORB_PICKUP, 1, 0.73f);
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
        return new LoreBuilder().declareVariable("1.33", "1.67", "2").declareVariable("100g", "200g", "350g")
                .setColor(ChatColor.GRAY).write("Hits with this sword deals ").setColor(ChatColor.RED)
                .writeVariable(0, level).write("x").next().setColor(ChatColor.RED).write("damage ")
                .setColor(ChatColor.GRAY).write("but cost ").setColor(ChatColor.GOLD).writeVariable(1, level).build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLD_SWORD };
    }
}
