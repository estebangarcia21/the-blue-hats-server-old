package me.stevemmmmm.thehypixelpit.managers.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class BowManager implements Listener {
    private static BowManager instance;

    private final HashMap<Arrow, PlayerInventory> data = new HashMap<>();

    public static BowManager getInstance() {
        if (instance == null) instance = new BowManager();

        return instance;
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            if (((Arrow) event.getProjectile()).getShooter() instanceof Player) {
                data.put((Arrow) event.getProjectile(), ((Player) ((Arrow) event.getProjectile()).getShooter()).getInventory());
            }
        }
    }

    public void registerArrow(Arrow arrow, Player player) {
        data.put(arrow, player.getInventory());
    }

    public ItemStack getBowFromArrow(Arrow arrow) {
        for (Arrow arr : data.keySet()) {
            if (arr.equals(arrow)) {
                return data.get(arr).getItemInHand();
            }
        }

        return new ItemStack(Material.BOW);
    }

    public ItemStack getLeggingsFromArrow(Arrow arrow) {
        return data.get(arrow).getLeggings();
    }
}
