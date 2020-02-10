package me.stevemmmmm.animationapi.core;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AnimationAPI extends JavaPlugin {
    public static AnimationAPI instance;

    private static HashMap<Animation, Integer> animationTaskIndexs = new HashMap<>();
    private static HashMap<Animation, Long> sequencers = new HashMap<>();
    private static HashMap<Animation, AtomicLong> endSequencerTicks = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
    }

    public static void playAnimation(Animation animation) {
        sequencers.put(animation, 0L);
        if (animation.getAnimationActions() != null) animation.getAnimationActions().onAnimationStart();

        endSequencerTicks.put(animation, new AtomicLong());

        ArrayList<Long> values = new ArrayList<>();
        for (Pair<Long, Frame> keyframe : animation.getAnimationSequence()) {
            values.add(keyframe.getKey());
        }

        Collections.sort(values);
        endSequencerTicks.get(animation).set(values.get(values.size() - 1));

        animationTaskIndexs.put(animation, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            for (Pair<Long, Frame> keyframe : animation.getAnimationSequence()) {
                if (keyframe.getKey().equals(sequencers.get(animation))) {
                    keyframe.getValue().play();
                }
            }

            if (sequencers.get(animation) > endSequencerTicks.get(animation).get()) {
                if (animation.getAnimationActions() != null) animation.getAnimationActions().onAnimationEnd();

                Bukkit.getServer().getScheduler().cancelTask(animationTaskIndexs.get(animation));
                animationTaskIndexs.remove(animation);
                endSequencerTicks.remove(animation);
            }

            sequencers.put(animation, sequencers.get(animation) + 1);
        }, 0L, 1L));
    }
}
