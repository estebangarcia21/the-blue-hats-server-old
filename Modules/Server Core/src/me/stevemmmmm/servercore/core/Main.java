package me.stevemmmmm.servercore.core;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;

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
