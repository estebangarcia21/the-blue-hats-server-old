package me.stevemmmmm.animationapi.core;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Sequence {
    private final HashMap<Long, Frame> animationSequence = new HashMap<>();
    private SequenceActions animationActions;

    public void addKeyFrame(long time, Frame frame) {
        animationSequence.put(time, frame);
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
