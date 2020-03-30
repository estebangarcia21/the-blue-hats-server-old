package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.CalculationMode;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.*;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Robinhood extends CustomEnchant {
    private LevelVariable<Float> damageReduction = new LevelVariable<>(.4f, .5f, .6f);

    private HashMap<Arrow, Integer> arrowTasks = new HashMap<>();

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getProjectile();
            Player player = (Player) event.getEntity();

            tryExecutingEnchant(player.getInventory().getItemInHand(), arrow, player);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Arrow removal = null;

            for (Map.Entry<Arrow, Integer> entry : arrowTasks.entrySet()) {
                if (entry.getKey() == event.getDamager()) {
                    Arrow arrow = (Arrow) event.getDamager();

                    if (arrow.getShooter() instanceof Player) {
                        Player player = (Player) arrow.getShooter();

                        if (itemHasEnchant(player.getInventory().getItemInHand(), this)) {
                            DamageManager.getInstance().addDamage(event, damageReduction.at(getEnchantLevel(player.getInventory().getItemInHand(), this)), CalculationMode.ADDITIVE);
                        }
                    }

                    //TODO Check the robin levels and implement damage reductions

                    Bukkit.getServer().getScheduler().cancelTask(arrowTasks.get(arrow));
                    removal = arrow;
                }
            }

            if (removal != null) arrowTasks.remove(removal);
        }
    }

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow removal = null;

            for (Map.Entry<Arrow, Integer> entry : arrowTasks.entrySet()) {
                if (entry.getKey() == event.getEntity()) {
                    Arrow arrow = (Arrow) event.getEntity();

                    Bukkit.getServer().getScheduler().cancelTask(arrowTasks.get(arrow));
                    removal = arrow;
                }
            }

            if (removal != null) arrowTasks.remove(removal);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Arrow arrow = (Arrow) args[0];
        Player player = (Player) args[1];

        //TODO Volley arrows damage not reduced

        arrowTasks.put(arrow, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
            List<Entity> closestEntities = player.getNearbyEntities(16, 16, 16);
            List<Player> closestPlayers = new ArrayList<>();

            for (Entity entity : closestEntities) {
                if (entity instanceof Player) {
                    closestPlayers.add((Player) entity);
                }
            }

            if (closestPlayers.isEmpty()) closestEntities = arrow.getNearbyEntities(16, 16 ,16);

            Player closestPlayer = null;

            for (Entity entity : closestEntities) {
                if (entity instanceof Player) {
                    if (entity != player) {
                        if (closestPlayer == null) {
                            closestPlayer = (Player) entity;
                            continue;
                        }

                        if (player.getLocation().toVector().distance(entity.getLocation().toVector()) < player.getLocation().toVector().distance(closestPlayer.getLocation().toVector())) {
                            closestPlayer = (Player) entity;
                        }
                    }
                }
            }

            if (closestPlayer == null) return;

            Location arrowLocation = arrow.getLocation();
            Location closestPlayerLoc = closestPlayer.getLocation();

            Vector arrowVector = arrowLocation.toVector();
            Vector closestPlayerVector = closestPlayerLoc.toVector();
            closestPlayerVector.setY(closestPlayerVector.getY() + 2);

            Vector direction = arrowVector.subtract(closestPlayerVector).normalize().multiply(-1);
            arrow.setVelocity(direction);
        }, 0L, 1L));
    }

    @Override
    public String getName() {
        return "Robinhood";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Robinhood";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String dmgAmount = level == 1 ? "40%" : level == 2 ? "50%" : level == 3 ? "60%" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "All your shots are homing but deal");
            add(ChatColor.RED + dmgAmount + ChatColor.GRAY + " damage");
        }};
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }
}
