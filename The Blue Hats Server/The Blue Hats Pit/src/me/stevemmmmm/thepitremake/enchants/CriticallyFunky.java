package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class CriticallyFunky extends CustomEnchant {
    private final EnchantProperty<Float> damageReduction = new EnchantProperty<>(0.35f, 0.35f, 0.6f);
    private final EnchantProperty<Float> damageIncrease = new EnchantProperty<>(0f, .14f, .3f);

    private final List<UUID> queue = new ArrayList<>();

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(((Player) event.getEntity()).getInventory().getLeggings(), event.getDamager(), event);
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                attemptEnchantExecution(((Player) event.getEntity()).getInventory().getLeggings(), event.getDamager(), event);
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damager = null;
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[1];

        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();

            if (!arrow.isCritical()) return;
        } else if (args[0] instanceof Player) {
            damager = (Player) args[0];
        }

        if (damager == null) return;

        if (!DamageManager.getInstance().isCriticalHit(damager)) return;

        if (queue.contains(damager.getUniqueId())) {
            DamageManager.getInstance().addDamage(event, damageIncrease.getValueAtLevel(level), CalculationMode.ADDITIVE);
            queue.remove(damager.getUniqueId());
        }

        if (level != 1) {
            queue.add(event.getEntity().getUniqueId());
        }

        DamageManager.getInstance().reduceDamage(event, damageReduction.getValueAtLevel(level));
        DamageManager.getInstance().removeExtraCriticalDamage(event);
    }

    @Override
    public String getName() {
        return "Critically Funky";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Critfunky";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .declareVariable("65%", "65%", "40%")
                .declareVariable("", "14%", "30%")
                .write("Critical hits against you deal").next()
                .setColor(ChatColor.BLUE).writeVariable(0, level).resetColor().write(" of the damage they").next()
                .write("normally would")
                .setWriteCondition(level != 1)
                .write(" and empower your").next()
                .write("next strike for ").setColor(ChatColor.RED).writeVariable(1, level).resetColor().write(" damage")
                .build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
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
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
