package me.stevemmmmm.thehypixelpit.managers;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.configapi.core.ConfigAPI;
import me.stevemmmmm.configapi.core.ConfigReader;
import me.stevemmmmm.configapi.core.ConfigWriter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LevelingSystem implements Listener, ConfigReader, ConfigWriter {
    private static LevelingSystem instance;

    private HashMap<Integer, Integer> xpPerLevel = new HashMap<>();
    private HashMap<Integer, Float> prestigeMultiplier = new HashMap<>();

    private HashMap<UUID, Integer> playerPrestiges = new HashMap<>();
    private HashMap<UUID, Integer> playerXP = new HashMap<>();

    private LevelingSystem() {
        initializeMaps();
    }

    public static LevelingSystem getInstance() {
        if (instance == null) instance = new LevelingSystem();

        return instance;
    }

    private void initializeMaps() {
        xpPerLevel.put(1, 20);

        for (int i = 2; i <= 120; i++) {
            xpPerLevel.put(i, xpPerLevel.get(i - 1) + (int) (80 * Math.round(Math.pow(Math.E, (float) i / 20))));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //TODO Check if the player joined a pit instance

        if (!playerPrestiges.containsKey(event.getPlayer().getUniqueId())) playerPrestiges.put(event.getPlayer().getUniqueId(), 0);
        if (!playerXP.containsKey(event.getPlayer().getUniqueId())) playerXP.put(event.getPlayer().getUniqueId(), 0);
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {

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
        ConfigAPI.write("Prestige", playerPrestiges);
    }
}
