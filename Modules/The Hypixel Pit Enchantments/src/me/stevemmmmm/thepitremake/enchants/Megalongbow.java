package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import me.stevemmmmm.thepitremake.managers.enchants.LevelVariable;
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
    private final LevelVariable<Integer> amplifier = new LevelVariable<>(1, 2, 3);

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow) {
            attemptEnchantExecution(((Player) event.getEntity()).getInventory().getItemInHand(), event.getProjectile(), event.getEntity());
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Arrow arrow = (Arrow) args[0];
        Player player = (Player) args[1];

        if (isNotOnCooldown(player)) {
            arrow.setCritical(true);
            arrow.setVelocity(player.getLocation().getDirection().multiply(2.90));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, amplifier.at(level)), true);
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
        return new LoreBuilder()
                .addVariable("II", "III", "IV")
                .write("One shot per second, this bow is").nextLine()
                .write("automatically fully drawn and").nextLine()
                .write("grants ").setColor(ChatColor.GREEN).write("Jump Boost ").writeVariable(0, level).resetColor().write(" (2s)")
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
