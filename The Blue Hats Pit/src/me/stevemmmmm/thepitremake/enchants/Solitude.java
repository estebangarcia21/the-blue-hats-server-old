package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Solitude extends CustomEnchant {
    private final EnchantProperty<Float> damageReduction = new EnchantProperty<>(.4f, .5f, .6f);
    private final EnchantProperty<Integer> playersNeeded = new EnchantProperty<>(1, 2, 2);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(((Player) event.getEntity()).getInventory().getLeggings(), event.getEntity(), event);
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

        if (players.size() <= playersNeeded.getValueAtLevel(level)) {
            DamageManager.getInstance().reduceDamage(event, damageReduction.getValueAtLevel(level));
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
        return new LoreBuilder()
                .declareVariable("-40%", "-50%", "-60%")
                .write("Recieve ").setColor(ChatColor.BLUE).writeVariable(0, level).resetColor().write(" damage when ")
                .setWriteCondition(level == 1).write("only").next().write("one other player is within 7").next().write("blocks").resetCondition()
                .setWriteCondition(level != 1).write("two").next().write("or less players are within 7").next().write("blocks")
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
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
