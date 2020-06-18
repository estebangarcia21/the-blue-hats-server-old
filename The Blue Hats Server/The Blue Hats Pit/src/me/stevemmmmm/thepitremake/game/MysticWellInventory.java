package me.stevemmmmm.thepitremake.game;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MysticWellInventory {
    private Inventory gui;
    private MysticWellState state;

    private MysticWellInventory() { }

    public static MysticWellInventory getDefaultInventory() {
        Inventory gui = Bukkit.createInventory(null, 45, ChatColor.GRAY + "Mystic Well");

        ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta gpMeta = grayGlassPane.getItemMeta();

        gpMeta.setDisplayName(ChatColor.GRAY + "Click an item in your inventory!");

        grayGlassPane.setItemMeta(gpMeta);

        gui.setItem(10, grayGlassPane);
        gui.setItem(11, grayGlassPane);
        gui.setItem(12, grayGlassPane);

        gui.setItem(19, grayGlassPane);
        gui.setItem(21, grayGlassPane);

//        gui.setItem(24, enchantmentTableInfoIdle);

        gui.setItem(28, grayGlassPane);
        gui.setItem(29, grayGlassPane);
        gui.setItem(30, grayGlassPane);

        MysticWellInventory mysticWellInventory = new MysticWellInventory();
        mysticWellInventory.gui = gui;

        return mysticWellInventory;
    }

    public ItemStack getItemInEnchantmentSlot() {
        return gui.getItem(20);
    }

    public MysticWellState getState() {
        return state;
    }

    enum MysticWellState {
        IDLE, ENCHANTING
    }
}
