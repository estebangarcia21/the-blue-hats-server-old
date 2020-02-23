package me.stevemmmmm.thehypixelpit.managers.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public interface DamageEnchant {
    void onHit(EntityDamageByEntityEvent event);

    double[] getPercentDamageIncreasePerLevel();

    DamageCalculationMode getCalculationMode();
}
