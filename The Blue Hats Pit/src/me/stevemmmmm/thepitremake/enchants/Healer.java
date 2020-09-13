package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantProperty;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Healer extends CustomEnchant {
    private final EnchantProperty<Integer> healAmount = new EnchantProperty<>(2, 4, 6);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(((Player) event.getDamager()).getItemInHand(), event.getDamager(), event.getEntity(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damager = (Player) args[0];
        Player damaged = (Player) args[1];

        damager.setHealth(Math.min(damager.getHealth() + healAmount.getValueAtLevel(level), damager.getMaxHealth()));
        damaged.setHealth(Math.min(damaged.getHealth() + healAmount.getValueAtLevel(level), damaged.getMaxHealth()));
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
        return new LoreBuilder()
                .declareVariable("1❤", "2❤", "3❤")
                .write("Hitting a player ").setColor(ChatColor.GREEN).write("heals").resetColor().write(" both you and them for ").setColor(ChatColor.RED).writeVariable(0, level)
                .build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return true;
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
