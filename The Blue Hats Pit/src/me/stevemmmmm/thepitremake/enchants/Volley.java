package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.core.Main;
import me.stevemmmmm.thepitremake.game.RegionManager;
import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Volley extends CustomEnchant {
    private final EnchantProperty<Integer> arrows = new EnchantProperty<>(2, 3, 4);

    private final HashMap<Arrow, Integer> volleyTasks = new HashMap<>();
    private final HashMap<Arrow, Integer> arrowCount = new HashMap<>();

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

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.INSTANCE, () -> volleyTasks.put(arrow,
                Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
                    if (!RegionManager.getInstance().playerIsInRegion(player, RegionManager.RegionType.SPAWN)) {
                        player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
                        Arrow volleyArrow = player.launchProjectile(Arrow.class);

                        volleyArrow.setVelocity(
                                player.getEyeLocation().getDirection().normalize().multiply(originalVelocity.length()));

                        EntityShootBowEvent event = new EntityShootBowEvent(player, item, volleyArrow, force);
                        Main.INSTANCE.getServer().getPluginManager().callEvent(event);

                        BowManager.getInstance().registerArrow(volleyArrow, player);

                        arrowCount.put(arrow, arrowCount.getOrDefault(arrow, 1) + 1);
                        if (arrowCount.get(arrow) > arrows.getValueAtLevel(level)) {
                            Bukkit.getServer().getScheduler().cancelTask(volleyTasks.get(arrow));
                            volleyTasks.remove(arrow);
                            arrowCount.remove(arrow);
                        }
                    }
                }, 0L, 2)), 2L);
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
        return new LoreBuilder().declareVariable("3", "4", "5").write("Shoot ").setColor(ChatColor.WHITE)
                .writeVariable(0, level).write(" arrows ").resetColor().write("at once").build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.BOW };
    }
}
