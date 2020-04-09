package me.stevemmmmm.thehypixelpit.managers.enchants;

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.enchants.Mirror;
import me.stevemmmmm.thehypixelpit.game.CombatManager;
import me.stevemmmmm.thehypixelpit.game.RegionManager;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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
    private HashMap<EntityDamageByEntityEvent, Double> absoluteReductionBuffer = new HashMap<>();

    private ArrayList<EntityDamageByEntityEvent> canceledEvents = new ArrayList<>();
    private List<EntityDamageByEntityEvent> removeCriticalDamage = new ArrayList<>();

    private DamageManager() { }

    public static DamageManager getInstance() {
        if (instance == null) instance = new DamageManager();

        return instance;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onHit(EntityDamageByEntityEvent event) {
        if (canceledEvents.contains(event)) {
            event.setCancelled(true);
        } else {
            event.setDamage(getDamageFromEvent(event));
        }

        canceledEvents.remove(event);
        additiveDamageBuffer.remove(event);
        multiplicativeDamageBuffer.remove(event);
        absoluteReductionBuffer.remove(event);
        reductionBuffer.remove(event);
        removeCriticalDamage.remove(event);
    }

    public double getDamageFromEvent(EntityDamageByEntityEvent event) {
        double damage = event.getDamage() * additiveDamageBuffer.getOrDefault(event, 1D) * multiplicativeDamageBuffer.getOrDefault(event, 1D) * reductionBuffer.getOrDefault(event, 1D) - absoluteReductionBuffer.getOrDefault(event, 0D);

        if (removeCriticalDamage.contains(event)) {
            damage *= .667;
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (player.getInventory().getLeggings() != null) {
                if (player.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS) {
                    if (!CustomEnchantManager.getInstance().getItemEnchants(player.getInventory().getLeggings()).isEmpty()) {
                        damage *= 0.871;
                    }
                }
            }
        }

        if (damage <= 0) damage = 1;

        return damage;
    }

    public double getFinalDamageFromEvent(EntityDamageByEntityEvent event) {
        double damage = event.getFinalDamage() * additiveDamageBuffer.getOrDefault(event, 1D) * multiplicativeDamageBuffer.getOrDefault(event, 1D) * reductionBuffer.getOrDefault(event, 1D) - absoluteReductionBuffer.getOrDefault(event, 0D);

        if (removeCriticalDamage.contains(event)) {
            damage *= .667;
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (player.getInventory().getLeggings() != null) {
                if (player.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS) {
                    if (!CustomEnchantManager.getInstance().getItemEnchants(player.getInventory().getLeggings()).isEmpty()) {
                        damage *= 0.871;
                    }
                }
            }
        }

        if (damage <= 0) damage = 1;

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

    public void setEventAsCanceled(EntityDamageByEntityEvent event) {
        canceledEvents.add(event);
    }

    public boolean isEventCancelled(EntityDamageByEntityEvent event) {
        return canceledEvents.contains(event);
    }

    public boolean playerIsInCanceledEvent(Player player) {
        for (EntityDamageByEntityEvent event : canceledEvents) {
            if (event.getDamager() instanceof Projectile) {
                if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    if (((Projectile) event.getDamager()).getShooter().equals(player)) {
                        return true;
                    }
                }
            }

            if (event.getDamager() instanceof Player) {
                if (event.getDamager().equals(player)) {
                    return true;
                }
            }

            if (event.getEntity() instanceof Player) {
                if (event.getEntity().equals(player)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean arrowIsInCanceledEvent(Arrow projectile) {
        for (EntityDamageByEntityEvent event : canceledEvents) {
            if (event.getDamager() instanceof Arrow) {
                if (event.getDamager().equals(projectile)) {
                    return true;
                }
            }

            if (event.getEntity() instanceof Arrow) {
                if (event.getEntity().equals(projectile)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void reduceDamage(EntityDamageByEntityEvent event, double value) {
        if (!reductionBuffer.containsKey(event)) reductionBuffer.put(event, 1D);

        if (reductionBuffer.get(event) == 1) {
            reductionBuffer.put(event, reductionBuffer.get(event) - (1 - value));
            return;
        }

        reductionBuffer.put(event, 1 - reductionBuffer.get(event) * value);
    }

    public void reduceAbsoluteDamage(EntityDamageByEntityEvent event, double value) {
        absoluteReductionBuffer.put(event, absoluteReductionBuffer.getOrDefault(event, 0D) + value);
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

        CombatManager.getInstance().combatTag(target);

        if (!CustomEnchant.itemHasEnchant(target.getInventory().getLeggings(), mirror)) {
            if (RegionManager.getInstance().playerIsInRegion(target, RegionManager.RegionType.SPAWN)) return;

            if (target.getHealth() - damage < 0) {
                target.setMaxHealth(target.getMaxHealth());
                manuallyCallDeathEvent(target);
            } else {
                target.damage(0);
                target.setHealth(target.getHealth() - damage);
            }
        } else if (CustomEnchant.getEnchantLevel(target.getInventory().getLeggings(), mirror) != 1) {
            if (RegionManager.getInstance().playerIsInRegion(reflectTo, RegionManager.RegionType.SPAWN)) return;

            try {
                if (reflectTo.getHealth() - (damage * mirror.damageReflection.at(CustomEnchant.getEnchantLevel(target.getInventory().getLeggings(), mirror))) < 0) {
                    reflectTo.setMaxHealth(target.getMaxHealth());
                    manuallyCallDeathEvent(reflectTo);
                } else {
                    reflectTo.damage(0);

                    CombatManager.getInstance().combatTag(target);
                    reflectTo.setHealth(Math.max(0, reflectTo.getHealth() - (damage * mirror.damageReflection.at(CustomEnchant.getEnchantLevel(target.getInventory().getLeggings(), mirror)))));
                }
            } catch (NullPointerException ignored) {

            }
        }
    }

    public boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0 && !((Entity) player).isOnGround() && player.getLocation().getBlock().getType() != Material.LADDER && player.getLocation().getBlock().getType() != Material.VINE && player.getLocation().getBlock().getType() != Material.STATIONARY_WATER && player.getLocation().getBlock().getType() != Material.STATIONARY_LAVA && player.getLocation().getBlock().getType() != Material.WATER && player.getLocation().getBlock().getType() != Material.LAVA && player.getVehicle() == null && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }

    private void manuallyCallDeathEvent(Player target) {
        PlayerDeathEvent manualEvent = new PlayerDeathEvent(target, new ArrayList<>(), 0,  "");
        Main.INSTANCE.getServer().getPluginManager().callEvent(manualEvent);
    }
}
