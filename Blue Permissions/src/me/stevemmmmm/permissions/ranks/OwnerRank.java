package me.stevemmmmm.permissions.ranks;

import me.stevemmmmm.permissions.core.Rank;
import org.bukkit.ChatColor;

import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class OwnerRank extends Rank {

    @Override
    public String getName() {
        return "Owner";
    }

    @Override
    public int getOrder() {
        return 999;
    }

    @Override
    public HashMap<String, Boolean> getPermissions() {
        return new HashMap<>();
    }

    @Override
    public String getPrefix() {
        return ChatColor.RED + "[Owner]";
    }

    @Override
    public ChatColor getNameColor() {
        return ChatColor.RED;
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.YELLOW;
    }

    @Override
    public RankType getRankType() {
        return RankType.STAFF;
    }
}
