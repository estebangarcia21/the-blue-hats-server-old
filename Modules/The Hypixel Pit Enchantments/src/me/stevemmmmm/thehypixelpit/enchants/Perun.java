package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Perun extends CustomEnchant {
    private LevelVariable<Integer> perunDamage = new LevelVariable<>(3, 4, 2);
    private LevelVariable<Float> damageReflection = new LevelVariable<>(0f, .25f, .5f);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            tryExecutingEnchant(((Player) event.getDamager()).getItemInHand(), event.getDamager(), event.getEntity());
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damager = (Player) args[0];
        Player target = (Player) args[1];

        int damage = perunDamage.at(level);

        if (level == 3) {
            if (target.getInventory().getBoots() != null) if (target.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS) damage += 2;
            if (target.getInventory().getChestplate() != null) if (target.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE) damage += 2;
            if (target.getInventory().getLeggings() != null) if (target.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS) damage += 2;
            if (target.getInventory().getHelmet() != null) if (target.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET) damage += 2;
        }

        if (itemHasEnchant(target.getInventory().getLeggings(), new Mirror())) {
            damager.setHealth(Math.max(damager.getHealth() - (damage * damageReflection.at(getEnchantLevel(target.getInventory().getLeggings(), new Mirror()))), 0));
        }
    }

    @Override
    public String getName() {
        return "Combo: Perun's Wrath";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Perun";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String dmgAmount = level == 1 ? "1.5❤" : level == 2 ? "2❤" : level == 3 ? "ext" : "";
        final String strike = level == 1 ? "fifth" : level == 2 ? "fourth" : level == 3 ? "fourth" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Every " + ChatColor.YELLOW + strike + ChatColor.GRAY + " hit strikes");
            add(ChatColor.YELLOW + "lightning" + ChatColor.GRAY + " for " + ChatColor.RED + (!dmgAmount.equalsIgnoreCase("ext") ? dmgAmount + ChatColor.GRAY + "." : "1❤ + 1❤"));
            if (dmgAmount.equalsIgnoreCase("ext")) add(ChatColor.GRAY + "per " + ChatColor.AQUA + "diamond piece" + ChatColor.GRAY + " on your victim");
            if (dmgAmount.equalsIgnoreCase("ext")) add(ChatColor.GRAY + "victim.");
            add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Lightning deals true damage");
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
