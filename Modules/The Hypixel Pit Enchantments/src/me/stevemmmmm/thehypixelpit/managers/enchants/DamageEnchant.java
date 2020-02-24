package me.stevemmmmm.thehypixelpit.managers.enchants;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public interface DamageEnchant {
    void onHit(EntityDamageByEntityEvent event);

    double[] getPercentDamageIncreasePerLevel();

    CalculationMode getDamageCalculationMode();
}
