package me.stevemmmmm.thepitremake.enchants;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import me.stevemmmmm.thepitremake.managers.enchants.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class RingArmor extends CustomEnchant {
    private final EnchantProperty<Float> damageReductionAmount = new EnchantProperty<>(.20f, .40f, .60f);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                attemptEnchantExecution(((Player) event.getEntity()).getInventory().getLeggings(), event);
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        DamageManager.getInstance().reduceDamage(((EntityDamageByEntityEvent) args[0]),
                damageReductionAmount.getValueAtLevel(level));
    }

    @Override
    public String getName() {
        return "Ring Armor";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Ringarmor";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("-20%", "-40%", "-60%").write("Recieve ")
                .writeVariable(ChatColor.BLUE, 0, level).write(" damage from").next().write("arrows").build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.A;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
