package com.example.clock.data;

public class StopwatchCount {

    private String count;
    private int presentTime;
    private int previousTime;

    public StopwatchCount(int presentTime, int previousTime, int count) {
        this.count = "count" + count;
        this.presentTime = presentTime;
        this.previousTime = previousTime;
    }

    public String getCount() {
        return count;
    }

    public int getPresentTime() {
        return presentTime;
    }

    public int getPreviousTime() {
        return previousTime;
    }
}
