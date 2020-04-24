package me.stevemmmmm.thepitremake.utils;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import java.util.Random;

public class MathUtils {

    public static int biasedRandomness(int max, float bias) {
        double v = Math.pow(new Random().nextDouble(), bias);
        return (int) (v * max);
    }
}
