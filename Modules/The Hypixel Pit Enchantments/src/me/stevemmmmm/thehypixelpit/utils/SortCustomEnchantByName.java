package me.stevemmmmm.thehypixelpit.utils;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;

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
            System.out.println("Error! You forgot to set a name to an enchant!");
        }

        return 0;
    }
}
