package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class DiamondStomp extends CustomEnchant {
    private final LevelVariable<Double> percentDamageIncrease = new LevelVariable<>(.07, .12, .25);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(((Player) event.getDamager()).getItemInHand(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[0];

        if (playerHasDiamondPiece((Player) event.getEntity())) {
            DamageManager.getInstance().addDamage(event, percentDamageIncrease.at(level), CalculationMode.ADDITIVE);
        }
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
    public String getName() {
        return "Diamond Stomp";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Diamondstomp";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("7%", "12%", "25%")
                .write("Deal ").setColor(ChatColor.RED).write("+").writeVariable(0, level).resetColor().write(" damage vs. players").nextLine()
                .write("wearing diamond armor")
                .build();
    }

    @Override
    public boolean isRemovedFromPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.A;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material getEnchantItemType() {
        return Material.GOLD_SWORD;
    }
}
