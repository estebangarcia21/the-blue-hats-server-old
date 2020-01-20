package me.stevemmmmm.thehypixelpit.chat;

import me.stevemmmmm.thehypixelpit.managers.GrindingSystem;
import me.stevemmmmm.thehypixelpit.managers.PitScoreboardManager;
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

public class Prestiges implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PitScoreboardManager.getInstance().sort(player);

        ((CraftPlayer) player).getHandle().listName = CraftChatMessage.fromString(GrindingSystem.getInstance().getFormattedPlayerLevel(player) + ChatColor.GOLD + " [MVP" + ChatColor.WHITE + "++" + ChatColor.GOLD + "] " + player.getName())[0];
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) player).getHandle()));

        for (Player p : player.getWorld().getPlayers()) {
            ((CraftPlayer) p).getHandle().listName = CraftChatMessage.fromString(GrindingSystem.getInstance().getFormattedPlayerLevel(p) + ChatColor.GOLD + " [MVP" + ChatColor.WHITE + "++" + ChatColor.GOLD + "] " + p.getName())[0];
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) p).getHandle()));
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        //TODO Correct values for chat

        event.setFormat(GrindingSystem.getInstance().getFormattedPlayerLevel(event.getPlayer()) + ChatColor.GOLD + " [MVP" + ChatColor.WHITE + "++" + ChatColor.GOLD + "] %s" + ChatColor.WHITE + ": %s");
    }
}