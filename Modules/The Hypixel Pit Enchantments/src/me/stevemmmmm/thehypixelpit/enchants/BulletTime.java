package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BulletTime extends CustomEnchant {

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {

    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            executeEnchant(((Player) event.getEntity()).getInventory().getItemInHand(), event);
        }
    }

    @Override
    public boolean executeEnchant(ItemStack sender, Object executedEvent) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) executedEvent;

        Player hitPlayer = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getDamager();

        if (hitPlayer.isBlocking()) {
            if (itemHasEnchant(sender, 1, this)) {
                arrow.remove();
                event.setCancelled(true);
                return true;
            }

            if (itemHasEnchant(sender, 2, this)) {
                event.setCancelled(true);
                hitPlayer.setHealth(Math.min(hitPlayer.getHealth() + 2, hitPlayer.getMaxHealth()));
                return true;
            }

            if (itemHasEnchant(sender, 3, this)) {
                event.setCancelled(true);
                arrow.remove();
                hitPlayer.setHealth(Math.min(hitPlayer.getHealth() + 3, hitPlayer.getMaxHealth()));
                return true;
            }
        }

        return false;
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
