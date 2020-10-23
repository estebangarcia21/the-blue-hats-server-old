package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantProperty;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Megalongbow extends CustomEnchant {
    private final EnchantProperty<Integer> amplifier = new EnchantProperty<>(1, 2, 3);

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow) {
            attemptEnchantExecution(((Player) event.getEntity()).getInventory().getItemInHand(), event.getProjectile(),
                    event.getEntity());
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Arrow arrow = (Arrow) args[0];
        Player player = (Player) args[1];

        if (isNotOnCooldown(player)) {
            arrow.setCritical(true);
            arrow.setVelocity(player.getLocation().getDirection().multiply(2.90));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, amplifier.getValueAtLevel(level)), true);
        }

        startCooldown(player, 1, true);
    }

    @Override
    public String getName() {
        return "Mega Longbow";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Megalongbow";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("II", "III", "IV").write("One shot per second, this bow is").next()
                .write("automatically fully drawn and").next().write("grants ").setColor(ChatColor.GREEN)
                .write("Jump Boost ").writeVariable(0, level).resetColor().write(" (2s)").build();
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
