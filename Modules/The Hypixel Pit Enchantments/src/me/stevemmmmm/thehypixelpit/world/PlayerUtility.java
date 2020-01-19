package me.stevemmmmm.thehypixelpit.world;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerUtility implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage("");
    }
}
