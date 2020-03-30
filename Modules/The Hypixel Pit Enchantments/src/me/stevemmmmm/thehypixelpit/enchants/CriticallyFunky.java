package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CriticallyFunky extends CustomEnchant implements DamageEnchant, DamageReductionEnchant {
    private LevelVariable<Float> damageReduction = new LevelVariable<>(0.65f, 0.65f, 0.4f);
    private LevelVariable<Integer> appliedLevel = new LevelVariable<>(0, 1, 2);

    private HashMap<UUID, Integer> queue = new HashMap<>();

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            tryExecutingEnchant(((Player) event.getEntity()).getInventory().getLeggings(), event.getDamager());
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                tryExecutingEnchant(((Player) event.getEntity()).getInventory().getLeggings(), event.getDamager());
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damager = (Player) args[0];
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[1];

        if (queue.containsKey(damager.getUniqueId())) {
            if (queue.get(damager.getUniqueId()) == 1) {
                event.setDamage(event.getDamage() * 1.14f);
                queue.remove(damager.getUniqueId());
            }

            if (queue.get(damager.getUniqueId()) == 2) {
                event.setDamage(event.getDamage() * 1.40f);
                queue.remove(damager.getUniqueId());
            }
        }

        if (!isCriticalHit(damager)) return;

        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();

            if (!arrow.isCritical()) return;
        }

        if (level != 1) {
            queue.put(event.getEntity().getUniqueId(), appliedLevel.at(level));
        }

        event.setDamage(event.getDamage() * damageReduction.at(level));
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
        final String data = level == 1 ? "65%:" : level == 2 ? "65%:14%" : level == 3 ? "40%:30%" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Critical hits against you deal");
            add(ChatColor.BLUE + data.split(":")[0] + ChatColor.GRAY + " of the damage they");
            add(ChatColor.GRAY + "normally would" + (level != 1 ? "and empower your" : ""));
            if (level != 0) add(ChatColor.GRAY + "next strike for " + ChatColor.RED + data.split(":")[1] + ChatColor.GRAY + " damage");
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

    @Override
    public double[] getPercentDamageIncreasePerLevel() {
        return new double[] { 0, .14, .3 };
    }

    @Override
    public double[] getPercentReductionPerLevel() {
        return new double[] { .4, .5, .6 };
    }

    @Override
    public CalculationMode getReductionCalculationMode() {
        return CalculationMode.ADDITIVE;
    }

    @Override
    public CalculationMode getDamageCalculationMode() {
        return CalculationMode.ADDITIVE;
    }
}
