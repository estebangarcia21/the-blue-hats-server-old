package me.stevemmmmm.configapi.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class ConfigAPI extends JavaPlugin {
    private static HashMap<JavaPlugin, List<String>> dataCategories = new HashMap<>();
    private static File file = new File("");

    private static ArrayList<ConfigWriter> configWriters = new ArrayList<>();

    public void onEnable() {
        Logger log = Bukkit.getLogger();
        log.info("------------------------------------------");
        log.info("ConfigAPI by Stevemmmmm");
        log.info("------------------------------------------");

//        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
//            for (ConfigWriter writer : configWriters) {
//                writer.writeToConfig();
//            }
//        }, 36000L, 36000L);
    }

    public void onDisable() {
        for (ConfigWriter writer : configWriters) {
            writer.writeToConfig();
        }
    }

    public static void addPlugin(JavaPlugin javaPlugin, String... args) {
        dataCategories.put(javaPlugin, new ArrayList<>(Arrays.asList(args)));
    }

    public static void registerConfigWriter(ConfigWriter writer) {
        configWriters.add(writer);
    }

    public static <T> void write(JavaPlugin plugin, String dataCategory, HashMap<UUID, T> object) {
        if (!dataCategories.get(plugin).contains(dataCategory)) {
            Bukkit.getLogger().severe("Directory " + dataCategory + " does not exist!");
            return;
        }

        List<String> data = new ArrayList<>();

        for (String str : readAllData(plugin)) {
            if (!dataCategory.equals(str.split(":")[0])) {
                data.add(str);
            }
        }

        for (Map.Entry<UUID, T> entry : object.entrySet()) {
            data.add(dataCategory + ":" + entry.getKey().toString() + ":" + entry.getValue().toString());
        }

        plugin.getConfig().set(file.getAbsolutePath() + "\\Data\\", data);
        plugin.saveConfig();
    }

    public static void write(JavaPlugin plugin, String dataCategory, UUID player, Object value) {
        if (!dataCategories.get(plugin).contains(dataCategory)) {
            Bukkit.getLogger().severe("Directory " + dataCategory + " does not exist!");
            return;
        }

        List<String> data = new ArrayList<>();

        for (String str : readAllData(plugin)) {
            System.out.println(str.split(":")[0]);

            if (!dataCategory.equals(str.split(":")[0])) {
                data.add(str);
            }
        }

        System.out.println(plugin + " | " + data);

        data.add(dataCategory + ":" + player + ":" + value.toString());

        plugin.getConfig().set(file.getAbsolutePath() + "\\Data\\", data);
        plugin.saveConfig();
    }

    public static HashMap<UUID, String> read(Plugin plugin, String dataCategory) {
        HashMap<UUID, String> data = new HashMap<>();

        for (String raw : plugin.getConfig().getStringList(file.getAbsolutePath() + "\\Data\\")) {
            if (raw.split(":")[0].equals(dataCategory)) {
                String[] splitData = raw.split(":");
                data.put(UUID.fromString(splitData[1]), splitData[2]);
            }
        }

        return data;
    }

    public static List<String> readAllData(JavaPlugin plugin) {
        return plugin.getConfig().getStringList(file.getAbsolutePath() + "\\Data\\");
    }
}
