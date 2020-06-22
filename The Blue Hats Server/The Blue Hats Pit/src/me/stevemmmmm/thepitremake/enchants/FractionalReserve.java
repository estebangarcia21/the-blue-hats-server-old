package me.stevemmmmm.thepitremake.enchants;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import me.stevemmmmm.thepitremake.managers.enchants.*;
import me.stevemmmmm.thepitremake.managers.other.GrindingSystem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class FractionalReserve extends CustomEnchant {
    private final EnchantProperty<Double> maximumDamageReduction = new EnchantProperty<>(.15D, .21D, .30D);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            attemptEnchantExecution(((Player) event.getEntity()).getInventory().getItemInHand(), event.getEntity(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player hitPlayer = (Player) args[0];

        double damageReduction = 0;

        for (int i = 10000; i <= GrindingSystem.getInstance().getPlayerGold(hitPlayer); i += 10000) {
            damageReduction += .10D;
        }

        if (damageReduction > maximumDamageReduction.getValueAtLevel(level)) {
            damageReduction = maximumDamageReduction.getValueAtLevel(level);
        }

        DamageManager.getInstance().reduceDamage(((EntityDamageByEntityEvent) args[1]), damageReduction);
    }

    @Override
    public String getName() {
        return "Fractional Reserve";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Frac";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .declareVariable("-15%", "-21%", "-30%")
                .write("Recieve ").write(ChatColor.BLUE, "-1% damage ").write("per").next()
                .write(ChatColor.GOLD, "10,000g ").write("you have (").writeVariable(ChatColor.BLUE, 0, level).next()
                .write("max)")
                .build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.AUCTION;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
