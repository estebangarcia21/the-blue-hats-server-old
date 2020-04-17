package me.stevemmmmm.thepitremake.enchants;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.DamageManager;
import me.stevemmmmm.thepitremake.managers.enchants.LevelVariable;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import me.stevemmmmm.thepitremake.managers.other.GrindingSystem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class FractionalReserve extends CustomEnchant {
    private final LevelVariable<Double> maximumDamageReduction = new LevelVariable<>(.15D, .21D, .30D);

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

        if (damageReduction > maximumDamageReduction.at(level)) {
            damageReduction = maximumDamageReduction.at(level);
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
                .addVariable("-15%", "-21%", "-30%")
                .write("Recieve ").write(ChatColor.BLUE, "-1% damage ").write("per").nextLine()
                .write(ChatColor.GOLD, "10,000g ").write("you have (").writeVariable(ChatColor.BLUE, 0, level).nextLine()
                .write("max)")
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

    @Override
    public Material getEnchantItemType() {
        return Material.LEATHER_LEGGINGS;
    }
}
