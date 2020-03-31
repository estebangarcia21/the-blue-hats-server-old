package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.ArrowManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
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

    //Supported volley enchants
    private Robinhood robinhood = new Robinhood();

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            if (((Arrow) event.getProjectile()).getShooter() instanceof Player) {
                Player player = (Player) ((Arrow) event.getProjectile()).getShooter();

                tryExecutingEnchant(player.getInventory().getItemInHand(), event.getProjectile(), player);
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Arrow arrow = (Arrow) args[0];
        Player player = (Player) args[1];

        ItemStack item = player.getInventory().getItemInHand();

        Vector originalVelocity = arrow.getVelocity();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, () -> volleyTasks.put(arrow,Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
            player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
            Arrow volleyArrow = player.launchProjectile(Arrow.class);

            volleyArrow.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(originalVelocity.length()));

            ArrowManager.getInstance().registerArrow(volleyArrow, item);

            for (CustomEnchant customEnchant : CustomEnchantManager.getInstance().getItemEnchants(item).keySet()) {
                if (customEnchant instanceof Robinhood) {
                    robinhood.tryExecutingEnchant(item, volleyArrow, player);
                }
            }

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
