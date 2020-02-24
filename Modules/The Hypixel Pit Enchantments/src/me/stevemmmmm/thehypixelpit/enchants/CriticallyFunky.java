package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CriticallyFunky extends CustomEnchant {
    private HashMap<UUID, Integer> queue = new HashMap<>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            executeEnchant(((Player) event.getEntity()).getInventory().getLeggings(), event);
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                Player player = (Player) ((Arrow) event.getDamager()).getShooter();
                executeEnchant(((Player) event.getEntity()).getInventory().getLeggings(), event);
            }
        }
    }

    @Override
    public boolean executeEnchant(ItemStack sender, Object executedEvent) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) executedEvent;
        Player damager = DamageManager.getInstance().getDamagerFromDamageEvent(event);

        if (queue.containsKey(damager.getUniqueId())) {
            if (queue.get(damager.getUniqueId()) == 1) {
                event.setDamage(event.getDamage() * 1.14f);
                queue.remove(damager.getUniqueId());
                return false;
            }

            if (queue.get(damager.getUniqueId()) == 2) {
                event.setDamage(event.getDamage() * 1.40f);
                queue.remove(damager.getUniqueId());
                return false;
            }
        }

        if (sender == null) return false;
        if (!isCriticalHit(damager)) return false;
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();

            if (!arrow.isCritical()) return false;
        }

        if (itemHasEnchant(sender, 1, this)) {
            event.setDamage(event.getDamage() * 0.65f);
            return true;
        }

        if (itemHasEnchant(sender, 2, this)) {
            event.setDamage(event.getDamage() * 0.65f);
            queue.put(event.getEntity().getUniqueId(), 1);
            return true;
        }

        if (itemHasEnchant(sender, 3, this)) {
            event.setDamage(event.getDamage() * 0.4f);
            queue.put(event.getEntity().getUniqueId(), 2);
            return true;
        }

        return false;
    }

    @Override
    public String getName() {
        return "Critically Funky";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Critfunky";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String data = level == 1 ? "65%:" : level == 2 ? "65%:14%" : level == 3 ? "40%:30%" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Critical hits against you deal");
            add(ChatColor.BLUE + data.split(":")[0] + ChatColor.GRAY + " of the damage they");
            add(ChatColor.GRAY + "normally would" + (level != 1 ? "and empower your" : ""));
            if (level != 0) add(ChatColor.GRAY + "next strike for " + ChatColor.RED + data.split(":")[1] + ChatColor.GRAY + " damage");
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
