package me.stevemmmmm.perworldinventories.core;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryData {
    private final Player player;

    private final ItemStack[] contents;
    private final ItemStack[] armor;

    public PlayerInventoryData(Player player) {
        this.player = player;

        contents = player.getInventory().getContents();
        armor = player.getInventory().getArmorContents();
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack[] getInventoryContents() {
        return contents;
    }

    public ItemStack[] getArmorContents() {
        return armor;
    }
}
