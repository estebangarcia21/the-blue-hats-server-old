package me.stevemmmmm.thepitremake.enchants;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;

public class Sweaty extends CustomEnchant {

    @Override
    public void applyEnchant(int level, Object... args) {

    }

    @Override
    public String getName() {
        return "Sweaty";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Sweaty";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("+20%", "+40% XP", "+60% XP")
                .addVariable("", "+50 max XP", "+100 max XP")
                .setWriteCondition(level == 1)
                .writeVariable(ChatColor.AQUA, 0, level).write(" XP from streak XP bonus")
                .setWriteCondition(level != 1)
                .write("Earn ").writeVariable(ChatColor.AQUA, 0, level).write(" from streak XP").next()
                .write("bonus and ").writeVariable(ChatColor.AQUA, 1, level).write(" per kill")
                .build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.C;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLD_SWORD, Material.LEATHER_LEGGINGS, Material.BOW };
    }
}
