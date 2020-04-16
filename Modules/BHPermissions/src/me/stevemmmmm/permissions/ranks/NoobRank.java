package me.stevemmmmm.permissions.ranks;

import me.stevemmmmm.permissions.core.Rank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class NoobRank extends Rank {

    @Override
    public String getName() {
        return "Noob";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public HashMap<String, Boolean> getPermissions() {
        return new HashMap<String, Boolean>() {{
            put("minecraft.command.me", false);
            put("bukkit.command.help", false);
            put("bukkit.command.plugins", false);
        }};
    }

    @Override
    public String getPrefix() {
        return ChatColor.GRAY + "[Noob]";
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
        return RankType.PLAYER;
    }
}
