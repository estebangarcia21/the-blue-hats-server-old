package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Perun extends CustomEnchant {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            triggerEnchant(((Player) event.getDamager()).getItemInHand(), event.getDamager(), event.getEntity());
        }
    }

    @Override
    public void triggerEnchant(ItemStack sender, Object... args) {
        Player player = (Player) args[0];
        Player damaged = (Player) args[1];

        if (itemHasEnchant(sender, 1, this)) {
            updateHitCount(player);

            if (hasRequiredHits(player, 5)) {
                lightningStrike(damaged, player, 3, false);
            }
        }

        if (itemHasEnchant(sender, 2, this)) {
            updateHitCount(player);

            if (hasRequiredHits(player, 5)) {
                lightningStrike(damaged, player, 4, false);
            }
        }

        if (itemHasEnchant(sender, 3, this)) {
            updateHitCount(player);

            if (hasRequiredHits(player, 4)) {
                lightningStrike(damaged, player, 2, true);
            }
        }
    }

    private void lightningStrike(Player target, Player damager, int damage, boolean increaseDmgByDiamondArmor) {
        if (increaseDmgByDiamondArmor) {
            if (target.getInventory().getBoots() != null) if (target.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS) damage += 2;
            if (target.getInventory().getChestplate() != null) if (target.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE) damage += 2;
            if (target.getInventory().getLeggings() != null) if (target.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS) damage += 2;
            if (target.getInventory().getHelmet() != null) if (target.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET) damage += 2;
        }

        target.getWorld().strikeLightningEffect(target.getLocation());

        if (itemHasEnchant(target.getInventory().getLeggings(), 1, new Mirror())) {
            return;
        }

        if (itemHasEnchant(target.getInventory().getLeggings(), 2, new Mirror())) {
            damager.setHealth(Math.max(damager.getHealth() - (damage * 0.25f), 0));
            return;
        }

        if (itemHasEnchant(target.getInventory().getLeggings(), 3, new Mirror())) {
            damager.setHealth(Math.max(damager.getHealth() - (damage * 0.5f), 0));
            return;
        }

        target.setHealth(Math.max(target.getHealth() - damage, 0));
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
