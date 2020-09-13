package me.stevemmmmm.thepitremake.utils;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
    public static int biasedRandomness(int min, int max, double bias) {
        if (min == max) {
            return min;
        }

        if (max < min || min < 0) {
            throw new IllegalArgumentException("Invalid arguments!");
        }

        double d = max - min;
        double r = 15 * d / bias;
        double x = Double.parseDouble(new DecimalFormat("#0.0").format(ThreadLocalRandom.current().nextDouble(0, r + 1)));

        return (int) Math.round(d * Math.pow(Math.E, -bias / 15 * x) + (max - d));
    }
}
