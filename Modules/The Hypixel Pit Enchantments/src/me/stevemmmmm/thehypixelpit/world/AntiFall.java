package me.stevemmmmm.thehypixelpit.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class AntiFall implements Listener {

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.FALL);
    }
}
