package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.game.RegionManager;
import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Explosive extends CustomEnchant {
    private final EnchantProperty<Double> explosionRange = new EnchantProperty<>(1D, 2.5D, 6D);
    private final EnchantProperty<Integer> cooldownTime = new EnchantProperty<>(5, 3, 5);
    private final EnchantProperty<Float> explosionPitch = new EnchantProperty<>(2f, 1f, 1.4f);

    private final EnchantProperty<Effect> explosionParticle = new EnchantProperty<>(Effect.EXPLOSION_LARGE,
            Effect.EXPLOSION_HUGE, Effect.EXPLOSION_HUGE);

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (event.getEntity().getShooter() instanceof Player) {
                attemptEnchantExecution(BowManager.getInstance().getBowFromArrow((Arrow) event.getEntity()),
                        event.getEntity());
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Arrow arrow = (Arrow) args[0];
        Player shooter = (Player) arrow.getShooter();

        if (isNotOnCooldown(shooter)) {
            for (Entity entity : arrow.getNearbyEntities(explosionRange.getValueAtLevel(level),
                    explosionRange.getValueAtLevel(level), explosionRange.getValueAtLevel(level))) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;

                    if (RegionManager.getInstance().playerIsInRegion(player, RegionManager.RegionType.SPAWN))
                        continue;

                    if (player != shooter) {
                        Vector force = player.getLocation().toVector().subtract(arrow.getLocation().toVector())
                                .normalize().multiply(1.25);
                        force.setY(.85f);

                        player.setVelocity(force);
                    }
                }
            }

            arrow.getWorld().playSound(arrow.getLocation(), Sound.EXPLODE, 0.75f,
                    explosionPitch.getValueAtLevel(level));
            arrow.getWorld().playEffect(arrow.getLocation(), explosionParticle.getValueAtLevel(level),
                    explosionParticle.getValueAtLevel(level).getData(), 100);
        }

        startCooldown(shooter, cooldownTime.getValueAtLevel(level), true);
    }

    @Override
    public String getName() {
        return "Explosive";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Explosive";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("POP", "BANG", "BOOM").setColor(ChatColor.GRAY).write("Arrows go ")
                .writeVariable(0, level).write("! (" + cooldownTime.getValueAtLevel(level) + "s cooldown)").build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
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
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.BOW };
    }
}
