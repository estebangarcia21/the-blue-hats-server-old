package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.animationapi.core.Sequence;
import me.stevemmmmm.animationapi.core.SequenceAPI;
import me.stevemmmmm.thehypixelpit.game.RegionManager;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.BowManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.LoreBuilder;
import me.stevemmmmm.thehypixelpit.managers.enchants.LevelVariable;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class DevilChicks extends CustomEnchant {
    private final LevelVariable<Integer> amountOfChicks = new LevelVariable<>(1, 2, 3);

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (event.getEntity().getShooter() instanceof Player) {
                attemptEnchantExecution(BowManager.getInstance().getBowFromArrow((Arrow) event.getEntity()), event.getEntity());
            }
        }
    }

    @Override
    public void applyEnchant(int level, Object... args) {
        Arrow arrow = (Arrow) args[0];
        Location location = arrow.getLocation();
        World world = location.getWorld();

        for (int i = 0; i < amountOfChicks.at(level); i++) {
            Vector direction = new Vector();

            float radius = 0.75f;

            direction.setX(location.getX() + (Math.random() - Math.random()) * radius);
            direction.setY(location.getY());
            direction.setZ(location.getZ() + (Math.random() - Math.random()) * radius);

            if (RegionManager.getInstance().locationIsInRegion(direction.toLocation(location.getWorld()), RegionManager.RegionType.SPAWN)) return;

            Chicken chicken = (Chicken) world.spawnEntity(direction.toLocation(location.getWorld()), EntityType.CHICKEN);
            chicken.setBaby();

            chicken.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3, 5));

            float volume = 0.5f;

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

                            DamageManager.getInstance().doTrueDamage(player, 2.4, (Player) arrow.getShooter());

                            createExplosion(player, chicken.getLocation());
                        }
                    }

                    world.playSound(location, Sound.EXPLODE, volume, 1.6f);
                    world.playEffect(chicken.getLocation(), Effect.EXPLOSION_LARGE, Effect.EXPLOSION_LARGE.getData(), 100);
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
        return "Devil Chicks!";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Devilchicks";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder()
                .writeOnlyIf(level == 1, "Arrows spawn with explosive chicken.")
                .writeOnlyIf(level == 2, "Arrows spawn many explosive chickens.")
                .writeOnlyIf(level == 3, "Arrows spawn too many explosive chickens.")
                .build();
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }

    @Override
    public Material getEnchantItemType() {
        return Material.BOW;
    }
}
