package me.stevemmmmm.thepitremake.utils;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {

    public static void main(String[] args) {
        runProgram();
    }

    private static void runProgram() {
        System.out.println("Magnitude");
        float magnitude = new Scanner(System.in).nextFloat();

        System.out.println("Angle");
        float inputAngle = new Scanner(System.in).nextFloat();

        double resultA = magnitude * Math.sin(Math.toRadians(inputAngle));
        System.out.println(Math.sin(inputAngle));
        double resultB = magnitude * Math.cos(Math.toRadians(inputAngle));

        for (int i = 0; i < 20; i++) System.out.println("");

        System.out.println(String.format("<%s, %s>", new BigDecimal(resultB).setScale(3, RoundingMode.HALF_UP).doubleValue(), new BigDecimal(resultA).setScale(3, RoundingMode.HALF_UP).doubleValue()));

        runProgram();
    }

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
