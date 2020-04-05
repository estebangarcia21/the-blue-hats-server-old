package me.stevemmmmm.thehypixelpit.game;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.text.DecimalFormat;

public class DamageIndicator implements Listener {
    private static DamageIndicator instance;

    public static DamageIndicator getInstance() {
        if (instance == null) instance = new DamageIndicator();

        return instance;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player attacked = (Player) event.getEntity();

            if (!RegionManager.getInstance().playerIsInRegion(attacked, RegionManager.RegionType.SPAWN) && !DamageManager.getInstance().isEventCancelled(event)) displayIndicator(damager, attacked, DamageManager.getInstance().getFinalDamageFromEvent(event));
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                Player damager = (Player) ((Arrow) event.getDamager()).getShooter();
                Player attacked = (Player) event.getEntity();

                if (!RegionManager.getInstance().playerIsInRegion(attacked, RegionManager.RegionType.SPAWN) && !DamageManager.getInstance().isEventCancelled(event)) displayIndicator(damager, attacked, DamageManager.getInstance().getFinalDamageFromEvent(event));
            }
        }
    }

    public void displayIndicator(Player damager, Player attacked, double damage) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + new IndicatorBuilder(attacked, (int) attacked.getHealth(), damage, (int) attacked.getMaxHealth()).build() + "\"}"), (byte) 2);
        ((CraftPlayer) damager).getHandle().playerConnection.sendPacket(packet);
    }

    static class IndicatorBuilder {
        private StringBuilder output = new StringBuilder();

        public IndicatorBuilder(Player damaged, int originalHealth, double damageTaken, int maxHealth) {
            output.append(ChatColor.GOLD.toString()).append(damaged.getName()).append(" ");

            EntityPlayer player = ((CraftPlayer) damaged).getHandle();

            int roundedDamageTaken = (int) damageTaken / 2;

            originalHealth /= 2;
            maxHealth /= 2;

            int result = Math.max(originalHealth - roundedDamageTaken, 0);

            if (result == 0) {
                roundedDamageTaken = 0;

                for (int i = 0; i < originalHealth; i++) {
                    roundedDamageTaken++;
                }
            }

            for (int i = 0; i < Math.max(originalHealth - roundedDamageTaken, 0); i++) {
                output.append(ChatColor.DARK_RED.toString()).append("❤");
            }

            for (int i = 0; i < roundedDamageTaken - (int) player.getAbsorptionHearts() / 2; i++) {
                output.append(ChatColor.RED.toString()).append("❤");
            }

            for (int i = originalHealth; i < maxHealth; i++) {
                output.append(ChatColor.BLACK.toString()).append("❤");
            }

            for (int i = 0; i < (int) player.getAbsorptionHearts() / 2; i++) {
                output.append(ChatColor.YELLOW.toString()).append("❤");
            }

            output.append(ChatColor.RED.toString()).append(" ").append(new DecimalFormat("###0.000").format(damageTaken / 2)).append("HP");
        }

        public String build() {
            return output.toString();
        }
    }
}
