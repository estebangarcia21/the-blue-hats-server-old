package me.stevemmmmm.animationapi.core;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import javafx.util.Pair;

import java.util.ArrayList;

public class Animation {
    private ArrayList<Pair<Long, Frame>> animationSequence = new ArrayList<>();
    private AnimationActions animationActions;

    public void addKeyFrame(long time, Frame frame) {
        animationSequence.add(new Pair<>(time, frame));
    }

    public ArrayList<Pair<Long, Frame>> getAnimationSequence() {
        return animationSequence;
    }

    public AnimationActions getAnimationActions() {
        return animationActions;
    }

    public void setAnimationActions(AnimationActions action) {
        animationActions = action;
    }
}
