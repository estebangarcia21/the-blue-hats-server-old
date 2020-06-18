package me.stevemmmmm.permissions.core;

import me.stevemmmmm.configapi.core.ConfigAPI;
import me.stevemmmmm.permissions.ranks.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Main extends JavaPlugin {
    public static Main INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        ConfigAPI.registerConfigWriteLocations(this, new HashMap<String, String>() {{
            put("PlayerRanks", "rank.player");
            put("StaffRanks", "rank.staff");
        }});

        RankManager.getInstance().registerRank(new NoobRank());
        RankManager.getInstance().registerRank(new NoneRank());
        RankManager.getInstance().registerRank(new ModeratorRank());
        RankManager.getInstance().registerRank(new OwnerRank());
        RankManager.getInstance().registerRank(new PoggerRank());

        ConfigAPI.registerConfigWriter(PermissionsManager.getInstance());
        ConfigAPI.registerConfigReader(PermissionsManager.getInstance());

        getServer().getPluginManager().registerEvents(PermissionsManager.getInstance(), this);

        getServer().getPluginCommand("setrank").setExecutor(new SetRankCommand());
    }

    @Override
    public void onDisable() { }
}
