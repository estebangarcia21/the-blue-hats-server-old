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

    private HashMap<UUID, Integer> playerPrestiges = new HashMap<>();
    private HashMap<UUID, Integer> playerXP = new HashMap<>();

    public static LevelingSystem getInstance() {
        if (instance == null) instance = new LevelingSystem();

        return instance;
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
        //TODO Implement config saving on an interval of time

        ConfigAPI.write("XP", playerXP);
        ConfigAPI.write("Prestige", playerPrestiges);

//        for (Map.Entry<UUID, Integer> entry : playerXP.entrySet()) {
//            ConfigAPI.write("Prestiges", entry.getKey(), entry.getValue());
//        }
//
//        for (Map.Entry<UUID, Integer> entry : playerXP.entrySet()) {
//            ConfigAPI.write("XP", entry.getKey(), entry.getValue());
//        }
    }
}
