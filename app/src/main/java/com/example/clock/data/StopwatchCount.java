package com.example.clock.data;

public class StopwatchCount {

    private String count;
    private String presentTime;
    private String previousTime;

    public StopwatchCount(String presentTime, String previousTime, int count) {
        this.count = "count " + count;
        this.presentTime = presentTime;
        this.previousTime = previousTime;
    }

    public String getCount() {
        return count;
    }

    public String getPresentTime() {
        return presentTime;
    }

    public String getPreviousTime() {
        return previousTime;
    }
}
