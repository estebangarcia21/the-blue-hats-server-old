package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.core.Main;
import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import me.stevemmmmm.thepitremake.managers.enchants.LevelVariable;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class DoubleJump extends CustomEnchant {
    private final LevelVariable<Integer> cooldownTime = new LevelVariable<>(20, 10, 5);

    private final HashMap<UUID, Integer> playerHasDoubleJumps = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        playerHasDoubleJumps.put(event.getPlayer().getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
            if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) {
                player.setAllowFlight(true);
                return;
            }

            if (itemHasEnchant(player.getInventory().getLeggings(), this)) {
                player.setAllowFlight(isNotOnCooldown(player));
            } else {
                player.setAllowFlight(false);
            }
        }, 0L, 1L));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.getServer().getScheduler().cancelTask(playerHasDoubleJumps.get(event.getPlayer().getUniqueId()));
        playerHasDoubleJumps.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onFlightAttempt(PlayerToggleFlightEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) event.setCancelled(true);

        attemptEnchantExecution(event.getPlayer().getInventory().getLeggings(), event);
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        PlayerToggleFlightEvent event = (PlayerToggleFlightEvent) args[0];

        Vector normalizedVelocity = event.getPlayer().getEyeLocation().getDirection().normalize();

        if (isNotOnCooldown(event.getPlayer())) {
            event.getPlayer().setVelocity(new Vector(normalizedVelocity.getX() * 3, 1.5, normalizedVelocity.getZ() * 3));
        }

        startCooldown(event.getPlayer(), cooldownTime.at(level), true);
    }

    @Override
    public String getName() {
        return "Double-jump";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Doublejump";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("20s", "10s", "5s")
                .write("You can double-jump. (").writeVariable(0, level).nextLine()
                .write("cooldown)")
                .build();
    }

    @Override
    public boolean isRemovedFromPassiveWorld() {
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
    public Material getEnchantItemType() {
        return Material.LEATHER_LEGGINGS;
    }
}
