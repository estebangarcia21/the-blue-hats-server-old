package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Pullbow extends CustomEnchant {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
//        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
//            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
//
//            }
//        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {

    }

    @Override
    public String getName() {
        return "Pullbow";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Pullbow";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .setWriteCondition(level == 1)
                .write("Hitting a player pulls them toward").next()
                .write("you (8s cooldown per player)")
                .setWriteCondition(level != 1)
                .write("Hitting a player puls them and").next()
                .write("nearby players toward you (8s").next()
                .write("cooldown)")
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
        return new Material[] { Material.BOW };
    }
}
