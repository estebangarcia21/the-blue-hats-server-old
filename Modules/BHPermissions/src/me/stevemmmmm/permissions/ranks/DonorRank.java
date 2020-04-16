package me.stevemmmmm.permissions.ranks;

import me.stevemmmmm.permissions.core.Rank;
import org.bukkit.ChatColor;

import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class DonorRank extends Rank {

    @Override
    public String getName() {
        return "Donor";
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public HashMap<String, Boolean> getPermissions() {
        return new HashMap<String, Boolean>() {{
            put("bhperms.chatcooldownbypass", true);
        }};
    }

    @Override
    public String getPrefix() {
        return ChatColor.LIGHT_PURPLE + "[" + ChatColor.WHITE + "Donor" + ChatColor.LIGHT_PURPLE + "]";
    }

    @Override
    public ChatColor getNameColor() {
        return ChatColor.AQUA;
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.WHITE;
    }

    @Override
    public RankType getRankType() {
        return RankType.PLAYER;
    }
}
