package me.stevemmmmm.configapi.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class ConfigAPI extends JavaPlugin {
    private static JavaPlugin plugin;

    private static List<String> dataCategories = new ArrayList<>();
    private static File file = new File("");

    public static void setPlugin(JavaPlugin javaPlugin, String... args) {
        plugin = javaPlugin;

        dataCategories.addAll(Arrays.asList(args));
    }

    public static <T> void write(String dataCategory, HashMap<UUID, T> object) {
        if (!dataCategories.contains(dataCategory)) {
            Bukkit.getLogger().severe("Directory " + dataCategory + " does not exist!");
            return;
        }

        List<String> data = new ArrayList<>();

        for (String str : readAllData()) {
            if (!dataCategory.equals(str.split(":")[0])) {
                data.add(str);
            }
        }

        //TODO Implement optional data deletion

        for (Map.Entry<UUID, T> entry : object.entrySet()) {
            data.add(dataCategory + ":" + entry.getKey().toString() + ":" + entry.getValue().toString());
        }

        plugin.getConfig().set(file.getAbsolutePath() + "\\Data\\", data);
        plugin.saveConfig();
    }

    public static void write(String dataCategory, UUID player, Object value) {
        File file = new File("");

        if (!dataCategories.contains(dataCategory)) {
            Bukkit.getLogger().severe("Directory " + dataCategory + " does not exist!");
            return;
        }

        List<String> data = new ArrayList<>();

        for (Map.Entry<UUID, String> entry : read(dataCategory).entrySet()) {
            data.add(entry.getKey() + ":" + entry.getValue());
        }

        //TODO Implement optional data deletion

        data.add(player.toString() + ":" + value.toString());

        plugin.getConfig().set(file.getAbsolutePath() + "\\Data\\", data);
        plugin.saveConfig();
    }

    public static HashMap<UUID, String> read(String dataCategory) {
        HashMap<UUID, String> data = new HashMap<>();

        for (String raw : plugin.getConfig().getStringList(file.getAbsolutePath() + "\\Data\\")) {
            if (raw.split(":")[0].equals(dataCategory)) {
                String[] splitData = raw.split(":");
                data.put(UUID.fromString(splitData[1]), splitData[2]);
            }
        }

        return data;
    }

    public static ArrayList<String> readRaw(String dataCategory) {
        ArrayList<String> data = new ArrayList<>();

        for (String raw : plugin.getConfig().getStringList(file.getAbsolutePath() + "\\Data\\")) {
            if (raw.split(":")[0].equals(dataCategory)) {
                data.add(raw);
            }
        }

        return data;
    }

    public static List<String> readAllData() {
        return plugin.getConfig().getStringList(file.getAbsolutePath() + "\\Data\\");
    }
}
