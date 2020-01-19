package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Volley extends CustomEnchant {
    private HashMap<Arrow, Integer> volleyTasks = new HashMap<>();
    private HashMap<Arrow, Integer> arrowCount = new HashMap<>();

    private Robinhood robinhood = new Robinhood();
    private HashMap<Arrow, Integer> arrowTasks = new HashMap<>();

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            if (((Arrow) event.getProjectile()).getShooter() instanceof Player) {
                Player player = (Player) ((Arrow) event.getProjectile()).getShooter();

                triggerEnchant(player.getInventory().getItemInHand(), player, event.getProjectile());
            }
        }
    }

    @Override
    public void triggerEnchant(ItemStack sender, Object... args) {
        Player player = (Player) args[0];
        Arrow arrow = (Arrow) args[1];

        if (itemHasEnchant(sender, 1, this)) {
            Vector originalVelocity = arrow.getVelocity();

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, () -> {
                volleyTasks.put(arrow,Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                    player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
                    Arrow volleyArrow = player.launchProjectile(Arrow.class);

                    volleyArrow.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(originalVelocity.length()));
                    if (itemHasEnchant(sender, new Robinhood())) robinhood.homeArrows(arrowTasks, volleyArrow, player);

                    arrowCount.put(arrow, arrowCount.getOrDefault(arrow, 1) + 1);
                    if (arrowCount.get(arrow) > 2) {
                        Bukkit.getServer().getScheduler().cancelTask(volleyTasks.get(arrow));
                        volleyTasks.remove(arrow);
                        arrowCount.remove(arrow);
                        return;
                    }
                }, 0L, 3L));
            }, 3L);
        }

        if (itemHasEnchant(sender, 2, this)) {
            Vector originalVelocity = arrow.getVelocity();

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, () -> {
                volleyTasks.put(arrow,Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                    player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
                    Arrow volleyArrow = player.launchProjectile(Arrow.class);

                    volleyArrow.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(originalVelocity.length()));
                    if (itemHasEnchant(sender, new Robinhood())) robinhood.homeArrows(arrowTasks, volleyArrow, player);

                    arrowCount.put(arrow, arrowCount.getOrDefault(arrow, 1) + 1);
                    if (arrowCount.get(arrow) > 3) {
                        Bukkit.getServer().getScheduler().cancelTask(volleyTasks.get(arrow));
                        volleyTasks.remove(arrow);
                        arrowCount.remove(arrow);
                        return;
                    }
                }, 0L, 3L));
            }, 3L);
        }

        if (itemHasEnchant(sender, 3, this)) {
            Vector originalVelocity = arrow.getVelocity();

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, () -> {
                volleyTasks.put(arrow,Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                    player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
                    Arrow volleyArrow = player.launchProjectile(Arrow.class);

                    volleyArrow.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(originalVelocity.length()));
                    if (itemHasEnchant(sender, new Robinhood())) robinhood.homeArrows(arrowTasks, volleyArrow, player);

                    arrowCount.put(arrow, arrowCount.getOrDefault(arrow, 1) + 1);
                    if (arrowCount.get(arrow) > 4) {
                        Bukkit.getServer().getScheduler().cancelTask(volleyTasks.get(arrow));
                        volleyTasks.remove(arrow);
                        arrowCount.remove(arrow);
                        return;
                    }
                }, 0L, 3L));
            }, 3L);
        }
    }

    @Override
    public String getName() {
        return "Volley";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Volley";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String arrow = level == 1 ? "3" : level == 2 ? "4" : level == 3 ? "5" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Shoot " + ChatColor.WHITE + arrow + " arrows " + ChatColor.GRAY + "at once");
        }};
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }
}
