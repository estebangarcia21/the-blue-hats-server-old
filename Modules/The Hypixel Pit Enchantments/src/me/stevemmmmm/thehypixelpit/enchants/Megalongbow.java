package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.EnchantVariable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Megalongbow extends CustomEnchant {
    private EnchantVariable<Integer> amplifier = new EnchantVariable<>(1, 2, 3);

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow) {
            tryExecutingEnchant(((Player) event.getEntity()).getInventory().getItemInHand(), event.getProjectile(), event.getEntity());
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Arrow arrow = (Arrow) args[0];
        Player player = (Player) args[1];

        arrow.setCritical(true);
        arrow.setVelocity(player.getLocation().getDirection().multiply(2.90));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, amplifier.at(level)), true);
    }

    @Override
    public String getName() {
        return "Mega Longbow";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Megalongbow";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String jumpBoost = level == 1 ? "Jump Boost II" : level == 2 ? "Jump Boost III" : level == 3 ? "Jump Boost IV" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "One shot per second, this bow is");
            add(ChatColor.GRAY + "automatically fully drawn and");
            add(ChatColor.GRAY + "grants " + ChatColor.GREEN + jumpBoost + ChatColor.GRAY + " (2s)");
        }};
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }
}
