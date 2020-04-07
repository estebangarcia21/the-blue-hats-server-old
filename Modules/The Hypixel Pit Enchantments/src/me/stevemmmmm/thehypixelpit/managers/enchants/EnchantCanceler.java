package me.stevemmmmm.thehypixelpit.managers.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.entity.Player;

public interface EnchantCanceler {
    boolean isCanceled(Player player);

    CustomEnchant getEnchant();
}
