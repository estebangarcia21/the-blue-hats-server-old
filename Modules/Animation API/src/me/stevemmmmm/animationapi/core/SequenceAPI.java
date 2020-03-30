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

public class SequenceAPI extends JavaPlugin {
    public static SequenceAPI instance;

    private static HashMap<Sequence, Integer> animationTaskIndexs = new HashMap<>();
    private static HashMap<Sequence, Long> sequencers = new HashMap<>();
    private static HashMap<Sequence, AtomicLong> endSequencerTicks = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
    }

    public static void startSequence(Sequence sequence) {
        sequencers.put(sequence, 0L);
        if (sequence.getAnimationActions() != null) sequence.getAnimationActions().onSequenceStart();

        endSequencerTicks.put(sequence, new AtomicLong());

        ArrayList<Long> values = new ArrayList<>();
        for (Pair<Long, Frame> keyframe : sequence.getAnimationSequence()) {
            values.add(keyframe.getKey());
        }

        Collections.sort(values);
        endSequencerTicks.get(sequence).set(values.get(values.size() - 1));

        animationTaskIndexs.put(sequence, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            for (Pair<Long, Frame> keyframe : sequence.getAnimationSequence()) {
                if (keyframe.getKey().equals(sequencers.get(sequence))) {
                    keyframe.getValue().play();
                }
            }

            if (sequencers.get(sequence) > endSequencerTicks.get(sequence).get()) {
                if (sequence.getAnimationActions() != null) sequence.getAnimationActions().onSequenceEnd();

                Bukkit.getServer().getScheduler().cancelTask(animationTaskIndexs.get(sequence));
                animationTaskIndexs.remove(sequence);
                endSequencerTicks.remove(sequence);
            }

            sequencers.put(sequence, sequencers.get(sequence) + 1);
        }, 0L, 1L));
    }
}
