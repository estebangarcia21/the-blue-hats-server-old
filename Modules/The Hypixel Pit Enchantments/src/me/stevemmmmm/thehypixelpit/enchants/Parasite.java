package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.DescriptionBuilder;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class Parasite extends CustomEnchant {
    private LevelVariable<Integer> healAmount = new LevelVariable<>(1, 2, 3);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                attemptEnchantExecution(((Player) ((Arrow) event.getDamager()).getShooter()).getInventory().getItemInHand(), ((Arrow) event.getDamager()).getShooter());
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player player = (Player) args[0];

        player.setHealth(Math.min(player.getHealth() + healAmount.at(level), player.getMaxHealth()));
    }

    @Override
    public String getName() {
        return "Parasite";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Parasite";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new DescriptionBuilder()
                .addVariable("0.5❤", "1.0❤", "1.5❤")
                .write("Heal ").setColor(ChatColor.RED).writeVariable(0, level).resetColor().write(" on arrow hit")
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
