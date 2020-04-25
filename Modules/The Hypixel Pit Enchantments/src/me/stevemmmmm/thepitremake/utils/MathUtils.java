package me.stevemmmmm.thepitremake.utils;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {

    public static void main(String[] args) {
        ArrayList<Integer> results = new ArrayList<>();

        for (int i = 0; i < 15; i++)
            results.add(biasedRandomness(1, 3, 3));

        Collections.sort(results);

        System.out.println("---");

        for (Integer i : results) {
            System.out.println(i);
        }
    }

    public static int biasedRandomness(int min, int max, double bias) {
        if (max <= min || min < 0) {
            throw new IllegalArgumentException("Invalid arguments!");
        }

        double d = max - min;
        double r = 15 + d;
        double x = Double.parseDouble(new DecimalFormat("#0.0").format(ThreadLocalRandom.current().nextDouble(0, r + 1)));;

        return (int) Math.round(d * Math.pow(Math.E, -bias / 15 * x) + (max - d));
    }
}
