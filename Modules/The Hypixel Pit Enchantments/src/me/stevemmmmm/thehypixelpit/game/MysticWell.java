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
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MysticWell implements Listener {
    private HashMap<UUID, Inventory> playerGui = new HashMap<>();

    private HashMap<UUID, Animation> activeAnimations = new HashMap<>();
    private HashMap<UUID, Integer> animationTasks = new HashMap<>();

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

            event.getWhoClicked().sendMessage(String.valueOf(event.getRawSlot()));
        }
    }

    private void setMysticWellAnimation(Player player, Animation animation) {
        if (activeAnimations.containsKey(player.getUniqueId())) {
            if (animation == activeAnimations.get(player.getUniqueId())) return;
        } else {
            activeAnimations.put(player.getUniqueId(), animation);
        }

        AtomicInteger animationSequenceIndex = new AtomicInteger(0);

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

        animationTasks.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
            switch (animation) {
                case IDLE:
                    Inventory gui = playerGui.get(player.getUniqueId());

                    //TODO Make panes global variables

                    ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
                    ItemMeta gpMeta = grayGlassPane.getItemMeta();

                    gpMeta.setDisplayName(ChatColor.GRAY + "Click an item in your inventory!");

                    grayGlassPane.setItemMeta(gpMeta);

                    ItemStack pinkPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 6);
                    ItemMeta pinkPaneMeta = pinkPane.getItemMeta();

                    for (int i = 0; i < gui.getSize(); i++) {
                        if (gui.getItem(i) == null) continue;

                        if (gui.getItem(i).getType() == Material.STAINED_GLASS_PANE) {
                            gui.setItem(i, grayGlassPane);
                        }
                    }

                    pinkPaneMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Click an item in your inventory!");

                    pinkPane.setItemMeta(pinkPaneMeta);

                    gui.setItem(idleIndexs.get(animationSequenceIndex.get()), pinkPane);
                    animationSequenceIndex.getAndIncrement();

                    if (animationSequenceIndex.get() > 7) {
                        animationSequenceIndex.set(0);
                    }
                    break;
                case ENCHANTING:

                    break;
            }
        }, 0L, 3L));
    }

    private Inventory createMysticWell() {
        Inventory gui = Bukkit.createInventory(null, 45, ChatColor.GRAY + "Mystic Well");

        ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta gpMeta = grayGlassPane.getItemMeta();

        gpMeta.setDisplayName(ChatColor.GRAY + "Click an item in your inventory!");

        grayGlassPane.setItemMeta(gpMeta);

        ItemStack enchantmentTableInfo = new ItemStack(Material.ENCHANTMENT_TABLE);
        ItemMeta etMeta = enchantmentTableInfo.getItemMeta();

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

        enchantmentTableInfo.setItemMeta(etMeta);

        gui.setItem(10, grayGlassPane);
        gui.setItem(11, grayGlassPane);
        gui.setItem(12, grayGlassPane);

        gui.setItem(19, grayGlassPane);
        gui.setItem(21, grayGlassPane);

        gui.setItem(24, enchantmentTableInfo);

        gui.setItem(28, grayGlassPane);
        gui.setItem(29, grayGlassPane);
        gui.setItem(30, grayGlassPane);

        return gui;
    }

    enum Animation {
        IDLE, ENCHANTING
    }
}