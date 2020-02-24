package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Megalongbow extends CustomEnchant {

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow) {
            executeEnchant(((Player) event.getEntity()).getInventory().getItemInHand(), event);
        }
    }

    @Override
    public boolean executeEnchant(ItemStack sender, Object executedEvent) {
        EntityShootBowEvent event = (EntityShootBowEvent) executedEvent;

        Arrow arrow = (Arrow) event.getProjectile();
        Player player = (Player) event.getEntity();

        if (itemHasEnchant(sender, 1, this)) {
            if (isOnCooldown(player)) {
                arrow.setCritical(true);
                arrow.setVelocity(player.getLocation().getDirection().multiply(2.90));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 1), true);
            }

            startCooldown(player, 16, false);
            return true;
        }

        if (itemHasEnchant(sender, 2, this)) {
            if (isOnCooldown(player)) {
                arrow.setCritical(true);
                arrow.setVelocity(player.getLocation().getDirection().multiply(2.90));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 2), true);
            }

            startCooldown(player, 16, false);
            return true;
        }

        if (itemHasEnchant(sender, 3, this)) {
            if (isOnCooldown(player)) {
                arrow.setCritical(true);
                arrow.setVelocity(player.getLocation().getDirection().multiply(2.90));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 3), true);
            }

            startCooldown(player, 16, false);
            return true;
        }

        return false;
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
