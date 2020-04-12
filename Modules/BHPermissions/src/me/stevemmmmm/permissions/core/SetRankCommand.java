package me.stevemmmmm.permissions.core;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.permissions.ranks.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("setrank") && player.isOp()) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Usage: /setrank <player> <staffrank>");
                } else {
                    if (args.length != 2) {
                        player.sendMessage(ChatColor.RED + "Error! Invalid arguments!");
                    } else {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        if (target == null) {
                            return true;
                        }

                        String rankName = args[1];

                        if (RankManager.getInstance().getRankByName(rankName) == null) {
                            player.sendMessage(ChatColor.RED + "Error! This rank does not exist!");
                            return true;
                        }

                        Rank rank = RankManager.getInstance().getRankByName(rankName);

                        PermissionsManager.getInstance().setPlayerRank(target, rank);
                        target.sendMessage(ChatColor.GREEN + "Hey! " + ChatColor.RED + "Your rank was just changed to " + rank.getNameColor() + rank.getName() + ChatColor.RED + "! You may need to reconnect to refresh you rank.");

                        player.sendMessage(ChatColor.GREEN + "Rank changed successfully!");
                    }
                }
            }
        }

        return true;
    }
}
