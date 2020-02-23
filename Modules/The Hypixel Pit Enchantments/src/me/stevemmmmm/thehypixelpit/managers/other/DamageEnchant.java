package me.stevemmmmm.thehypixelpit.managers.other;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.EnvironmentalEnchant;
import org.bukkit.inventory.ItemStack;

public abstract class DamageEnchant extends CustomEnchant {
    public abstract boolean triggerEnchant(ItemStack sender, Object... args);

    public abstract double[] getPercentDamageIncreasePerLevel();

    public abstract DamageCalculationMode getCalculationMode();
}
