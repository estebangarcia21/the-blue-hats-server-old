package me.stevemmmmm.permissions.core;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.configapi.core.ConfigAPI;
import me.stevemmmmm.permissions.ranks.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        ConfigAPI.addPlugin(this, "PlayerRanks", "StaffRanks");

        getServer().getPluginManager().registerEvents(PermissionsManager.getInstance(), this);

        getServer().getPluginCommand("setrank").setExecutor(new SetRankCommand());

        RankManager.getInstance().registerRank(new NoobRank());
        RankManager.getInstance().registerRank(new NoneRank());
        RankManager.getInstance().registerRank(new ModeratorRank());
        RankManager.getInstance().registerRank(new OwnerRank());
        RankManager.getInstance().registerRank(new DonorRank());

        PermissionsManager.getInstance().readConfig();
    }

    @Override
    public void onDisable() {
        PermissionsManager.getInstance().writeToConfig();
    }
}
