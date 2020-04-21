package me.stevemmmmm.thepitremake.enchants;

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

public class ComboDamage extends CustomEnchant {
    private final LevelVariable<Integer> hitsNeeded = new LevelVariable<>(4, 3, 3);
    private final LevelVariable<Float> damageAmount = new LevelVariable<>(.2f, .3f, .45f);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            attemptEnchantExecution(((Player) event.getDamager()).getInventory().getItemInHand(), event.getDamager(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player player = (Player) args[0];
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[1];

        updateHitCount(player);

        if (hasRequiredHits(player, hitsNeeded.at(level))) {
            player.playSound(player.getLocation(), Sound.DONKEY_HIT, 1, 0.5f);
            DamageManager.getInstance().addDamage(event, damageAmount.at(level), CalculationMode.ADDITIVE);
        }
    }

    @Override
    public String getName() {
        return "Combo: Damage";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Combodamage";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("fourth", "third", "third")
                .addVariable("+20%", "+30%", "+45%")
                .write("Every ").setColor(ChatColor.YELLOW).writeVariable(0, level).resetColor().write(" strike deals").nextLine()
                .setColor(ChatColor.RED).writeVariable(1, level).resetColor().write(" damage")
                .build();
    }

    @Override
    public boolean isRemovedFromPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.A;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material getEnchantItemType() {
        return Material.GOLD_SWORD;
    }
}
