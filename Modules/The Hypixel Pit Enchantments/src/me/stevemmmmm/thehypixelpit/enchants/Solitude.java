package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.DescriptionBuilder;
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

public class Solitude extends CustomEnchant {
    private LevelVariable<Float> damageReduction = new LevelVariable<>(.4f, .5f, .6f);
    private LevelVariable<Integer> playersNeeded = new LevelVariable<>(1, 2, 2);

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

        if (players.size() <= playersNeeded.at(level)) {
            DamageManager.getInstance().reduceDamage(event, damageReduction.at(level));
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
        return new DescriptionBuilder()
                .addVariable("-40%", "-50%", "-60%")
                .write("Recieve ").setColor(ChatColor.BLUE).writeVariable(0, level).resetColor().write(" damage when ")
                .setWriteCondition(level == 1).write("only").nextLine().write("one other player is within 7").nextLine().write("blocks").resetCondition()
                .setWriteCondition(level != 1).write("two").nextLine().write("or less players are within 7").nextLine().write("blocks")
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
