package me.stevemmmmm.animationapi.core;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Sequence {
    private final HashMap<Long, Frame> animationSequence = new HashMap<>();
    private SequenceActions animationActions;

    private boolean loop;

    public Sequence addKeyFrame(long time, Frame frame) {
        animationSequence.put(time, frame);

        return this;
    }

    public Sequence repeatAddKeyFrame(Frame frame, long time, int delay, int amount) {
        addKeyFrame(time, frame);

        long delayTime = time;

        for (int i = 0; i < amount; i++) {
            addKeyFrame(delayTime + delay, frame);
            delayTime += delay;
        }

        return this;
    }

    public Sequence setAnimationActions(SequenceActions action) {
        animationActions = action;

        return this;
    }

    public Sequence loop() {
        loop = true;

        return this;
    }

    public boolean isLooping() {
        return loop;
    }

    public HashMap<Long, Frame> getAnimationSequence() {
        return animationSequence;
    }

    public SequenceActions getAnimationActions() {
        return animationActions;
    }
}
