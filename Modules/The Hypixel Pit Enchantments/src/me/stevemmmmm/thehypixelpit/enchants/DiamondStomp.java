package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.enchants.DamageCalculationMode;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageEnchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class DiamondStomp extends DamageEnchant {
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            triggerEnchant(((Player) event.getDamager()).getItemInHand(), event);
        }
    }

    @Override
    public boolean triggerEnchant(ItemStack sender, EntityDamageByEntityEvent event) {
        Player damaged = (Player) event.getEntity();

        if (itemHasEnchant(sender, 1, this)) {
            if (playerHasDiamondPiece(damaged)) event.setDamage(event.getDamage() * 1.07f);
        }

        if (itemHasEnchant(sender, 2, this)) {
            if (playerHasDiamondPiece(damaged)) event.setDamage(event.getDamage() * 1.12f);
        }

        if (itemHasEnchant(sender, 3, this)) {
            return playerHasDiamondPiece(damaged);
        }

        return false;
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

    @Override
    public double[] getPercentDamageIncreasePerLevel() {
        return new double[] { .07, .12, .25 };
    }

    @Override
    public DamageCalculationMode getCalculationMode() {
        return DamageCalculationMode.ADDITIVE;
    }
}
