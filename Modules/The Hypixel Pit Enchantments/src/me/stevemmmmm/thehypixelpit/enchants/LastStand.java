package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.LoreBuilder;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class LastStand extends CustomEnchant {
    private LevelVariable<Integer> amplifier = new LevelVariable<>(0, 1, 2);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(this, ((Player) event.getEntity()).getInventory().getLeggings(), event.getEntity());
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damaged = (Player) args[0];

        if (damaged.getHealth() < 10) {
            damaged.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, amplifier.at(level)));
        }
    }

    @Override
    public String getName() {
        return "Last Stand";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Laststand";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("I", "II", "III")
                .write("Gain ").setColor(ChatColor.BLUE).write("Resistance ").writeVariable(0, level).resetColor().write(" (4").nextLine()
                .write("seconds) when reaching ").setColor(ChatColor.RED).write("3❤")
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

    @Override
    public Material getEnchantItemType() {
        return Material.LEATHER_LEGGINGS;
    }
}
