package me.stevemmmmm.configapi.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class ConfigAPI extends JavaPlugin {
    private static JavaPlugin plugin;

    private static List<String> directories = new ArrayList<>();
    private static File file = new File("");

    public static void setPlugin(JavaPlugin javaPlugin, String... args) {
        plugin = javaPlugin;

        directories.addAll(Arrays.asList(args));
    }

    public static <T> void write(String directory, HashMap<UUID, T> object) {
        if (!directories.contains(directory)) {
            Bukkit.getLogger().severe("Directory " + directory + " does not exist!");
            return;
        }

        List<String> data = new ArrayList<>();

        for (Map.Entry<UUID, String> entry : read(directory).entrySet()) {
            data.add(entry.getKey() + ":" + entry.getValue());
        }

        //TODO Implement optional data deletion

        for (Map.Entry<UUID, T> entry : object.entrySet()) {
            data.add(entry.getKey().toString() + ":" + entry.getValue().toString());
        }

        plugin.getConfig().set(file.getAbsolutePath() + "\\Data\\" + directory, data);
        plugin.saveConfig();
    }

    public static void write(String directory, UUID player, Object value) {
        File file = new File("");

        if (!directories.contains(directory)) {
            Bukkit.getLogger().severe("Directory " + directory + " does not exist!");
            return;
        }

        List<String> data = new ArrayList<>();

        for (Map.Entry<UUID, String> entry : read(directory).entrySet()) {
            data.add(entry.getKey() + ":" + entry.getValue());
        }

        //TODO Implement optional data deletion

        data.add(player.toString() + ":" + value.toString());

        plugin.getConfig().set(file.getAbsolutePath() + "\\Data\\" + directory, data);
        plugin.saveConfig();
    }

    public static HashMap<UUID, String> read(String directory) {
        HashMap<UUID, String> data = new HashMap<>();

        for (String raw : plugin.getConfig().getStringList(file.getAbsolutePath() + "\\Data\\" + directory)) {
            String[] splitData = raw.split(":");
            data.put(UUID.fromString(splitData[0]), splitData[1]);
        }

        return data;
    }
}
