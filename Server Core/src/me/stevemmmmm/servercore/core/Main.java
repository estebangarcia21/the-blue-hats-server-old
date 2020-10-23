package me.stevemmmmm.servercore.core;

import org.bukkit.plugin.java.JavaPlugin;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Main extends JavaPlugin {
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        // protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onDisable() {

    }
}
