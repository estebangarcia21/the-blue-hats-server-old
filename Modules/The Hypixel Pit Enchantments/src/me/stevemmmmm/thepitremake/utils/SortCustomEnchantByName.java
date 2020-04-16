package me.stevemmmmm.thepitremake.utils;

import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;

import java.util.Comparator;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class SortCustomEnchantByName implements Comparator<CustomEnchant> {

    @Override
    public int compare(CustomEnchant a, CustomEnchant b) {
        try {
            return a.getName().compareTo(b.getName());
        } catch (NullPointerException exception) {
            System.out.println("Bro wtf ur retarded! You forgot to set a name to an enchant dumbass!");
        }

        return 0;
    }
}
