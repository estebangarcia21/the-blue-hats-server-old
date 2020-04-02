package me.stevemmmmm.thehypixelpit.game;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.core.Main;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class MysticWell implements Listener {
    private HashMap<UUID, Inventory> playerGui = new HashMap<>();

    private HashMap<UUID, MysticWellState> activeAnimations = new HashMap<>();
    private HashMap<UUID, Integer> animationTasks = new HashMap<>();

    private ItemStack enchantmentTableInfoIdle = new ItemStack(Material.ENCHANTMENT_TABLE);
    private ItemStack enchantmentTableInfoT1 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private ItemStack enchantmentTableInfoT2 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private ItemStack enchantmentTableInfoT3 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private ItemStack enchantmentTableInfoItsRollin = new ItemStack(Material.ENCHANTMENT_TABLE);
    private ItemStack enchantmentTableInfoMaxTier = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);

    //TODO Rewrite the mystic well

    private HashMap<UUID, AtomicInteger> animationSequenceIndexs = new HashMap<>();
    private HashMap<UUID, AtomicInteger> sequenceRepititions = new HashMap<>();

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
        if (!playerGui.containsKey(event.getPlayer().getUniqueId())) playerGui.put(event.getPlayer().getUniqueId(), createMysticWell());
        if (!animationSequenceIndexs.containsKey(event.getPlayer().getUniqueId())) animationSequenceIndexs.put(event.getPlayer().getUniqueId(), new AtomicInteger());
        if (!sequenceRepititions.containsKey(event.getPlayer().getUniqueId())) sequenceRepititions.put(event.getPlayer().getUniqueId(), new AtomicInteger());
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            event.setCancelled(true);
            event.getPlayer().openInventory(playerGui.get(event.getPlayer().getUniqueId()));
            if (!activeAnimations.containsKey(event.getPlayer().getUniqueId())) setMysticWellState((Player) event.getPlayer(), MysticWellState.IDLE);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(ChatColor.GRAY + "Mystic Well") && event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
            event.setCancelled(true);

            //Enchantment confirm slot
            if (event.getRawSlot() == 24) {
                if (event.getCurrentItem().getType() != Material.STAINED_CLAY) setMysticWellState((Player) event.getWhoClicked(), MysticWellState.ENCHANTING);
            }

            //Target mystic item slot
            if (event.getRawSlot() == 20) {
                if (event.getCurrentItem().getType() != Material.AIR) {
                    if (event.getCurrentItem().getType() == Material.GOLD_SWORD || event.getCurrentItem().getType() == Material.BOW || event.getCurrentItem().getType() == Material.LEATHER_LEGGINGS) {
                        for (int i = 0; i < event.getWhoClicked().getInventory().getSize(); i++) {
                            if (event.getWhoClicked().getInventory().getItem(i) == null) {
                                playerGui.get(event.getWhoClicked().getUniqueId()).setItem(24, enchantmentTableInfoIdle);
                                event.getWhoClicked().getInventory().setItem(i, event.getCurrentItem());
                                playerGui.get(event.getWhoClicked().getUniqueId()).setItem(event.getSlot(), new ItemStack(Material.AIR));
                                break;
                            }
                        }
                    }
                }
            } else if (event.getCurrentItem().getType() != Material.AIR) {
                if (event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    String[] itemTokens = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).split(" ");
                    if (itemTokens[0].equalsIgnoreCase("Fresh") || itemTokens[0].equalsIgnoreCase("Tier")) {
                        playerGui.get(event.getWhoClicked().getUniqueId()).setItem(24, getInfoFromTier(getItemTier(event.getCurrentItem())));
                        playerGui.get(event.getWhoClicked().getUniqueId()).setItem(20, event.getCurrentItem());
                        event.getWhoClicked().getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getName().equals(ChatColor.GRAY + "Mystic Well")) {
            if (activeAnimations.get(event.getPlayer().getUniqueId()) == MysticWellState.ENCHANTING) {
                sequenceRepititions.get(event.getPlayer().getUniqueId()).set(6);
            }
        }
    }

    private void setMysticWellState(Player player, MysticWellState animation) {
        if (activeAnimations.containsKey(player.getUniqueId())) {
            if (activeAnimations.get(player.getUniqueId()) != animation) {
                Bukkit.getServer().getScheduler().cancelTask(animationTasks.get(player.getUniqueId()));
                activeAnimations.put(player.getUniqueId(), animation);
                animationTasks.remove(player.getUniqueId());
            } else return;
        } else {
            activeAnimations.put(player.getUniqueId(), animation);
        }

        ArrayList<Integer> rotaterIndexs = new ArrayList<Integer>() {{
            add(10);
            add(11);
            add(12);
            add(21);
            add(30);
            add(29);
            add(28);
            add(19);
        }};

        Inventory gui = playerGui.get(player.getUniqueId());

        if (animation == MysticWellState.IDLE) {
            animationTasks.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
                ItemMeta gpMeta = grayGlassPane.getItemMeta();
                gpMeta.setDisplayName(ChatColor.GRAY + "Click an item in your inventory!");
                grayGlassPane.setItemMeta(gpMeta);

                ItemStack pinkPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 6);
                ItemMeta pinkPaneMeta = pinkPane.getItemMeta();
                pinkPaneMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Click an item in your inventory!");
                pinkPane.setItemMeta(pinkPaneMeta);

                for (int i = 0; i < gui.getSize(); i++) {
                    if (gui.getItem(i) == null) continue;

                    if (gui.getItem(i).getType() == Material.STAINED_GLASS_PANE) {
                        gui.setItem(i, grayGlassPane);
                    }
                }

                gui.setItem(rotaterIndexs.get(animationSequenceIndexs.get(player.getUniqueId()).get()), pinkPane);
                animationSequenceIndexs.get(player.getUniqueId()).getAndIncrement();

                if (animationSequenceIndexs.get(player.getUniqueId()).get() > 7) {
                    animationSequenceIndexs.get(player.getUniqueId()).set(0);
                }
            }, 0L, 3L));

            return;
        }

        if (animation == MysticWellState.ENCHANTING) {
            ItemStack[] animationItems = new ItemStack[] { new ItemStack(Material.INK_SACK, 1, (byte) 0), new ItemStack(Material.INK_SACK, 1, (byte) 1), new ItemStack(Material.INK_SACK, 1, (byte) 2), new ItemStack(Material.INK_SACK, 1, (byte) 3), new ItemStack(Material.INK_SACK, 1, (byte) 4), new ItemStack(Material.INK_SACK, 1, (byte) 5), new ItemStack(Material.INK_SACK, 1, (byte) 6), new ItemStack(Material.INK_SACK, 1, (byte) 7), new ItemStack(Material.INK_SACK, 1, (byte) 8), new ItemStack(Material.INK_SACK, 1, (byte) 9), new ItemStack(Material.INK_SACK, 1, (byte) 10), new ItemStack(Material.INK_SACK, 1, (byte) 11), new ItemStack(Material.INK_SACK, 1, (byte) 12), new ItemStack(Material.INK_SACK, 1, (byte) 13), new ItemStack(Material.INK_SACK, 1, (byte) 14) };
            ItemStack originalItem = playerGui.get(player.getUniqueId()).getItem(20);

            gui.setItem(24, enchantmentTableInfoItsRollin);

            animationTasks.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                ItemStack greenGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13);
                ItemMeta greenGlassPaneItemMeta = greenGlassPane.getItemMeta();

                greenGlassPaneItemMeta.setDisplayName(" ");

                greenGlassPane.setItemMeta(greenGlassPaneItemMeta);

                ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
                ItemMeta gpMeta = grayGlassPane.getItemMeta();

                gpMeta.setDisplayName(" ");

                grayGlassPane.setItemMeta(gpMeta);

                for (int i = 0; i < gui.getSize(); i++) {
                    if (gui.getItem(i) == null) continue;

                    if (gui.getItem(i).getType() == Material.STAINED_GLASS_PANE) {
                        gui.setItem(i, grayGlassPane);
                    }
                }

                ItemStack item = animationItems[ThreadLocalRandom.current().nextInt(animationItems.length)];
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(ChatColor.YELLOW + "Its' rollin!");
                item.setItemMeta(itemMeta);

                gui.setItem(20, item);
                gui.setItem(rotaterIndexs.get(animationSequenceIndexs.get(player.getUniqueId()).get()), greenGlassPane);

                animationSequenceIndexs.get(player.getUniqueId()).getAndIncrement();

                if (animationSequenceIndexs.get(player.getUniqueId()).get() > 7) {
                    sequenceRepititions.get(player.getUniqueId()).getAndIncrement();
                    animationSequenceIndexs.get(player.getUniqueId()).set(0);
                }

                if (sequenceRepititions.get(player.getUniqueId()).get() > 5) {
                    gui.setItem(20, tierItem(originalItem));
                    gui.setItem(24, getInfoFromTier(getItemTier(originalItem)));
                    sequenceRepititions.get(player.getUniqueId()).set(0);
                    setMysticWellState(player, MysticWellState.IDLE);
                }
            }, 0L, 2L));
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

    private ItemStack tierItem(ItemStack item) {
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();

        if (item.getType() == Material.GOLD_SWORD) {
            if (ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Fresh Mystic Sword")) {
                meta.setDisplayName(ChatColor.GREEN + "Tier I Sword");
            } else if (meta.getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Tier I Sword")) {
                meta.setDisplayName(ChatColor.YELLOW + "Tier II Sword");
            } else if (meta.getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Tier II Sword")) {
                meta.setDisplayName(ChatColor.RED + "Tier III Sword");
            }
        } else if (item.getType() == Material.LEATHER_LEGGINGS) {
            if (ChatColor.stripColor(meta.getDisplayName()).split(" ")[0].equalsIgnoreCase("Fresh")) {
                String color = meta.getDisplayName().split(" ")[1].toUpperCase();
                if (color.equalsIgnoreCase("Orange")) color = "GOLD";

                meta.setDisplayName(ChatColor.valueOf(color) + "Tier I " + meta.getDisplayName().split(" ")[1] + " Pants");
            } else {
                ArrayList<String> nameTokens = new ArrayList<>(Arrays.asList(ChatColor.stripColor(meta.getDisplayName()).split(" ")));

                if (nameTokens.contains("I")) {
                    String color = meta.getDisplayName().split(" ")[2].toUpperCase();
                    if (color.equalsIgnoreCase("Orange")) color = "GOLD";

                    meta.setDisplayName(ChatColor.valueOf(color) + "Tier II " + meta.getDisplayName().split(" ")[2] + " Pants");
                } else if (nameTokens.contains("II")) {
                    String color = meta.getDisplayName().split(" ")[2].toUpperCase();
                    if (color.equalsIgnoreCase("Orange")) color = "GOLD";

                    meta.setDisplayName(ChatColor.valueOf(color) + "Tier III " + meta.getDisplayName().split(" ")[2] + " Pants");
                }
            }
        }

        item.setItemMeta(meta);

        return item;
    }

    public int getItemTier(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(ChatColor.stripColor(meta.getDisplayName()).split(" ")));

        if (tokens.contains("I")) {
            return 1;
        } else if (tokens.contains("II")) {
            return 2;
        } else if (tokens.contains("III")) {
            return 3;
        } else if (tokens.contains("Fresh")) {
            return 0;
        }

        return -1;
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
