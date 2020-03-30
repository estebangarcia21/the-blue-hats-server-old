package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.CalculationMode;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class DiamondStomp extends CustomEnchant {
    private LevelVariable<Double> percentDamageIncrease = new LevelVariable<>(.07, .12, .25);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            tryExecutingEnchant(((Player) event.getDamager()).getItemInHand(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[0];

        if (playerHasDiamondPiece((Player) event.getEntity())) {
            DamageManager.getInstance().addDamage(event, percentDamageIncrease.at(level), CalculationMode.ADDITIVE);
        }
    }

    @Override
    public String getName() {
        return "Diamond Stomp";
    }

    @Override
    public String getEnchantReferenceName() {
        return "DiamondStomp";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String damageAmount = level == 1 ? "7%" : level == 2 ? "12%" : level == 3 ? "25%" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Deal " + ChatColor.RED + "+" + damageAmount + ChatColor.GRAY + " damage vs. players");
            add(ChatColor.GRAY + "wearing diamond armor");
        }};
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    private boolean playerHasDiamondPiece(Player player) {
        if (player.getInventory().getBoots() != null) {
            if (player.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS) {
                 return true;
            }
        }

        if (player.getInventory().getLeggings() != null) {
            if (player.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS) {
                return true;
            }
        }

        if (player.getInventory().getChestplate() != null) {
            if (player.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE) {
                return true;
            }
        }

        if (player.getInventory().getHelmet() != null) {
            return player.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET;
        }

        return false;
    }
}
