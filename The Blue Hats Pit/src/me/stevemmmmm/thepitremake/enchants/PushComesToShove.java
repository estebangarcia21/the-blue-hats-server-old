package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class PushComesToShove extends CustomEnchant {
    private final EnchantProperty<Float> pctsForce = new EnchantProperty<>(12f, 25f, 35f);
    private final EnchantProperty<Float> damageAmount = new EnchantProperty<>(0f, 1f, 2f);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                attemptEnchantExecution(BowManager.getInstance().getBowFromArrow((Arrow) event.getDamager()),
                        ((Arrow) event.getDamager()).getShooter(), event.getEntity(), event.getDamager());
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player player = (Player) args[0];
        Player hit = (Player) args[1];
        Arrow arrow = (Arrow) args[2];

        updateHitCount(player);

        if (hasRequiredHits(player, 3)) {
            Vector velocity = arrow.getVelocity().normalize().multiply(pctsForce.getValueAtLevel(level) / 2.35);
            velocity.setY(0);

            hit.setVelocity(velocity);

            hit.damage(damageAmount.getValueAtLevel(level));
        }
    }

    @Override
    public String getName() {
        return "Push Comes to Shove";
    }

    @Override
    public String getEnchantReferenceName() {
        return "PCTS";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("Punch", "Punch V", "Punch VII").declareVariable("", "+0.5❤", "+1❤")
                .write("Every 3rd shot on a player has").next().writeVariable(ChatColor.AQUA, 0, level)
                .setWriteCondition(level != 1).write(" and deals ").writeVariable(ChatColor.RED, 1, level).next()
                .write("extra damage").build();
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
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.BOW };
    }
}
