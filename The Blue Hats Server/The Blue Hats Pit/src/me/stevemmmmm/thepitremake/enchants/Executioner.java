package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.permissions.core.PermissionsManager;
import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Executioner extends CustomEnchant {
    private final EnchantProperty<Integer> heartsToDie = new EnchantProperty<>(3, 4, 4);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(((Player) event.getDamager()).getInventory().getItemInHand(), event.getEntity(), event.getDamager(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player hit = (Player) args[0];

        if (hit.getHealth() - DamageManager.getInstance().getFinalDamageFromEvent((EntityDamageByEntityEvent) args[2]) / 2 <= heartsToDie.getValueAtLevel(level) && hit.getHealth() > 0) {
            hit.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "EXECUTED!" + ChatColor.GRAY + " by " + PermissionsManager.getInstance().getPlayerRank((Player) args[1]).getNameColor() + ((Player) args[1]).getName() + ChatColor.GRAY + " (insta-kill below " + ChatColor.RED + heartsToDie.getValueAtLevel(level) / 2 + "❤" + ChatColor.GRAY + ")");
            hit.getWorld().playSound(hit.getLocation(), Sound.VILLAGER_DEATH, 1, 0.5f);

            //TODO Add particle

            DamageManager.getInstance().safeSetPlayerHealth(hit, 0);
        }
    }

    @Override
    public String getName() {
        return "Executioner";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Executioner";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("1.5❤", "2❤", "2❤")
                .write("Hitting an enemy to below ").writeVariable(ChatColor.RED, 0, level).next()
                .write("instantly kills them")
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
        return new Material[] { Material.GOLD_SWORD };
    }
}
