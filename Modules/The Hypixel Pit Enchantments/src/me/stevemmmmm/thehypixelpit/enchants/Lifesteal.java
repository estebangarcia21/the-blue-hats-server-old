package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class Lifesteal extends CustomEnchant {
    private LevelVariable<Float> healPercentage = new LevelVariable<>(0.04f, 0.08f, 0.13f);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            tryExecutingEnchant(((Player) event.getDamager()).getItemInHand(), event.getDamager(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damager = (Player) args[0];

        damager.setHealth(Math.min(damager.getHealth() + DamageManager.getInstance().getDamageFromEvent((EntityDamageByEntityEvent) args[1]) * healPercentage.at(level), damager.getMaxHealth()));
    }

    @Override
    public String getName() {
        return "Lifesteal";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Lifesteal";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String healAmount = level == 1 ? "4%" : level == 2 ? "8%" : level == 3 ? "13%" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Heal for " + ChatColor.RED + healAmount + ChatColor.GRAY + " of damage dealt");
        }};
    }

    @Override
    public boolean isTierTwoEnchant() {
        return true;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }
}
