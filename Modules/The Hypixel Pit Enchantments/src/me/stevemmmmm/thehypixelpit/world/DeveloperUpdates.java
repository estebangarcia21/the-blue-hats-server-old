package me.stevemmmmm.thehypixelpit.world;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@SuppressWarnings("all")
public class DeveloperUpdates implements Listener {
    private final String update = "Added /spawn!";
    private final String testMessage = "Working on Push Comes to Shove!";
    private final boolean isTesting = false;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        displayUpdate(event.getPlayer());
    }

    private void displayUpdate(Player player) {
        if (isTesting) {
            IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.RED + "⚠ " + ChatColor.YELLOW + "Testing! Expect restarts..." + ChatColor.RED + " ⚠" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

            PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
            PacketPlayOutTitle length = new PacketPlayOutTitle(20, 5 * 20, 20);

            IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.WHITE + testMessage + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

            PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
            PacketPlayOutTitle subTitleLength = new PacketPlayOutTitle(20, 5 * 20, 20);


            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitle);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitleLength);
        } else {
            IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.RED + "What's new?" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

            PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
            PacketPlayOutTitle length = new PacketPlayOutTitle(20, 5 * 20, 20);

            IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.GREEN + update + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

            PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
            PacketPlayOutTitle subTitleLength = new PacketPlayOutTitle(20, 5 * 20, 20);


            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitle);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitleLength);
        }
    }
}
