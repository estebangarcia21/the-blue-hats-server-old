package me.stevemmmmm.thehypixelpit.managers.other;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class DamageManager implements Listener {
    private static DamageManager instance;

    private DamageManager() { }

    public static DamageManager getInstance() {
        if (instance == null) instance = new DamageManager();

        return instance;
    }

    public double calculateDamage(double eventDamage, ItemStack item) {
        double percentDamageIncrease = 1;
        double multiplier = 1;

        for (CustomEnchant enchant : CustomEnchantManager.getInstance().getItemEnchants(item).keySet()) {
            if (enchant instanceof DamageEnchant) {
                if (((DamageEnchant) enchant).getCalculationMode() == DamageCalculationMode.ADDITIVE) {
                    percentDamageIncrease += ((DamageEnchant) enchant).getPercentDamageIncreasePerLevel()[CustomEnchantManager.getInstance().getItemEnchants(item).get(enchant) - 1];
                }

                if (((DamageEnchant) enchant).getCalculationMode() == DamageCalculationMode.MULTIPLICATIVE) {
                    multiplier += ((DamageEnchant) enchant).getPercentDamageIncreasePerLevel()[CustomEnchantManager.getInstance().getItemEnchants(item).get(enchant) - 1];
                }
            }
        }

        percentDamageIncrease *= multiplier;

        return eventDamage * percentDamageIncrease;
    }
}
