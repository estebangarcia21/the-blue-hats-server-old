package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class BulletTime extends CustomEnchant {
    private LevelVariable<Integer> healingAmount = new LevelVariable<>(0, 2, 3);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            tryExecutingEnchant(((Player) event.getEntity()).getInventory().getItemInHand(), event.getEntity());
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player hitPlayer = (Player) args[0];

        hitPlayer.setHealth(Math.min(hitPlayer.getHealth() + healingAmount.at(level), hitPlayer.getMaxHealth()));
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
        final String healthAmount = level == 1 ? "" : level == 2 ? "1❤" : level == 3 ? "1.5❤" : "";

        if (level == 1) {
            return new ArrayList<String>() {{
                add(ChatColor.GRAY + "Blocking destroys arrows that");
                add(ChatColor.GRAY + "hit you");
            }};
        }

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Blocking destroys arrows hitting");
            add(ChatColor.GRAY + "you. Destroying arrows this way");
            add(ChatColor.GRAY + "heals " + ChatColor.RED + healthAmount);
        }};
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
