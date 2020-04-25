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

public class PainFocus extends CustomEnchant {
    private final LevelVariable<Float> damageIncreasePerHeartLost = new LevelVariable<>(.01f, .02f, .05f);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(((Player) event.getDamager()).getInventory().getItemInHand(), event.getDamager(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player player = (Player) args[0];
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[1];

        int heartsLost = (int) (player.getMaxHealth() - player.getHealth()) / 2;

        DamageManager.getInstance().addDamage(event, damageIncreasePerHeartLost.at(level) * heartsLost, CalculationMode.ADDITIVE);
    }

    @Override
    public String getName() {
        return "Pain Focus";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Painfocus";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("+1%", "+2%", "+5%")
                .write("Deal ").setColor(ChatColor.RED).writeVariable(0, level).resetColor().write(" damage per ").setColor(ChatColor.RED).write("❤").resetColor().next()
                .write("you're missing")
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
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLD_SWORD };
    }
}
