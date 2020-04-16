package me.stevemmmmm.permissions.ranks;

import me.stevemmmmm.permissions.core.Rank;
import me.stevemmmmm.permissions.ranks.RankType;
import org.bukkit.ChatColor;

import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class ModeratorRank extends Rank {

    @Override
    public String getName() {
        return "Mod";
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public HashMap<String, Boolean> getPermissions() {
        return new HashMap<String, Boolean>() {{
            put("minecraft.command.kick", true);
            put("minecraft.command.ban", true);
            put("minecraft.command.ban-ip", true);
            put("minecraft.command.tp", true);
            put("minecraft.command.pardon", true);
            put("minecraft.command.pardon-ip", true);
            put("minecraft.command.gamemode", true);
            put("bhperms.chatcooldownbypass", true);
        }};
    }

    @Override
    public String getPrefix() {
        return ChatColor.DARK_GREEN + "[Mod]";
    }

    @Override
    public ChatColor getNameColor() {
        return ChatColor.DARK_GREEN;
    }

    @Override
    public ChatColor getChatColor() {
        return ChatColor.WHITE;
    }

    @Override
    public RankType getRankType() {
        return RankType.STAFF;
    }
}
