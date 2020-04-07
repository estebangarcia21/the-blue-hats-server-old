package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import me.stevemmmmm.thehypixelpit.managers.enchants.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class Chipping extends CustomEnchant {
    private LevelVariable<Integer> damageAmount = new LevelVariable<>(1, 2, 3);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                attemptEnchantExecution(this, ((Player) ((Arrow) event.getDamager()).getShooter()).getInventory().getItemInHand(), event.getEntity(), ((Arrow) event.getDamager()).getShooter());
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
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }
}
