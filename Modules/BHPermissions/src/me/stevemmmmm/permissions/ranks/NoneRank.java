package me.stevemmmmm.permissions.ranks;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.permissions.core.Rank;
import org.bukkit.ChatColor;

import java.util.HashMap;

public class NoneRank extends Rank {

    @Override
    public String getName() {
        return "None";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public HashMap<String, Boolean> getPermissions() {
        return new HashMap<>();
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public ChatColor getNameColor() {
        return ChatColor.GRAY;
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.GRAY;
    }

    @Override
    public RankType getRankType() {
        return RankType.STAFF;
    }
}
