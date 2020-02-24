package me.stevemmmmm.thehypixelpit.managers.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface DamageReductionEnchant {
    void onHit(EntityDamageByEntityEvent event);

    double[] getPercentReductionPerLevel();

    CalculationMode getReductionCalculationMode();
}
