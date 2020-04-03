package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.LoreBuilder;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class BulletTime extends CustomEnchant {
    private LevelVariable<Integer> healingAmount = new LevelVariable<>(0, 2, 3);

    private HashMap<Arrow, Integer> arrowTasks = new HashMap<>();

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            attemptEnchantExecution(((Player) event.getEntity()).getInventory().getItemInHand(), event);
        }
    }

//    @EventHandler
//    public void onBowShoot(EntityShootBowEvent event) {
//        if (event.getProjectile() instanceof Arrow) {
//            Arrow arrow = (Arrow) event.getProjectile();
//
//            double range = 2.2;
//
//            if (arrow.getShooter() instanceof Player) {
//                arrowTasks.put(arrow, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
//                    for (Entity entity : arrow.getNearbyEntities(range, range, range)) {
//                        if (entity instanceof Player) {
//                            Player player = (Player) entity;
//
//                            if (player.isBlocking()) {
//                                attemptEnchantExecution(player.getInventory().getItemInHand(), player, arrow);
//                                break;
//                            }
//                        }
//                    }
//                }, 0L, 1L));
//            }
//        }
//    }
//
//    @EventHandler
//    public void onArrowLand(ProjectileHitEvent event) {
//        if (event.getEntity() instanceof Arrow) {
//            Arrow arrow = (Arrow) event.getEntity();
//
//            if (arrowTasks.containsKey(arrow)) {
//                Bukkit.getServer().getScheduler().cancelTask(arrowTasks.get(arrow));
//                arrowTasks.remove(arrow);
//            }
//        }
//    }

    @Override
    public void applyEnchant(int level, Object... args) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[0];

        Player hitPlayer = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getDamager();

        if (hitPlayer.isBlocking()) {
            PacketAdapter adapter = new PacketAdapter(Main.INSTANCE, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_STATUS) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    event.setCancelled(true);
                }
            };

            Main.protocolManager.addPacketListener(adapter);
            Main.protocolManager.removePacketListener(adapter);

            hitPlayer.getWorld().playSound(hitPlayer.getLocation(), Sound.FIZZ, 1f, 1.5f);
            arrow.getWorld().playEffect(arrow.getLocation(), Effect.EXPLOSION, 0, 1);

//            Bukkit.getServer().getScheduler().cancelTask(arrowTasks.get(arrow));
//            arrow.remove();

            hitPlayer.setHealth(Math.min(hitPlayer.getHealth() + healingAmount.at(level), hitPlayer.getMaxHealth()));
        }
    }

    @Override
    public String getName() {
        return "Bullet Time";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Bullettime";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("", "1❤", "1.5❤")
                .setWriteCondition(level == 1)
                .setColor(ChatColor.GRAY).write("Blocking destroys arrows that").nextLine()
                .setColor(ChatColor.GRAY).write("hit you")
                .resetCondition()
                .setWriteCondition(level != 1)
                .setColor(ChatColor.GRAY).write("Blocking destroys arrows hitting").nextLine()
                .setColor(ChatColor.GRAY).write("you. Destroying arrows this way").nextLine()
                .setColor(ChatColor.GRAY).write("heals ").setColor(ChatColor.RED).writeVariable(0, level)
                .build();
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }
}
