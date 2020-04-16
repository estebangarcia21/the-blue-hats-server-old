package me.stevemmmmm.thehypixelpit.utils;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.entity.Arrow;
import org.bukkit.inventory.ItemStack;

public class TelebowData {
    private final Arrow arrow;
    private final ItemStack bow;
    private final boolean isSneaking;

    public TelebowData(Arrow arrow, ItemStack bow, boolean isSneaking) {
        this.arrow = arrow;
        this.bow = bow;
        this.isSneaking = isSneaking;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public ItemStack getBow() {
        return bow;
    }

    public boolean isSneaking() {
        return isSneaking;
    }
}
