package me.stevemmmmm.thepitremake.game;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MysticWellInventory {
    private static final String INVENTORY_NAME = ChatColor.GRAY + "Mystic Well";

    private Player player;
    private Inventory gui;
    private MysticWellState state;

    private MysticWellInventory() { }

    public static MysticWellInventory newInventory(Player player) {
        Inventory gui = Bukkit.createInventory(null, 45, INVENTORY_NAME);

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
        mysticWellInventory.player = player;

        return mysticWellInventory;
    }

    public static boolean pressedEnchantButton(InventoryClickEvent event) {
        return event.getRawSlot() == 24 && event.getClickedInventory().getName().equals(INVENTORY_NAME);
    }

    public boolean canEnchant() {
        //TODO Implement

        return false;
    }

    public Inventory getRawInventory() {
        return gui;
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
