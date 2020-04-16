package me.stevemmmmm.thehypixelpit.world;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class AntiFall implements Listener {

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.FALL);
    }
}
