package me.stevemmmmm.thehypixelpit.managers.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class DamageManager implements Listener {
    private static DamageManager instance;

    private HashMap<EntityDamageByEntityEvent, Float> damageBuffer = new HashMap<>();

    private DamageManager() { }

    public static DamageManager getInstance() {
        if (instance == null) instance = new DamageManager();

        return instance;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            event.setDamage(calculateFinalEventDamage(event));
        }

        //TODO Implement arrows
    }

    public Player getDamagerFromDamageEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            return (Player) event.getDamager();
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {

                return (Player) ((Arrow) event.getDamager()).getShooter();
            }
        }

        return null;
    }

    public void addDamageToEvent(EntityDamageByEntityEvent event, CalculationMode mode, float value) {
        if (!damageBuffer.containsKey(event)) {
            damageBuffer.put(event, 0f);
        }


    }

    public boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0 && !((Entity) player).isOnGround() && player.getLocation().getBlock().getType() != Material.LADDER && player.getLocation().getBlock().getType() != Material.VINE && player.getLocation().getBlock().getType() != Material.STATIONARY_WATER && player.getLocation().getBlock().getType() != Material.STATIONARY_LAVA && player.getLocation().getBlock().getType() != Material.WATER && player.getLocation().getBlock().getType() != Material.LAVA && player.getVehicle() == null && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }

    private double calculateFinalEventDamage(EntityDamageByEntityEvent event) {
        return calculateDamage(event, (Player) event.getDamager()) * calculateDamageReduction(event, (Player) event.getDamager());
    }

    public double calculateDamage(EntityDamageByEntityEvent event, Player player) {
        double percentDamageIncrease = 1;
        double multiplier = 1;

        ItemStack attackItem = player.getItemInHand();
        ItemStack pants = player.getInventory().getLeggings();

        if (attackItem.getType() != Material.AIR) {
            if (CustomEnchantManager.getInstance().getItemEnchants(attackItem) != null) {
                for (CustomEnchant enchant : CustomEnchantManager.getInstance().getItemEnchants(attackItem).keySet()) {
                    if (enchant instanceof DamageEnchant) {
                        if (((DamageEnchant) enchant).getDamageCalculationMode() == CalculationMode.ADDITIVE) {
                            if (enchant.executeEnchant(attackItem, event)) {
                                percentDamageIncrease += ((DamageEnchant) enchant).getPercentDamageIncreasePerLevel()[CustomEnchantManager.getInstance().getItemEnchants(attackItem).get(enchant) - 1];
                            }
                        }

                        if (((DamageEnchant) enchant).getDamageCalculationMode() == CalculationMode.MULTIPLICATIVE) {
                            if (enchant.executeEnchant(attackItem, event)) {
                                multiplier += ((DamageEnchant) enchant).getPercentDamageIncreasePerLevel()[CustomEnchantManager.getInstance().getItemEnchants(attackItem).get(enchant) - 1];
                            }
                        }
                    }
                }
            }
        }

        if (pants != null) {
            if (CustomEnchantManager.getInstance().getItemEnchants(pants) != null) {
                for (CustomEnchant enchant : CustomEnchantManager.getInstance().getItemEnchants(pants).keySet()) {
                    if (enchant instanceof DamageEnchant) {
                        if (((DamageEnchant) enchant).getDamageCalculationMode() == CalculationMode.ADDITIVE) {
                            if (enchant.executeEnchant(pants, event)) {
                                try {
                                    percentDamageIncrease += ((DamageEnchant) enchant).getPercentDamageIncreasePerLevel()[CustomEnchantManager.getInstance().getItemEnchants(attackItem).get(enchant) - 1];
                                } catch (NullPointerException e) {
                                    System.out.println(enchant.getName() + "hsa caused an error!");
                                }
                            }
                        }

                        if (((DamageEnchant) enchant).getDamageCalculationMode() == CalculationMode.MULTIPLICATIVE) {
                            if (enchant.executeEnchant(pants, event)) {
                                multiplier += ((DamageEnchant) enchant).getPercentDamageIncreasePerLevel()[CustomEnchantManager.getInstance().getItemEnchants(attackItem).get(enchant) - 1];
                            }
                        }
                    }
                }
            }
        }

        return percentDamageIncrease * multiplier * event.getDamage() ;
    }

    private double calculateDamageReduction(EntityDamageByEntityEvent event, Player player) {
        //TODO Damage reduction
        if (true) {
            return 1;
        }

        double percentDamageIncrease = 0;
        double multiplier = 1;

        ItemStack pants = player.getInventory().getLeggings();

        if (pants == null) return 1;

        if (pants.getType() != Material.AIR) {
            if (CustomEnchantManager.getInstance().getItemEnchants(pants) != null) {
                for (CustomEnchant enchant : CustomEnchantManager.getInstance().getItemEnchants(pants).keySet()) {
                    if (enchant instanceof DamageReductionEnchant) {
                        if (((DamageReductionEnchant) enchant).getReductionCalculationMode() == CalculationMode.ADDITIVE) {
                            if (enchant.executeEnchant(pants, event)) {
                                double value = ((DamageEnchant) enchant).getPercentDamageIncreasePerLevel()[CustomEnchantManager.getInstance().getItemEnchants(pants).get(enchant) - 1];

                                if (percentDamageIncrease == 0) {
                                    percentDamageIncrease += value;
                                } else {
                                    percentDamageIncrease *= value;
                                }
                            }
                        }

                        if (((DamageReductionEnchant) enchant).getReductionCalculationMode() == CalculationMode.MULTIPLICATIVE) {
                            if (enchant.executeEnchant(pants, event)) {
                                multiplier += ((DamageEnchant) enchant).getPercentDamageIncreasePerLevel()[CustomEnchantManager.getInstance().getItemEnchants(pants).get(enchant) - 1];
                            }
                        }
                    }
                }
            }
        }

        return (percentDamageIncrease) * multiplier;
    }
}
