package me.stevemmmmm.thehypixelpit.managers.other;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
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
        double percentDamageIncrease = 0;

        for (CustomEnchant enchant : CustomEnchantManager.getInstance().getItemEnchants(item).keySet()) {
            if (enchant instanceof DamageEnchant) {
                percentDamageIncrease += ((DamageEnchant) enchant).getPercentDamageIncreasePerLevel()[CustomEnchantManager.getInstance().getItemEnchants(item).get(enchant)];
            }
        }
    }

    public void setDamage(float damage) {

    }

}
