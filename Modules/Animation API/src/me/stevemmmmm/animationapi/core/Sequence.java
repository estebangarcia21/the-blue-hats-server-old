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

    public void addKeyFrame(long time, Frame frame) {
        animationSequence.put(time, frame);
    }

    public void repeatAddKeyFrame(Frame frame, long time, int delay, int amount) {
        addKeyFrame(time, frame);

        long delayTime = time;

        for (int i = 0; i < amount; i++) {
            addKeyFrame(delayTime + delay, frame);
            delayTime += delay;
        }
    }

    public HashMap<Long, Frame> getAnimationSequence() {
        return animationSequence;
    }

    public SequenceActions getAnimationActions() {
        return animationActions;
    }

    public void setAnimationActions(SequenceActions action) {
        animationActions = action;
    }
}
