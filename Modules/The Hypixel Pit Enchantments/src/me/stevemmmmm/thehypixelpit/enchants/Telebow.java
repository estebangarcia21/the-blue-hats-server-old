package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import me.stevemmmmm.thehypixelpit.utils.TelebowData;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
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
    private LevelVariable<Integer> cooldownTimes = new LevelVariable<>(90, 45, 20);

    private HashMap<UUID, TelebowData> telebowData = new HashMap<>();

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();

            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();

                if (telebowData.containsKey(player.getUniqueId())) {
                    if (itemHasEnchant(telebowData.get(player.getUniqueId()).getBow(), this)) {
                        if (arrow == telebowData.get(player.getUniqueId()).getArrow() && telebowData.get(player.getUniqueId()).isSneaking()) tryExecutingEnchant(telebowData.get(player.getUniqueId()).getBow(), player, arrow);
                    }
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
                        if (arrow == telebowData.get(player.getUniqueId()).getArrow() && telebowData.get(player.getUniqueId()).isSneaking()) tryExecutingEnchant(telebowData.get(player.getUniqueId()).getBow(), player, arrow);
                    }
                }
            }
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();

                if (itemHasEnchant(shooter.getInventory().getItemInHand(), this)) {
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

            if (itemHasEnchant(player.getInventory().getItemInHand(), this) && getCooldownTime(player) != 0 && player.isSneaking()) {
                PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.RED + "Telebow Cooldown: " + getCooldownTime(player) + "(s)" + "\"}"), (byte) 2);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }

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

        if (isNotOnCooldown(player)) {
            player.teleport(arrow);
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
        final String sDelay = level == 1 ? "90s" : level == 2 ? "45s" : level == 3 ? "20s" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Sneak to shoot a teleportation");
            add(ChatColor.GRAY + "arrow (" + sDelay + " cooldown, -3s per bow");
            add(ChatColor.GRAY + "hit)");
        }};
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }
}
