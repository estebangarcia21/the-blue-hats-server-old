package me.stevemmmmm.thepitremake.game.duels;

import me.stevemmmmm.configapi.sqlmanagement.databases.PlayerRanksDatabase;
import me.stevemmmmm.configapi.sqlmanagement.managers.SQLManager;
import me.stevemmmmm.permissions.core.Rank;
import me.stevemmmmm.permissions.ranks.RankManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class GameUtility implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setMaxHealth(24);
        event.getPlayer().setFoodLevel(19);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
