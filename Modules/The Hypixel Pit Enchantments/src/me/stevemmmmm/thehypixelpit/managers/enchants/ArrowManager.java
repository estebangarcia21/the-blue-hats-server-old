package me.stevemmmmm.thehypixelpit.managers.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ArrowManager implements Listener {
    private static ArrowManager instance;

    private HashMap<Arrow, ItemStack> data = new HashMap<>();

    public static ArrowManager getInstance() {
        if (instance == null) instance = new ArrowManager();

        return instance;
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            if (((Arrow) event.getProjectile()).getShooter() instanceof Player) {
                data.put((Arrow) event.getProjectile(), ((Player) ((Arrow) event.getProjectile()).getShooter()).getInventory().getItemInHand());
            }
        }
    }

    public void registerArrow(Arrow arrow, ItemStack object) {
        data.put(arrow, object);
    }

    public ItemStack getItemStackFromArrow(Arrow arrow) {
        return data.get(arrow);
    }
}
