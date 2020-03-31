package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.ArrowManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.DescriptionBuilder;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Wasp extends CustomEnchant {
    private LevelVariable<Integer> weaknessAmplifier = new LevelVariable<>(1, 2, 3);
    private LevelVariable<Integer> duration = new LevelVariable<>(6, 11, 16);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                tryExecutingEnchant(ArrowManager.getInstance().getItemStackFromArrow((Arrow) event.getDamager()), event.getEntity());
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player hit = (Player) args[0];

        hit.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration.at(level) * 20, weaknessAmplifier.at(level)), true);
    }

    @Override
    public String getName() {
        return "Wasp";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Wasp";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new DescriptionBuilder()
                .addVariable("II", "III", "IV")
                .setColor(ChatColor.GRAY).write("Apply ").setColor(ChatColor.RED).write("Weakness ").writeVariable(0, level).setColor(ChatColor.GRAY).write(" (" + duration.at(level) + "s) on hit")
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
