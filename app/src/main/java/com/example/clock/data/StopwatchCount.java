package com.example.clock.data;

public class StopwatchCount {

    private static int count = 1;
    private int presentTime;
    private int previousTime;

    public StopwatchCount(int presentTime, int previousTime) {
        this.presentTime = presentTime;
        this.previousTime = previousTime;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }

    public void resetCount() {
        count = 1;
    }

    public int getPresentTime() {
        return presentTime;
    }

    public int getPreviousTime() {
        return previousTime;
    }
}
