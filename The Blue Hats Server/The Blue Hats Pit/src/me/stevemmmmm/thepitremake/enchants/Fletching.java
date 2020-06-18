package me.stevemmmmm.thepitremake.enchants;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class Fletching extends CustomEnchant {
    private final EnchantProperty<Float> damageAmount = new EnchantProperty<>(.07f, .12f, .20f);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                attemptEnchantExecution(BowManager.getInstance().getBowFromArrow(((Arrow) event.getDamager())), event);
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        DamageManager.getInstance().addDamage(((EntityDamageByEntityEvent) args[0]), damageAmount.getValueAtLevel(level), CalculationMode.ADDITIVE);
    }

    @Override
    public String getName() {
        return "Fletching";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Fletching";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("+7%", "+12%", "+20%")
                .write("Deal ").writeVariable(ChatColor.RED, 0, level).write(" bow damage")
                .build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
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
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.BOW };
    }
}
