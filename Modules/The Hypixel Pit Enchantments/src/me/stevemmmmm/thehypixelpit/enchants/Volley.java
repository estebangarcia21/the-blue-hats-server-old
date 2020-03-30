package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Volley extends CustomEnchant {
    private HashMap<Arrow, Integer> volleyTasks = new HashMap<>();
    private HashMap<Arrow, Integer> arrowCount = new HashMap<>();

    //Supported volley enchants
    private Robinhood robinhood = new Robinhood();
    private HashMap<Arrow, Integer> robinhoodArrowTasks = new HashMap<>();

    private DevilChicks devilChicks = new DevilChicks();

    private ArrayList<Arrow> shotArrows = new ArrayList<>();

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            if (((Arrow) event.getProjectile()).getShooter() instanceof Player) {
                Player player = (Player) ((Arrow) event.getProjectile()).getShooter();

                executeEnchant(player.getInventory().getItemInHand(), event);
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow bufferArrow = null;

            for (Arrow shotArrow : shotArrows) {
                if (shotArrow == event.getEntity()) {
                    bufferArrow = shotArrow;

                    if (event.getEntity().getShooter() instanceof Player) {
                        Player player = (Player) event.getEntity().getShooter();

                        ItemStack sender = player.getInventory().getItemInHand();

                        if (itemHasEnchant(sender, 1, devilChicks)) {
                            devilChicks.spawnDevils(shotArrow.getLocation(), 1);
                        } else if (itemHasEnchant(sender, 2, devilChicks)) {
                            devilChicks.spawnDevils(shotArrow.getLocation(), 2);
                        } else if (itemHasEnchant(sender, 3, devilChicks)) {
                            devilChicks.spawnDevils(shotArrow.getLocation(), 3);
                        }
                    }
                }
            }

            if (bufferArrow != null) shotArrows.remove(bufferArrow);
        }
    }

    @Override
    public boolean executeEnchant(ItemStack sender, Object executedEvent) {
        EntityShootBowEvent event = (EntityShootBowEvent) executedEvent;

        Arrow arrow = (Arrow) event.getProjectile();
        Player player = (Player) arrow.getShooter();

        if (itemHasEnchant(sender, 1, this)) {
            Vector originalVelocity = arrow.getVelocity();

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, () -> volleyTasks.put(arrow,Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
                Arrow volleyArrow = player.launchProjectile(Arrow.class);

                volleyArrow.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(originalVelocity.length()));

                procSupportedEnchants(sender, volleyArrow, player);

                shotArrows.add(volleyArrow);

                arrowCount.put(arrow, arrowCount.getOrDefault(arrow, 1) + 1);
                if (arrowCount.get(arrow) > 2) {
                    Bukkit.getServer().getScheduler().cancelTask(volleyTasks.get(arrow));
                    volleyTasks.remove(arrow);
                    arrowCount.remove(arrow);
                }
            }, 0L, 2L)), 3L);
        }

        if (itemHasEnchant(sender, 2, this)) {
            Vector originalVelocity = arrow.getVelocity();

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, () -> volleyTasks.put(arrow,Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
                Arrow volleyArrow = player.launchProjectile(Arrow.class);

                volleyArrow.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(originalVelocity.length()));

                procSupportedEnchants(sender, volleyArrow, player);

                arrowCount.put(arrow, arrowCount.getOrDefault(arrow, 1) + 1);
                if (arrowCount.get(arrow) > 3) {
                    Bukkit.getServer().getScheduler().cancelTask(volleyTasks.get(arrow));
                    volleyTasks.remove(arrow);
                    arrowCount.remove(arrow);
                }
            }, 0L, 2L)), 3L);
        }

        if (itemHasEnchant(sender, 3, this)) {
            Vector originalVelocity = arrow.getVelocity();

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, () -> volleyTasks.put(arrow,Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
                Arrow volleyArrow = player.launchProjectile(Arrow.class);

                volleyArrow.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(originalVelocity.length()));

                procSupportedEnchants(sender, volleyArrow, player);

                arrowCount.put(arrow, arrowCount.getOrDefault(arrow, 1) + 1);
                if (arrowCount.get(arrow) > 4) {
                    Bukkit.getServer().getScheduler().cancelTask(volleyTasks.get(arrow));
                    volleyTasks.remove(arrow);
                    arrowCount.remove(arrow);
                }
            }, 0L, 2L)), 3L);
        }

        return false;
    }

    private void procSupportedEnchants(ItemStack sender, Arrow volleyArrow, Player player) {
        if (itemHasEnchant(sender, robinhood)) robinhood.homeArrows(robinhoodArrowTasks, volleyArrow, player);
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
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }
}
