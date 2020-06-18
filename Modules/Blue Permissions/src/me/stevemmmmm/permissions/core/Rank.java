package me.stevemmmmm.permissions.core;

import me.stevemmmmm.permissions.ranks.RankType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public abstract class Rank {

    public String getFormattedName(Player player) {
        return getPrefix() + " " + getNameColor() + player.getName() + ChatColor.WHITE + ": " + getChatColor();
    }

    public <T extends Rank> boolean equalsRank(Class<T> type) {
        return type == getClass();
    }

    public abstract String getName();

    public abstract int getOrder();

    public abstract HashMap<String, Boolean> getPermissions();

    public abstract String getPrefix();

    public abstract ChatColor getNameColor();

    public abstract ChatColor getChatColor();

    public abstract RankType getRankType();
}
