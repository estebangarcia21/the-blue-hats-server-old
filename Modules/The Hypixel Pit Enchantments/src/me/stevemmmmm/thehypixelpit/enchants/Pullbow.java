package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Pullbow extends CustomEnchant {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                
            }
        }
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
                .write("Hitting a player pulls them toward").nextLine()
                .write("you (8s cooldown per player)")
                .setWriteCondition(level != 1)
                .write("Hitting a player puls them and").nextLine()
                .write("nearby players toward you (8s").nextLine()
                .write("cooldown)")
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
        return Material.BOW;
    }
}
