package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.LoreBuilder;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BulletTime extends CustomEnchant {
    private LevelVariable<Integer> healingAmount = new LevelVariable<>(0, 2, 3);
    private double range = 3.25;

    private HashMap<UUID, Integer> bulletTimeTasks = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        bulletTimeTasks.put(event.getPlayer().getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
            for (Entity entity : event.getPlayer().getNearbyEntities(range, 1, range)) {
                if (entity instanceof Arrow) {
                    Arrow arrow = (Arrow) entity;

                    if (event.getPlayer().isBlocking()) {
                        attemptEnchantExecution(event.getPlayer().getInventory().getItemInHand(), event.getPlayer(), arrow);
                        break;
                    }
                }
            }
        }, 0L, 1L));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getServer().getScheduler().cancelTask(bulletTimeTasks.get(event.getPlayer().getUniqueId()));
        bulletTimeTasks.remove(event.getPlayer().getUniqueId());
    }

//    @EventHandler
//    public void onBowShoot(EntityShootBowEvent event) {
//        if (event.getProjectile() instanceof Arrow) {
//            Arrow arrow = (Arrow) event.getProjectile();
//
//            if (arrow.getShooter() instanceof Player) {
//                bulletTimeTasks.put(arrow, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
//                    for (Entity entity : arrow.getNearbyEntities(range, 1, range)) {
//                        if (entity instanceof Player) {
//                            Player player = (Player) entity;
//                            if (player.isBlocking()) {
//                                attemptEnchantExecution(player.getInventory().getItemInHand(), player, arrow);
//                                break;
//                            }
//                        }
//                    }
//                }, 0L, 1L));
//            }
//        }
//    }

//    @EventHandler
//    public void onArrowLand(ProjectileHitEvent event) {
//        if (event.getEntity() instanceof Arrow) {
//            Arrow arrow = (Arrow) event.getEntity();
//
//            if (bulletTimeTasks.containsKey(arrow)) {
//                Bukkit.getServer().getScheduler().cancelTask(bulletTimeTasks.get(arrow));
//                bulletTimeTasks.remove(arrow);
//            }
//        }
//    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player hitPlayer = (Player) args[0];
        Arrow arrow = (Arrow) args[1];

        if (hitPlayer.isBlocking()) {
            hitPlayer.getWorld().playSound(hitPlayer.getLocation(), Sound.FIZZ, 1f, 1.5f);
            arrow.getWorld().playEffect(arrow.getLocation(), Effect.EXPLOSION, 0, 30);

            for (Entity entity : hitPlayer.getNearbyEntities(range, 1, range)) {
                if (entity instanceof Arrow) {
                    Arrow nearby = (Arrow) entity;

                    nearby.remove();
                    hitPlayer.setHealth(Math.min(hitPlayer.getHealth() + healingAmount.at(level), hitPlayer.getMaxHealth()));
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Bullet Time";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Bullettime";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("", "1❤", "1.5❤")
                .setWriteCondition(level == 1)
                .setColor(ChatColor.GRAY).write("Blocking destroys arrows that").nextLine()
                .setColor(ChatColor.GRAY).write("hit you")
                .resetCondition()
                .setWriteCondition(level != 1)
                .setColor(ChatColor.GRAY).write("Blocking destroys arrows hitting").nextLine()
                .setColor(ChatColor.GRAY).write("you. Destroying arrows this way").nextLine()
                .setColor(ChatColor.GRAY).write("heals ").setColor(ChatColor.RED).writeVariable(0, level)
                .build();
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }
}
