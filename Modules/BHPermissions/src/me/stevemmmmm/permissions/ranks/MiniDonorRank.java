package me.stevemmmmm.permissions.ranks;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import me.stevemmmmm.permissions.core.Rank;
import org.bukkit.ChatColor;

import java.util.HashMap;

//$1 Cost
public class MiniDonorRank extends Rank {
    @Override
    public String getName() {
        return "MiniDonor";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public HashMap<String, Boolean> getPermissions() {
        return new HashMap<String, Boolean>() {{
            put("bhperms.chatcooldownbypass", true);

            //TODO Extra perms
        }};
    }

    @Override
    public String getPrefix() {
        return ChatColor.AQUA + "[" + getNameColor() + getName() + ChatColor.AQUA + "]";
    }

    @Override
    public ChatColor getNameColor() {
        return ChatColor.WHITE;
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
