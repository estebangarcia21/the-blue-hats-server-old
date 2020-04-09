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

public class ComboSwift extends CustomEnchant {
    private LevelVariable<Integer> hitsNeeded = new LevelVariable<>(4, 3, 3);
    private LevelVariable<Integer> speedTime = new LevelVariable<>(3, 4, 5);
    private LevelVariable<Integer> speedAmplifier = new LevelVariable<>(0, 1, 1);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(this, ((Player) event.getDamager()).getInventory().getItemInHand(), event.getDamager());
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player player = (Player) args[0];

        updateHitCount(player);

        if (hasRequiredHits(player, hitsNeeded.at(level))) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedTime.at(level) * 20, speedAmplifier.at(level)), true);
        }
    }

    @Override
    public String getName() {
        return "Combo: Swift";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Comboswift";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("fourth", "third", "third")
                .addVariable("Speed I", "Speed II", "Speed III")
                .addVariable("3", "4", "5")
                .write("Every ").writeVariable(ChatColor.YELLOW, 0, level).write(" strike gain").nextLine()
                .writeVariable(ChatColor.YELLOW, 1, level).write(" (").writeVariable(2, level).write("s)")
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
        return Material.GOLD_SWORD;
    }
}
