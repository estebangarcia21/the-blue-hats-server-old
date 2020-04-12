package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import me.stevemmmmm.thehypixelpit.managers.enchants.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;

public class Executioner extends CustomEnchant {
    private LevelVariable<Integer> heartsToDie = new LevelVariable<>(3, 4, 4);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(this, ((Player) event.getDamager()).getInventory().getItemInHand(), event.getEntity(), event.getDamager(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player hit = (Player) args[0];

        if (hit.getHealth() - DamageManager.getInstance().getFinalDamageFromEvent((EntityDamageByEntityEvent) args[2]) / 2 <= heartsToDie.at(level) && hit.getHealth() > 0) {
            hit.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "EXECUTED!" + ChatColor.GRAY + " by " + ChatColor.GOLD + ((Player) args[1]).getName() + ChatColor.GRAY + " (insta-kill below " + ChatColor.RED + heartsToDie.at(level) / 2 + "❤" + ChatColor.GRAY + ")");
            hit.getWorld().playSound(hit.getLocation(), Sound.VILLAGER_DEATH, 1, 0.5f);

            //TODO ADd particle

            DamageManager.getInstance().killPlayer(hit);
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
                .write("Hitting an enemy to below ").writeVariable(ChatColor.RED, 0, level).nextLine()
                .write("instantly kills them")
                .build();
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }

    @Override
    public Material getEnchantItemType() {
        return Material.GOLD_SWORD;
    }
}
