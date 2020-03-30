package me.stevemmmmm.thehypixelpit.managers.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class EnchantVariable<T> {
    private T[] values;

    @SafeVarargs
    public EnchantVariable(T... values) {
        this.values = values;
    }

    public T at(int level) {
        return values[level - 1];
    }
}
