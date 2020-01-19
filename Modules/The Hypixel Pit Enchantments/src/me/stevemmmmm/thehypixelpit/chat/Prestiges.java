package me.stevemmmmm.thehypixelpit.chat;

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.GrindingSystem;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
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

        ((CraftPlayer) player).getHandle().listName = CraftChatMessage.fromString(ChatColor.GRAY + "[" + ChatColor.GRAY + GrindingSystem.getInstance().getPlayerPrestige(event.getPlayer()) + ChatColor.GRAY + "]" + ChatColor.GOLD + " [MVP" + ChatColor.WHITE + "++" + ChatColor.GOLD + "] " + player.getName())[0];
        ((CraftPlayer) player).getHandle().displayName = "E";
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) player).getHandle()));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        //TODO Correct values for chat

        event.setFormat(ChatColor.GRAY + "[" + ChatColor.GRAY + GrindingSystem.getInstance().getPlayerPrestige(event.getPlayer()) + ChatColor.GRAY + "]" + ChatColor.GOLD + " [MVP" + ChatColor.WHITE + "++" + ChatColor.GOLD + "] %s" + ChatColor.WHITE + ": %s");
    }
}
