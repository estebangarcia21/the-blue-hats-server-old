package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LuckyShot extends CustomEnchant {
    private LevelVariable<Integer> percentChance = new LevelVariable<>(2, 3, 10);

    private ArrayList<Arrow> hitLuckyShotArrows = new ArrayList<>();

    private List<UUID> canLuckyShot = new ArrayList<>();

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getDamager();
            hitLuckyShotArrows.add(arrow);

            if (arrow.getShooter() instanceof Player) {
                attemptEnchantExecution(this, BowManager.getInstance().getBowFromArrow(arrow), event);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.INSTANCE, () -> {
            if (event.getEntity() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getEntity();

                if (event.getEntity().getShooter() instanceof Player) {
                    if (getAttemptedEnchantExecutionFeedback(BowManager.getInstance().getBowFromArrow((Arrow) event.getEntity()))) {
                        if (!hitLuckyShotArrows.contains(arrow)) {
                            canLuckyShot.remove(((Player) (event.getEntity()).getShooter()).getUniqueId());
                        }
                    }
                }
            }
        }, 1L);
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            if (((Arrow) event.getProjectile()).getShooter() instanceof Player) {
                if (CustomEnchant.itemHasEnchant(((Player) ((Arrow) event.getProjectile()).getShooter()).getItemInHand(), this)) {
                    int level = CustomEnchant.getEnchantLevel(((Player) ((Arrow) event.getProjectile()).getShooter()).getItemInHand(), this);

                    if (percentChance(percentChance.at(level))) {
                        canLuckyShot.add(((Player) ((Arrow) event.getProjectile()).getShooter()).getUniqueId());
                        ((Player) ((Arrow) event.getProjectile()).getShooter()).sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "LUCKY SHOT!" + ChatColor.LIGHT_PURPLE + " Quadruple damage!");
                    }
                }
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[0];
        Arrow arrow = (Arrow) event.getDamager();

        if (canLuckyShot.contains(((Player) ((Arrow) event.getDamager()).getShooter()).getUniqueId())) {
            hitLuckyShotArrows.remove(arrow);
            canLuckyShot.remove(((Player) arrow.getShooter()).getUniqueId());
            DamageManager.getInstance().addDamage(event, 4, CalculationMode.MULTIPLICATIVE);
        }
    }

    @Override
    public String getName() {
        return "Lucky Shot";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Luckyshot";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("2%", "5%", "10%")
                .setColor(ChatColor.YELLOW).writeVariable(0, level).resetColor().write(" chance for a shot to deal").nextLine()
                .write("quadruple damage")
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
}
