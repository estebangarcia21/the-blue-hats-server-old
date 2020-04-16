package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.game.RegionManager;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.BowManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.LoreBuilder;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import me.stevemmmmm.thehypixelpit.utils.TelebowData;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Telebow extends CustomEnchant {
    private final LevelVariable<Integer> cooldownTimes = new LevelVariable<>(90, 45, 20);

    private final HashMap<UUID, TelebowData> telebowData = new HashMap<>();

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();

            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();

                if (telebowData.containsKey(player.getUniqueId())) {
                    if (itemHasEnchant(telebowData.get(player.getUniqueId()).getBow(), this)) {
                        if (arrow == telebowData.get(player.getUniqueId()).getArrow() && telebowData.get(player.getUniqueId()).isSneaking()) attemptEnchantExecution(telebowData.get(player.getUniqueId()).getBow(), player, arrow);
                    }
                }
            }
        }

        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();

            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();

                if (itemHasEnchant(BowManager.getInstance().getBowFromArrow(arrow), this) && getCooldownTime(player) != 0 && telebowData.get(player.getUniqueId()).isSneaking() && getCooldownTime(player) != 20) {
                    PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.RED + "Telebow Cooldown: " + getCooldownTime(player) + "(s)" + "\"}"), (byte) 2);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
        }
    }

    @EventHandler
    public void onArrowHitPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();

                if (itemHasEnchant(player.getInventory().getItemInHand(), this) && getCooldownTime(player) != 0 && player.isSneaking()) {
                    PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.RED + "Telebow Cooldown: " + getCooldownTime(player) + "(s)" + "\"}"), (byte) 2);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }

                if (telebowData.containsKey(player.getUniqueId())) {
                    if (itemHasEnchant(telebowData.get(player.getUniqueId()).getBow(), this)) {
                        if (arrow == telebowData.get(player.getUniqueId()).getArrow() && telebowData.get(player.getUniqueId()).isSneaking()) attemptEnchantExecution(telebowData.get(player.getUniqueId()).getBow(), player, arrow);
                    }
                }
            }
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();

                if (itemHasEnchant(BowManager.getInstance().getBowFromArrow(arrow), this)) {
                    setCooldownTime(shooter, getCooldownTime(shooter) - 3, true);
                }
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow) {
            Player player = (Player) event.getEntity();
            Arrow arrow = (Arrow) event.getProjectile();

            if (itemHasEnchant(player.getInventory().getItemInHand(), this)) {
                if (player.isSneaking()) {
                    telebowData.put(player.getUniqueId(), new TelebowData(arrow, player.getInventory().getItemInHand(), true));
                } else {
                    telebowData.put(player.getUniqueId(), new TelebowData(arrow, player.getInventory().getItemInHand(), false));
                }
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player player = (Player) args[0];
        Arrow arrow = (Arrow) args[1];

        if (isNotOnCooldown(player) && !RegionManager.getInstance().locationIsInRegion(arrow.getLocation(), RegionManager.RegionType.SPAWN)) {
            player.teleport(arrow);
            player.getWorld().playSound(arrow.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 2f);
        }

        startCooldown(player, cooldownTimes.at(level), true);
    }

    @Override
    public String getName() {
        return "Telebow";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Telebow";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("90s", "45s", "20s")
                .write("Sneak to shoot a teleportation").nextLine()
                .write("arrow (").writeVariable(0, level).write(" cooldown, -3 per bow").nextLine()
                .write("hit)")
                .build();
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }

    @Override
    public Material getEnchantItemType() {
        return Material.BOW;
    }
}
