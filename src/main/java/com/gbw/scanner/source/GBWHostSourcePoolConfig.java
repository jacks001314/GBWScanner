package com.gbw.scanner.source;

import java.util.List;

public class GBWHostSourcePoolConfig {

    private int sleepTime;
    private int queueLimits;

    private List<GBWHostSourcePoolConfigEntry> sourceConfigs;

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


    public List<GBWHostSourcePoolConfigEntry> getSourceConfigs() {
        return sourceConfigs;
    }

    public void setSourceConfigs(List<GBWHostSourcePoolConfigEntry> sourceConfigs) {
        this.sourceConfigs = sourceConfigs;
    }


}
