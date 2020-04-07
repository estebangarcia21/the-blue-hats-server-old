package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.LoreBuilder;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Healer extends CustomEnchant {
    private LevelVariable<Integer> healAmount = new LevelVariable<>(2, 4, 6);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(this, ((Player) event.getDamager()).getItemInHand(), event.getDamager(), event.getEntity(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damager = (Player) args[0];
        Player damaged = (Player) args[1];

        damager.setHealth(Math.min(damager.getHealth() + healAmount.at(level), damager.getMaxHealth()));
        damaged.setHealth(Math.min(damaged.getHealth() + healAmount.at(level), damaged.getMaxHealth()));
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
                .addVariable("1❤", "2❤", "3❤")
                .write("Hitting a player ").setColor(ChatColor.GREEN).write("heals").resetColor().write(" both you and them for ").setColor(ChatColor.RED).writeVariable(0, level)
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
