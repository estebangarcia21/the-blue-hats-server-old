package me.stevemmmmm.instanceapi.core;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.servercore.core.ServerGame;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class InstanceManager {
    private static InstanceManager instance;

    private final HashMap<ServerGame, ArrayList<World>> gameInstances = new HashMap<>();

    private InstanceManager() { }

    public static InstanceManager getInstance() {
        if (instance == null) instance = new InstanceManager();

        return instance;
    }

    public void generateGameInstance(ServerGame game) {
        ArrayList<World> instances = gameInstances.getOrDefault(game, new ArrayList<>());

        World world = replicateWorld(game.getGameMap().getName(), game.getReferenceName() + "_" + instances.size());

        if (world != null) {
            world.setGameRuleValue("announceAdvancements", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("keepInventory", "true");
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("randomTickSpeed", "0");
        }

        instances.add(world);

        gameInstances.put(game, instances);
    }

    private World replicateWorld(String original, String destination) {
        File srcDir = new File(Bukkit.getServer().getWorldContainer(), original);

        if (!srcDir.exists()) {
            Bukkit.getLogger().warning("Backup does not exist!");
            return null;
        }

        File destDir = new File(Bukkit.getServer().getWorldContainer(), destination);

        try {
            FileUtils.copyDirectory(srcDir, destDir);

            for (File file : Objects.requireNonNull(destDir.listFiles())) {
                if (file.isFile()) {
                    if (file.getName().equalsIgnoreCase("uid.dat") || file.getName().equalsIgnoreCase("session.lock")) {
                        boolean result = file.delete();

                        if (!result) {
                            System.out.println("Error while generating instances!");
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return Bukkit.getServer().createWorld(new WorldCreator(destination));
    }

    public void removeGameInstances() {
        for (Map.Entry<ServerGame, ArrayList<World>> entry : gameInstances.entrySet()) {
            for (World world : entry.getValue()) {
                for (Player player : world.getPlayers()) {
                    ((CraftPlayer) player).getHandle().playerConnection.disconnect("Instance Shutdown! (Server Restart?)");
                }

                Bukkit.unloadWorld(world, false);

                File path = world.getWorldFolder();

                try {
                    Files.walk(path.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(file -> {
                        boolean result = file.delete();

                        if (!result) {
                            System.out.println("Error while removing instance files!");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
