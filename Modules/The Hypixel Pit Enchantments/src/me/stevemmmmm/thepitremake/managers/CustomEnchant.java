package me.stevemmmmm.thepitremake.managers;

import me.stevemmmmm.thepitremake.commands.TogglePvPCommand;
import me.stevemmmmm.thepitremake.core.Main;
import me.stevemmmmm.thepitremake.game.RegionManager;
import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchantManager;
import me.stevemmmmm.thepitremake.managers.enchants.DamageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public abstract class CustomEnchant implements Listener {
    private final HashMap<UUID, Boolean> playersToCooldownState = new HashMap<>();
    private final HashMap<UUID, Long> cooldownTimes = new HashMap<>();
    private final HashMap<UUID, Integer> cooldownTasks = new HashMap<>();

    private final HashMap<UUID, Integer> playersToHitsWithEnchant = new HashMap<>();
    private final HashMap<UUID, Long> hitAmountResetTimes = new HashMap<>();
    private final HashMap<UUID, Integer> hitAmountResetTasks = new HashMap<>();

    public abstract void applyEnchant(int level, Object... args) ;

    public abstract String getName();

    public abstract String getEnchantReferenceName();

    public abstract ArrayList<String> getDescription(int level);

    public abstract boolean isTierTwoEnchant();

    public abstract boolean isRareEnchant();

    public abstract Material getEnchantItemType();

    public boolean attemptEnchantExecution(ItemStack source, Object... args) {
        if (TogglePvPCommand.pvpIsToggledOff) return false;

        if (itemHasEnchant(source, this)) {
            for (Object object : args) {
                if (object instanceof Player) {
                    if (DamageManager.getInstance().playerIsInCanceledEvent((Player) object)) {
                        return false;
                    }

                    if (RegionManager.getInstance().playerIsInRegion((Player) object, RegionManager.RegionType.SPAWN)) {
                        return false;
                    }
                }

                if (object instanceof Arrow) {
                    if (DamageManager.getInstance().arrowIsInCanceledEvent((Arrow) object)) {
                        return false;
                    }

                    if (RegionManager.getInstance().locationIsInRegion(((Arrow) object).getLocation(), RegionManager.RegionType.SPAWN)) {
                        return false;
                    }
                }
            }

            applyEnchant(getEnchantLevel(source, this), args);
            return true;
        }

        return false;
    }

    public boolean getAttemptedEnchantExecutionFeedback(ItemStack source) {
        return false;
    }

    public void startCooldown(Player player, long ticks, boolean isSeconds) {
        if (isSeconds) ticks *= 20;

        if (!cooldownTimes.containsKey(player.getUniqueId())) cooldownTimes.put(player.getUniqueId(), ticks);
        if (!playersToCooldownState.containsKey(player.getUniqueId())) playersToCooldownState.put(player.getUniqueId(), false);

        if (!cooldownTasks.containsKey(player.getUniqueId())) {
            cooldownTimes.put(player.getUniqueId(), ticks);

            cooldownTasks.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
                playersToCooldownState.put(player.getUniqueId(), true);

                cooldownTimes.put(player.getUniqueId(), cooldownTimes.get(player.getUniqueId()) - 1);

                if (cooldownTimes.get(player.getUniqueId()) <= 0f) {
                    playersToCooldownState.put(player.getUniqueId(), false);
                    cooldownTimes.put(player.getUniqueId(), 0L);
                    Bukkit.getServer().getScheduler().cancelTask(cooldownTasks.get(player.getUniqueId()));
                    cooldownTasks.remove(player.getUniqueId());
                }
            }, 0L, 1L));
        }
    }

    public boolean percentChance(int percent) {
        return ThreadLocalRandom.current().nextInt(0, 100) <= percent;
    }

    public static boolean itemHasEnchant(ItemStack item, CustomEnchant enchant) {
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

    public static boolean itemHasEnchant(ItemStack item, CustomEnchant enchant, int level) {
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

    public static int getEnchantLevel(ItemStack item, CustomEnchant enchant) {
        if (item == null || item.getType() == Material.AIR) return 0;
        if (item.getItemMeta().getLore() == null) return 0;

        List<String> lore = item.getItemMeta().getLore();

        String appendRare = "";

        if (enchant.isRareEnchant()) appendRare = ChatColor.LIGHT_PURPLE + "RARE! ";

        if (lore.contains(appendRare + ChatColor.BLUE + enchant.getName())) return 1;

        for (int i = 2; i <= 3; i++) {
            if (lore.contains(appendRare + ChatColor.BLUE + enchant.getName() + " " + CustomEnchantManager.getInstance().convertToRomanNumeral(i))) return i;
        }

        return 0;
    }

    public boolean isNotOnCooldown(Player player) {
        if (!playersToCooldownState.containsKey(player.getUniqueId())) playersToCooldownState.put(player.getUniqueId(), false);

        return !playersToCooldownState.get(player.getUniqueId());
    }

    public long getCooldownTime(Player player) {
        if (!cooldownTimes.containsKey(player.getUniqueId())) cooldownTimes.put(player.getUniqueId(), 0L);

        return cooldownTimes.get(player.getUniqueId()) / 20;
    }

    public void setCooldownTime(Player player, long ticks, boolean isSeconds) {
        if (isSeconds) ticks *= 20;

        if (!cooldownTimes.containsKey(player.getUniqueId())) cooldownTimes.put(player.getUniqueId(), 0L);

        cooldownTimes.put(player.getUniqueId(), Math.max(ticks, 0));
    }

    public void updateHitCount(Player player) {
        if (!playersToHitsWithEnchant.containsKey(player.getUniqueId())) {
            playersToHitsWithEnchant.put(player.getUniqueId(), 0);
            hitAmountResetTimes.put(player.getUniqueId(), 0L);
        }

        hitAmountResetTimes.put(player.getUniqueId(), 0L);
        playersToHitsWithEnchant.put(player.getUniqueId(), playersToHitsWithEnchant.get(player.getUniqueId()) + 1);
        startHitResetTimer(player);
    }

    public void updateHitCount(Player player, int amount) {
        if (!playersToHitsWithEnchant.containsKey(player.getUniqueId())) playersToHitsWithEnchant.put(player.getUniqueId(), 1);

        playersToHitsWithEnchant.put(player.getUniqueId(), playersToHitsWithEnchant.get(player.getUniqueId()) + amount);
    }

    public boolean hasRequiredHits(Player player, int hitAmount) {
        if (!playersToHitsWithEnchant.containsKey(player.getUniqueId())) playersToHitsWithEnchant.put(player.getUniqueId(), 1);

        if (playersToHitsWithEnchant.get(player.getUniqueId()) >= hitAmount) {
            playersToHitsWithEnchant.put(player.getUniqueId(), 0);
            return true;
        }

        return false;
    }

    public void startHitResetTimer(Player player) {
        if (!hitAmountResetTasks.containsKey(player.getUniqueId())) {
            hitAmountResetTasks.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
                hitAmountResetTimes.put(player.getUniqueId(), hitAmountResetTimes.getOrDefault(player.getUniqueId(), 0L) + 1);

                if (hitAmountResetTimes.get(player.getUniqueId()) >= 5) {
                    playersToHitsWithEnchant.put(player.getUniqueId(), 0);
                    hitAmountResetTimes.put(player.getUniqueId(), 0L);
                    Bukkit.getServer().getScheduler().cancelTask(hitAmountResetTasks.get(player.getUniqueId()));
                    hitAmountResetTasks.remove(player.getUniqueId());
                }
            }, 0L, 20L));
        } else {
            hitAmountResetTimes.put(player.getUniqueId(), 0L);
        }
    }

    @Deprecated
    public boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0 && !((Entity) player).isOnGround() && player.getLocation().getBlock().getType() != Material.LADDER && player.getLocation().getBlock().getType() != Material.VINE && player.getLocation().getBlock().getType() != Material.STATIONARY_WATER && player.getLocation().getBlock().getType() != Material.STATIONARY_LAVA && player.getLocation().getBlock().getType() != Material.WATER && player.getLocation().getBlock().getType() != Material.LAVA && player.getVehicle() == null && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
    }
}
