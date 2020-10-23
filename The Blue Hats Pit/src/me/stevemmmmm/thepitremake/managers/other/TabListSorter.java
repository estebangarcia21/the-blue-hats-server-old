package me.stevemmmmm.thepitremake.managers.other;

import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class TabListSorter implements Listener {
    private static TabListSorter instance;

    public static TabListSorter getInstance() {
        if (instance == null)
            instance = new TabListSorter();

        return instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ((CraftPlayer) player).getHandle().listName = CraftChatMessage
                .fromString(GrindingSystem.getInstance().getFormattedPlayerLevelWithoutPrestige(player) + ChatColor.GOLD
                        + " " + player.getName())[0];
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) player).getHandle()));

        for (Player p : player.getWorld().getPlayers()) {
            ((CraftPlayer) p).getHandle().listName = CraftChatMessage
                    .fromString(GrindingSystem.getInstance().getFormattedPlayerLevelWithoutPrestige(p) + ChatColor.GOLD
                            + " " + p.getName())[0];
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) p).getHandle()));
        }

    }
}
