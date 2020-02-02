package com.gbw.scanner.source;

public class GBWHostSourceConfig {

    private int sleepTime;
    private int queueLimits;

    public GBWHostSourceConfig() {
    }

    public int getSleepTime() {
        return this.sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getQueueLimits() {
        return this.queueLimits;
    }

    public void setQueueLimits(int queueLimits) {
        this.queueLimits = queueLimits;
    }
}
