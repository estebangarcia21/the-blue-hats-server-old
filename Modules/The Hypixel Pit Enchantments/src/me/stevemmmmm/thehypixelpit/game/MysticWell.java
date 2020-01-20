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
import org.bukkit.material.Dye;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class MysticWell implements Listener {
    private HashMap<UUID, Inventory> playerGui = new HashMap<>();

    private HashMap<UUID, Animation> activeAnimations = new HashMap<>();
    private HashMap<UUID, Integer> animationTasks = new HashMap<>();

    private ItemStack enchantmentTableInfoIdle = new ItemStack(Material.ENCHANTMENT_TABLE);
    private ItemStack enchantmentTableInfoT1 = new ItemStack(Material.ENCHANTMENT_TABLE);

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
            add(ChatColor.GRAY + "Cost:" + ChatColor.GOLD + " 1000g");
            add(" ");
            add(ChatColor.YELLOW + "Click to upgrade!");
        }});

        enchantmentTableInfoT1.setItemMeta(et1Meta);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!playerGui.containsKey(event.getPlayer().getUniqueId())) playerGui.put(event.getPlayer().getUniqueId(), createMysticWell());
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            event.setCancelled(true);
            event.getPlayer().openInventory(playerGui.get(event.getPlayer().getUniqueId()));
            setMysticWellAnimation((Player) event.getPlayer(), Animation.IDLE);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            Bukkit.getServer().getScheduler().cancelTask(animationTasks.get(event.getPlayer().getUniqueId()));
            activeAnimations.remove(event.getPlayer().getUniqueId());
            animationTasks.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(ChatColor.GRAY + "Mystic Well")) {
            event.setCancelled(true);

            //Enchantment confirm slot
            if (event.getRawSlot() == 24) {
                setMysticWellAnimation((Player) event.getWhoClicked(), Animation.ENCHANTING);
            }

            //Target mystic item slot
            if (event.getRawSlot() == 20) {
                for (int i = 0; i < event.getWhoClicked().getInventory().getSize(); i++) {
                    if (event.getWhoClicked().getInventory().getItem(i) == null) {
                        event.getWhoClicked().getInventory().setItem(i, event.getCurrentItem());
                        playerGui.get(event.getWhoClicked().getUniqueId()).remove(event.getCurrentItem());
                        break;
                    }
                }
            } else if (event.getCurrentItem() != null) {
                String[] itemTokens = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).split(" ");
                if (itemTokens[0].equalsIgnoreCase("Fresh")) {
                    //TODO Implement sounds

                    playerGui.get(event.getWhoClicked().getUniqueId()).setItem(20, event.getCurrentItem());
                    event.getWhoClicked().getInventory().remove(event.getCurrentItem());
                }
            }

            if (playerGui.get(event.getWhoClicked().getUniqueId()).getItem(20) != null) {
                playerGui.get(event.getWhoClicked().getUniqueId()).setItem(24, enchantmentTableInfoT1);
            } else {
                playerGui.get(event.getWhoClicked().getUniqueId()).setItem(24, enchantmentTableInfoIdle);
            }
        }
    }

    private void setMysticWellAnimation(Player player, Animation animation) {
        if (activeAnimations.containsKey(player.getUniqueId())) {
            if (activeAnimations.get(player.getUniqueId()) != animation) {
                Bukkit.getServer().getScheduler().cancelTask(animationTasks.get(player.getUniqueId()));
                activeAnimations.put(player.getUniqueId(), animation);
                animationTasks.remove(player.getUniqueId());
            } else return;
        } else {
            activeAnimations.put(player.getUniqueId(), animation);
        }

        AtomicInteger animationSequenceIndex = new AtomicInteger(0);
        AtomicInteger sequenceRepititions = new AtomicInteger(0);

        ArrayList<Integer> idleIndexs = new ArrayList<Integer>() {{
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

        if (animation == Animation.IDLE) {
            //TODO If item is in the well -> Item in well! Click to get it back.

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

                gui.setItem(idleIndexs.get(animationSequenceIndex.get()), pinkPane);
                animationSequenceIndex.getAndIncrement();

                if (animationSequenceIndex.get() > 7) {
                    animationSequenceIndex.set(0);
                }
            }, 0L, 3L));

            return;
        }

        if (animation == Animation.ENCHANTING) {
            ItemStack[] animationItems = new ItemStack[] { new ItemStack(Material.INK_SACK, 1, (byte) 0),
                    new ItemStack(Material.INK_SACK, 1, (byte) 1),
                    new ItemStack(Material.INK_SACK, 1, (byte) 2),
                    new ItemStack(Material.INK_SACK, 1, (byte) 3),
                    new ItemStack(Material.INK_SACK, 1, (byte) 4),
                    new ItemStack(Material.INK_SACK, 1, (byte) 5),
                    new ItemStack(Material.INK_SACK, 1, (byte) 6),
                    new ItemStack(Material.INK_SACK, 1, (byte) 7),
                    new ItemStack(Material.INK_SACK, 1, (byte) 8),
                    new ItemStack(Material.INK_SACK, 1, (byte) 9),
                    new ItemStack(Material.INK_SACK, 1, (byte) 10),
                    new ItemStack(Material.INK_SACK, 1, (byte) 11),
                    new ItemStack(Material.INK_SACK, 1, (byte) 12),
                    new ItemStack(Material.INK_SACK, 1, (byte) 13),
                    new ItemStack(Material.INK_SACK, 1, (byte) 14), };

            ItemStack originalItem = playerGui.get(player.getUniqueId()).getItem(20);

            animationTasks.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                ItemStack greenGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13);
                ItemMeta greenGlassPaneItemMeta = greenGlassPane.getItemMeta();

                greenGlassPaneItemMeta.setDisplayName(ChatColor.GREEN + "Its' rollin!");

                greenGlassPane.setItemMeta(greenGlassPaneItemMeta);

                ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
                ItemMeta gpMeta = grayGlassPane.getItemMeta();

                gpMeta.setDisplayName(ChatColor.GRAY + "Its' rollin!");

                grayGlassPane.setItemMeta(gpMeta);

                for (int i = 0; i < gui.getSize(); i++) {
                    if (gui.getItem(i) == null) continue;

                    if (gui.getItem(i).getType() == Material.STAINED_GLASS_PANE) {
                        gui.setItem(i, grayGlassPane);
                    }
                }

                gui.setItem(20, animationItems[ThreadLocalRandom.current().nextInt(animationItems.length)]);

                gui.setItem(idleIndexs.get(animationSequenceIndex.get()), greenGlassPane);
                animationSequenceIndex.getAndIncrement();

                if (animationSequenceIndex.get() > 7) {
                    sequenceRepititions.getAndIncrement();
                    animationSequenceIndex.set(0);
                }

                if (sequenceRepititions.get() > 5) {
                    gui.setItem(20, tierItem(originalItem));
                    setMysticWellAnimation(player, Animation.IDLE);
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
        //TODO Enchantment logic

        return item;
    }

    enum Animation {
        IDLE, ENCHANTING
    }
}
