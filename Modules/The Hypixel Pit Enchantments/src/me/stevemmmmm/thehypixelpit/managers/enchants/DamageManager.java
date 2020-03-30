package me.stevemmmmm.thehypixelpit.managers.enchants;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class DamageManager implements Listener {
    private static DamageManager instance;

    private HashMap<EntityDamageByEntityEvent, Double> additiveDamageBuffer = new HashMap<>();
    private HashMap<EntityDamageByEntityEvent, Double> multiplicativeDamageBuffer = new HashMap<>();

    private HashMap<EntityDamageByEntityEvent, Double> reductionBuffer = new HashMap<>();

    private DamageManager() { }

    public static DamageManager getInstance() {
        if (instance == null) instance = new DamageManager();

        return instance;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onHit(EntityDamageByEntityEvent event) {
        event.setDamage(getDamageFromEvent(event));

        additiveDamageBuffer.remove(event);
        multiplicativeDamageBuffer.remove(event);
    }

    public double getDamageFromEvent(EntityDamageByEntityEvent event) {
        if (!additiveDamageBuffer.containsKey(event)) additiveDamageBuffer.put(event, 1D);
        if (!multiplicativeDamageBuffer.containsKey(event)) multiplicativeDamageBuffer.put(event, 1D);

        return event.getDamage() * additiveDamageBuffer.get(event) * multiplicativeDamageBuffer.get(event);
    }

    public void addDamage(EntityDamageByEntityEvent event, double value, CalculationMode mode) {
        if (!additiveDamageBuffer.containsKey(event)) additiveDamageBuffer.put(event, 1D);
        if (!multiplicativeDamageBuffer.containsKey(event)) multiplicativeDamageBuffer.put(event, 1D);

        if (mode == CalculationMode.ADDITIVE) {
            additiveDamageBuffer.put(event, additiveDamageBuffer.get(event) + value);
        }

        if (mode == CalculationMode.MULTIPLICATIVE) {
            multiplicativeDamageBuffer.put(event, multiplicativeDamageBuffer.get(event) + value);
        }
    }

    public boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0 && !((Entity) player).isOnGround() && player.getLocation().getBlock().getType() != Material.LADDER && player.getLocation().getBlock().getType() != Material.VINE && player.getLocation().getBlock().getType() != Material.STATIONARY_WATER && player.getLocation().getBlock().getType() != Material.STATIONARY_LAVA && player.getLocation().getBlock().getType() != Material.WATER && player.getLocation().getBlock().getType() != Material.LAVA && player.getVehicle() == null && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }
}
