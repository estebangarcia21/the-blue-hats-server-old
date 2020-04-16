package me.stevemmmmm.thehypixelpit.chat;

import me.stevemmmmm.permissions.core.PermissionsManager;
import me.stevemmmmm.permissions.core.Rank;
import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.other.GrindingSystem;
import me.stevemmmmm.thehypixelpit.managers.other.PitScoreboardManager;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredListener;

import java.util.List;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class LevelChatFormatting implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Rank playerRank = PermissionsManager.getInstance().getPlayerRank(event.getPlayer());

        GrindingSystem.getInstance().setPlayerLevel(player, 120);
        GrindingSystem.getInstance().setPlayerPrestige(player, 35);

        ((CraftPlayer) player).getHandle().listName = CraftChatMessage.fromString(GrindingSystem.getInstance().getFormattedPlayerLevelWithoutPrestige(player) + playerRank.getNameColor() + " " + player.getName())[0];
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) player).getHandle()));
    }
}