package me.stevemmmmm.thepitremake.enchants;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import me.stevemmmmm.thepitremake.managers.enchants.LevelVariable;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class ComboHeal extends CustomEnchant {
    private final LevelVariable<Float> healingAmount = new LevelVariable<>(.80f, 1.6f, 2.4f);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            attemptEnchantExecution(((Player) event.getDamager()).getInventory().getItemInHand(), event.getDamager());
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player player = (Player) args[0];

        updateHitCount(player);

        if (hasRequiredHits(player, 4)) {
            EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

            player.playSound(player.getLocation(), Sound.DONKEY_HIT, 1, 0.5f);
            nmsPlayer.setAbsorptionHearts(Math.min(nmsPlayer.getAbsorptionHearts() + healingAmount.at(level), 8));
        }
    }

    @Override
    public String getName() {
        return "Combo: Heal";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Comboheal";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("0.4", "0.8", "1.2")
                .write("Every ").write(ChatColor.YELLOW, "fourth ").write("strike heals").nextLine()
                .writeVariable(ChatColor.RED, 0, level).write(ChatColor.RED, "❤").write(" and grants ").writeVariable(ChatColor.GOLD, 0, level).write(ChatColor.GOLD, "❤").nextLine()
                .write("absorption")
                .build();
    }

    @Override
    public boolean isRemovedFromPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
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
