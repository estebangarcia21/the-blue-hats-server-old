package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Assassin extends CustomEnchant {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                Player player = (Player) ((Arrow) event.getDamager()).getShooter();

                if (((Player) event.getEntity()).isSneaking()) {
                    triggerEnchant(((Player) event.getEntity()).getInventory().getLeggings(), player, event.getEntity());
                }
            }
        }

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            if (((Player) event.getEntity()).isSneaking()) {
                triggerEnchant(((Player) event.getEntity()).getInventory().getLeggings(), event.getDamager(), event.getEntity());
            }
        }
    }

    @Override
    public void triggerEnchant(ItemStack sender, Object... args) {
        if (sender == null) return;

        Player target = (Player) args[0];
        Player player = (Player) args[1];

        if (itemHasEnchant(sender, 1, this)) {
            if (!isOnCooldown(player)) {
                Location tpLoc = target.getLocation().subtract(target.getEyeLocation().getDirection().normalize());
                tpLoc.setY(target.getLocation().getY());

                if (tpLoc.getBlock().getType() == Material.AIR) {
                    player.teleport(tpLoc);
                } else {
                    player.teleport(target);
                }

                player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 2);
            }

            startCooldown(player, 5, true);
        }

        if (itemHasEnchant(sender, 2, this)) {
            if (!isOnCooldown(player)) {
                Location tpLoc = target.getLocation().subtract(target.getEyeLocation().getDirection().normalize());
                tpLoc.setY(target.getLocation().getY());

                if (tpLoc.getBlock().getType() == Material.AIR) {
                    player.teleport(tpLoc);
                } else {
                    player.teleport(target);
                }

                player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 2);
            }

            startCooldown(player, 4, true);
        }

        if (itemHasEnchant(sender, 3, this)) {
            if (!isOnCooldown(player)) {
                Location tpLoc = target.getLocation().subtract(target.getEyeLocation().getDirection().normalize());
                tpLoc.setY(target.getLocation().getY());

                if (tpLoc.getBlock().getType() == Material.AIR) {
                    player.teleport(tpLoc);
                } else {
                    player.teleport(target);
                }

                player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 2);
            }

            startCooldown(player, 3, true);
        }
    }

    @Override
    public String getName() {
        return "Assassin";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Assassin";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String cooldown = level == 1 ? "5s" : level == 2 ? "4s" : level == 3 ? "3s" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Sneaking teleports you behind");
            add(ChatColor.GRAY + "your");
            add(ChatColor.GRAY + "attacker. (" + cooldown + " cooldown)");
        }};
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }
}
