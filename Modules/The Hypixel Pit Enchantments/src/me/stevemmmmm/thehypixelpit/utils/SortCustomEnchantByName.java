package me.stevemmmmm.thehypixelpit.utils;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.EnvironmentalEnchant;

import java.util.Comparator;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class SortCustomEnchantByName implements Comparator<CustomEnchant> {

    @Override
    public int compare(CustomEnchant a, CustomEnchant b) {
        return a.getName().compareTo(b.getName());
    }
}
