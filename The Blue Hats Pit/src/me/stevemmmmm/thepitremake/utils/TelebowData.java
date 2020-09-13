package me.stevemmmmm.thepitremake.utils;

import org.bukkit.entity.Arrow;
import org.bukkit.inventory.ItemStack;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

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
