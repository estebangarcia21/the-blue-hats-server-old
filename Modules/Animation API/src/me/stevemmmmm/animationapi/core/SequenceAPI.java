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
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class SequenceAPI extends JavaPlugin {
    public static SequenceAPI instance;

    private static HashMap<Sequence, Integer> animationTaskIndexs = new HashMap<>();
    private static HashMap<Sequence, Long> sequencers = new HashMap<>();
    private static HashMap<Sequence, Long> endSequencerTicks = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
    }

    public static void startSequence(Sequence sequence) {
        sequencers.put(sequence, 0L);

        if (sequence.getAnimationActions() != null) sequence.getAnimationActions().onSequenceStart();

        ArrayList<Long> values = new ArrayList<>(sequence.getAnimationSequence().keySet());

        Collections.sort(values);
        endSequencerTicks.put(sequence, values.get(values.size() - 1));

        animationTaskIndexs.put(sequence, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            for (Map.Entry<Long, Frame> keyframe : sequence.getAnimationSequence().entrySet()) {
                if (keyframe.getKey().equals(sequencers.get(sequence))) {
                    keyframe.getValue().play();
                }
            }

            if (sequencers.get(sequence) > endSequencerTicks.get(sequence)) {
                if (sequence.getAnimationActions() != null) sequence.getAnimationActions().onSequenceEnd();

                Bukkit.getServer().getScheduler().cancelTask(animationTaskIndexs.get(sequence));
                animationTaskIndexs.remove(sequence);
                endSequencerTicks.remove(sequence);
            }

            sequencers.put(sequence, sequencers.get(sequence) + 1);
        }, 0L, 1L));
    }
}
