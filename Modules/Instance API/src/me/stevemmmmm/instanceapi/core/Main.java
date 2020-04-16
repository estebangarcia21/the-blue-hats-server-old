package me.stevemmmmm.instanceapi.core;

import org.bukkit.plugin.java.JavaPlugin;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        InstanceManager.getInstance().generateGameInstance(me.stevemmmmm.thehypixelpit.core.Main.INSTANCE);
    }

    @Override
    public void onDisable() {

    }
}
