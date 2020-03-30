package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.animationapi.core.Sequence;
import me.stevemmmmm.animationapi.core.SequenceAPI;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class DevilChicks extends CustomEnchant {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {

        }
    }

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (event.getEntity().getShooter() instanceof Player) {
                executeEnchant(((Player) event.getEntity().getShooter()).getInventory().getItemInHand(), event);
            }
        }
    }

    @Override
    public boolean executeEnchant(ItemStack sender, Object executedEvent) {
        Arrow arrow = null;

        if (executedEvent instanceof EntityDamageByEntityEvent) {
            arrow = (Arrow) ((EntityDamageByEntityEvent) executedEvent).getDamager();
        }

        if (executedEvent instanceof ProjectileHitEvent) {
            arrow = (Arrow) ((ProjectileHitEvent) executedEvent).getEntity();
        }

        if (arrow == null) return false;

        if (itemHasEnchant(sender, 1, this)) {
            spawnDevils(arrow.getLocation(), 1);
            return true;
        }

        if (itemHasEnchant(sender, 2, this)) {
            spawnDevils(arrow.getLocation(), 2);
            return true;
        }

        if (itemHasEnchant(sender, 3, this)) {
            spawnDevils(arrow.getLocation(), 3);
            return true;
        }

        return false;
    }

    public void spawnDevils(Location location, int amountOfChicks) {
        World world = location.getWorld();

        for (int i = 0; i < amountOfChicks; i++) {
            Chicken chicken = (Chicken) world.spawnEntity(location, EntityType.CHICKEN);
            chicken.setBaby();

            chicken.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3, 5));

            Sequence soundAnimation = new Sequence() {{
                addKeyFrame(10, () -> {
                    world.playSound(location, Sound.CHICKEN_HURT, 1, 2);

                    for (Entity entity : chicken.getNearbyEntities(1, 1, 1)) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;

                            if (player.getInventory().getLeggings() != null) {
                                ItemStack pants = player.getInventory().getLeggings();

                                if (!itemHasEnchant(pants, new Mirror())) player.setHealth(Math.max(0, player.getHealth() - 2.4));
                            } else {
                                player.setHealth(Math.max(0, player.getHealth() - 2.4));
                            }

                            createExplosion(player, chicken.getLocation());
                        }
                    }

                    chicken.remove();
                });
            }};

            SequenceAPI.startSequence(soundAnimation);
        }
    }

    private void createExplosion(Player target, Location position) {
        Vector explosion = target.getLocation().toVector().subtract(position.toVector()).normalize();

        target.setVelocity(explosion);
    }

    @Override
    public String getName() {
        return "Devil Chicks";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Devilchicks";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new ArrayList<String>() {
            //TODO DC Desc
        };
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
