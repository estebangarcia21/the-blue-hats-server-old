package me.stevemmmmm.thepitremake.game;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import me.stevemmmmm.animationapi.core.Sequence;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class NewMysticWell implements Listener {
    private final HashMap<UUID, Inventory> playerGuis = new HashMap<>();

    private final HashMap<UUID, MysticWell.MysticWellAnimation> mysticWellStates = new HashMap<>();
    private final HashMap<UUID, Sequence> mysticWellSequences = new HashMap<>();

    private static final ItemStack enchantmentTableInfoIdle = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoT1 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoT2 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoT3 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoItsRollin = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoMaxTier = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);

    public NewMysticWell() {
        init();
    }

    public void init() {
        ItemMeta etMeta = enchantmentTableInfoIdle.getItemMeta();

        etMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        etMeta.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "Find a " + ChatColor.AQUA + "Mystic Bow" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Mystic");
            add(ChatColor.YELLOW + "Sword" + ChatColor.GRAY + " or " + ChatColor.RED + "P" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "n" + ChatColor.GREEN + "t" + ChatColor.BLUE + "s" + ChatColor.GRAY + " from");
            add(ChatColor.GRAY + "killing players");
            add(" ");
            add(ChatColor.GRAY + "Enchant these items in the well");
            add(ChatColor.GRAY + "for tons of buffs.");
            add(" ");
            add(ChatColor.LIGHT_PURPLE + "Click an item in your inventory!");
        }});

        enchantmentTableInfoIdle.setItemMeta(etMeta);

        ItemMeta et1Meta = enchantmentTableInfoT1.getItemMeta();

        et1Meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        et1Meta.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "Upgrade:" + ChatColor.GREEN + " Tier I");
            add(ChatColor.GRAY + "Cost:" + ChatColor.GOLD + " 1,000g");
            add(" ");
            add(ChatColor.YELLOW + "Click to upgrade!");
        }});

        enchantmentTableInfoT1.setItemMeta(et1Meta);

        ItemMeta et2Meta = enchantmentTableInfoT1.getItemMeta();

        et2Meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        et2Meta.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "Upgrade:" + ChatColor.YELLOW + " Tier II");
            add(ChatColor.GRAY + "Cost:" + ChatColor.GOLD + " 4,000g");
            add(" ");
            add(ChatColor.YELLOW + "Click to upgrade!");
        }});

        enchantmentTableInfoT2.setItemMeta(et2Meta);

        ItemMeta et3Meta = enchantmentTableInfoT1.getItemMeta();

        et3Meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        et3Meta.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "Upgrade:" + ChatColor.RED + " Tier III");
            add(ChatColor.GRAY + "Cost:" + ChatColor.GOLD + " 8,000g");
            add(" ");
            add(ChatColor.YELLOW + "Click to upgrade!");
        }});

        enchantmentTableInfoT3.setItemMeta(et3Meta);

        ItemMeta enirMeta = enchantmentTableInfoItsRollin.getItemMeta();
        enirMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Its' rollin!");
        enchantmentTableInfoItsRollin.setItemMeta(enirMeta);

        ItemMeta etMax = enchantmentTableInfoMaxTier.getItemMeta();

        etMax.setDisplayName(ChatColor.RED + "Mystic Well");
        etMax.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "This item cannot be");
            add(ChatColor.GRAY + "upgraded any further");
            add(" ");
            add(ChatColor.RED + "Maxed out upgrade tier!");
        }});

        enchantmentTableInfoMaxTier.setItemMeta(etMax);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!playerGuis.containsKey(event.getPlayer().getUniqueId())) playerGuis.put(event.getPlayer().getUniqueId(), createMysticWell());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().getName().equalsIgnoreCase(ChatColor.GRAY + "Mystic Well") || event.getSlotType() != InventoryType.SlotType.OUTSIDE) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();

        //Enchant button
        if (event.getRawSlot() == 24) {

        }

        updateGui(event);
    }

    public ItemStack getTargetItem(Player player) {
        return null;
    }

    private void updateGui(InventoryClickEvent event) {

    }

    private Inventory createMysticWell() {
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

        gui.setItem(24, enchantmentTableInfoIdle);

        gui.setItem(28, grayGlassPane);
        gui.setItem(29, grayGlassPane);
        gui.setItem(30, grayGlassPane);

        return gui;
    }
}
