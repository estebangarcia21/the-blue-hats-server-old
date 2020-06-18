package me.stevemmmmm.thepitremake.managers;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class ServerMOTDInitializer implements Listener {

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        event.setMotd(ChatColor.AQUA.toString() + ChatColor.BOLD + "The Blue Hats" + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + " ▶ " + ChatColor.YELLOW + "Pit Recreation\n" + ChatColor.GOLD + "Welcome back!");
    }
}
