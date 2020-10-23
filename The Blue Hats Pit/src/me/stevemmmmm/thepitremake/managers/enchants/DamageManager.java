package me.stevemmmmm.thepitremake.managers.enchants;

import me.stevemmmmm.thepitremake.enchants.Mirror;
import me.stevemmmmm.thepitremake.game.CombatManager;
import me.stevemmmmm.thepitremake.game.RegionManager;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class DamageManager implements Listener {
    private static DamageManager instance;

    private static final HashMap<EntityDamageByEntityEvent, EventData> eventData = new HashMap<>();

    private final ArrayList<EntityDamageByEntityEvent> canceledEvents = new ArrayList<>();
    private final ArrayList<EntityDamageByEntityEvent> removeCriticalDamage = new ArrayList<>();

    private DamageManager() {
    }

    public static DamageManager getInstance() {
        if (instance == null)
            instance = new DamageManager();

        return instance;
    }

    static class EventData {
        private double additiveDamage = 1;
        private double multiplicativeDamage;

        private double reductionAmount = 1;
        private double absoluteReductionAmount;

        private EventData() {
        }

        public static EventData fromEvent(EntityDamageByEntityEvent event) {
            if (!eventData.containsKey(event)) {
                eventData.put(event, new EventData());
            }

            return eventData.get(event);
        }

        public double getAdditiveDamage() {
            return additiveDamage;
        }

        public void setAdditiveDamage(double additiveDamage) {
            this.additiveDamage = additiveDamage;
        }

        public double getMultiplicativeDamage() {
            return multiplicativeDamage;
        }

        public void setMultiplicativeDamage(double multiplicativeDamage) {
            this.multiplicativeDamage = multiplicativeDamage;
        }

        public double getReductionAmount() {
            return reductionAmount;
        }

        public void setReductionAmount(double reductionAmount) {
            this.reductionAmount = reductionAmount;
        }

        public double getAbsoluteReductionAmount() {
            return absoluteReductionAmount;
        }

        public void setAbsoluteReductionAmount(double absoluteReductionAmount) {
            this.absoluteReductionAmount = absoluteReductionAmount;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!eventData.containsKey(event)) {
            eventData.put(event, new EventData());
        }

        if (canceledEvents.contains(event)) {
            event.setCancelled(true);
        } else {
            event.setDamage(getDamageFromEvent(event));
        }

        eventData.remove(event);
        canceledEvents.remove(event);
        removeCriticalDamage.remove(event);
    }

    public double getDamageFromEvent(EntityDamageByEntityEvent event) {
        return calculateDamage(event.getDamage(), event);
    }

    public double getFinalDamageFromEvent(EntityDamageByEntityEvent event) {
        return calculateDamage(event.getFinalDamage(), event);
    }

    public void addDamage(EntityDamageByEntityEvent event, double value, CalculationMode mode) {
        EventData data = EventData.fromEvent(event);

        if (mode == CalculationMode.ADDITIVE) {
            data.setAdditiveDamage(data.getAdditiveDamage() + value);
        }

        if (mode == CalculationMode.MULTIPLICATIVE) {
            data.setMultiplicativeDamage(data.getMultiplicativeDamage() + value);
        }
    }

    public void reduceDamage(EntityDamageByEntityEvent event, double value) {
        EventData data = EventData.fromEvent(event);

        if (data.getReductionAmount() == 1) {
            data.setReductionAmount(data.getReductionAmount() - (1 - value));
            return;
        }

        data.setReductionAmount(1 - data.getReductionAmount() * value);
    }

    public void reduceAbsoluteDamage(EntityDamageByEntityEvent event, double value) {
        EventData data = EventData.fromEvent(event);

        data.setAbsoluteReductionAmount(data.getAbsoluteReductionAmount() + value);
    }

    public void removeExtraCriticalDamage(EntityDamageByEntityEvent event) {
        removeCriticalDamage.add(event);
    }

    public void setEventAsCanceled(EntityDamageByEntityEvent event) {
        canceledEvents.add(event);
    }

    public boolean isEventNotCancelled(EntityDamageByEntityEvent event) {
        return !canceledEvents.contains(event);
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
            if (target.getHealth() - damage < 0) {
                safeSetPlayerHealth(target, 0);
            } else {
                target.damage(0);
                safeSetPlayerHealth(target, target.getHealth() - damage);
            }
        } else if (CustomEnchant.getEnchantLevel(target.getInventory().getLeggings(), mirror) != 1) {
            try {
                if (reflectTo.getHealth() - (damage * mirror.damageReflection.getValueAtLevel(
                        CustomEnchant.getEnchantLevel(target.getInventory().getLeggings(), mirror))) < 0) {
                    safeSetPlayerHealth(target, 0);
                } else {
                    reflectTo.damage(0);

                    CombatManager.getInstance().combatTag(target);
                    safeSetPlayerHealth(reflectTo,
                            Math.max(0, reflectTo.getHealth() - (damage * mirror.damageReflection.getValueAtLevel(
                                    CustomEnchant.getEnchantLevel(target.getInventory().getLeggings(), mirror)))));
                }
            } catch (NullPointerException ignored) {

            }
        }
    }

    public void safeSetPlayerHealth(Player player, double health) {
        if (!RegionManager.getInstance().playerIsInRegion(player, RegionManager.RegionType.SPAWN)) {
            if (health >= 0 && health <= player.getMaxHealth()) {
                player.setHealth(health);
            }
        }
    }

    public boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0 && !((Entity) player).isOnGround()
                && player.getLocation().getBlock().getType() != Material.LADDER
                && player.getLocation().getBlock().getType() != Material.VINE
                && player.getLocation().getBlock().getType() != Material.STATIONARY_WATER
                && player.getLocation().getBlock().getType() != Material.STATIONARY_LAVA
                && player.getLocation().getBlock().getType() != Material.WATER
                && player.getLocation().getBlock().getType() != Material.LAVA && player.getVehicle() == null
                && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }

    // TODO Fix lifesteal damage calculation
    private double calculateDamage(double initialDamage, EntityDamageByEntityEvent event) {
        EventData data = EventData.fromEvent(event);

        double damage = initialDamage * data.getAdditiveDamage() * data.getMultiplicativeDamage()
                * data.getReductionAmount() - data.getAbsoluteReductionAmount();

        if (removeCriticalDamage.contains(event)) {
            damage *= .667;
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (player.getInventory().getLeggings() != null) {
                if (player.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS) {
                    if (!CustomEnchantManager.getInstance().getItemEnchants(player.getInventory().getLeggings())
                            .isEmpty()) {
                        // TODO This is the only way to try to make leather pants to iron in 1.8.8
                        // spigot. I need to upgrade to 1.9+ spigot to fix this. This heavily changes
                        // the damage output
                        damage *= 0.871;
                    }
                }
            }
        }

        if (damage <= 0)
            damage = 1;

        return damage;
    }
}
