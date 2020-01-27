package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchant;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Stun extends CustomEnchant {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            triggerEnchant(((Player) event.getDamager()).getInventory().getItemInHand(), event);
        }
    }

    @Override
    public void triggerEnchant(ItemStack sender, Object... args) {
        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) args[0];

        if (itemHasEnchant(sender, 1, this)) {
            updateHitCount((Player) event.getEntity());

            if (hasRequiredHits((Player) event.getEntity(), 5)) {
                ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 8), true);
                ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, -8), true);
                event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ANVIL_LAND, 1, 0.1f);

                IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.RED + "STUNNED!" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

                PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                PacketPlayOutTitle length = new PacketPlayOutTitle(0, 20, 0);

                IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.YELLOW + "You cannot move!" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

                PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
                PacketPlayOutTitle subTitleLength = new PacketPlayOutTitle(0, 20, 0);

                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(title);
                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(length);

                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(subTitle);
                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(subTitleLength);
            }
        }

        if (itemHasEnchant(sender, 2, this)) {
            updateHitCount((Player) event.getEntity());

            if (hasRequiredHits((Player) event.getEntity(), 4)) {
                ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 8), true);
                ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, -8), true);
                event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ANVIL_LAND, 1, 0.1f);

                IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.RED + "STUNNED!" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

                PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                PacketPlayOutTitle length = new PacketPlayOutTitle(0, 40, 0);

                IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.YELLOW + "You cannot move!" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

                PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
                PacketPlayOutTitle subTitleLength = new PacketPlayOutTitle(0, 40, 0);


                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(title);
                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(length);

                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(subTitle);
                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(subTitleLength);
            }
        }

        if (itemHasEnchant(sender, 3, this)) {
            updateHitCount((Player) event.getEntity());

            if (hasRequiredHits((Player) event.getEntity(), 4)) {
                ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 8), true);
                ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 30, -8), true);
                event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ANVIL_LAND, 1, 0.1f);

                IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.RED + "STUNNED!" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

                PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                PacketPlayOutTitle length = new PacketPlayOutTitle(0, 60, 0);

                IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.YELLOW + "You cannot move!" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

                PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
                PacketPlayOutTitle subTitleLength = new PacketPlayOutTitle(0, 60, 0);


                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(title);
                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(length);

                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(subTitle);
                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(subTitleLength);
            }
        }
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
        final String strikes = level == 1 ? "fifth" : level == 2 ? "fourth" : level == 3 ? "fourth" : "";
        final String duration = level == 1 ? "0.5" : level == 2 ? "1.0" : level == 3 ? "1.5" : "";

        return new ArrayList<String>() {{
            add(ChatColor.GRAY + "Every " + ChatColor.YELLOW + strikes + ChatColor.GRAY + " strike on an enemy");
            add(ChatColor.GRAY + "stuns them for " + duration + " seconds");
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
}
