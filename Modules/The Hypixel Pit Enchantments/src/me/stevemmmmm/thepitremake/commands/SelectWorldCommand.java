package me.stevemmmmm.thepitremake.commands;

import me.stevemmmmm.thepitremake.game.WorldSelection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class SelectWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("selectworld")) {
                WorldSelection.getInstance().displaySelectionMenu(player);
            }
        }

        return true;
    }
}
