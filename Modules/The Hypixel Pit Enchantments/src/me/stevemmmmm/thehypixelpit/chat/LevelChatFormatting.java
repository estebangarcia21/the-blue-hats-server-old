package me.stevemmmmm.thehypixelpit.chat;

import me.stevemmmmm.thehypixelpit.managers.other.GrindingSystem;
import me.stevemmmmm.thehypixelpit.managers.other.PitScoreboardManager;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class LevelChatFormatting implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        GrindingSystem.getInstance().setPlayerLevel(player, 120);
        GrindingSystem.getInstance().setPlayerPrestige(player, 35);

        if (event.getPlayer().getName().equalsIgnoreCase("Stevemmmmmmm") || event.getPlayer().getName().equalsIgnoreCase("SUNDEWS") || event.getPlayer().getName().equalsIgnoreCase("OVEREXERTED")) {
            ((CraftPlayer) player).getHandle().listName = CraftChatMessage.fromString(GrindingSystem.getInstance().getFormattedPlayerLevelWithoutPrestige(player) + ChatColor.RED + " " + player.getName())[0];
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) player).getHandle()));
        } else {
            ((CraftPlayer) player).getHandle().listName = CraftChatMessage.fromString(GrindingSystem.getInstance().getFormattedPlayerLevelWithoutPrestige(player) + ChatColor.GOLD + " " + player.getName())[0];
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) player).getHandle()));
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        //TODO Correct values for chat

        if (event.getPlayer().getName().equalsIgnoreCase("Stevemmmmmmm")) {
            event.setFormat(GrindingSystem.getInstance().getFormattedPlayerLevel(event.getPlayer()) + ChatColor.RED + " [OWNER] " + ChatColor.RED + "%s" + ChatColor.WHITE + ": " + ChatColor.YELLOW + "%s");
        } else if (event.getPlayer().getName().equalsIgnoreCase("SUNDEWS") || event.getPlayer().getName().equalsIgnoreCase("OVEREXERTED")) {
            event.setFormat(GrindingSystem.getInstance().getFormattedPlayerLevel(event.getPlayer()) + ChatColor.RED + " [ADMIN] " + ChatColor.RED + "%s" + ChatColor.WHITE + ": " + ChatColor.WHITE + "%s");
        } else {
            event.setFormat(GrindingSystem.getInstance().getFormattedPlayerLevel(event.getPlayer()) + ChatColor.GOLD + " [MVP" + ChatColor.WHITE + "++" + ChatColor.GOLD + "] %s" + ChatColor.WHITE + ": %s");
        }
    }
}