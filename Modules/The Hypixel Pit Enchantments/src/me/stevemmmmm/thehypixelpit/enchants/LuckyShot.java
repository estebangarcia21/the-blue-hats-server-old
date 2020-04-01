package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LuckyShot extends CustomEnchant {
    private LevelVariable<Integer> percentChance = new LevelVariable<>(2, 3, 10);

    private List<UUID> canLuckyShot = new ArrayList<>();

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player) {
                attemptEnchantExecution(ArrowManager.getInstance().getItemStackFromArrow(arrow), event);
            }
        }
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

        if (canLuckyShot.contains(((Player) ((Arrow) event.getDamager()).getShooter()).getUniqueId())) {
            canLuckyShot.remove(((Player) ((Arrow) event.getDamager()).getShooter()).getUniqueId());
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
        return new DescriptionBuilder()
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
