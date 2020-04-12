package me.stevemmmmm.permissions.ranks;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.permissions.core.Rank;
import org.bukkit.ChatColor;

import java.util.HashMap;

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
