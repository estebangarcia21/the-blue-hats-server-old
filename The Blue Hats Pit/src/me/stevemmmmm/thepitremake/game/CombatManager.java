package me.stevemmmmm.thepitremake.game;

import me.stevemmmmm.thepitremake.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class CombatManager implements Listener {
    private static CombatManager instance;

    private final HashMap<UUID, Integer> combatTasks = new HashMap<>();
    private final HashMap<UUID, Integer> combatTime = new HashMap<>();

    private CombatManager() {
    }

    public static CombatManager getInstance() {
        if (instance == null)
            instance = new CombatManager();

        return instance;
    }

    @EventHandler
    public void initOnPlayerJoin(PlayerJoinEvent event) {
        combatTime.put(event.getPlayer().getUniqueId(), 0);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            combatTag((Player) event.getDamager());
            combatTag((Player) event.getEntity());
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                combatTag(((Player) ((Arrow) event.getDamager()).getShooter()));
                combatTag((Player) event.getEntity());
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        removePlayerFromCombat(event.getEntity());
    }

    public boolean playerIsInCombat(Player player) {
        return combatTime.getOrDefault(player.getUniqueId(), 0) != 0;
    }

    public void combatTag(Player player) {
        if (RegionManager.getInstance().playerIsInRegion(player, RegionManager.RegionType.SPAWN))
            return;

        combatTime.put(player.getUniqueId(), calculateCombatTime(player));

        if (!combatTasks.containsKey(player.getUniqueId()))
            combatTasks.put(player.getUniqueId(),
                    Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
                        if (combatTime.get(player.getUniqueId()) == 0) {
                            Bukkit.getServer().getScheduler().cancelTask(combatTasks.get(player.getUniqueId()));
                            combatTasks.remove(player.getUniqueId());
                            return;
                        }

                        combatTime.put(player.getUniqueId(), combatTime.get(player.getUniqueId()) - 1);
                    }, 0L, 20L));
    }

    public int getCombatTime(Player player) {
        return combatTime.get(player.getUniqueId());
    }

    private int calculateCombatTime(Player player) {
        int time = 15;

        return time;
    }

    private void removePlayerFromCombat(Player player) {
        if (playerIsInCombat(player)) {
            Bukkit.getServer().getScheduler().cancelTask(combatTasks.get(player.getUniqueId()));
            combatTime.put(player.getUniqueId(), 0);
            combatTasks.remove(player.getUniqueId());
        }
    }
}
