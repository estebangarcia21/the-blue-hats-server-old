package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.CalculationMode;
import me.stevemmmmm.thepitremake.managers.enchants.DamageManager;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import me.stevemmmmm.thepitremake.managers.enchants.LevelVariable;
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
                .write("Deal ").setColor(ChatColor.RED).writeVariable(0, level).resetColor().write(" damage per ").setColor(ChatColor.RED).write("❤").resetColor().nextLine()
                .write("you're missing")
                .build();
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
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
