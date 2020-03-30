package me.stevemmmmm.thehypixelpit.managers.enchants;

import me.stevemmmmm.thehypixelpit.enchants.Mirror;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class DamageManager implements Listener {
    private static DamageManager instance;

    private HashMap<EntityDamageByEntityEvent, Double> additiveDamageBuffer = new HashMap<>();
    private HashMap<EntityDamageByEntityEvent, Double> multiplicativeDamageBuffer = new HashMap<>();

    private HashMap<EntityDamageByEntityEvent, Double> reductionBuffer = new HashMap<>();

    private List<EntityDamageByEntityEvent> removeCriticalDamage = new ArrayList<>();

    private DamageManager() { }

    public static DamageManager getInstance() {
        if (instance == null) instance = new DamageManager();

        return instance;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onHit(EntityDamageByEntityEvent event) {
        System.out.println(event.getDamager());
        System.out.println(reductionBuffer.get(event));
        System.out.println(additiveDamageBuffer.get(event));
        System.out.println(multiplicativeDamageBuffer.get(event));
        System.out.println(getDamageFromEvent(event));

        event.setDamage(getDamageFromEvent(event));

        additiveDamageBuffer.remove(event);
        multiplicativeDamageBuffer.remove(event);
        reductionBuffer.remove(event);
        removeCriticalDamage.remove(event);
    }

    public double getDamageFromEvent(EntityDamageByEntityEvent event) {
        if (!additiveDamageBuffer.containsKey(event)) additiveDamageBuffer.put(event, 1D);
        if (!multiplicativeDamageBuffer.containsKey(event)) multiplicativeDamageBuffer.put(event, 1D);
        if (!reductionBuffer.containsKey(event)) reductionBuffer.put(event, 1D);

        double damage = event.getDamage() * additiveDamageBuffer.get(event) * multiplicativeDamageBuffer.get(event) * reductionBuffer.get(event);

        if (removeCriticalDamage.contains(event)) {
            damage *= .667;
        }

        return damage;
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

    public void reduceDamage(EntityDamageByEntityEvent event, double value) {
        if (!reductionBuffer.containsKey(event)) reductionBuffer.put(event, 1D);

        if (reductionBuffer.get(event) == 1) {
            reductionBuffer.put(event, reductionBuffer.get(event) - (1 - value));
            return;
        }

        reductionBuffer.put(event, 1 - reductionBuffer.get(event) * value);
    }

    public void removeExtraCriticalDamage(EntityDamageByEntityEvent event) {
        removeCriticalDamage.add(event);
    }

    public void doTrueDamage(Player target, double damage) {
        Mirror mirror = new Mirror();

        if (!CustomEnchant.itemHasEnchant(target.getInventory().getLeggings(), mirror)) {
            target.setHealth(Math.max(0, target.getHealth() - damage));
        }
    }

    public void doTrueDamage(Player target, double damage, Player reflectTo) {
        Mirror mirror = new Mirror();

        if (!CustomEnchant.itemHasEnchant(target.getInventory().getLeggings(), mirror)) {
            target.setHealth(Math.max(0, target.getHealth() - damage));
        } else if (CustomEnchant.itemHasEnchant(target.getInventory().getLeggings(), mirror)) {
            reflectTo.setHealth(Math.max(0, target.getHealth() - (damage * mirror.damageReflection.at(CustomEnchant.getEnchantLevel(target.getInventory().getLeggings(), mirror)))));
        }
    }

    public boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0 && !((Entity) player).isOnGround() && player.getLocation().getBlock().getType() != Material.LADDER && player.getLocation().getBlock().getType() != Material.VINE && player.getLocation().getBlock().getType() != Material.STATIONARY_WATER && player.getLocation().getBlock().getType() != Material.STATIONARY_LAVA && player.getLocation().getBlock().getType() != Material.WATER && player.getLocation().getBlock().getType() != Material.LAVA && player.getVehicle() == null && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }
}
