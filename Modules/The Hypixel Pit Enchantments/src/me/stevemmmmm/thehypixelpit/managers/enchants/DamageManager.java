package me.stevemmmmm.thehypixelpit.managers.enchants;

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.enchants.Mirror;
import me.stevemmmmm.thehypixelpit.game.DamageIndicator;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
        event.setDamage(getDamageFromEvent(event));

        additiveDamageBuffer.remove(event);
        multiplicativeDamageBuffer.remove(event);
        reductionBuffer.remove(event);
        removeCriticalDamage.remove(event);
    }

    public double getDamageFromEvent(EntityDamageByEntityEvent event) {
        double damage = event.getDamage() * additiveDamageBuffer.getOrDefault(event, 1D) * multiplicativeDamageBuffer.getOrDefault(event, 1D) * reductionBuffer.getOrDefault(event, 1D);

        if (removeCriticalDamage.contains(event)) {
            damage *= .667;
        }

        return damage;
    }

    public double getFinalDamageFromEvent(EntityDamageByEntityEvent event) {
        double damage = event.getFinalDamage() * additiveDamageBuffer.getOrDefault(event, 1D) * multiplicativeDamageBuffer.getOrDefault(event, 1D) * reductionBuffer.getOrDefault(event, 1D);

        if (removeCriticalDamage.contains(event)) {
            damage *= .667;
        }

        return damage;
    }

    public void addDamage(EntityDamageByEntityEvent event, double value, CalculationMode mode) {
        if (mode == CalculationMode.ADDITIVE) {
            additiveDamageBuffer.put(event, additiveDamageBuffer.getOrDefault(event, 1D) + value);
        }

        if (mode == CalculationMode.MULTIPLICATIVE) {
            multiplicativeDamageBuffer.put(event, multiplicativeDamageBuffer.getOrDefault(event, 0D) + value);
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
            target.damage(0);
        }
    }

    public void doTrueDamage(Player target, double damage, Player reflectTo) {
        Mirror mirror = new Mirror();

        target.damage(0);

        if (!CustomEnchant.itemHasEnchant(target.getInventory().getLeggings(), mirror)) {
            if (target.getHealth() - damage < 0) {
                target.setHealth(target.getMaxHealth());
                manuallyCallDeathEvent(target);
            } else {
                target.setHealth(target.getMaxHealth() - damage);
            }
        } else if (CustomEnchant.getEnchantLevel(target.getInventory().getLeggings(), mirror) != 1) {
            if (reflectTo.getHealth() - (damage * mirror.damageReflection.at(CustomEnchant.getEnchantLevel(target.getInventory().getLeggings(), mirror))) < 0) {
                reflectTo.setMaxHealth(reflectTo.getMaxHealth());
                manuallyCallDeathEvent(reflectTo);
            } else {
                reflectTo.setHealth(reflectTo.getHealth() - (damage * mirror.damageReflection.at(CustomEnchant.getEnchantLevel(target.getInventory().getLeggings(), mirror))));
            }
        }
    }

    public boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0 && !((Entity) player).isOnGround() && player.getLocation().getBlock().getType() != Material.LADDER && player.getLocation().getBlock().getType() != Material.VINE && player.getLocation().getBlock().getType() != Material.STATIONARY_WATER && player.getLocation().getBlock().getType() != Material.STATIONARY_LAVA && player.getLocation().getBlock().getType() != Material.WATER && player.getLocation().getBlock().getType() != Material.LAVA && player.getVehicle() == null && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }

    private void manuallyCallDeathEvent(Player target) {
        PlayerDeathEvent manualEvent = new PlayerDeathEvent(target, new ArrayList<>(), 0,  "");
        Main.instance.getServer().getPluginManager().callEvent(manualEvent);
    }
}
