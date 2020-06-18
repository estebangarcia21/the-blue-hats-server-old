package me.stevemmmmm.servercore.core;

import org.bukkit.World;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public interface ServerGame {
    String getGameName();

    String getReferenceName();

    World getGameMap();

    WorldType getWorldType();
}
