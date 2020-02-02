package com.gbw.scanner.plugins.detect;

import java.util.List;

public class GBWDetectConfig {

    private int threads;

    private int defaultTimeout;
    private int conTimeout;
    private int readTimeout;

    private List<GBWDetectTypeConfig> detects;

    public List<GBWDetectTypeConfig> getDetects() {
        return detects;
    }

    public void setDetects(List<GBWDetectTypeConfig> detects) {
        this.detects = detects;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(int defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public int getConTimeout() {
        return conTimeout;
    }

    public void setConTimeout(int conTimeout) {
        this.conTimeout = conTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
