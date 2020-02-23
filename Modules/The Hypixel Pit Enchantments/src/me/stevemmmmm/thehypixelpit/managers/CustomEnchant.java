package me.stevemmmmm.thehypixelpit.managers;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.EnvironmentalEnchant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public abstract class CustomEnchant implements Listener {
    private HashMap<UUID, Boolean> playerIsOnCooldown = new HashMap<>();
    private HashMap<UUID, Long> cooldownTimerTimes = new HashMap<>();
    private HashMap<UUID, Integer> cooldownTasks = new HashMap<>();

    private HashMap<UUID, Integer> hitMappings = new HashMap<>();
    private HashMap<UUID, Long> cooldownTimesHitTimer = new HashMap<>();
    private HashMap<UUID, Integer> cooldownResetTasksHitTimer = new HashMap<>();

    public abstract String getName();

    public abstract String getEnchantReferenceName();

    public abstract ArrayList<String> getDescription(int level);

    public abstract boolean isTierTwoEnchant();

    public abstract boolean isRareEnchant();

    public void startCooldown(Player player, long ticks, boolean isSeconds) {
        if (isSeconds) ticks *= 20;

        if (!cooldownTimerTimes.containsKey(player.getUniqueId())) cooldownTimerTimes.put(player.getUniqueId(), ticks);
        if (!playerIsOnCooldown.containsKey(player.getUniqueId())) playerIsOnCooldown.put(player.getUniqueId(), false);

        if (!cooldownTasks.containsKey(player.getUniqueId())) {
            cooldownTimerTimes.put(player.getUniqueId(), ticks);

            cooldownTasks.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                if (cooldownTimerTimes.get(player.getUniqueId()) <= 0f) {
                    playerIsOnCooldown.put(player.getUniqueId(), false);
                    cooldownTimerTimes.put(player.getUniqueId(), 0L);
                    Bukkit.getServer().getScheduler().cancelTask(cooldownTasks.get(player.getUniqueId()));
                    cooldownTasks.remove(player.getUniqueId());
                    return;
                }

                playerIsOnCooldown.put(player.getUniqueId(), true);
                cooldownTimerTimes.put(player.getUniqueId(), cooldownTimerTimes.get(player.getUniqueId()) - 1);
            }, 0L, 1L));
        }
    }

    public boolean percentChance(int percent) {
        return percent >= ThreadLocalRandom.current().nextInt(0, 101);
    }

    public boolean itemHasEnchant(ItemStack item, CustomEnchant enchant) {
        if (item == null || item.getType() == Material.AIR) return false;
        if (item.getItemMeta().getLore() == null) return false;

        List<String> lore = item.getItemMeta().getLore();

        String appendRare = "";

        if (enchant.isRareEnchant()) appendRare = ChatColor.LIGHT_PURPLE + "RARE! ";

        if (lore.contains(appendRare + ChatColor.BLUE + enchant.getName())) return true;

        for (int i = 2; i <= 3; i++) {
            if (lore.contains(appendRare + ChatColor.BLUE + enchant.getName() + " " + CustomEnchantManager.getInstance().convertToRomanNumeral(i))) return true;
        }

        return false;
    }

    public boolean itemHasEnchant(ItemStack item, int level, CustomEnchant enchant) {
        if (item == null || item.getType() == Material.AIR) return false;
        if (item.getItemMeta().getLore() == null) return false;

        List<String> lore = item.getItemMeta().getLore();

        String appendRare = "";

        if (enchant.isRareEnchant()) appendRare = ChatColor.LIGHT_PURPLE + "RARE! ";

        if (level == 1) {
            return lore.contains(appendRare + ChatColor.BLUE + enchant.getName());
        }

        return lore.contains(appendRare + ChatColor.BLUE + enchant.getName() + " " + CustomEnchantManager.getInstance().convertToRomanNumeral(level));
    }

    public boolean isOnCooldown(Player player) {
        if (!playerIsOnCooldown.containsKey(player.getUniqueId())) playerIsOnCooldown.put(player.getUniqueId(), false);

        return !playerIsOnCooldown.get(player.getUniqueId());
    }

    public long getCooldownTime(Player player) {
        if (!cooldownTimerTimes.containsKey(player.getUniqueId())) cooldownTimerTimes.put(player.getUniqueId(), 0L);

        return cooldownTimerTimes.get(player.getUniqueId()) / 20;
    }

    public void setCooldownTime(Player player, long ticks, boolean isSeconds) {
        if (isSeconds) ticks *= 20;

        if (!cooldownTimerTimes.containsKey(player.getUniqueId())) cooldownTimerTimes.put(player.getUniqueId(), 0L);

        cooldownTimerTimes.put(player.getUniqueId(), Math.max(ticks, 0));
    }

    public void updateHitCount(Player player) {
        if (!hitMappings.containsKey(player.getUniqueId())) {
            hitMappings.put(player.getUniqueId(), 0);
            cooldownTimesHitTimer.put(player.getUniqueId(), 0L);
        }

        cooldownTimesHitTimer.put(player.getUniqueId(), 0L);
        hitMappings.put(player.getUniqueId(), hitMappings.get(player.getUniqueId()) + 1);
        startHitResetTimer(player);
    }

    public void updateHitCount(Player player, int amount) {
        if (!hitMappings.containsKey(player.getUniqueId())) hitMappings.put(player.getUniqueId(), 1);

        hitMappings.put(player.getUniqueId(), hitMappings.get(player.getUniqueId()) + amount);
    }

    public boolean hasRequiredHits(Player player, int hitAmount) {
        if (!hitMappings.containsKey(player.getUniqueId())) hitMappings.put(player.getUniqueId(), 1);

        if (hitMappings.get(player.getUniqueId()) >= hitAmount) {
            hitMappings.put(player.getUniqueId(), 0);
            return true;
        } else return false;
    }

    public void startHitResetTimer(Player player) {
        if (!cooldownTimesHitTimer.containsKey(player.getUniqueId())) cooldownTimesHitTimer.put(player.getUniqueId(), 0L);

        if (!cooldownResetTasksHitTimer.containsKey(player.getUniqueId())) {
            cooldownResetTasksHitTimer.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                if (cooldownTimesHitTimer.get(player.getUniqueId()) >= 3) {
                    hitMappings.put(player.getUniqueId(), 0);
                    cooldownTimesHitTimer.put(player.getUniqueId(), 0L);
                    Bukkit.getServer().getScheduler().cancelTask(cooldownResetTasksHitTimer.get(player.getUniqueId()));
                    cooldownResetTasksHitTimer.remove(player.getUniqueId());
                    return;
                }

                cooldownTimesHitTimer.put(player.getUniqueId(), cooldownTimesHitTimer.get(player.getUniqueId()) + 1);
            }, 0L, 20L));
        } else {
            cooldownTimesHitTimer.put(player.getUniqueId(), 0L);
        }
    }

    public boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0 && !((Entity) player).isOnGround() && player.getLocation().getBlock().getType() != Material.LADDER && player.getLocation().getBlock().getType() != Material.VINE && player.getLocation().getBlock().getType() != Material.STATIONARY_WATER && player.getLocation().getBlock().getType() != Material.STATIONARY_LAVA && player.getLocation().getBlock().getType() != Material.WATER && player.getLocation().getBlock().getType() != Material.LAVA && player.getVehicle() == null && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }
}
