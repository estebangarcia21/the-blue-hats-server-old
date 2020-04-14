package me.stevemmmmm.servercore.core;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.World;

public interface ServerGame {
    String getGameName();

    String getReferenceName();

    World getGameMap();

    WorldType getWorldType();
}
