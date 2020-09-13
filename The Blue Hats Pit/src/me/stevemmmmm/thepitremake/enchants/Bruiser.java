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

public class Bruiser extends CustomEnchant {
    private final EnchantProperty<Integer> heartsReduced = new EnchantProperty<>(1, 2, 4);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(((Player) event.getEntity()).getInventory().getItemInHand(), event.getEntity(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player player = (Player) args[0];

        if (player.isBlocking()) {
            DamageManager.getInstance().reduceAbsoluteDamage((EntityDamageByEntityEvent) args[1], heartsReduced.getValueAtLevel(level));
        }
    }

    @Override
    public String getName() {
        return "Bruiser";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Bruiser";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .declareVariable("0.5❤", "1❤", "2❤")
                .write("Blocking with your swords reduces").next()
                .write("received damage by ").writeVariable(ChatColor.RED, 0, level)
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
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLD_SWORD };
    }
}
