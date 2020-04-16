package me.stevemmmmm.thepitremake.commands;

import me.stevemmmmm.thepitremake.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class PitAboutCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("pitabout")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "The Hypixel Pit Remake " + Main.version);
                    player.sendMessage(ChatColor.YELLOW + "by " + ChatColor.RED + "Stevemmmmm");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(" ");
                    player.sendMessage(ChatColor.DARK_PURPLE.toString() + ChatColor.UNDERLINE + "Discord" + ChatColor.YELLOW + " ▶ " + ChatColor.BLUE + "Stevemmmmm#9796");
                }
            }
        }

        return true;
    }
}
