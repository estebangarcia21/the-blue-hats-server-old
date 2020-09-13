package me.stevemmmmm.permissions.ranks;

import me.stevemmmmm.permissions.core.Rank;
import org.bukkit.ChatColor;

import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

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
