package me.stevemmmmm.thehypixelpit.commands;

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.enchants.LoreBuilder;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class GiveFreshItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("givefreshitem")) {
                if (args.length == 1) {
                    String object = args[0];

                    ItemStack item;
                    LeatherArmorMeta freshPantsMeta = null;

                    if (object.equalsIgnoreCase("Sword")) {
                        item = new ItemStack(Material.GOLD_SWORD, 1);

                        ItemMeta meta = item.getItemMeta();

                        meta.setDisplayName(ChatColor.YELLOW + "Mystic Sword");

//                        meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);

                        meta.setLore(new LoreBuilder().write("Kept on death").nextLine().nextLine().write("Used in the mystic well").build());

                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
                        meta.spigot().setUnbreakable(true);

                        item.setItemMeta(meta);
                    } else if (object.equalsIgnoreCase("Bow")) {
                        item = new ItemStack(Material.BOW, 1);

                        ItemMeta meta = item.getItemMeta();

                        meta.setDisplayName(ChatColor.AQUA + "Mystic Bow");

//                        meta.addEnchant(Enchantment.DURABILITY, 1, true);

                        meta.setLore(new LoreBuilder().write("Kept on death").nextLine().nextLine().write("Used in the mystic well").build());

                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
                        meta.spigot().setUnbreakable(true);

                        item.setItemMeta(meta);
                    } else {
                        item = new ItemStack(Material.LEATHER_LEGGINGS, 1);
                        freshPantsMeta = (LeatherArmorMeta) item.getItemMeta();
                    }

                    if (freshPantsMeta != null) {
                        if (object.equalsIgnoreCase("Red")) {
                            freshPantsMeta.setColor(Color.fromRGB(0xFF5555));

                            addPantsLore(freshPantsMeta, "Red", ChatColor.RED);
                        }

                        if (object.equalsIgnoreCase("Green")) {
                            freshPantsMeta.setColor(Color.fromRGB(0x55FF55));

                            addPantsLore(freshPantsMeta, "Green", ChatColor.GREEN);
                        }

                        if (object.equalsIgnoreCase("Blue")) {
                            freshPantsMeta.setColor(Color.fromRGB(0x5555FF));

                            addPantsLore(freshPantsMeta, "Blue", ChatColor.BLUE);
                        }

                        if (object.equalsIgnoreCase("Yellow")) {
                            freshPantsMeta.setColor(Color.fromRGB(0xFFFF55));

                            addPantsLore(freshPantsMeta, "Yellow", ChatColor.YELLOW);
                        }

                        if (object.equalsIgnoreCase("Orange")) {
                            freshPantsMeta.setColor(Color.fromRGB(0xFFAA00));

                            addPantsLore(freshPantsMeta, "Orange", ChatColor.GOLD);
                        }

                        if (object.equalsIgnoreCase("Dark")) {
                            freshPantsMeta.setColor(Color.fromRGB(0x000000));

                            addPantsLore(freshPantsMeta, "Dark", ChatColor.DARK_PURPLE);
                        }

                        if (object.equalsIgnoreCase("Sewer")) {
                            freshPantsMeta.setColor(Color.fromRGB(0x7DC383));

                            addPantsLore(freshPantsMeta, "Sewer", ChatColor.DARK_AQUA);
                        }

                        if (object.equalsIgnoreCase("Aqua")) {
                            freshPantsMeta.setColor(Color.fromRGB(0x55FFFF ));

                            addPantsLore(freshPantsMeta,"Aqua", ChatColor.AQUA);
                        }

                        if (freshPantsMeta.getLore() == null) {
                            sender.sendMessage(ChatColor.RED + "Bruh you typed something in wrong...");
                            return true;
                        }

                        item.setItemMeta(freshPantsMeta);
                    }

                    player.getInventory().addItem(item);
                    player.updateInventory();
                } else {
                    player.sendMessage(ChatColor.DARK_PURPLE + "Usage:" + ChatColor.RED + " /givefreshitem <type>");
                }
            }
        }

        return true;
    }

    private void addPantsLore(LeatherArmorMeta newMeta, String name, ChatColor color) {
        newMeta.setDisplayName(color + "Fresh " + name + " Pants");

        newMeta.spigot().setUnbreakable(true);

        newMeta.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "Kept on death");
            add(" ");
            add(color + "Used in the mystic well");
            add(color + "Also, a fashion statement");
        }});
    }
}
