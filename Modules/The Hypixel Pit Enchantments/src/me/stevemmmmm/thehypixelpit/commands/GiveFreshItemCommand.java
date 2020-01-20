package me.stevemmmmm.thehypixelpit.commands;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.core.Main;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class GiveFreshItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("givefreshitem")) {
                //TODO Removed dyed lore

                if (args.length == 1) {
                    String object = args[0];

                    ItemStack freshPants = new ItemStack(Material.LEATHER_LEGGINGS, 1);
                    LeatherArmorMeta freshPantsMeta = (LeatherArmorMeta) freshPants.getItemMeta();

                    if (object.equalsIgnoreCase("Red")) {
                        freshPantsMeta.setColor(Color.RED);

                        addPantsLore(freshPantsMeta, "Red", ChatColor.RED);
                    }

                    if (object.equalsIgnoreCase("Green")) {
                        freshPantsMeta.setColor(Color.GREEN);

                        addPantsLore(freshPantsMeta, "Green", ChatColor.GREEN);
                    }

                    if (object.equalsIgnoreCase("Blue")) {
                        freshPantsMeta.setColor(Color.BLUE);

                        addPantsLore(freshPantsMeta, "Blue", ChatColor.BLUE);
                    }

                    if (object.equalsIgnoreCase("Yellow")) {
                        freshPantsMeta.setColor(Color.YELLOW);

                        addPantsLore(freshPantsMeta, "Yellow", ChatColor.YELLOW);
                    }

                    if (object.equalsIgnoreCase("Orange")) {
                        freshPantsMeta.setColor(Color.ORANGE);

                        addPantsLore(freshPantsMeta, "Orange", ChatColor.GOLD);
                    }

                    freshPants.setItemMeta(freshPantsMeta);

                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                        if (player.getInventory().getItem(i) == null) {
                            player.getInventory().setItem(i, freshPants);
                            return true;
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.DARK_PURPLE + "Usage:" + ChatColor.RED + " /givefreshitem <type>");
                }
            }
        }

        return true;
    }

    private void addPantsLore(LeatherArmorMeta itemMeta, String name, ChatColor color) {
        itemMeta.setDisplayName(color + "Fresh " + name + " Pants");
        itemMeta.spigot().setUnbreakable(true);

        itemMeta.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "Kept on death");
            add(" ");
            add(color + "Used in the mystic well");
            add(color + "Also, a fashion statement");
        }});
    }
}
