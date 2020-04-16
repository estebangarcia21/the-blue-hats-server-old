package me.stevemmmmm.thehypixelpit.utils;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class DevelopmentMode implements Listener {
    private final boolean isActive = false;

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getPlayer().getName().equalsIgnoreCase("Stevemmmmmmm")) return;

        if (isActive) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You cannot connect to the server!\n\n" +
                    ChatColor.GRAY + "The server is currently being worked on by the developer, " + ChatColor.YELLOW + "Stevemmmmm" + ChatColor.GRAY + ". Many restarts to update changes make the " + ChatColor.RED + "server currently unplayable" + ChatColor.GRAY + ".\n" +
                    ChatColor.GRAY + "If you would like to play on the server, ask the developer by contacting him on discord. \n\n" +
                    ChatColor.RED + "Developer's Discord" + ChatColor.YELLOW + " ▶ " + ChatColor.WHITE + "Stevemmmmm#0001");
        }
    }
}
