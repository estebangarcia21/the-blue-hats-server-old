package me.stevemmmmm.perworldinventories.core;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.configapi.core.ConfigAPI;
import me.stevemmmmm.instanceapi.core.InstanceManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(SerializeInventories.getInstance(), this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            ConfigAPI.InventorySerializer.serializePlayerInventory(instance, Bukkit.getWorld("world"), player);
            ConfigAPI.InventorySerializer.serializePlayerInventory(instance, Bukkit.getWorld("ThePit_0"), player);
        }

        InstanceManager.getInstance().removeGameInstances();
    }
}
