package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.BowManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.LoreBuilder;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Volley extends CustomEnchant {
    private LevelVariable<Integer> arrows = new LevelVariable<>(2, 3, 4);

    private HashMap<Arrow, Integer> volleyTasks = new HashMap<>();
    private HashMap<Arrow, Integer> arrowCount = new HashMap<>();

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            Arrow eventArrow = (Arrow) event.getProjectile();

            for (Arrow arrow : volleyTasks.keySet()) {
                if (eventArrow.getShooter().equals(arrow.getShooter())) {
                    return;
                }
            }

            if (((Arrow) event.getProjectile()).getShooter() instanceof Player) {
                Player player = (Player) ((Arrow) event.getProjectile()).getShooter();

                attemptEnchantExecution(player.getInventory().getItemInHand(), event.getProjectile(), player, event);
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Arrow arrow = (Arrow) args[0];
        Player player = (Player) args[1];
        EntityShootBowEvent e = (EntityShootBowEvent) args[2];
        float force = e.getForce();

        ItemStack item = player.getInventory().getItemInHand();

        Vector originalVelocity = arrow.getVelocity();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.INSTANCE, () -> volleyTasks.put(arrow,Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
            player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
            Arrow volleyArrow = player.launchProjectile(Arrow.class);

            volleyArrow.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(originalVelocity.length()));

            EntityShootBowEvent event = new EntityShootBowEvent(player, item, volleyArrow, force);
            Main.INSTANCE.getServer().getPluginManager().callEvent(event);

            BowManager.getInstance().registerArrow(volleyArrow, player);

            arrowCount.put(arrow, arrowCount.getOrDefault(arrow, 1) + 1);
            if (arrowCount.get(arrow) > arrows.at(level)) {
                Bukkit.getServer().getScheduler().cancelTask(volleyTasks.get(arrow));
                volleyTasks.remove(arrow);
                arrowCount.remove(arrow);
            }
        }, 0L, 2L)), 3L);
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
        return new LoreBuilder()
                .addVariable("3", "4", "5")
                .write("Shoot ").setColor(ChatColor.WHITE).writeVariable(0, level).write(" arrows ").resetColor().write("at once")
                .build();
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
