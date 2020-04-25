package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Chipping extends CustomEnchant {
    private final LevelVariable<Integer> damageAmount = new LevelVariable<>(1, 2, 3);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                attemptEnchantExecution(((Player) ((Arrow) event.getDamager()).getShooter()).getInventory().getItemInHand(), event.getEntity(), ((Arrow) event.getDamager()).getShooter());
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player hitPlayer = (Player) args[0];
        Player damager = (Player) args[1];

        DamageManager.getInstance().doTrueDamage(hitPlayer, damageAmount.at(level), damager);
    }

    @Override
    public String getName() {
        return "Chipping";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Chipping";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("0.5❤", "1.0❤", "1.5❤")
                .write("Deals ").writeVariable(ChatColor.RED, 0, level).write(" extra true damage")
                .build();
    }

    @Override
    public boolean isRemovedFromPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.BOW };
    }
}
