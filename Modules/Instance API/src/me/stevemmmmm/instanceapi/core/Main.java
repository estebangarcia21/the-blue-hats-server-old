package me.stevemmmmm.instanceapi.core;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        InstanceManager.getInstance().generateGameInstance(me.stevemmmmm.thehypixelpit.core.Main.INSTANCE);
    }

    @Override
    public void onDisable() {

    }
}
