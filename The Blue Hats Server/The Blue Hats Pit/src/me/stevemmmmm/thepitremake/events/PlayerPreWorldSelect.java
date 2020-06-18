package me.stevemmmmm.thepitremake.events;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPreWorldSelect extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    public PlayerPreWorldSelect(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
