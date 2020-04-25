package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import me.stevemmmmm.thepitremake.managers.enchants.LoreBuilder;
import me.stevemmmmm.thepitremake.managers.enchants.LevelVariable;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class ComboStun extends CustomEnchant {
    private final LevelVariable<Integer> duration = new LevelVariable<>(10, 16, 30);
    private final LevelVariable<Integer> hitsNeeded = new LevelVariable<>(5, 4, 4);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            attemptEnchantExecution(((Player) event.getDamager()).getInventory().getItemInHand(), event.getDamager(), event.getEntity());
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Player damager = (Player) args[0];
        Player hit = (Player) args[1];

        updateHitCount(damager);

        if (hasRequiredHits(damager, hitsNeeded.at(level))) {
            hit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration.at(level), 8), true);
            hit.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration.at(level), -8), true);
            hit.getWorld().playSound(hit.getLocation(), Sound.ANVIL_LAND, 1, 0.1f);

            sendPackets(hit);
        }
    }

    private void sendPackets(Player player) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.RED + "STUNNED!" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(0, 60, 0);

        IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.YELLOW + "You cannot move!" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
        PacketPlayOutTitle subTitleLength = new PacketPlayOutTitle(0, 60, 0);


        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitleLength);
    }

    @Override
    public String getName() {
        return "Combo: Stun";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Combostun";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .addVariable("fifth", "fourth", "fourth")
                .addVariable("0.5", "0.8", "1.5")
                .write("Every ").setColor(ChatColor.YELLOW).writeVariable(0, level).resetColor().write(" strike on an enemy").next()
                .write("stuns them for ").writeVariable(1, level).write(" seconds")
                .build();
    }

    @Override
    public boolean isRemovedFromPassiveWorld() {
        return true;
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
        return new Material[] { Material.GOLD_SWORD };
    }
}
