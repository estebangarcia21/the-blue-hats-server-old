package me.stevemmmmm.thepitremake.game;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class MysticWellIcons {
    private static boolean hasInitialized;

    private static final ItemStack enchantmentTableInfoIdle = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoT1 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoT2 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoT3 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoItsRollin = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoMaxTier = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);

    public void getIcon(Icon icon) {
    }
}
