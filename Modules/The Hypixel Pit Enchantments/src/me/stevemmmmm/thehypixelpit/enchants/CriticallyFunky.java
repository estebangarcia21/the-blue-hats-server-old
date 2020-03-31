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
import java.util.List;
import java.util.UUID;

public class CriticallyFunky extends CustomEnchant {
    private LevelVariable<Float> damageReduction = new LevelVariable<>(0.35f, 0.35f, 0.6f);
    private LevelVariable<Float> damageIncrease = new LevelVariable<>(0f, .14f, .3f);

    private List<UUID> queue = new ArrayList<>();

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            tryExecutingEnchant(((Player) event.getEntity()).getInventory().getLeggings(), event.getDamager(), event);
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                tryExecutingEnchant(((Player) event.getEntity()).getInventory().getLeggings(), event.getDamager(), event);
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

        if (!isCriticalHit(damager)) return;

        if (queue.contains(damager.getUniqueId())) {
            DamageManager.getInstance().addDamage(event, damageIncrease.at(level), CalculationMode.ADDITIVE);
            queue.remove(damager.getUniqueId());
        }

        if (level != 1) {
            queue.add(event.getEntity().getUniqueId());
        }

        DamageManager.getInstance().reduceDamage(event, damageReduction.at(level));
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
        final String data = level == 1 ? "65%:" : level == 2 ? "65%:14%" : level == 3 ? "40%:30%" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Critical hits against you deal");
            add(ChatColor.BLUE + data.split(":")[0] + ChatColor.GRAY + " of the damage they");
            add(ChatColor.GRAY + "normally would" + (level != 1 ? "and empower your" : ""));
            if (level != 1) add(ChatColor.GRAY + "next strike for " + ChatColor.RED + data.split(":")[1] + ChatColor.GRAY + " damage");
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
}
