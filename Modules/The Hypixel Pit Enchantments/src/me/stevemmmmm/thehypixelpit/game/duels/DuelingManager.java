package me.stevemmmmm.thehypixelpit.game.duels;

import javafx.util.Pair;
import me.stevemmmmm.animationapi.core.Animation;
import me.stevemmmmm.animationapi.core.AnimationAPI;
import me.stevemmmmm.animationapi.core.AnimationActions;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class DuelingManager implements Listener {
    private static DuelingManager instance;

    private ArrayList<Duel> activeDuels = new ArrayList<>();
    private ArrayList<Pair<Vector, Vector>> duelPositions = new ArrayList<>();

    private DuelingManager() {
        init();
    }

    public static DuelingManager getInstance() {
        if (instance == null) instance = new DuelingManager();

        return instance;
    }

    private void init() {
        //TODO Maps right here
        duelPositions.add(new Pair<>(new Vector(0, 100, 0), new Vector(0, 100, 0)));
    }

    public void startDuel(Duel duel) {
        activeDuels.add(duel);

        initializeDuel(duel);
    }

    public void stopDuel(Player player) {
        if (getPlayerDuel(player) == null) return;

        deInitializeDuel(getPlayerDuel(player));
    }

    public Duel getPlayerDuel(Player player) {
        for (Duel duel : activeDuels) {
            if (duel.getPlayerA() == player) {
                return duel;
            }
        }

        return null;
    }

    private void initializeDuel(Duel duel) {
        if (duel.getPlayerAPos() == null || duel.getPlayerBPos() == null) {
            Pair<Vector, Vector> positions = duelPositions.get(ThreadLocalRandom.current().nextInt(duelPositions.size()));

            duel.setPlayerAPos(positions.getKey());
            duel.setPlayerBPos(positions.getValue());
        }

        duel.getPlayerA().teleport(new Location(duel.getPlayerA().getWorld(), duel.getPlayerAPos().getX(), duel.getPlayerAPos().getY(), duel.getPlayerAPos().getZ()));
        duel.getPlayerB().teleport(new Location(duel.getPlayerB().getWorld(), duel.getPlayerBPos().getX(), duel.getPlayerBPos().getY(), duel.getPlayerBPos().getZ()));

        AnimationAPI.playAnimation(new Animation() {{
            addKeyFrame(0,() -> {
                sendTitle(duel.getPlayerA(),ChatColor.RED +"Get Ready!",ChatColor.AQUA + "The duel is starting in" + ChatColor.YELLOW + " 3 " + ChatColor.AQUA + "seconds!");
                sendTitle(duel.getPlayerB(),ChatColor.RED +"Get Ready!",ChatColor.AQUA + "The duel is starting in" + ChatColor.YELLOW + " 3 " + ChatColor.AQUA + "seconds!");
            });

            addKeyFrame(20,() -> {
                sendTitle(duel.getPlayerA(),ChatColor.RED +"Get Ready!",ChatColor.AQUA + "The duel is starting in" + ChatColor.YELLOW + " 2 " + ChatColor.AQUA + "seconds!");
                sendTitle(duel.getPlayerB(),ChatColor.RED +"Get Ready!",ChatColor.AQUA + "The duel is starting in" + ChatColor.YELLOW + " 2 " + ChatColor.AQUA + "seconds!");
            });

            addKeyFrame(40,() -> {
                sendTitle(duel.getPlayerA(),ChatColor.RED +"Get Ready!",ChatColor.AQUA + "The duel is starting in" + ChatColor.YELLOW + " 1 " + ChatColor.AQUA + "seconds!");
                sendTitle(duel.getPlayerB(),ChatColor.RED +"Get Ready!",ChatColor.AQUA + "The duel is starting in" + ChatColor.YELLOW + " 1 " + ChatColor.AQUA + "seconds!");
            });

            addKeyFrame(60,() -> {
                //TODO Add keyframe
            });
            setAnimationActions(new AnimationActions() {
                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {

                }
            });
        }});
    }

    private void deInitializeDuel(Duel duel) {

    }

    private void sendTitle(Player player, String titleValue, String subTitleValue) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + titleValue + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(0, 60, 20);

        IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subTitleValue + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
        PacketPlayOutTitle subTitleLength = new PacketPlayOutTitle(0, 60, 20);


        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitleLength);
    }
}
