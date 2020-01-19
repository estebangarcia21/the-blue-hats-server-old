package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class LuckyShot extends CustomEnchant {

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();

                triggerEnchant(player.getInventory().getItemInHand(), event);
            }
        }
    }

    @Override
    public void triggerEnchant(ItemStack sender, Object... args) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[0];

        if (itemHasEnchant(sender, 1, this)) {
            if (percentChance(2)) event.setDamage(event.getDamage() * 4);
        }

        if (itemHasEnchant(sender, 2, this)) {
            if (percentChance(5)) event.setDamage(event.getDamage() * 4);
        }

        if (itemHasEnchant(sender, 3, this)) {
            if (percentChance(10)) event.setDamage(event.getDamage() * 4);
        }
    }

    @Override
    public String getName() {
        return "Lucky Shot";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Luckyshot";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String percentChance = level == 1 ? "2%" : level == 2 ? "5%" : level == 3 ? "10%" : "";

        return new ArrayList<String>() {{
            add(ChatColor.YELLOW + percentChance + ChatColor.GRAY + " chance for a shot to deal");
            add(ChatColor.GRAY + "quadruple damage");
        }};
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }
}
