package me.stevemmmmm.thehypixelpit.managers.enchants;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class LevelVariable<T> {
    private final T[] values;

    @SafeVarargs
    public LevelVariable(T... values) {
        this.values = values;
    }

    public T at(int level) {
        return values[level - 1];
    }
}
