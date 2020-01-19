package me.stevemmmmm.thehypixelpit.chat;

import me.stevemmmmm.thehypixelpit.managers.GrindingSystem;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Prestiges implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(ChatColor.RED + "[" + GrindingSystem.getInstance().getPlayerPrestige(event.getPlayer()) + "] %s: " + ChatColor.WHITE + "%s");
    }
}
