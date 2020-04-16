package me.stevemmmmm.servercore.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Main extends JavaPlugin {
    public static Main instance;
    public static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onDisable() {

    }
}
