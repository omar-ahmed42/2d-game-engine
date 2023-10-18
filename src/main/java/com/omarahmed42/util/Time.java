package com.omarahmed42.util;

public class Time {
    private Time() {}

    public static float timeStarted = System.nanoTime();

    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
    }
}
