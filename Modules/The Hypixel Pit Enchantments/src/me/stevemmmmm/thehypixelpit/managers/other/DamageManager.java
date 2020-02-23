package me.stevemmmmm.thehypixelpit.managers.other;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.EnvironmentalEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class DamageManager implements Listener {
    private static DamageManager instance;

    private DamageManager() { }

    public static DamageManager getInstance() {
        if (instance == null) instance = new DamageManager();

        return instance;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            event.setDamage(calculateDamage(event.getDamage(), ((Player) event.getDamager()).getItemInHand()));
        }

        //TODO Implement arrows
    }

    public double calculateDamage(double eventDamage, ItemStack item) {
        double percentDamageIncrease = 1;
        double multiplier = 0;

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

        //TODO Check if each damage inc meets the requirements

        return eventDamage * percentDamageIncrease;
    }

    public boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0 && !((Entity) player).isOnGround() && player.getLocation().getBlock().getType() != Material.LADDER && player.getLocation().getBlock().getType() != Material.VINE && player.getLocation().getBlock().getType() != Material.STATIONARY_WATER && player.getLocation().getBlock().getType() != Material.STATIONARY_LAVA && player.getLocation().getBlock().getType() != Material.WATER && player.getLocation().getBlock().getType() != Material.LAVA && player.getVehicle() == null && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }
}
