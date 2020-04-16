package me.stevemmmmm.thepitremake.managers.enchants;

import me.stevemmmmm.thepitremake.managers.CustomEnchant;
import org.bukkit.entity.Player;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public interface EnchantCanceler {
    boolean isCanceled(Player player);

    CustomEnchant getEnchant();
}
