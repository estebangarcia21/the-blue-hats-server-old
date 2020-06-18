package me.stevemmmmm.permissions.ranks;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import me.stevemmmmm.permissions.core.Rank;
import org.bukkit.ChatColor;

import java.util.HashMap;

//$1 Cost
public class PoggerRank extends Rank {
    @Override
    public String getName() {
        return "Pogger";
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
        return ChatColor.LIGHT_PURPLE + "[" + getNameColor() + getName() + ChatColor.LIGHT_PURPLE + "]";
    }

    @Override
    public ChatColor getNameColor() {
        return ChatColor.LIGHT_PURPLE;
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
