package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class BulletTime extends CustomEnchant implements EnchantCanceler {
    private final EnchantProperty<Integer> healingAmount = new EnchantProperty<>(0, 2, 3);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                attemptEnchantExecution(((Player) event.getEntity()).getInventory().getItemInHand(), event.getEntity(), event.getDamager(), event);
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player hitPlayer = (Player) args[0];
        Arrow arrow = (Arrow) args[1];

        if (hitPlayer.isBlocking()) {
            DamageManager.getInstance().setEventAsCanceled((EntityDamageByEntityEvent) args[2]);

            arrow.setKnockbackStrength(0);
            arrow.setBounce(true);

            hitPlayer.getWorld().playSound(hitPlayer.getLocation(), Sound.FIZZ, 1f, 1.5f);
            arrow.getWorld().playEffect(arrow.getLocation(), Effect.EXPLOSION, 0, 30);

            arrow.remove();

            hitPlayer.setHealth(Math.min(hitPlayer.getHealth() + healingAmount.getValueAtLevel(level), hitPlayer.getMaxHealth()));
        }
    }

    @Override
    public boolean isCanceled(Player player) {
        return itemHasEnchant(player.getInventory().getItemInHand(), this) && player.isBlocking();
    }

    @Override
    public CustomEnchant getEnchant() {
        return this;
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
                .setColor(ChatColor.GRAY).write("Blocking destroys arrows that").next()
                .setColor(ChatColor.GRAY).write("hit you")
                .resetCondition()
                .setWriteCondition(level != 1)
                .setColor(ChatColor.GRAY).write("Blocking destroys arrows hitting").next()
                .setColor(ChatColor.GRAY).write("you. Destroying arrows this way").next()
                .setColor(ChatColor.GRAY).write("heals ").setColor(ChatColor.RED).writeVariable(0, level)
                .build();
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
        return new Material[] { Material.GOLD_SWORD };
    }
}
