package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.CalculationMode;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageReductionEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Solitude extends CustomEnchant implements DamageReductionEnchant {
    private LevelVariable<Float> damageReduction = new LevelVariable<>(.6f, .5f, .4f);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            tryExecutingEnchant(((Player) event.getEntity()).getInventory().getLeggings(), event.getEntity(), event);
        }
    }

    @Override
    public void applyEnchant(int level, Object... args)  {
        Player damaged = (Player) args[0];
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[1];

        List<Entity> entities = damaged.getNearbyEntities(7, 7, 7);
        List<Player> players = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity instanceof Player) {
                if (entity != damaged) {
                    players.add((Player) entity);
                }
            }
        }

        if (players.size() <= 2) {
            event.setDamage(event.getDamage() * damageReduction.at(level));
        }
    }

    @Override
    public String getName() {
        return "Solitude";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Solitude";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String percent = level == 1 ? "-40%" : level == 2 ? "-50%" : level == 3 ? "-60%" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Receive " + ChatColor.BLUE + percent + ChatColor.GRAY + " damage when two");
            add(ChatColor.GRAY + "or less players are within 7");
            add(ChatColor.GRAY + "blocks");
        }};
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
    public double[] getPercentReductionPerLevel() {
        return new double[] { .4, .5, .6 };
    }

    @Override
    public CalculationMode getReductionCalculationMode() {
        return CalculationMode.ADDITIVE;
    }
}
