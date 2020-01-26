package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.core.Main;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Robinhood extends CustomEnchant {
    private HashMap<Arrow, Integer> arrowTasks = new HashMap<>();

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getProjectile();
            Player player = (Player) event.getEntity();

            triggerEnchant(player.getInventory().getItemInHand(), arrow, player);
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

                        if (itemHasEnchant(player.getInventory().getItemInHand(), 1, this)) {
                            event.setDamage(event.getDamage() * .40f);
                        }

                        if (itemHasEnchant(player.getInventory().getItemInHand(), 2, this)) {
                            event.setDamage(event.getDamage() * .50f);
                        }

                        if (itemHasEnchant(player.getInventory().getItemInHand(), 3, this)) {
                            event.setDamage(event.getDamage() * .60f);
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
    public void triggerEnchant(ItemStack sender, Object... args) {
        Arrow arrow = (Arrow) args[0];
        Player player = (Player) args[1];

        if (itemHasEnchant(sender, 1, this)) {
            homeArrows(arrowTasks, arrow, player);
        }

        if (itemHasEnchant(sender, 2, this)) {
            homeArrows(arrowTasks, arrow, player);
        }

        if (itemHasEnchant(sender, 3, this)) {
            homeArrows(arrowTasks, arrow, player);
        }
    }

    public void homeArrows(HashMap<Arrow, Integer> arrowTasks, Arrow arrow, Player player) {
        arrowTasks.put(arrow, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
            List<Entity> closestEntities = arrow.getNearbyEntities(16, 16, 16);
            Player closestPlayer = null;

            for (Entity entity : closestEntities) {
                if (entity instanceof Player) {
                    if (entity != player) {
                        closestPlayer = (Player) entity;
                        break;
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
