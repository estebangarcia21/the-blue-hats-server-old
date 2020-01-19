package me.stevemmmmm.thehypixelpit.managers;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.configapi.core.ConfigAPI;
import me.stevemmmmm.configapi.core.ConfigReader;
import me.stevemmmmm.configapi.core.ConfigWriter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GrindingSystem implements Listener, ConfigReader, ConfigWriter {
    private static GrindingSystem instance;

    private HashMap<Integer, Integer> xpPerLevel = new HashMap<>();
    private HashMap<Integer, Float> prestigeMultiplier = new HashMap<>();

    private HashMap<UUID, Integer> playerPrestiges = new HashMap<>();
    private HashMap<UUID, Integer> playerXP = new HashMap<>();
    private HashMap<UUID, Double> playerGold = new HashMap<>();

    private GrindingSystem() {
        initializeMaps();
    }

    public static GrindingSystem getInstance() {
        if (instance == null) instance = new GrindingSystem();

        return instance;
    }

    public void giveXP(Player player, int amount) {
        playerXP.put(player.getUniqueId(), playerXP.get(player.getUniqueId()) + amount);
    }

    public int giveRandomXP(Player player) {
        int xp = ThreadLocalRandom.current().nextInt(22, 35);
        playerXP.put(player.getUniqueId(), playerXP.get(player.getUniqueId()) + xp);

        return xp;
    }

    public void giveGold(Player player, int amount) {
        playerGold.put(player.getUniqueId(), playerGold.get(player.getUniqueId()) + amount);
    }

    public double giveRandomGold(Player player) {
        DecimalFormat df = new DecimalFormat("###.##");
        double gold = ThreadLocalRandom.current().nextDouble(10, 25);
        playerGold.put(player.getUniqueId(), Double.parseDouble(df.format(playerXP.get(player.getUniqueId()) + gold)));

        return gold;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //TODO Check if the player joined a pit instance

        if (!playerPrestiges.containsKey(event.getPlayer().getUniqueId())) playerPrestiges.put(event.getPlayer().getUniqueId(), 0);
        if (!playerXP.containsKey(event.getPlayer().getUniqueId())) playerXP.put(event.getPlayer().getUniqueId(), 0);
        if (!playerGold.containsKey(event.getPlayer().getUniqueId())) playerGold.put(event.getPlayer().getUniqueId(), 0D);
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        DecimalFormat df = new DecimalFormat("##0.00");
        if (event.getEntity().getKiller() != null) event.getEntity().getKiller().sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "KILL!" + ChatColor.GRAY + " on " + event.getEntity().getName() + ChatColor.AQUA + " +" + giveRandomXP(event.getEntity().getKiller()) + "XP" + ChatColor.GOLD + " +" + df.format(giveRandomGold(event.getEntity().getKiller())) + "g");
    }

    private void initializeMaps() {
        prestigeMultiplier.put(0, 1f);
        prestigeMultiplier.put(1, 1.15f);
        prestigeMultiplier.put(2, 1.225f);
        prestigeMultiplier.put(3, 1.5f);

        xpPerLevel.put(1, 20);

        for (int i = 2; i <= 120; i++) {
            xpPerLevel.put(i, xpPerLevel.get(i - 1) + (int) (80 * Math.round(Math.pow(Math.E, (float) i / 20))));
        }
    }

    public int getPlayerPrestige(Player player) {
        return playerPrestiges.getOrDefault(player.getUniqueId(), 0);
    }

    public int getPlayerXP(Player player) {
        return playerXP.get(player.getUniqueId());
    }

    public double getPlayerGold(Player player) {
        return playerGold.get(player.getUniqueId());
    }

    @Override
    public void readConfig() {
        for (Map.Entry<UUID, String> entry : ConfigAPI.read("Prestige").entrySet()) {
            playerPrestiges.put(entry.getKey(), Integer.parseInt(entry.getValue()));
        }

        for (Map.Entry<UUID, String> entry : ConfigAPI.read("XP").entrySet()) {
            playerXP.put(entry.getKey(), Integer.parseInt(entry.getValue()));
        }
    }

    @Override
    public void writeToConfig() {
        ConfigAPI.write("XP", playerXP);
        ConfigAPI.write("Gold", playerGold);
        ConfigAPI.write("Prestige", playerPrestiges);
    }
}
