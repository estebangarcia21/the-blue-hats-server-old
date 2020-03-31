package me.stevemmmmm.thehypixelpit.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.animationapi.core.Sequence;
import me.stevemmmmm.animationapi.core.SequenceAPI;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class DevilChicks extends CustomEnchant {
    private LevelVariable<Integer> amountOfChicks = new LevelVariable<>(1, 2, 3);

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (event.getEntity().getShooter() instanceof Player) {
                tryExecutingEnchant(((Player) event.getEntity().getShooter()).getInventory().getItemInHand(), event.getEntity().getLocation());
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Location location = (Location) args[0];
        World world = location.getWorld();

        for (int i = 0; i < amountOfChicks.at(level); i++) {
            Vector direction = new Vector();

            direction.setX(location.getX() + Math.random() - Math.random());
            direction.setY(location.getY());
            direction.setZ(location.getZ() + Math.random() - Math.random());

            Chicken chicken = (Chicken) world.spawnEntity(direction.toLocation(location.getWorld()), EntityType.CHICKEN);
            chicken.setBaby();

            chicken.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3, 5));

            float volume = 1.75f;

            Sequence soundAnimation = new Sequence() {{
                addKeyFrame(1, () -> world.playSound(location, Sound.NOTE_SNARE_DRUM, volume, 0.6f));
                addKeyFrame(2, () -> world.playSound(location, Sound.NOTE_SNARE_DRUM, volume, 0.7f));
                addKeyFrame(3, () -> world.playSound(location, Sound.NOTE_SNARE_DRUM, volume, 0.8f));
                addKeyFrame(4, () -> world.playSound(location, Sound.NOTE_SNARE_DRUM, volume, 0.9f));
                addKeyFrame(5, () -> world.playSound(location, Sound.NOTE_SNARE_DRUM, volume, 1.0f));
                addKeyFrame(6, () -> world.playSound(location, Sound.NOTE_SNARE_DRUM, volume, 1.1f));
                addKeyFrame(7, () -> world.playSound(location, Sound.NOTE_SNARE_DRUM, volume, 1.2f));
                addKeyFrame(8, () -> world.playSound(location, Sound.NOTE_SNARE_DRUM, volume, 1.3f));
                addKeyFrame(9, () -> world.playSound(location, Sound.NOTE_SNARE_DRUM, volume, 1.4f));

                addKeyFrame(10, () -> {
                    world.playSound(location, Sound.CHICKEN_HURT, 1, 2);

                    for (Entity entity : chicken.getNearbyEntities(1, 1, 1)) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;

                            DamageManager.getInstance().doTrueDamage(player, 2.4, player);

                            createExplosion(player, chicken.getLocation());
                        }
                    }

                    world.playSound(location, Sound.EXPLODE, volume, 1.6f);
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
