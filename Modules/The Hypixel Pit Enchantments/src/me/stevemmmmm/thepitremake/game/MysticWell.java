package me.stevemmmmm.thepitremake.game;

import me.stevemmmmm.animationapi.core.Sequence;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class MysticWell implements Listener {
    private final HashMap<UUID, Inventory> playerGuis = new HashMap<>();

    private final HashMap<UUID, MysticWellState> mysticWellStates = new HashMap<>();

    private final ItemStack enchantmentTableInfoIdle = new ItemStack(Material.ENCHANTMENT_TABLE);
    private final ItemStack enchantmentTableInfoT1 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private final ItemStack enchantmentTableInfoT2 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private final ItemStack enchantmentTableInfoT3 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private final ItemStack enchantmentTableInfoItsRollin = new ItemStack(Material.ENCHANTMENT_TABLE);
    private final ItemStack enchantmentTableInfoMaxTier = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);

    private final int[] glassPanes = new int[] { 10, 11, 12, 21, 30, 29, 28, 19 };

    public MysticWell() {
        ItemMeta etMeta = enchantmentTableInfoIdle.getItemMeta();

        etMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        etMeta.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "Find a " + ChatColor.AQUA + "Mystic Bow" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Mystic");
            add(ChatColor.YELLOW + "Sword" + ChatColor.GRAY + " or " + ChatColor.RED + "P" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "n" + ChatColor.GREEN + "t" + ChatColor.BLUE + "s" + ChatColor.GRAY + "from");
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
            add(ChatColor.RED + "Max out upgrade tier!");
        }});

        enchantmentTableInfoMaxTier.setItemMeta(etMax);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!playerGuis.containsKey(event.getPlayer().getUniqueId())) playerGuis.put(event.getPlayer().getUniqueId(), createMysticWell());
        //TODO Read from invetory API
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            event.setCancelled(true);
            event.getPlayer().openInventory(playerGuis.get(event.getPlayer().getUniqueId()));
//            if (!mysticWellStates.containsKey(event.getPlayer().getUniqueId())) enchantItem((Player) event.getPlayer(), MysticWellState.IDLE);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(ChatColor.GRAY + "Mystic Well") && event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
            event.setCancelled(true);

            //Enchantment confirm slot
            if (event.getInventory().getItem(event.getSlot()).getType() == Material.ENCHANTMENT_TABLE) {
                if (event.getCurrentItem().getType() != Material.STAINED_CLAY) enchantItem((Player) event.getWhoClicked(), event.getInventory().getItem(20));
            }

            //Target mystic item slot
            if (event.getRawSlot() == 20) {
                if (event.getCurrentItem().getType() != Material.AIR) {
                    if (event.getCurrentItem().getType() == Material.GOLD_SWORD || event.getCurrentItem().getType() == Material.BOW || event.getCurrentItem().getType() == Material.LEATHER_LEGGINGS) {
                        for (int i = 0; i < event.getWhoClicked().getInventory().getSize(); i++) {
                            if (event.getWhoClicked().getInventory().getItem(i) == null) {
                                playerGuis.get(event.getWhoClicked().getUniqueId()).setItem(24, enchantmentTableInfoIdle);
                                event.getWhoClicked().getInventory().setItem(i, event.getCurrentItem());
                                playerGuis.get(event.getWhoClicked().getUniqueId()).setItem(event.getSlot(), new ItemStack(Material.AIR));
                                break;
                            }
                        }
                    }
                }
            } else if (event.getCurrentItem().getType() != Material.AIR) {
                if (event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    String[] itemTokens = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).split(" ");
                    if (itemTokens[0].equalsIgnoreCase("Fresh") || itemTokens[0].equalsIgnoreCase("Tier")) {
//                        playerGuis.get(event.getWhoClicked().getUniqueId()).setItem(24, getInfoFromTier(getItemTier(event.getCurrentItem())));
                        playerGuis.get(event.getWhoClicked().getUniqueId()).setItem(20, event.getCurrentItem());
                        event.getWhoClicked().getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getName().equals(ChatColor.GRAY + "Mystic Well")) {
            if (mysticWellStates.get(event.getPlayer().getUniqueId()) == MysticWellState.ENCHANTING) {
//                sequenceRepititions.get(event.getPlayer().getUniqueId()).set(6);
            }
        }
    }

    private void enchantItem(Player player, ItemStack item) {
        if (item == null) return;

        final int[] position = { 0 };

        Sequence enchantmentSequence = new Sequence() {{
            addKeyFrame(0, () -> setGlassPanesToColor(player, "Pink"));
            addKeyFrame(2, () -> setGlassPanesToColor(player, "Gray"));
            addKeyFrame(4, () -> setGlassPanesToColor(player, "Pink"));
            addKeyFrame(6, () -> setGlassPanesToColor(player, "Gray"));

            addKeyFrameByDelay(() -> {
                addKeyFrame(8, () -> setPaneToPink(player, position[0]));
                position[0]++;
            }, 8, 2, 50);
        }};
    }

    private void setPaneToPink(Player player, int index) {
        setGlassPanesToColor(player, "Gray");

        playerGuis.get(player.getUniqueId()).setItem(glassPanes[index], new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 6));
    }

    private void setGlassPanesToColor(Player player, String color) {
        if (color.equalsIgnoreCase("Green")) {
            for (int glassPane : glassPanes) {
                playerGuis.get(player.getUniqueId()).setItem(glassPane, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13));
            }
        }

        if (color.equalsIgnoreCase("Gray")) {
            for (int glassPane : glassPanes) {
                playerGuis.get(player.getUniqueId()).setItem(glassPane, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            }
        }

        if (color.equalsIgnoreCase("Pink")) {
            for (int glassPane : glassPanes) {
                playerGuis.get(player.getUniqueId()).setItem(glassPane, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 6));
            }
        }
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

    private ItemStack getInfoFromTier(int tier) {
        switch (tier) {
            case 0:
                return enchantmentTableInfoT1;
            case 1:
                return enchantmentTableInfoT2;
            case 2:
                return enchantmentTableInfoT3;
            case 3:
                return enchantmentTableInfoMaxTier;
        }

        return null;
    }

    enum MysticWellState {
        IDLE, ENCHANTING
    }
}
