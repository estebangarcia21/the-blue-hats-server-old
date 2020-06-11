package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Perun extends CustomEnchant {
    private final EnchantProperty<Integer> perunDamage = new EnchantProperty<>(3, 4, 2);
    private final EnchantProperty<Integer> hitsNeeded = new EnchantProperty<>(5, 4, 4);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(((Player) event.getDamager()).getItemInHand(), event.getDamager(), event.getEntity());
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damager = (Player) args[0];
        Player target = (Player) args[1];

        int damage = perunDamage.getValueAtLevel(level);

        updateHitCount(damager);

        if (hasRequiredHits(damager, hitsNeeded.getValueAtLevel(level))) {
            if (level == 3) {
                if (target.getInventory().getBoots() != null) if (target.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS) damage += 2;
                if (target.getInventory().getChestplate() != null) if (target.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE) damage += 2;
                if (target.getInventory().getLeggings() != null) if (target.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS) damage += 2;
                if (target.getInventory().getHelmet() != null) if (target.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET) damage += 2;
            }

            damager.getWorld().strikeLightningEffect(target.getLocation());
            DamageManager.getInstance().doTrueDamage(target, damage, damager);
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
        return new LoreBuilder()
                .addVariable("1.5❤", "2❤", "1❤ + 1❤")
                .addVariable("fifth", "fourth", "fourth")
                .write("Every ").setColor(ChatColor.YELLOW).writeVariable(1, level).resetColor().write(" hit strikes").next()
                .setColor(ChatColor.YELLOW).write("lightning").resetColor().write(" for ").setColor(ChatColor.RED)
                .writeVariable(0, level).resetColor().writeOnlyIf(level != 3, ".").next()
                .setWriteCondition(level == 3)
                .write("per ").setColor(ChatColor.AQUA).write("diamond piece").resetColor().write(" on your victim").next()
                .write("victim.").next()
                .resetCondition()
                .write(ChatColor.ITALIC + "Lightning deals true damage")
                .build();
    }

    @Override
    public boolean isRemovedFromPassiveWorld() {
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
