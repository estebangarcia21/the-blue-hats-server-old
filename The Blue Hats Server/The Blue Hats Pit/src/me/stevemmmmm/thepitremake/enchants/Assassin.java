package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantProperty;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Assassin extends CustomEnchant {
    private final EnchantProperty<Integer> cooldownTime = new EnchantProperty<>(5, 4, 3);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                if (((Player) event.getEntity()).isSneaking()) {
                    attemptEnchantExecution(((Player) event.getEntity()).getInventory().getLeggings(), ((Arrow) event.getDamager()).getShooter(), event.getEntity());
                }
            }
        }

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            if (((Player) event.getEntity()).isSneaking()) {
                attemptEnchantExecution(((Player) event.getEntity()).getInventory().getLeggings(), event.getDamager(), event.getEntity());
            }
        }
    }


    @Override
    public void applyEnchant(int level, Object... args) {
        Player target = (Player) args[0];
        Player player = (Player) args[1];

        if (isNotOnCooldown(player)) {
            Location tpLoc = target.getLocation().subtract(target.getEyeLocation().getDirection().normalize());
            tpLoc.setY(target.getLocation().getY());

            if (tpLoc.getBlock().getType() == Material.AIR) {
                player.teleport(tpLoc);
            } else {
                player.teleport(target);
            }

            player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 2);
        }

        startCooldown(player, cooldownTime.getValueAtLevel(level), true);
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
        return new LoreBuilder()
                .addVariable("5s", "4s", "3s")
                .setColor(ChatColor.GRAY)
                .write("Sneaking teleports you behind").next()
                .write("your").next()
                .write("attacker. (").writeVariable(0, level).write(" cooldown)")
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
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
