package me.stevemmmmm.thehypixelpit.game;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class RegionManager implements Listener {
    private static RegionManager instance;

    private ArrayList<Region> regions = new ArrayList<>();

    private RegionManager() {
        initSpawnRegions();
    }

    public static RegionManager getInstance() {
        if (instance == null) instance = new RegionManager();

        return instance;
    }

    private void initSpawnRegions() {
        regions.add(new Region(new Vector(35.5, 77.5, 30), new Vector(-42.5, 111.5, -45.5), RegionType.SPAWN));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity() instanceof Player) {
            Player hit = (Player) event.getEntity();

            if (playerIsInRegion(hit, RegionType.SPAWN)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            if (((Arrow) event.getProjectile()).getShooter() instanceof Player) {
                if (playerIsInRegion(((Player) ((Arrow) event.getProjectile()).getShooter()), RegionType.SPAWN)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public boolean playerIsInRegion(Player player, RegionType regionType) {
        Location location = player.getLocation();

        for (Region region : regions) {
            if (region.regionType == regionType) {
                if (location.getY() > region.lowerBound.getY() && location.getY() < region.higherBound.getY() && location.getX() < region.lowerBound.getX() && location.getX() > region.higherBound.getX() && location.getZ() < region.lowerBound.getZ() && location.getZ() > region.higherBound.getZ()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean locationIsInRegion(Location location, RegionType regionType) {
        for (Region region : regions) {
            if (region.regionType == regionType) {
                if (location.getY() > region.lowerBound.getY() && location.getY() < region.higherBound.getY() && location.getX() < region.lowerBound.getX() && location.getX() > region.higherBound.getX() && location.getZ() < region.lowerBound.getZ() && location.getZ() > region.higherBound.getZ()) {
                    return true;
                }
            }
        }

        return false;
    }

    static class Region {
        private Vector lowerBound;
        private Vector higherBound;
        private RegionType regionType;

        public Region(Vector lowerBound, Vector higherBound, RegionType regionType) {
            this.higherBound = higherBound;
            this.lowerBound = lowerBound;
            this.regionType = regionType;
        }
    }

    public enum RegionType {
        SPAWN
    }
}
